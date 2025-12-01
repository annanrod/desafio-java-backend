package com.anna.power.desafio_java_backend.controller;

import com.anna.power.desafio_java_backend.service.UsuarioService;
import com.anna.power.desafio_java_backend.service.VeiculoService;
import com.anna.power.desafio_java_backend.infrastructure.entities.Usuario;
import com.anna.power.desafio_java_backend.infrastructure.entities.Veiculo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final VeiculoService veiculoService;

    @PostMapping
    public ResponseEntity<Void> salvarUsuario(@Valid @RequestBody Usuario usuario){
        usuarioService.salvarUsuario(usuario);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/veiculos")
    public ResponseEntity<Veiculo> criarVeiculoParaUsuario(@PathVariable Integer id,
                                                           @RequestBody Veiculo veiculo) {
        Veiculo salvo = veiculoService.salvarVeiculoParaUsuario(id, veiculo);
        return ResponseEntity.ok(salvo);
    }

    @GetMapping("/por-email")
    public ResponseEntity<Optional<Usuario>> buscarUsuarioPorEmail(@RequestParam String email){
        return ResponseEntity.ok(usuarioService.buscarUsuarioPorEmail(email));
    }

    @GetMapping("/por-data")
    public ResponseEntity<List<Usuario>> buscarUsuarioPorData(@RequestParam LocalDate dataInicio, LocalDate dataFim){
        return ResponseEntity.ok(usuarioService.buscarUsuarioPorData(dataInicio, dataFim));
    }

    @GetMapping("/{id}/veiculos")
    public ResponseEntity<List<Veiculo>> listarVeiculosDoUsuario(@PathVariable Integer id) {
        Usuario usuario = usuarioService.buscarUsuarioPorId(id);
        return ResponseEntity.ok(usuario.getVeiculos());
    }

    @DeleteMapping
    public ResponseEntity<Void> deletarUsuarioPorEmail(@RequestParam String email){
        usuarioService.deletarUsuarioPorEmail(email);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<Void> atualizarUsuarioPorId(@RequestParam Integer id,
                                                      @RequestBody Usuario usuario){
        usuarioService.atualizarUsuarioPorId(id, usuario);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> alterarStatus(@PathVariable Integer id,
                                           @RequestParam String status) {

        return ResponseEntity.ok(usuarioService.alterarStatus(id, status));
    }
}
