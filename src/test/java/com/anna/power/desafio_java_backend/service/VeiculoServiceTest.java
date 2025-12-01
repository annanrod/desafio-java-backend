package com.anna.power.desafio_java_backend.service;

import com.anna.power.desafio_java_backend.domain.enums.StatusUsuario;
import com.anna.power.desafio_java_backend.infrastructure.entities.Usuario;
import com.anna.power.desafio_java_backend.infrastructure.entities.Veiculo;
import com.anna.power.desafio_java_backend.infrastructure.repository.UsuarioRepository;
import com.anna.power.desafio_java_backend.infrastructure.repository.VeiculoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VeiculoServiceTest {

    @InjectMocks
    VeiculoService service;

    @Mock
    VeiculoRepository repository;

    @Mock
    UsuarioRepository usuarioRepository;

    Veiculo veiculo;
    Usuario usuario;

    @BeforeEach
    public void setup() {

        usuario = Usuario.builder()
                .nome("Anna F")
                .email("annafff@email.com")
                .phone("31988880000")
                .cpf("01987654321")
                .zipCode("30520000")
                .address("Rua das Flores")
                .number(100)
                .complement("Apto 202")
                .status(StatusUsuario.ATIVO)
                .build();

        veiculo = Veiculo.builder()
                .plate("ABC1234")
                .fipePrice(BigDecimal.valueOf(15000))
                .yearFabrication(2020)
                .usuario(usuario)
                .build();
    }

    @Test
    @DisplayName("Deve salvar veículo com sucesso")
    void salvarVeiculo() {

        when(repository.saveAndFlush(veiculo)).thenReturn(veiculo);

        service.salvarVeiculo(veiculo);

        verify(repository).saveAndFlush(veiculo);
        verifyNoMoreInteractions(repository);
    }

    @Test
    @DisplayName("Deve buscar veículo por placa com sucesso")
    void buscarVeiculoPorPlaca() {

        when(repository.findByPlate(veiculo.getPlate()))
                .thenReturn(Optional.of(veiculo));

        Veiculo resultado = service.buscarVeiculoPorPlaca(veiculo.getPlate());

        assertEquals(veiculo.getPlate(), resultado.getPlate());

        verify(repository).findByPlate(veiculo.getPlate());
        verifyNoMoreInteractions(repository);
    }

    @Test
    @DisplayName("Deve lançar erro ao buscar veículo inexistente")
    void buscarVeiculoPorPlacaInexistente() {

        when(repository.findByPlate("XYZ0000"))
                .thenReturn(Optional.empty());

        RuntimeException e = assertThrows(RuntimeException.class, () ->
                service.buscarVeiculoPorPlaca("XYZ0000")
        );

        assertEquals("Veículo não encontrado", e.getMessage());

        verify(repository).findByPlate("XYZ0000");
        verifyNoMoreInteractions(repository);
    }

    @Test
    @DisplayName("Deve atualizar veículo com sucesso")
    void atualizarVeiculoPorId() {

        Integer id = 1;

        Veiculo atualizado = Veiculo.builder()
                .plate("XYZ9999")
                .fipePrice(BigDecimal.valueOf(25000))
                .yearFabrication(2023)
                .usuario(usuario)
                .build();

        when(repository.findById(id)).thenReturn(Optional.of(veiculo));
        when(repository.saveAndFlush(any(Veiculo.class))).thenReturn(veiculo);

        service.atualizarVeiculoPorId(id, atualizado);

        verify(repository).findById(id);
        verify(repository).saveAndFlush(any(Veiculo.class));
        verifyNoMoreInteractions(repository);
    }

    @Test
    @DisplayName("Deve lançar erro ao tentar atualizar veículo inexistente")
    void atualizarVeiculoInexistente() {

        Integer id = 99;

        when(repository.findById(id)).thenReturn(Optional.empty());

        RuntimeException e = assertThrows(RuntimeException.class, () ->
                service.atualizarVeiculoPorId(id, veiculo)
        );

        assertEquals("Veículo não encontrado", e.getMessage());

        verify(repository).findById(id);
        verifyNoMoreInteractions(repository);
    }

    @Test
    @DisplayName("Deve deletar veículo por placa com sucesso")
    void deletarVeiculoPorPlaca() {

        doNothing().when(repository).deleteByPlate(veiculo.getPlate());

        service.deletarVeiculoPorPlaca(veiculo.getPlate());

        verify(repository).deleteByPlate(veiculo.getPlate());
        verifyNoMoreInteractions(repository);
    }

    @Test
    @DisplayName("Deve salvar veículo para usuário existente")
    void salvarVeiculoParaUsuario() {

        when(usuarioRepository.findById(1))
                .thenReturn(Optional.of(usuario));

        when(repository.saveAndFlush(any(Veiculo.class)))
                .thenReturn(veiculo);

        Veiculo resultado = service.salvarVeiculoParaUsuario(1, veiculo);

        assertEquals(usuario, resultado.getUsuario());

        verify(usuarioRepository).findById(1);
        verify(repository).saveAndFlush(veiculo);
        verifyNoMoreInteractions(repository);
    }

    @Test
    @DisplayName("Deve lançar erro ao salvar veículo para usuário inexistente")
    void salvarVeiculoParaUsuarioInexistente() {

        when(usuarioRepository.findById(2))
                .thenReturn(Optional.empty());

        RuntimeException e = assertThrows(RuntimeException.class, () ->
                service.salvarVeiculoParaUsuario(2, veiculo)
        );

        assertEquals("Usuário não encontrado", e.getMessage());

        verify(usuarioRepository).findById(2);
        verifyNoInteractions(repository);
    }

    @Test
    @DisplayName("Não deve permitir cadastrar veículo para usuário inativo")
    void naoPermiteCadastroVeiculoUsuarioInativo() {

        Usuario usuarioInativo = new Usuario();
        usuarioInativo.setStatus(StatusUsuario.INATIVO);

        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuarioInativo));

        RuntimeException e = assertThrows(RuntimeException.class, () ->
                service.salvarVeiculoParaUsuario(1, new Veiculo())
        );

        assertEquals("Usuário inativo não pode cadastrar veículos", e.getMessage());
    }
}
