package com.anna.power.desafio_java_backend.integration;

import com.anna.power.desafio_java_backend.service.VeiculoService;
import com.anna.power.desafio_java_backend.service.UsuarioService;
import com.anna.power.desafio_java_backend.domain.enums.StatusUsuario;
import com.anna.power.desafio_java_backend.infrastructure.client.FipeClient;
import com.anna.power.desafio_java_backend.infrastructure.entities.Brand;
import com.anna.power.desafio_java_backend.infrastructure.entities.Model;
import com.anna.power.desafio_java_backend.infrastructure.entities.Usuario;
import com.anna.power.desafio_java_backend.infrastructure.entities.Veiculo;
import com.anna.power.desafio_java_backend.infrastructure.repository.UsuarioRepository;
import com.anna.power.desafio_java_backend.infrastructure.repository.VeiculoRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.Year;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
class VeiculoServiceIT {

    @Autowired
    private VeiculoService veiculoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private VeiculoRepository veiculoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EntityManager em;

    @MockBean
    private FipeClient fipeClient;

    @BeforeEach
    void setup() {
        em.createNativeQuery("TRUNCATE TABLE veiculo RESTART IDENTITY CASCADE").executeUpdate();
        em.createNativeQuery("TRUNCATE TABLE usuario RESTART IDENTITY CASCADE").executeUpdate();
    }

    private Usuario criarUsuario() {
        Usuario usuario = Usuario.builder()
                .nome("Anna")
                .email("anna@mail.com")
                .cpf("00011122233")
                .status(StatusUsuario.ATIVO)
                .build();

        return usuarioRepository.save(usuario);
    }

    @Test
    @DisplayName("Deve salvar veículo associado a usuário com sucesso")
    void deveSalvarVeiculo() {
        Usuario usuario = criarUsuario();
        int anoAtual = Year.now().getValue();
        String yearId = (anoAtual + 1) + "-1";

        Veiculo veiculo = Veiculo.builder()
                .plate("ABC1234")
                .fipePrice(BigDecimal.valueOf(15000))
                .yearFabrication(anoAtual)
                .yearModel(Integer.valueOf(yearId))
                .brand(Brand.builder().id(1).fipeBrandId(21).brandName("Fiat").build())
                .model(Model.builder().id(1).fipeModelId(7027).modelName("Uno").build())
                .usuario(usuario)
                .build();


        veiculoService.salvarVeiculo(veiculo);

        assertThat(veiculoRepository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("Deve buscar veículo por placa com sucesso")
    void deveBuscarPorPlaca() {
        Usuario usuario = criarUsuario();
        int anoAtual = Year.now().getValue();
        String yearId = (anoAtual + 1) + "-1";

        Veiculo veiculo = Veiculo.builder()
                .plate("XYZ9999")
                .fipePrice(BigDecimal.valueOf(15000))
                .yearFabrication(anoAtual)
                .yearModel(Integer.valueOf(yearId))
                .brand(Brand.builder().id(1).fipeBrandId(21).brandName("Fiat").build())
                .model(Model.builder().id(1).fipeModelId(7027).modelName("Uno").build())
                .usuario(usuario)
                .build();

        veiculoRepository.save(veiculo);

        Veiculo encontrado = veiculoService.buscarVeiculoPorPlaca("XYZ9999");

        assertThat(encontrado).isNotNull();
    }

    @Test
    @DisplayName("Deve deletar veículo sem quebrar FK")
    void deveDeletarVeiculo() {
        Usuario usuario = criarUsuario();
        int anoAtual = Year.now().getValue();
        String yearId = (anoAtual + 1) + "-1";

        Veiculo veiculo = Veiculo.builder()
                .plate("ABC5432")
                .fipePrice(BigDecimal.valueOf(19000))
                .yearFabrication(anoAtual)
                .yearModel(Integer.valueOf(yearId))
                .brand(Brand.builder().id(1).fipeBrandId(21).brandName("Fiat").build())
                .model(Model.builder().id(1).fipeModelId(7027).modelName("Uno").build())
                .usuario(usuario)
                .build();


        Veiculo salvo = veiculoRepository.save(veiculo);

        veiculoService.deletarVeiculoPorPlaca(salvo.getPlate());

        assertThat(veiculoRepository.count()).isZero();
    }

    @Test
    @DisplayName("Deve salvar veículo com preço FIPE preenchido (integração)")
    void salvarVeiculoComPrecoFipe() {
        int anoAtual = Year.now().getValue();
        String yearId = (anoAtual + 1) + "-1";

        Veiculo veiculo = Veiculo.builder()
                .plate("XYZ9876")
                .yearFabrication(anoAtual)
                .yearModel(anoAtual + 1)
                .brand(Brand.builder().id(1).fipeBrandId(21).brandName("Fiat").build())
                .model(Model.builder().id(1).fipeModelId(7027).modelName("Uno").build())
                .build();

        when(fipeClient.buscarPrecoFipe("cars", 21, 7027, yearId))
                .thenReturn(Map.of("price", "45000.00"));

        veiculoService.salvarVeiculo(veiculo);

        Veiculo salvo = veiculoRepository.findByPlate("XYZ9876")
                .orElseThrow(() -> new RuntimeException("Veículo não foi salvo"));

        assertThat(salvo.getFipePrice()).isEqualByComparingTo(new BigDecimal("45000.00"));
    }
}
