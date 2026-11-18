package com.proyecto.inventario.services;

import com.proyecto.inventario.entities.Inventario;
import com.proyecto.inventario.repository.inventarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class inventarioServiceImpl implements inventarioService {

    private static final Set<String> ESTADOS_PERMITIDOS = Set.of(
            "Disponible", "Reservado", "Prestado", "En Mantenimiento", "Fuera de Servicio"
    );

    private final inventarioRepository inventarioRepository;

    public inventarioServiceImpl(inventarioRepository inventarioRepository) {
        this.inventarioRepository = inventarioRepository;
    }

    @Override
    public List<Inventario> listar() {
        return inventarioRepository.findAll();
    }

    @Override
    public Optional<Inventario> obtenerPorId(String equipoId) {
        return inventarioRepository.findById(equipoId);
    }

    @Override
    public Inventario crear(Inventario inventario) {
        validar(inventario);
        if (inventarioRepository.existsById(inventario.getEquipoId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El equipo ya existe en el inventario");
        }
        return inventarioRepository.save(inventario);
    }

    @Override
    public Optional<Inventario> actualizar(String equipoId, Inventario datos) {
        return inventarioRepository.findById(equipoId).map(inventario -> {
            validarUbicacion(datos);
            validarStocks(datos.getStockTotal(), datos.getStockDisponible());
            validarEstado(datos.getEstado());
            inventario.setStockTotal(datos.getStockTotal());
            inventario.setStockDisponible(datos.getStockDisponible());
            inventario.setEstado(datos.getEstado());
            inventario.setUbicacionFisica(datos.getUbicacionFisica());
            return inventarioRepository.save(inventario);
        });
    }

    @Override
    public Optional<Inventario> cambiarEstado(String equipoId, String estado) {
        validarEstado(estado);
        return inventarioRepository.findById(equipoId).map(inventario -> {
            inventario.setEstado(estado);
            return inventarioRepository.save(inventario);
        });
    }

    @Override
    public Optional<Inventario> actualizarStock(String equipoId, Integer stockDisponible) {
        if (stockDisponible == null || stockDisponible < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El stock disponible no puede ser negativo");
        }
        return inventarioRepository.findById(equipoId).map(inventario -> {
            if (stockDisponible > inventario.getStockTotal()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "El stock disponible no puede superar el stock total");
            }
            inventario.setStockDisponible(stockDisponible);
            return inventarioRepository.save(inventario);
        });
    }

    @Override
    public Optional<Inventario> darDeBaja(String equipoId) {
        return inventarioRepository.findById(equipoId).map(inventario -> {
            inventario.setEstado("Fuera de Servicio");
            return inventarioRepository.save(inventario);
        });
    }

    private void validar(Inventario inventario) {
        if (inventario == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El inventario es obligatorio");
        }
        validarCampos(inventario);
        validarStocks(inventario.getStockTotal(), inventario.getStockDisponible());
        validarEstado(inventario.getEstado());
    }

    private void validarCampos(Inventario inventario) {
        if (inventario == null || inventario.getEquipoId() == null || inventario.getEquipoId().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El equipoId es obligatorio");
        }
        validarUbicacion(inventario);
    }

    private void validarUbicacion(Inventario inventario) {
        if (inventario == null || inventario.getUbicacionFisica() == null
                || inventario.getUbicacionFisica().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La ubicacionFisica es obligatoria");
        }
    }

    private void validarStocks(Integer stockTotal, Integer stockDisponible) {
        if (stockTotal == null || stockTotal < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El stock total debe ser mayor o igual a cero");
        }
        if (stockDisponible == null || stockDisponible < 0 || stockDisponible > stockTotal) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El stock disponible debe estar entre cero y el stock total");
        }
    }

    private void validarEstado(String estado) {
        if (estado == null || !ESTADOS_PERMITIDOS.contains(estado)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El estado no es válido");
        }
    }
}
