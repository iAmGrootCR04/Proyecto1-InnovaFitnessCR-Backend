package com.java.spring.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.java.spring.entity.Rutina;

public interface RutinaRepository extends JpaRepository<Rutina, Long> {

    @Query("SELECT COUNT(r) FROM Rutina r")
    long countTotalRutinas();

    @Query("SELECT r.nivelDificultad, COUNT(r) FROM Rutina r GROUP BY r.nivelDificultad")
    List<Object[]> countRutinasPorDificultad();
}