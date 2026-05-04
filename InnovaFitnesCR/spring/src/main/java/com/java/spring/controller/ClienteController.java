package com.java.spring.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.java.spring.entity.Cliente;
import com.java.spring.service.ClienteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin(origins = "*")
@Tag(name = "Clientes", description = "API para gestionar clientes")
@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    @Autowired
    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un cliente por ID")
    public Cliente getById(@PathVariable long id) {
        return clienteService.getById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}/update-profile")
    @Operation(summary = "Actualizar perfil (peso, altura y seguridad)")
    public ResponseEntity<?> updateProfile(@PathVariable Long id, @RequestBody Map<String, Object> dto) {
        try {
            clienteService.updateProfile(id, dto);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping
    @Operation(summary = "Obtener todos los clientes")
    public List<Cliente> get() {
        return clienteService.get();
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo cliente")
    public Cliente add(@RequestBody @NonNull Cliente cliente) {
        return clienteService.add(cliente);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modificar un cliente")
    public Cliente update(@PathVariable long id, @RequestBody @NonNull Cliente cliente) {
        return clienteService.update(id, cliente);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un cliente")
    public void delete(@PathVariable long id) {
        clienteService.delete(id);
    }

    // --- GESTIÓN DE RUTINAS FAVORITAS ---
    @PostMapping("/{clienteId}/favoritos/{rutinaId}")
    @Operation(summary = "Agregar una rutina a los favoritos del cliente")
    public void agregarFavorita(@PathVariable Long clienteId, @PathVariable Long rutinaId) {
        clienteService.agregarRutinaAFavoritos(clienteId, rutinaId);
    }

    @DeleteMapping("/{clienteId}/favoritos/{rutinaId}")
    @Operation(summary = "Eliminar una rutina de los favoritos del cliente")
    public void eliminarFavorita(@PathVariable Long clienteId, @PathVariable Long rutinaId) {
        clienteService.eliminarRutinaDeFavoritos(clienteId, rutinaId);
    }

    @GetMapping("/stats/total")
    @Operation(summary = "Cantidad total de clientes")
    public long getTotalClientes() {
        return clienteService.getTotalClientes();
    }

    @GetMapping("/stats/ingresos")
    @Operation(summary = "Suma total de mensualidades")
    public Double getIngresosTotales() {
        return clienteService.getIngresoMensualTotal();
    }

    @GetMapping("/stats/promedio-edad")
    @Operation(summary = "Edad promedio de los clientes")
    public Double getPromedioEdad() {
        return clienteService.getPromedioEdad();
    }

    @GetMapping("/stats/promedio-altura")
    @Operation(summary = "Altura promedio de los clientes")
    public Double getPromedioAltura() {
        return clienteService.getPromedioAltura();
    }

    @GetMapping("/stats/promedio-peso")
    @Operation(summary = "Peso promedio de los clientes")
    public Double getPromedioPeso() {
        return clienteService.getPromedioPeso();
    }

    @GetMapping("/stats/promedio-mensualidad")
    @Operation(summary = "Monto promedio pagado por mensualidad")
    public Double getPromedioMensualidad() {
        return clienteService.getPromedioMensualidad();
    }
}