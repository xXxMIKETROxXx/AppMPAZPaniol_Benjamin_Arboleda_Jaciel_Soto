package com.proyecto.equipos.services;

import com.proyecto.equipos.entities.Equipo;

import java.util.List;
import java.util.Optional;

public interface equipoService {

    List<Equipo> listar();

    Optional<Equipo> obtenerPorId(String id);

    List<Equipo> listarPorCategoria(String categoria);

    Equipo crear(Equipo equipo);

    Optional<Equipo> actualizar(String id, Equipo equipo);
}
