package com.anna.power.desafio_java_backend.domain.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleCreatedEvent {

    private Integer vehicleId;
    private String plate;
    private Integer brandId;
    private Integer modelId;
    private Integer yearModel;
}
