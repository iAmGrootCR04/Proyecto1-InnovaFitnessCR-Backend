package com.java.spring.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.java.spring.entity.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findByUsuarioEmail(String email);

    @Query("SELECT COUNT(c) FROM Cliente c")
    long countTotalClientes();

    @Query("SELECT SUM(c.mensualidad) FROM Cliente c")
    Double sumIngresoMensualTotal();

    @Query(value = "SELECT AVG(TIMESTAMPDIFF(YEAR, c.fecha_nacimiento, CURDATE())) FROM clientes c", nativeQuery = true)
    Double avgEdadClientes();

    @Query("SELECT AVG(c.altura) FROM Cliente c")
    Double avgAlturaClientes();

    @Query("SELECT AVG(c.peso) FROM Cliente c")
    Double avgPesoClientes();

    @Query("SELECT AVG(c.mensualidad) FROM Cliente c")
    Double avgMensualidad();
}