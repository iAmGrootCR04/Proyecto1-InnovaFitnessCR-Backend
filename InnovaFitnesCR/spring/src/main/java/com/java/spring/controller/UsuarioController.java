package com.java.spring.controller;

import java.util.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import com.java.spring.entity.Usuario;
import com.java.spring.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin(origins = "*")
@Tag(name = "Usuarios", description = "API para gestionar usuarios")
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    @Operation(summary = "Obtener todos los usuarios", description = "Devuelve una lista de usuarios")
    public List<Usuario> get() {
        return usuarioService.get();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un usuario por ID", description = "Busca un usuario en la base de datos según su ID")
    public Usuario getById(@PathVariable long id) {
        return usuarioService.getById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo usuario", description = "Agrega un nuevo usuario a la base de datos")
    public Usuario add(@RequestBody Usuario usuario) {
        return usuarioService.add(usuario);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modificar un usuario", description = "Modifica un usuario existente en la base de datos")
    public Usuario update(@PathVariable long id, @RequestBody Usuario usuario) {
        return usuarioService.update(id, usuario);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un usuario", description = "Elimina un usuario de la base de datos")
    public void delete(@PathVariable long id) {
        usuarioService.delete(id);
    }
}
