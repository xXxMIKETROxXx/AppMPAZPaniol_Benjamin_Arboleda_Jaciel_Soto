package com.proyecto.mpazpaniol.entity;

import jakarta.persistence.*;

/**
 * Entidad JPA que define cómo se creará la tabla "solicitudes" en PostgreSQL.
 */
@Entity
@Table(name = "solicitudes")
public class SolicitudEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Clave primaria autoincrementable en PostgreSQL

    private Long equipoId;          // ID del equipo solicitado en el pañol
    private String solicitanteEmail; // Correo institucional del docente o alumno
    private String cargo;            // Ej: "Docente", "Alumnos"
    private String sala;             // Ej: "Sala 402"
    private String fechaReserva;     // Fecha y hora del préstamo
    private String estado;           // "PENDIENTE", "ACEPTADA", "ENTREGADA", "DEVUELTA", "RECHAZADA"
    
    @Column(length = 500)
    private String motivoRechazo;    // Justificación obligatoria en caso de rechazo

    // Constructor vacío obligatorio exigido por la especificación JPA
    public SolicitudEntity() {}

    // Getters y Setters para encapsulamiento
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getEquipoId() { return equipoId; }
    public void setEquipoId(Long equipoId) { this.equipoId = equipoId; }

    public String getSolicitanteEmail() { return solicitanteEmail; }
    public void setSolicitanteEmail(String solicitanteEmail) { this.solicitanteEmail = solicitanteEmail; }

    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }

    public String getSala() { return sala; }
    public void setSala(String sala) { this.sala = sala; }

    public String getFechaReserva() { return fechaReserva; }
    public void setFechaReserva(String fechaReserva) { this.fechaReserva = fechaReserva; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getMotivoRechazo() { return motivoRechazo; }
    public void setMotivoRechazo(String motivoRechazo) { this.motivoRechazo = motivoRechazo; }
}