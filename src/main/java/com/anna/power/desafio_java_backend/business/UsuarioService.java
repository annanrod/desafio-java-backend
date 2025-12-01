package com.anna.power.desafio_java_backend.business;

import com.anna.power.desafio_java_backend.domain.enums.StatusUsuario;
import com.anna.power.desafio_java_backend.infrastructure.entities.Usuario;
import com.anna.power.desafio_java_backend.infrastructure.repository.UsuarioRepository;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.springframework.util.Assert.notNull;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    public void salvarUsuario(Usuario usuario){
        usuario.setCreatedAt(LocalDate.now());
        repository.saveAndFlush(usuario);
    }

    public Usuario buscarUsuarioPorId(Integer id){
        return repository.findById(id).orElseThrow(() ->
                new RuntimeException("Usuario não encontrado"));
    }

    public Optional<Usuario> buscarUsuarioPorEmail(String email){
        return Optional.ofNullable(repository.findByEmail(email).orElseThrow(
                () -> new RuntimeException("E-mail não encontrado")
        ));

    }

    public List<Usuario> buscarUsuarioPorData(LocalDate dataInicio, LocalDate dataFim){
        return repository.findByCreatedAtBetween(dataInicio, dataFim);
    }

    public void deletarUsuarioPorEmail(String email){
        repository.deleteByEmail(email);
    }

    public void atualizarUsuarioPorId(Integer id, Usuario usuario){

        Usuario usuarioEntity = repository.findById(id).orElseThrow(() ->
                new RuntimeException("Usuario não encontrado"));

        if (usuario.getEmail() != null)
            usuarioEntity.setEmail(usuario.getEmail());
        if (usuario.getNome() != null)
            usuarioEntity.setNome(usuario.getNome());
        if (usuario.getCpf() != null)
            usuarioEntity.setCpf(usuario.getCpf());
        if (usuario.getPhone() != null)
            usuarioEntity.setPhone(usuario.getPhone());
        if (usuario.getZipCode() != null)
            usuarioEntity.setZipCode(usuario.getZipCode());
        if (usuario.getAddress() != null)
            usuarioEntity.setAddress(usuario.getAddress());
        if (usuario.getNumber() != null)
            usuarioEntity.setNumber(usuario.getNumber());
        if (usuario.getComplement() != null)
            usuarioEntity.setComplement(usuario.getComplement());
        if (usuario.getStatus() != null)
            usuarioEntity.setStatus(usuario.getStatus());

        repository.saveAndFlush(usuarioEntity);
    }

    public Usuario alterarStatus(Integer id, String status) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        usuario.setStatus(StatusUsuario.valueOf(status.toUpperCase()));
        return repository.saveAndFlush(usuario);
    }


}
