package com.java.spring.service;

import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.java.spring.entity.RutinaEjercicio;
import com.java.spring.repository.RutinaEjercicioRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RutinaEjercicioService {

    private final RutinaEjercicioRepository rutinaEjercicioRepository;

    public RutinaEjercicio add(RutinaEjercicio rutinaEjercicio) {
        return rutinaEjercicioRepository.save(rutinaEjercicio);
    }

    public List<RutinaEjercicio> get() {
        return rutinaEjercicioRepository.findAll();
    }

    public Optional<RutinaEjercicio> getById(long id) {
        return rutinaEjercicioRepository.findById(id);
    }

    public void delete(long id) {
        rutinaEjercicioRepository.deleteById(id);
    }

    public RutinaEjercicio update(long id, RutinaEjercicio rutinaEjercicio) {
        return rutinaEjercicioRepository.findById(id)
                .map(existing -> {
                    existing.setRutina(rutinaEjercicio.getRutina());
                    existing.setEjercicio(rutinaEjercicio.getEjercicio());
                    existing.setSeries(rutinaEjercicio.getSeries());
                    existing.setRepeticiones(rutinaEjercicio.getRepeticiones());
                    existing.setDescanso(rutinaEjercicio.getDescanso());
                    return rutinaEjercicioRepository.save(existing);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "RutinaEjercicio no encontrado"));
    }
}
