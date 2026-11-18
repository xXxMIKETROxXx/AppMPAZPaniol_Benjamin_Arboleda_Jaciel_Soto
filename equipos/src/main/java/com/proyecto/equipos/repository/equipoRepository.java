package com.proyecto.equipos.repository;

import com.proyecto.equipos.entities.Equipo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface equipoRepository extends JpaRepository<Equipo, String> {

    List<Equipo> findByCategoria(String categoria);
}
