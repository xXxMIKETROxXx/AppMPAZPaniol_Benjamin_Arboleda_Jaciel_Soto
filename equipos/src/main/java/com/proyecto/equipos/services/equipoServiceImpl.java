package com.proyecto.equipos.services;

import com.proyecto.equipos.entities.Equipo;
import com.proyecto.equipos.repository.equipoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class equipoServiceImpl implements equipoService {

    private final equipoRepository equipoRepository;

    public equipoServiceImpl(equipoRepository equipoRepository) {
        this.equipoRepository = equipoRepository;
    }

    @Override
    public List<Equipo> listar() {
        return equipoRepository.findAll();
    }

    @Override
    public Optional<Equipo> obtenerPorId(String id) {
        return equipoRepository.findById(id);
    }

    @Override
    public List<Equipo> listarPorCategoria(String categoria) {
        if (categoria == null || categoria.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La categoria es obligatoria");
        }
        return equipoRepository.findByCategoria(categoria);
    }

    @Override
    public Equipo crear(Equipo equipo) {
        validar(equipo);
        if (equipo.getId() == null || equipo.getId().isBlank()) {
            equipo.setId(UUID.randomUUID().toString());
        } else if (equipoRepository.existsById(equipo.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El equipo ya existe");
        }
        return equipoRepository.save(equipo);
    }

    @Override
    public Optional<Equipo> actualizar(String id, Equipo datos) {
        return equipoRepository.findById(id).map(equipo -> {
            validar(datos);
            equipo.setNombre(datos.getNombre());
            equipo.setMarca(datos.getMarca());
            equipo.setModelo(datos.getModelo());
            equipo.setCategoria(datos.getCategoria());
            return equipoRepository.save(equipo);
        });
    }

    private void validar(Equipo equipo) {
        if (equipo == null
                || vacio(equipo.getNombre())
                || vacio(equipo.getMarca())
                || vacio(equipo.getModelo())
                || vacio(equipo.getCategoria())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Nombre, marca, modelo y categoria son obligatorios");
        }
    }

    private boolean vacio(String valor) {
        return valor == null || valor.isBlank();
    }
}
