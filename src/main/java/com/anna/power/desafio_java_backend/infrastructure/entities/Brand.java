package com.anna.power.desafio_java_backend.infrastructure.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "brand")
@Entity

public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "fipe_brand_id", unique = true, updatable = false)
    private Integer fipeBrandId;

    @Column(name = "brand_name", updatable = false)
    private String brandName;

    @OneToMany(mappedBy = "brand")
    @JsonIgnore
    private List<Model> models;

}
