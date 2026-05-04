package com.java.spring.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RutinaEjercicioDTO {
    private Long ejercicioId;
    private Long series;
    private Long repeticiones;
    private String descanso;
    private Long orden;
}
