package com.java.spring.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.java.spring.entity.RutinaEjercicio;
import com.java.spring.service.RutinaEjercicioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*")
@Tag(name = "RutinaEjercicios", description = "API para gestionar la relación Rutina ↔ Ejercicio")
@RestController
@RequestMapping("/rutina-ejercicios")
@RequiredArgsConstructor
public class RutinaEjercicioController {

    private final RutinaEjercicioService rutinaEjercicioService;

    @GetMapping
    @Operation(summary = "Obtener todas las relaciones RutinaEjercicio")
    public List<RutinaEjercicio> get() {
        return rutinaEjercicioService.get();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una relación RutinaEjercicio por ID")
    public ResponseEntity<RutinaEjercicio> getById(@PathVariable long id) {
        return rutinaEjercicioService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    @Operation(summary = "Crear una nueva relación RutinaEjercicio")
    public ResponseEntity<RutinaEjercicio> add(@RequestBody RutinaEjercicio rutinaEjercicio) {
        return ResponseEntity.status(HttpStatus.CREATED).body(rutinaEjercicioService.add(rutinaEjercicio));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una relación RutinaEjercicio")
    public ResponseEntity<RutinaEjercicio> update(@PathVariable long id, @RequestBody RutinaEjercicio rutinaEjercicio) {
        return ResponseEntity.ok(rutinaEjercicioService.update(id, rutinaEjercicio));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una relación RutinaEjercicio")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        rutinaEjercicioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
