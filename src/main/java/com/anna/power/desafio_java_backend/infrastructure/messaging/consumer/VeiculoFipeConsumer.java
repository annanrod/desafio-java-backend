package com.anna.power.desafio_java_backend.infrastructure.messaging.consumer;

import com.anna.power.desafio_java_backend.domain.events.VehicleCreatedEvent;
import com.anna.power.desafio_java_backend.infrastructure.client.FipeClient;
import com.anna.power.desafio_java_backend.infrastructure.entities.Veiculo;
import com.anna.power.desafio_java_backend.infrastructure.repository.VeiculoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class VeiculoFipeConsumer {

    private final FipeClient fipeClient;
    private final VeiculoRepository veiculoRepository;

//    @RabbitListener(queues = "vehicle.queue")
    public void processar(VehicleCreatedEvent evento) {

        log.info("📩 Mensagem recebida: {}", evento);

        Veiculo veiculo = veiculoRepository.findById(evento.getVehicleId())
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado para atualização FIPE."));

        Map<String, Object> precoFipe = fipeClient.buscarPrecoFipe(
                "cars",
                evento.getBrandId(),
                evento.getModelId(),
                String.valueOf(evento.getYearModel())
        );

        if (precoFipe == null || precoFipe.get("price") == null) {
            log.error("❌ Erro ao consultar FIPE.");
            return;
        }

//        veiculo.setFipePrice(new BigDecimal(precoFipe.get("price").toString()));
//        veiculoRepository.save(veiculo);

        log.info("✅ FIPE atualizada automaticamente para o veículo {}", veiculo.getPlate());
    }
}
