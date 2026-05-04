package com.java.spring.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.java.spring.dto.GrupoMuscular;
import com.java.spring.entity.Ejercicio;

public interface EjercicioRepository extends JpaRepository<Ejercicio, Long> {

    @Query("SELECT e FROM Ejercicio e WHERE e.categoria IN :grupos")
    List<Ejercicio> findByGrupoMuscularIn(@Param("grupos") List<GrupoMuscular> grupos);

    @Query("SELECT COUNT(e) FROM Ejercicio e")
    long countTotalEjercicios();

    @Query("SELECT COUNT(DISTINCT e.categoria) FROM Ejercicio e")
    long countCategoriasUnicas();

    @Query("SELECT DISTINCT e.categoria FROM Ejercicio e")
    List<GrupoMuscular> findAllCategoriasPresentes();
}