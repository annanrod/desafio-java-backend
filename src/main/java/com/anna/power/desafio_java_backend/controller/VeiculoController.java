package com.anna.power.desafio_java_backend.controller;

import com.anna.power.desafio_java_backend.business.VeiculoService;
import com.anna.power.desafio_java_backend.infrastructure.entities.Veiculo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/veiculo")
@RequiredArgsConstructor
public class VeiculoController {

    private final VeiculoService veiculoService;

    @PostMapping
    public ResponseEntity<Void> salvarVeiculo(@RequestBody Veiculo veiculo){
        veiculoService.salvarVeiculo(veiculo);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Veiculo> buscarVeiculoPorPlaca(@RequestParam String plate){
        return ResponseEntity.ok(veiculoService.buscarVeiculoPorPlaca(plate));
    }

    @DeleteMapping
    public ResponseEntity<Void> deletarVeiculoPorPlaca(@RequestParam String plate){
        veiculoService.deletarVeiculoPorPlaca(plate);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<Void> atualizarVeiculoPorId(@RequestParam Integer id,
                                                      @RequestBody Veiculo veiculo){
        veiculoService.atualizarVeiculoPorId(id, veiculo);
        return ResponseEntity.ok().build();
    }

}
