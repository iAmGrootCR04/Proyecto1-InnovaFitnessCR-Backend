package com.java.spring.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.java.spring.entity.Usuario;
import com.java.spring.repository.UsuarioRepository;

import io.micrometer.common.lang.NonNull;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UsuarioService {
    private UsuarioRepository usuarioRepository;
    private PasswordEncoder passwordEncoder;

    @SuppressWarnings("deprecation")
    public Usuario add(@NonNull Usuario usuario) {
        usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        return usuarioRepository.save(usuario);
    }

    public List<Usuario> get() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> getById(long id) {
        return usuarioRepository.findById(id);
    }

    public void delete(long id) {
        usuarioRepository.deleteById(id);
    }

    @SuppressWarnings("deprecation")
    public Usuario update(long id, @NonNull Usuario usuario) {
        Optional<Usuario> existingUsuario = usuarioRepository.findById(id);
        if (existingUsuario.isPresent()) {
            Usuario updatedUsuario = existingUsuario.get();
            updatedUsuario.setNombre((usuario.getNombre()));

            if (usuario.getContrasena() != null && !usuario.getContrasena().isEmpty()) {
                updatedUsuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
            }

            updatedUsuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
            updatedUsuario.setEmail(usuario.getEmail());

            updatedUsuario.setRol(usuario.getRol());

            updatedUsuario.setTelefono(usuario.getTelefono());

            return usuarioRepository.save(updatedUsuario);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrada");
        }
    }
}
