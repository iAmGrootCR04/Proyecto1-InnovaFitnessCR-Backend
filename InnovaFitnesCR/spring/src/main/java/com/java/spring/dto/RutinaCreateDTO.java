package com.java.spring.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RutinaCreateDTO {
    private String nombre;
    private String descripcion;
    private String nivelDificultad;
    private String categoria;
    private List<RutinaEjercicioDTO> ejercicios;
}