package com.proyecto.inventario.services;

import com.proyecto.inventario.entities.Inventario;

import java.util.List;
import java.util.Optional;

public interface inventarioService {

    List<Inventario> listar();

    Optional<Inventario> obtenerPorId(String equipoId);

    Inventario crear(Inventario inventario);

    Optional<Inventario> actualizar(String equipoId, Inventario inventario);

    Optional<Inventario> cambiarEstado(String equipoId, String estado);

    Optional<Inventario> actualizarStock(String equipoId, Integer stockDisponible);

    Optional<Inventario> darDeBaja(String equipoId);
}
