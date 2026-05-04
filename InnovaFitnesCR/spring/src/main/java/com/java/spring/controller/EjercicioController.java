package com.java.spring.controller;

import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import com.java.spring.dto.CategoriaRutina;
import com.java.spring.dto.GrupoMuscular;
import com.java.spring.entity.Ejercicio;
import com.java.spring.service.CloudinaryService;
import com.java.spring.service.EjercicioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "Ejercicios", description = "API para gestionar ejercicios")
@RestController
@RequestMapping("/ejercicios")
public class EjercicioController {

    @Autowired
    private EjercicioService ejercicioService;

    @Autowired
    private CloudinaryService cloudinaryService;

    @GetMapping
    @Operation(summary = "Obtener todos los ejercicios")
    public List<Ejercicio> get() {
        return ejercicioService.get();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un ejercicio por ID")
    public Ejercicio getById(@PathVariable long id) {
        return ejercicioService.getById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo ejercicio")
    public Ejercicio add(@RequestBody @NonNull Ejercicio ejercicio) {
        return ejercicioService.add(ejercicio);
    }

    @GetMapping("/por-categoria/{categoria}")
    public List<Ejercicio> listarPorCategoria(@PathVariable String categoria) {
        CategoriaRutina categoriaEnum = CategoriaRutina.valueOf(categoria.toUpperCase());
        return ejercicioService.obtenerEjerciciosPorCategoria(categoriaEnum);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modificar un ejercicio")
    public Ejercicio update(@PathVariable long id, @RequestBody @NonNull Ejercicio ejercicio) {
        return ejercicioService.update(id, ejercicio);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un ejercicio")
    public void delete(@PathVariable long id) {
        ejercicioService.delete(id);
    }

    @PostMapping("/upload-image")
    @Operation(summary = "Subir una imagen a Cloudinary")
    public String uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            return cloudinaryService.uploadImage(file);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al subir la imagen: " + e.getMessage());
        }
    }

    @PostMapping("/create-with-image")
    @Operation(summary = "Crear un nuevo ejercicio con imagen")
    public Ejercicio createWithImage(
            @RequestParam("nombre") String nombre,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("categoria") String categoria,
            @RequestParam("file") MultipartFile file) {
        try {
            String imagenUrl = cloudinaryService.uploadImage(file);

            Ejercicio ejercicio = new Ejercicio();
            ejercicio.setNombre(nombre);
            ejercicio.setDescripcion(descripcion);
            ejercicio.setCategoria(GrupoMuscular.valueOf(categoria.toUpperCase()));
            ejercicio.setImagenUrl(imagenUrl);

            return ejercicioService.add(ejercicio);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al crear ejercicio con imagen: " + e.getMessage());
        }
    }

    @PutMapping(path = "/{id}/with-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Actualizar un ejercicio con imagen")
    public Ejercicio updateWithImage(
            @PathVariable long id,
            @RequestParam("nombre") String nombre,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("categoria") String categoria,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            Ejercicio ejercicio = ejercicioService.getById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

            if (file != null && !file.isEmpty()) {
                String imagenUrl = cloudinaryService.uploadImage(file);
                ejercicio.setImagenUrl(imagenUrl);
            }

            ejercicio.setNombre(nombre);
            ejercicio.setDescripcion(descripcion);
            ejercicio.setCategoria(GrupoMuscular.valueOf(categoria.toUpperCase()));

            return ejercicioService.update(id, ejercicio);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al actualizar ejercicio: " + e.getMessage());
        }
    }

    @GetMapping("/stats/total")
    @Operation(summary = "Cantidad total de ejercicios registrados")
    public long getTotalEjercicios() {
        return ejercicioService.getTotalEjercicios();
    }

    @GetMapping("/stats/categorias-count")
    @Operation(summary = "Cantidad de categorías únicas en uso")
    public long getCountCategorias() {
        return ejercicioService.getCantidadCategoriasUnicas();
    }

    @GetMapping("/stats/categorias-lista")
    @Operation(summary = "Lista de nombres de categorías presentes en la BD")
    public List<GrupoMuscular> getListaCategorias() {
        return ejercicioService.getListarCategoriasPresentes();
    }
}