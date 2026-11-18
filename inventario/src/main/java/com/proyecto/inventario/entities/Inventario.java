package com.proyecto.inventario.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "inventario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inventario {

    @Id
    private String equipoId;
    private Integer stockTotal;
    private Integer stockDisponible;
    private String estado;
    private String ubicacionFisica;
}
