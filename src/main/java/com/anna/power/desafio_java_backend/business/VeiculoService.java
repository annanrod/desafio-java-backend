package com.anna.power.desafio_java_backend.business;

import com.anna.power.desafio_java_backend.domain.enums.StatusUsuario;
import com.anna.power.desafio_java_backend.domain.events.VehicleCreatedEvent;
import com.anna.power.desafio_java_backend.infrastructure.client.FipeClient;
import com.anna.power.desafio_java_backend.infrastructure.entities.Usuario;
import com.anna.power.desafio_java_backend.infrastructure.entities.Veiculo;
import com.anna.power.desafio_java_backend.infrastructure.repository.UsuarioRepository;
import com.anna.power.desafio_java_backend.infrastructure.repository.VeiculoRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.math.BigDecimal;
import java.time.Year;
import java.util.Map;

@Service
public class VeiculoService {

    private final VeiculoRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final FipeClient fipeClient;
    private final RabbitTemplate rabbitTemplate;

    public VeiculoService(VeiculoRepository repository, UsuarioRepository usuarioRepository, FipeClient fipeClient, RabbitTemplate rabbitTemplate) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
        this.fipeClient = fipeClient;
        this.rabbitTemplate = rabbitTemplate;
    }

    public void salvarVeiculo(Veiculo veiculo){
        String tipoCombustivel = "-1";
        String anoModelo = veiculo.getYearModel();
        String yearCode = (anoModelo + tipoCombustivel);


        Map<String, Object> preco = fipeClient.buscarPrecoFipe(
                "cars",
                veiculo.getBrand().getFipeBrandId(),
                veiculo.getModel().getFipeModelId(),
                yearCode
        );


        if (preco == null || preco.get("price") == null) {
            throw new RuntimeException("Não foi possível obter o valor FIPE para o veículo.");
        }

        String valorFipe = preco.get("price").toString();

        veiculo.setFipePrice(new String(valorFipe));

        repository.saveAndFlush(veiculo);

        System.out.println(veiculo.getYearModel());
        System.out.println(anoModelo);
        System.out.println(yearCode);

//        VehicleCreatedEvent evento = new VehicleCreatedEvent(
//                veiculo.getId(),
//                veiculo.getPlate(),
//                veiculo.getBrand().getFipeBrandId(),
//                veiculo.getModel().getFipeModelId(),
//                veiculo.getYearModel()
//        );
//
//        rabbitTemplate.convertAndSend(
//                "vehicle.exchange",
//                "vehicle.created",
//                evento
//        );
    }

    @Cacheable(value = "veiculos", key = "#plate")
    public Veiculo buscarVeiculoPorPlaca(String plate){
        return repository.findByPlate(plate).orElseThrow(
                () -> new RuntimeException("Veículo não encontrado")
        );
    }

    @CacheEvict(value = "veiculos", key = "#plate")
    public void deletarVeiculoPorPlaca(String plate){
        repository.deleteByPlate(plate);
    }

    @CacheEvict(value = "veiculos", allEntries = true)
    public void atualizarVeiculoPorId(Integer id, Veiculo veiculo){
        Veiculo veiculoEntity = repository.findById(id).orElseThrow(
                () -> new RuntimeException("Veículo não encontrado"));

        if (veiculo.getPlate() != null) veiculoEntity.setPlate(veiculo.getPlate());
        if (veiculo.getFipePrice() != null) veiculoEntity.setFipePrice(veiculo.getFipePrice());
        if (veiculo.getYearFabrication() != null) veiculoEntity.setYearFabrication(veiculo.getYearFabrication());
        if (veiculo.getUsuario() != null) veiculoEntity.setUsuario(veiculo.getUsuario());
        if (veiculo.getYearModel() != null) veiculoEntity.setYearModel(veiculo.getYearModel());
        if (veiculo.getBrand() != null) veiculoEntity.setBrand(veiculo.getBrand());
        if (veiculo.getModel() != null) veiculoEntity.setModel(veiculo.getModel());

        validarAnoVeiculo(veiculoEntity);

        repository.saveAndFlush(veiculoEntity);
    }

    public Veiculo salvarVeiculoParaUsuario(Integer usuarioId, Veiculo veiculo) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (usuario.getStatus() == StatusUsuario.INATIVO) {
            throw new RuntimeException("Usuário inativo não pode cadastrar veículos");
        }

        veiculo.setUsuario(usuario);
        return repository.saveAndFlush(veiculo);
    }

    private void validarAnoVeiculo(Veiculo veiculo) {
        Integer anoFabricacao = veiculo.getYearFabrication();
        Integer anoModelo = Integer.valueOf(veiculo.getYearModel());

        if (anoFabricacao == null || anoModelo == null) {
            throw new IllegalArgumentException("Ano de fabricação e ano modelo são obrigatórios.");
        }

        int anoAtual = Year.now().getValue();

        if (anoFabricacao < 1900 || anoFabricacao > anoAtual) {
            throw new IllegalArgumentException("Ano de fabricação inválido.");
        }

        if (anoModelo < anoFabricacao) {
            throw new IllegalArgumentException("Ano modelo não pode ser menor que o ano de fabricação.");
        }
        if (anoModelo > anoFabricacao + 1) {
            throw new IllegalArgumentException("Ano modelo deve ser igual ao ano de fabricação ou, no máximo, um ano à frente.");
        }
        if (anoModelo > anoAtual + 1) {
            throw new IllegalArgumentException("Ano modelo não pode ser maior que o ano seguinte ao ano atual.");
        }
    }

}
