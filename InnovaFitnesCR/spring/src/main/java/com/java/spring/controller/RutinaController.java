package com.java.spring.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import com.java.spring.dto.RutinaCreateDTO;
import com.java.spring.entity.Rutina;
import com.java.spring.service.RutinaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin(origins = "*")
@Tag(name = "Rutinas", description = "API para gestionar rutinas")
@RestController
@RequestMapping("/rutinas")
public class RutinaController {

    @Autowired
    private RutinaService rutinaService;

    @GetMapping
    @Operation(summary = "Obtener todas las rutinas")
    public List<Rutina> get() {
        return rutinaService.get();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una rutina por ID")
    public Rutina getById(@PathVariable long id) {
        return rutinaService.getById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @Operation(summary = "Crear una nueva rutina")
    public Rutina add(@RequestBody @NonNull RutinaCreateDTO rutina) {
        return rutinaService.add(rutina);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una rutina")
    public void delete(@PathVariable long id) {
        rutinaService.delete(id);
    }

    @GetMapping("/stats/total")
    @Operation(summary = "Cantidad total de rutinas registradas")
    public long getTotalRutinas() {
        return rutinaService.getTotalRutinas();
    }

    @GetMapping("/stats/dificultad")
    @Operation(summary = "Distribución de rutinas por nivel de dificultad")
    public List<Object[]> getRutinasPorDificultad() {
        return rutinaService.getRutinasPorDificultad();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody RutinaCreateDTO dto) {
        rutinaService.actualizarRutina(id, dto);
        return ResponseEntity.ok().build();
    }
}