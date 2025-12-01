package com.anna.power.desafio_java_backend.infrastructure.client;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Component
public class FipeClient {

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String apiKey =
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJlMWM0OGNiMS1iODZjLTQxYjItYmY4NC0zNjYyZmRkZTZjNzEiLCJlbWFpbCI6ImFubmFic25zc0BnbWFpbC5jb20iLCJpYXQiOjE3NjQ1OTMzNTR9.b_MnFw_ARMPnzOUHAOr7hzUAtkHX5cvi1zMTEspiJAo";

    private static final String BASE_URL = "https://fipe.parallelum.com.br/api/v2";

    private HttpEntity<Void> buildAuthHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(headers);
    }

    @Cacheable(value = "fipeBrands", key = "#vehicleType")
    public List<Map<String, Object>> buscarMarcas(String vehicleType) {
        String url = BASE_URL + "/" + vehicleType + "/brands";

        ResponseEntity<List<Map<String, Object>>> resposta =
                restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        buildAuthHeaders(),
                        new ParameterizedTypeReference<>() {}
                );

        return resposta.getBody();
    }

    @Cacheable(value = "fipeModels", key = "#vehicleType + '-' + #brandId")
    public List<Map<String, Object>> buscarModelos(String vehicleType, Integer brandId) {
        String url = BASE_URL + "/" + vehicleType + "/brands/" + brandId + "/models";

        ResponseEntity<List<Map<String, Object>>> resposta =
                restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        buildAuthHeaders(),
                        new ParameterizedTypeReference<>() {}
                );

        return resposta.getBody();
    }

    @Cacheable(value = "fipeYears", key = "#vehicleType + '-' + #brandId + '-' + #modelId")
    public List<Map<String, Object>> buscarAnos(String vehicleType, Integer brandId, Integer modelId) {
        String url = BASE_URL + "/" + vehicleType + "/brands/" + brandId + "/models/" + modelId + "/years";

        ResponseEntity<List<Map<String, Object>>> resposta =
                restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        buildAuthHeaders(),
                        new ParameterizedTypeReference<>() {}
                );

        return resposta.getBody();
    }

    @Cacheable(value = "fipePrice", key = "#vehicleType + '-' + #brandId + '-' + #modelId + '-' + #yearId")
    public Map<String, Object> buscarPrecoFipe(String vehicleType, Integer brandId, Integer modelId, String yearId) {

        String url = BASE_URL + "/" + vehicleType + "/brands/" + brandId +
                "/models/" + modelId + "/years/" + yearId;

        ResponseEntity<Map<String, Object>> resposta =
                restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        buildAuthHeaders(),
                        new ParameterizedTypeReference<>() {}
                );

        return resposta.getBody();
    }
}
