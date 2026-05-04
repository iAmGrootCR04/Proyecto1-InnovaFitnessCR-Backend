package com.java.spring.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.java.spring.dto.CategoriaRutina;
import com.java.spring.dto.GrupoMuscular;
import com.java.spring.entity.Ejercicio;
import com.java.spring.repository.EjercicioRepository;
import io.micrometer.common.lang.NonNull;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EjercicioService {
    private EjercicioRepository ejercicioRepository;

    @SuppressWarnings("deprecation")
    public Ejercicio add(@NonNull Ejercicio ejercicio) {
        return ejercicioRepository.save(ejercicio);
    }

    public List<Ejercicio> get() {
        return ejercicioRepository.findAll();
    }

    public Optional<Ejercicio> getById(long id) {
        return ejercicioRepository.findById(id);
    }

    public void delete(long id) {
        ejercicioRepository.deleteById(id);
    }

    public List<Ejercicio> obtenerEjerciciosPorCategoria(CategoriaRutina categoria) {
        List<GrupoMuscular> gruposPermitidos = ReglasCategoriaRutina.obtenerGruposPermitidos(categoria);

        if (gruposPermitidos == null || gruposPermitidos.isEmpty()) {
            return new ArrayList<>();
        }

        return ejercicioRepository.findByGrupoMuscularIn(gruposPermitidos);
    }

    public Ejercicio update(long id, @SuppressWarnings("deprecation") @NonNull Ejercicio ejercicio) {
        Optional<Ejercicio> existingrutina = ejercicioRepository.findById(id);
        if (existingrutina.isPresent()) {
            Ejercicio updateEjercicio = existingrutina.get();

            updateEjercicio.setDescripcion(ejercicio.getDescripcion());
            updateEjercicio.setCategoria(ejercicio.getCategoria());
            updateEjercicio.setNombre(ejercicio.getNombre());
            updateEjercicio.setImagenUrl(ejercicio.getImagenUrl());

            return ejercicioRepository.save(updateEjercicio);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ejercicio no encontrada");
        }
    }

    public long getTotalEjercicios() {
        return ejercicioRepository.countTotalEjercicios();
    }

    public long getCantidadCategoriasUnicas() {
        return ejercicioRepository.countCategoriasUnicas();
    }

    public List<GrupoMuscular> getListarCategoriasPresentes() {
        return ejercicioRepository.findAllCategoriasPresentes();
    }
}