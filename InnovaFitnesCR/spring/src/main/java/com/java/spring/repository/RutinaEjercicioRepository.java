package com.java.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.java.spring.entity.Ejercicio;
import com.java.spring.entity.Rutina;
import com.java.spring.entity.RutinaEjercicio;

public interface RutinaEjercicioRepository extends JpaRepository<RutinaEjercicio, Long> {
    void deleteByRutinaAndEjercicio(Rutina rutina, Ejercicio ejercicio);
}