package com.anna.power.desafio_java_backend.business;

import com.anna.power.desafio_java_backend.domain.enums.StatusUsuario;
import com.anna.power.desafio_java_backend.infrastructure.entities.Usuario;
import com.anna.power.desafio_java_backend.infrastructure.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @InjectMocks
    UsuarioService service;

    @Mock
    UsuarioRepository repository;

    Usuario usuario;

    @BeforeEach
    public void setup(){
        usuario = Usuario.builder()
                .nome("Anna")
                .email("anna@email.com")
                .phone("31999990000")
                .cpf("12345678901")
                .zipCode("30520000")
                .address("Rua das Flores")
                .number(100)
                .complement("Apto 202")
                .status(StatusUsuario.ATIVO)
                .build();
    }

    @Test
    @DisplayName("Deve salvar usuário e definir data de criação automaticamente com sucesso")
    void salvarUsuario() {

        when(repository.saveAndFlush(any(Usuario.class))).thenReturn(usuario);

        service.salvarUsuario(usuario);

        assertNotNull(usuario.getCreatedAt());
        verify(repository).saveAndFlush(usuario);
    }

    @Test
    @DisplayName("Deve buscar usuários, por email, com sucesso")
    void buscarUsuarioPorEmail() {
        when(repository.findByEmail(usuario.getEmail()))                .thenReturn(Optional.of(usuario));

        Optional<Usuario> resultado = service.buscarUsuarioPorEmail(usuario.getEmail());

        assertTrue(resultado.isPresent());
        assertEquals(usuario.getEmail(), resultado.get().getEmail());

        verify(repository).findByEmail(usuario.getEmail());
        verifyNoMoreInteractions(repository);
    }

    @Test
    @DisplayName("Deve lançar erro ao tentar buscar usuário com email inexistente")
    void buscarUsuarioPorEmailInexistente() {
        String email = "naoexiste@email.com";

        when(repository.findByEmail(email)).thenReturn(Optional.empty());

        RuntimeException e = assertThrows(RuntimeException.class, () ->
                service.buscarUsuarioPorEmail(email)
        );

        assertEquals("E-mail não encontrado", e.getMessage());

        verify(repository).findByEmail(email);
        verifyNoMoreInteractions(repository);
    }

    @Test
    @DisplayName("Deve filtrar usuários, por data de criação, com sucesso")
    void buscarUsuarioPorData() {

        LocalDate dataInicio = LocalDate.now().minusDays(10);
        LocalDate dataFim = LocalDate.now();

        when(repository.findByCreatedAtBetween(dataInicio, dataFim))
                .thenReturn(Collections.singletonList(usuario));

        List<Usuario> resultado = service.buscarUsuarioPorData(dataInicio, dataFim);

        assertEquals(1, resultado.size());
        assertEquals(usuario, resultado.get(0));

        verify(repository).findByCreatedAtBetween(dataInicio, dataFim);
        verifyNoMoreInteractions(repository);
    }

    @Test
    @DisplayName("Deve buscar usuário por ID com sucesso")
    void buscarUsuarioPorId() {

        when(repository.findById(1)).thenReturn(Optional.of(usuario));

        Usuario resultado = service.buscarUsuarioPorId(1);

        assertEquals(usuario, resultado);
        verify(repository).findById(1);
    }

    @Test
    @DisplayName("Deve lançar erro ao buscar usuário inexistente por ID")
    void buscarUsuarioPorIdInexistente() {

        when(repository.findById(1)).thenReturn(Optional.empty());

        RuntimeException e = assertThrows(RuntimeException.class, () ->
                service.buscarUsuarioPorId(1)
        );

        assertEquals("Usuario não encontrado", e.getMessage());
        verify(repository).findById(1);
    }


    @Test
    @DisplayName("Deve deletar usuários com sucesso")
    void deletarUsuarioPorEmail() {

        String email = "anna@email.com";

        doNothing().when(repository).deleteByEmail(email);

        service.deletarUsuarioPorEmail(email);

        verify(repository).deleteByEmail(email);
        verifyNoMoreInteractions(repository);
    }

    @Test
    @DisplayName("Deve atualizar usuários com sucesso")
    void atualizarUsuarioPorId() {

        Integer id = 1;

        Usuario atualizado = Usuario.builder()
                .nome("Jonh")
                .email("jonh@email.com")
                .phone("31999991111")
                .cpf("12345698752")
                .zipCode("30520111")
                .address("Rua das Folhas")
                .number(200)
                .complement("Apto 101")
                .status(StatusUsuario.INATIVO)
                .build();

        when(repository.findById(id)).thenReturn(Optional.of(usuario));
        when(repository.saveAndFlush(any(Usuario.class))).thenReturn(usuario);

        service.atualizarUsuarioPorId(id, atualizado);

        verify(repository).findById(id);
        verify(repository).saveAndFlush(any(Usuario.class));
        verifyNoMoreInteractions(repository);
    }

    @Test
    @DisplayName("Deve lançar erro ao tentar atualizar usuário inexistente")
    void atualizarUsuarioInexistente() {

        Integer id = 99;

        when(repository.findById(id)).thenReturn(Optional.empty());

        RuntimeException e = assertThrows(RuntimeException.class, () ->
                service.atualizarUsuarioPorId(id, usuario)
        );

        assertEquals("Usuario não encontrado", e.getMessage());

        verify(repository).findById(id);
        verifyNoMoreInteractions(repository);
    }

    @Test
    @DisplayName("Deve alterar o status do usuário com sucesso")
    void alterarStatus() {

        usuario.setStatus(StatusUsuario.ATIVO);

        when(repository.findById(1)).thenReturn(Optional.of(usuario));
        when(repository.saveAndFlush(usuario)).thenReturn(usuario);

        Usuario resultado = service.alterarStatus(1, "inativo");

        assertEquals(StatusUsuario.INATIVO, resultado.getStatus());
        verify(repository).findById(1);
        verify(repository).saveAndFlush(usuario);
    }

    @Test
    @DisplayName("Deve lançar erro ao alterar status de usuário inexistente")
    void alterarStatusUsuarioInexistente() {

        when(repository.findById(1)).thenReturn(Optional.empty());

        RuntimeException e = assertThrows(RuntimeException.class, () ->
                service.alterarStatus(1, "ativo")
        );

        assertEquals("Usuário não encontrado", e.getMessage());
        verify(repository).findById(1);
    }


}