package com.java.spring.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.java.spring.entity.Cliente;
import com.java.spring.entity.Rutina;
import com.java.spring.entity.Usuario;
import com.java.spring.repository.ClienteRepository;
import com.java.spring.repository.RutinaRepository;
import com.java.spring.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final RutinaRepository rutinaRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void updateProfile(Long id, Map<String, Object> datos) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado"));

        if (datos.get("peso") != null) {
            cliente.setPeso(Double.parseDouble(datos.get("peso").toString()));
        }

        if (datos.get("altura") != null) {
            cliente.setAltura(Double.parseDouble(datos.get("altura").toString()));
        }

        if (datos.get("passwordNueva") != null && !datos.get("passwordNueva").toString().isEmpty()) {
            String passActual = (String) datos.get("passwordActual");
            String passNueva = (String) datos.get("passwordNueva");

            Usuario usuario = cliente.getUsuario();

            if (passwordEncoder.matches(passActual, usuario.getContrasena())) {
                usuario.setContrasena(passwordEncoder.encode(passNueva));
                usuarioRepository.save(usuario);
            } else {
                throw new RuntimeException("La contraseña actual es incorrecta");
            }
        }

        clienteRepository.save(cliente);
    }

    public Cliente add(@NonNull Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public List<Cliente> get() {
        List<Cliente> clientes = clienteRepository.findAll();
        return new ArrayList<>(clientes);
    }

    public Optional<Cliente> getById(long id) {
        return clienteRepository.findById(id);
    }

    public void delete(long id) {
        clienteRepository.deleteById(id);
    }

    @Transactional
    public Cliente update(long id, @NonNull Cliente cliente) {
        return clienteRepository.findById(id).map(existingCliente -> {
            existingCliente.setUsuario(cliente.getUsuario());
            existingCliente.setPeso(cliente.getPeso());
            existingCliente.setAltura(cliente.getAltura());
            existingCliente.setMensualidad(cliente.getMensualidad());
            existingCliente.setFechaNacimiento(cliente.getFechaNacimiento());
            return clienteRepository.save(existingCliente);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado"));
    }

    @Transactional
    public void agregarRutinaAFavoritos(Long clienteId, Long rutinaId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado"));

        Rutina rutina = rutinaRepository.findById(rutinaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rutina no encontrada"));

        cliente.getRutinasFavoritas().add(rutina);
        clienteRepository.save(cliente);
    }

    @Transactional
    public void eliminarRutinaDeFavoritos(Long clienteId, Long rutinaId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado"));

        cliente.getRutinasFavoritas().removeIf(r -> r.getId().equals(rutinaId));
        clienteRepository.save(cliente);
    }

    public Optional<Cliente> obtenerPorEmail(String email) {
        return clienteRepository.findByUsuarioEmail(email);
    }

    public long getTotalClientes() {
        return clienteRepository.countTotalClientes();
    }

    public Double getIngresoMensualTotal() {
        Double total = clienteRepository.sumIngresoMensualTotal();
        return total != null ? total : 0.0;
    }

    public Double getPromedioEdad() {
        Double avg = clienteRepository.avgEdadClientes();
        return avg != null ? avg : 0.0;
    }

    public Double getPromedioAltura() {
        Double avg = clienteRepository.avgAlturaClientes();
        return avg != null ? avg : 0.0;
    }

    public Double getPromedioPeso() {
        Double avg = clienteRepository.avgPesoClientes();
        return avg != null ? avg : 0.0;
    }

    public Double getPromedioMensualidad() {
        Double avg = clienteRepository.avgMensualidad();
        return avg != null ? avg : 0.0;
    }
}