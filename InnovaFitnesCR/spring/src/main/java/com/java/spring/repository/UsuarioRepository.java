package com.java.spring.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.java.spring.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
  Optional<Usuario> findByEmail(String email);
}
