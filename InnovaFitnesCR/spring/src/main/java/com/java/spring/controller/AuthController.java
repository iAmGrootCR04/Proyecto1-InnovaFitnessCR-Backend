package com.java.spring.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import com.java.spring.dto.AuthResponse;
import com.java.spring.dto.LoginRequest;
import com.java.spring.entity.Usuario;
import com.java.spring.repository.UsuarioRepository;
import com.java.spring.security.JwtUtil;

@RestController
@RequestMapping("/auth")
public class AuthController {

        private final AuthenticationManager authenticationManager;
        private final UserDetailsService userDetailsService;
        private final JwtUtil jwtUtil;
        private final UsuarioRepository usuarioRepository;

        public AuthController(AuthenticationManager authenticationManager,
                        UserDetailsService userDetailsService,
                        JwtUtil jwtUtil,
                        UsuarioRepository usuarioRepository) {
                this.authenticationManager = authenticationManager;
                this.userDetailsService = userDetailsService;
                this.jwtUtil = jwtUtil;
                this.usuarioRepository = usuarioRepository;
        }

        @PostMapping("/login")
        public ResponseEntity<?> login(@RequestBody LoginRequest request) {
                try {
                        authenticationManager.authenticate(
                                        new UsernamePasswordAuthenticationToken(
                                                        request.getUsername(),
                                                        request.getPassword()));

                        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
                        String token = jwtUtil.generateToken(userDetails);

                        Usuario usuario = usuarioRepository.findByEmail(request.getUsername())
                                        .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

                        String role = userDetails.getAuthorities().stream()
                                        .findFirst()
                                        .map(a -> a.getAuthority())
                                        .orElse("ROLE_USER");

                        return ResponseEntity.ok(new AuthResponse(token, role, usuario.getId()));

                } catch (BadCredentialsException e) {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                        .body("Credenciales inválidas");
                }
        }
}
