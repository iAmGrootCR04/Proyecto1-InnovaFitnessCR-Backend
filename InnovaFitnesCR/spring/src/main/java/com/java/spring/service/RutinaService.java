package com.java.spring.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.java.spring.dto.CategoriaRutina;
import com.java.spring.dto.RutinaCreateDTO;
import com.java.spring.dto.RutinaEjercicioDTO;
import com.java.spring.entity.Ejercicio;
import com.java.spring.entity.Rutina;
import com.java.spring.entity.RutinaEjercicio;
import com.java.spring.repository.EjercicioRepository;
import com.java.spring.repository.RutinaEjercicioRepository;
import com.java.spring.repository.RutinaRepository;
import io.micrometer.common.lang.NonNull;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RutinaService {
    private final RutinaRepository rutinaRepository;
    private final EjercicioRepository ejercicioRepository;
    private final RutinaEjercicioRepository ejercicioRutinaRepository;

    @SuppressWarnings("deprecation")
    public Rutina add(@NonNull RutinaCreateDTO dto) {
        Rutina rutina = new Rutina();
        rutina.setNombre(dto.getNombre());
        rutina.setDescripcion(dto.getDescripcion());

        CategoriaRutina categoria = CategoriaRutina.valueOf(dto.getCategoria().toUpperCase());
        rutina.setCategoria(categoria);
        rutina.setNivelDificultad(dto.getNivelDificultad());

        Rutina rutinaGuardada = rutinaRepository.save(rutina);

        if (dto.getEjercicios() != null && !dto.getEjercicios().isEmpty()) {
            List<RutinaEjercicio> relaciones = agregarEjercicios(rutinaGuardada, dto.getEjercicios());
            rutinaGuardada.setEjerciciosRutina(relaciones);
        }

        return rutinaGuardada;
    }

    public List<Rutina> get() {
        return rutinaRepository.findAll();
    }

    public Optional<Rutina> getById(long id) {
        return rutinaRepository.findById(id);
    }

    public void delete(long id) {
        rutinaRepository.deleteById(id);
    }

    public Rutina update(long id, @SuppressWarnings("deprecation") @NonNull Rutina rutina) {
        return rutinaRepository.findById(id)
                .map(existing -> {
                    existing.setNombre(rutina.getNombre());
                    existing.setDescripcion(rutina.getDescripcion());
                    existing.setNivelDificultad(rutina.getNivelDificultad());
                    existing.setCategoria(rutina.getCategoria());

                    if (rutina.getEjerciciosRutina() != null) {
                        rutina.getEjerciciosRutina().forEach(re -> re.setRutina(existing));
                        existing.setEjerciciosRutina(rutina.getEjerciciosRutina());
                    }

                    return rutinaRepository.save(existing);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rutina no encontrada"));
    }

    private List<RutinaEjercicio> agregarEjercicios(Rutina rutina, List<RutinaEjercicioDTO> ejerciciosDTO) {
        List<RutinaEjercicio> relaciones = ejerciciosDTO.stream().map(dto -> {
            Ejercicio ejercicio = ejercicioRepository.findById(dto.getEjercicioId())
                    .orElseThrow(() -> new RuntimeException(
                            "Ejercicio no encontrado: " + dto.getEjercicioId()));

            RutinaEjercicio re = new RutinaEjercicio();
            re.setRutina(rutina);
            re.setEjercicio(ejercicio);
            re.setSeries(dto.getSeries());
            re.setRepeticiones(dto.getRepeticiones());
            re.setDescanso(dto.getDescanso());
            re.setOrdenEjercicio(dto.getOrden());
            return re;
        })
                .collect(Collectors.toList());

        return ejercicioRutinaRepository.saveAll(relaciones);
    }

    public long getTotalRutinas() {
        return rutinaRepository.countTotalRutinas();
    }

    public List<Object[]> getRutinasPorDificultad() {
        return rutinaRepository.countRutinasPorDificultad();
    }

    @Transactional
    public void actualizarRutina(Long id, RutinaCreateDTO dto) {

        Rutina rutina = rutinaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rutina no encontrada"));

        rutina.setNombre(dto.getNombre());
        rutina.setDescripcion(dto.getDescripcion());
        rutina.setNivelDificultad(dto.getNivelDificultad());
        rutina.getEjerciciosRutina().clear();

        if (dto.getEjercicios() != null) {
            for (RutinaEjercicioDTO ejDto : dto.getEjercicios()) {
                RutinaEjercicio nuevoEjercicio = new RutinaEjercicio();

                Ejercicio ej = ejercicioRepository.findById(ejDto.getEjercicioId()).get();

                nuevoEjercicio.setEjercicio(ej);
                nuevoEjercicio.setSeries(ejDto.getSeries());
                nuevoEjercicio.setRepeticiones(ejDto.getRepeticiones());
                nuevoEjercicio.setDescanso(ejDto.getDescanso());
                nuevoEjercicio.setOrdenEjercicio(ejDto.getOrden());
                nuevoEjercicio.setRutina(rutina);
                rutina.getEjerciciosRutina().add(nuevoEjercicio);
            }
        }
        rutinaRepository.save(rutina);
    }
}