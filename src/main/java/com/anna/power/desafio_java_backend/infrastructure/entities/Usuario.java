package com.anna.power.desafio_java_backend.infrastructure.entities;

import com.anna.power.desafio_java_backend.domain.enums.StatusUsuario;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "usuario")
@Entity

public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Nome é obrigatório")
    @Column(name = "nome")
    private String nome;

    @Email(message = "E-mail inválido")
    @NotBlank(message = "E-mail é obrigatório")
    @Column(name = "email", unique = true)
    private String email;

    @Pattern(
            regexp = "\\d{10,11}",
            message = "Telefone deve ter 10 ou 11 dígitos numéricos"
    )
    @Column(name = "phone", nullable = true)
    private String phone;

    @Pattern(
            regexp = "\\d{11}",
            message = "CPF deve conter 11 dígitos numéricos"
    )
    @Column(name = "cpf", unique = true)
    private String cpf;

    @Column(name = "zip_code", nullable = true)
    private String zipCode;

    @Column(name = "address", nullable = true)
    private String address;

    @Column(name = "number", nullable = true)
    private Integer number;

    @Column(name = "complement", nullable = true)
    private String complement;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDate createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusUsuario status;

    @OneToMany(mappedBy = "usuario")
    @JsonIgnore
    private List<Veiculo> veiculos;

}
