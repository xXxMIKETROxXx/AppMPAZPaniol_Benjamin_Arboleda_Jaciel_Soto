package com.proyecto.inventario.repository;

import com.proyecto.inventario.entities.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface inventarioRepository extends JpaRepository<Inventario, String> {
}
