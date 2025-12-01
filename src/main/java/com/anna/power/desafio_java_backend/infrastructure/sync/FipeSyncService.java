package com.anna.power.desafio_java_backend.infrastructure.sync;

import com.anna.power.desafio_java_backend.infrastructure.client.FipeClient;
import com.anna.power.desafio_java_backend.infrastructure.entities.Brand;
import com.anna.power.desafio_java_backend.infrastructure.entities.Model;
import com.anna.power.desafio_java_backend.infrastructure.repository.BrandRepository;
import com.anna.power.desafio_java_backend.infrastructure.repository.ModelRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class FipeSyncService {

    private final FipeClient fipeClient;
    private final BrandRepository brandRepository;
    private final ModelRepository modelRepository;

//    @PostConstruct
    public void sincronizarFipe() {
        atualizarMarcasEModelos();
    }

    public void atualizarMarcasEModelos() {

        List<Map<String, Object>> marcas = fipeClient.buscarMarcas("cars");

        for (Map<String, Object> marca : marcas) {
            Integer fipeBrandId = Integer.parseInt(marca.get("code").toString());
            String brandName = marca.get("name").toString();

            Brand brand = brandRepository.findByFipeBrandId(fipeBrandId)
                    .orElseGet(() -> brandRepository.save(
                            Brand.builder()
                                    .fipeBrandId(fipeBrandId)
                                    .brandName(brandName)
                                    .build()
                    ));

            List<Map<String, Object>> modelos = fipeClient.buscarModelos("cars", fipeBrandId);

            for (Map<String, Object> modelo : modelos) {
                Integer fipeModelId = Integer.parseInt(modelo.get("code").toString());
                String modelName = modelo.get("name").toString();

                modelRepository.findByFipeModelId(fipeModelId)
                        .orElseGet(() -> (Model) modelRepository.save(
                                Model.builder()
                                        .fipeModelId(fipeModelId)
                                        .modelName(modelName)
                                        .brand(brand)
                                        .build()
                        ));
            }
        }
    }
}
