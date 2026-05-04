package com.java.spring.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.java.spring.entity.Usuario;
import com.java.spring.repository.UsuarioRepository;;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initUsers(UsuarioRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByEmail("admin").isEmpty()) {
                userRepository.save(new Usuario(
                        "Itorino",
                        "admin",
                        passwordEncoder.encode("admin"),
                        "ROLE_ADMIN",
                        "8888-8888"));
            }
            if (userRepository.findByEmail("user").isEmpty()) {
                userRepository.save(new Usuario(
                        "Yo",
                        "user",
                        passwordEncoder.encode("user"),
                        "ROLE_USER",
                        "8888-8888"));
            }
        };
    }
}
