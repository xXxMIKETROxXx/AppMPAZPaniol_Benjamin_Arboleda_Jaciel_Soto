package com.proyecto.inventario.controller;

import com.proyecto.inventario.entities.Inventario;
import com.proyecto.inventario.services.inventarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/inventario")
public class inventarioController {

    private final inventarioService inventarioService;

    public inventarioController(inventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    @GetMapping
    public ResponseEntity<List<Inventario>> listar() {
        return ResponseEntity.ok(inventarioService.listar());
    }

    @GetMapping("/{equipoId}")
    public ResponseEntity<Inventario> obtenerPorId(@PathVariable String equipoId) {
        return inventarioService.obtenerPorId(equipoId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Inventario> crear(@RequestBody Inventario inventario) {
        return ResponseEntity.status(HttpStatus.CREATED).body(inventarioService.crear(inventario));
    }

    @PutMapping("/{equipoId}")
    public ResponseEntity<Inventario> actualizar(
            @PathVariable String equipoId,
            @RequestBody Inventario inventario
    ) {
        return inventarioService.actualizar(equipoId, inventario)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/{equipoId}/estado")
    public ResponseEntity<Inventario> cambiarEstado(
            @PathVariable String equipoId,
            @RequestParam String valor
    ) {
        return inventarioService.cambiarEstado(equipoId, valor)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/{equipoId}/stock")
    public ResponseEntity<Inventario> actualizarStock(
            @PathVariable String equipoId,
            @RequestParam Integer valor
    ) {
        return inventarioService.actualizarStock(equipoId, valor)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/{equipoId}/baja")
    public ResponseEntity<Inventario> darDeBaja(@PathVariable String equipoId) {
        return inventarioService.darDeBaja(equipoId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
