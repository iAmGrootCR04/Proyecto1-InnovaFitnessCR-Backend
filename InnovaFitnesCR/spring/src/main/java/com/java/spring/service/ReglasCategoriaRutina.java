package com.java.spring.service;

import java.util.List;
import java.util.Map;

import com.java.spring.dto.CategoriaRutina;
import com.java.spring.dto.GrupoMuscular;

public class ReglasCategoriaRutina {

        private static final Map<CategoriaRutina, List<GrupoMuscular>> REGLAS = Map.of(
                        CategoriaRutina.CUERPO_COMPLETO, List.of(
                                        GrupoMuscular.PIERNAS,
                                        GrupoMuscular.PECHO,
                                        GrupoMuscular.ESPALDA,
                                        GrupoMuscular.HOMBROS,
                                        GrupoMuscular.BRAZOS,
                                        GrupoMuscular.CORE),
                        CategoriaRutina.TREN_SUPERIOR, List.of(
                                        GrupoMuscular.PECHO,
                                        GrupoMuscular.ESPALDA,
                                        GrupoMuscular.HOMBROS,
                                        GrupoMuscular.BRAZOS),
                        CategoriaRutina.TREN_INFERIOR, List.of(
                                        GrupoMuscular.PIERNAS,
                                        GrupoMuscular.GLUTEO,
                                        GrupoMuscular.CORE));

        public static List<GrupoMuscular> obtenerGruposPermitidos(CategoriaRutina categoria) {
                return REGLAS.getOrDefault(categoria, List.of());
        }
}