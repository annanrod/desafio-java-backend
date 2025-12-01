package com.anna.power.desafio_java_backend.integration;

import com.anna.power.desafio_java_backend.service.UsuarioService;
import com.anna.power.desafio_java_backend.domain.enums.StatusUsuario;
import com.anna.power.desafio_java_backend.infrastructure.entities.Usuario;
import com.anna.power.desafio_java_backend.infrastructure.repository.UsuarioRepository;
import com.anna.power.desafio_java_backend.infrastructure.repository.VeiculoRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class UsuarioServiceIT {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private VeiculoRepository veiculoRepository;

    @Autowired
    private EntityManager em;

    @BeforeEach
    void setup() {
        // Limpa TUDO para evitar FK errors
        em.createNativeQuery("TRUNCATE TABLE veiculo RESTART IDENTITY CASCADE").executeUpdate();
        em.createNativeQuery("TRUNCATE TABLE usuario RESTART IDENTITY CASCADE").executeUpdate();
    }

    @Test
    @DisplayName("Deve salvar um usuário no banco com sucesso")
    void deveSalvarUsuario() {
        Usuario usuario = Usuario.builder()
                .nome("Anna")
                .email("anna@email.com")
                .cpf("12345678901")
                .status(StatusUsuario.ATIVO)
                .build();

        usuarioService.salvarUsuario(usuario);

        assertThat(usuarioRepository.count()).isEqualTo(1);
        assertThat(usuarioRepository.findAll().get(0).getNome()).isEqualTo("Anna");
    }

    @Test
    @DisplayName("Deve atualizar um usuário corretamente no banco")
    void deveAtualizarUsuario() {
        Usuario usuario = Usuario.builder()
                .nome("João")
                .email("joao@mail.com")
                .cpf("99999999999")
                .status(StatusUsuario.ATIVO)
                .build();

        Usuario salvo = usuarioRepository.save(usuario);

        Usuario atualizado = Usuario.builder()
                .nome("João da Silva")
                .email("silva@mail.com")
                .cpf("99999999999")
                .build();

        usuarioService.atualizarUsuarioPorId(salvo.getId(), atualizado);

        Usuario buscado = usuarioRepository.findById(salvo.getId()).get();

        assertThat(buscado.getNome()).isEqualTo("João da Silva");
        assertThat(buscado.getEmail()).isEqualTo("silva@mail.com");
    }

    @Test
    @DisplayName("Deve deletar um usuário sem violar FK")
    void deveDeletarUsuario() {
        Usuario usuario = Usuario.builder()
                .nome("Maria")
                .email("maria@mail.com")
                .cpf("12345670000")
                .status(StatusUsuario.ATIVO)
                .build();

        Usuario salvo = usuarioRepository.save(usuario);

        usuarioService.deletarUsuarioPorEmail(salvo.getEmail());

        assertThat(usuarioRepository.count()).isZero();
    }
}
