package com.anna.power.desafio_java_backend.infrastructure.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "veiculo")
@Entity

public class Veiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "plate", unique = true)
    private String plate;

//    @Column(name = "advertised_price")
//    private BigDecimal advertisedPrice;

    @Column(name = "fipe_price")
    private String fipePrice;

    @Column(name = "year_fabrication")
    private Integer yearFabrication;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDate createdAt = LocalDate.now();

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @ManyToOne
    @JoinColumn(name = "model_id")
    private Model model;

    @Column(name = "year_model")
    private String yearModel;

}
