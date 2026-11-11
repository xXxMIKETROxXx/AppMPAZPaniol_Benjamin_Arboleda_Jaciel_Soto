package com.proyecto.mpazpaniol.controller;

import com.proyecto.mpazpaniol.entity.SolicitudEntity;
import com.proyecto.mpazpaniol.repository.SolicitudRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST que expone las rutas HTTP a las que se conectará la app de Android vía Retrofit.
 */
@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudController {

    private final SolicitudRepository solicitudRepository;

    // Inyección de dependencias por constructor
    public SolicitudController(SolicitudRepository solicitudRepository) {
        this.solicitudRepository = solicitudRepository;
    }

    // GET: http://localhost:8080/api/solicitudes (Obtener todas)
    @GetMapping
    public List<SolicitudEntity> obtenerTodas() {
        return solicitudRepository.findAll();
    }

    // POST: http://localhost:8080/api/solicitudes (Crear nueva reserva)
    @PostMapping
    public SolicitudEntity crearSolicitud(@RequestBody SolicitudEntity solicitud) {
        solicitud.setEstado("PENDIENTE"); // Fuerza el estado inicial por regla de negocio
        return solicitudRepository.save(solicitud);
    }

    // PUT: http://localhost:8080/api/solicitudes/{id}/estado?nuevoEstado=ACEPTADA
    @PutMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstado(@PathVariable Long id, @RequestParam String nuevoEstado) {
        return solicitudRepository.findById(id).map(solicitud -> {
            solicitud.setEstado(nuevoEstado);
            solicitudRepository.save(solicitud);
            return ResponseEntity.ok(solicitud);
        }).orElse(ResponseEntity.notFound().build());
    }

    // PUT: http://localhost:8080/api/solicitudes/{id}/rechazar?motivo=No+hay+stock
    // Regla de Negocio: Rechazo con motivo obligatorio
    @PutMapping("/{id}/rechazar")
    public ResponseEntity<?> rechazarSolicitud(@PathVariable Long id, @RequestParam String motivo) {
        if (motivo == null || motivo.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Error: El motivo de rechazo es obligatorio por regla de negocio.");
        }

        return solicitudRepository.findById(id).map(solicitud -> {
            solicitud.setEstado("RECHAZADA");
            solicitud.setMotivoRechazo(motivo);
            solicitudRepository.save(solicitud);
            return ResponseEntity.ok(solicitud);
        }).orElse(ResponseEntity.notFound().build());
    }
}