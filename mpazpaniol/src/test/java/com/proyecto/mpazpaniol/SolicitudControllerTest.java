package com.proyecto.mpazpaniol;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.proyecto.mpazpaniol.controller.SolicitudController;
import com.proyecto.mpazpaniol.entity.SolicitudEntity;
import com.proyecto.mpazpaniol.repository.SolicitudRepository;

/**
 * Pruebas unitarias para SolicitudController utilizando JUnit 5 y Mockito.
 * Valida las reglas de negocio del microservicio de pañol de forma aislada.
 */
@ExtendWith(MockitoExtension.class)
public class SolicitudControllerTest {

    @Mock
    private SolicitudRepository solicitudRepository;

    @InjectMocks
    private SolicitudController solicitudController;

    private SolicitudEntity solicitudBase;

    @BeforeEach
    void setUp() {
        solicitudBase = new SolicitudEntity();
        solicitudBase.setId(1L);
        solicitudBase.setEquipoId(101L);
        solicitudBase.setSolicitanteEmail("jaciel.soto@duocuc.cl");
        solicitudBase.setCargo("Alumnos");
        solicitudBase.setSala("Sala 402");
        solicitudBase.setFechaReserva("2026-11-12 18:30");
        solicitudBase.setEstado("PENDIENTE");
    }

    @Test
    @DisplayName("GET /api/solicitudes - Debe retornar la lista de solicitudes registradas")
    void testObtenerTodas() {
        // 1. Arrange: Simulamos que la BD tiene 1 solicitud
        when(solicitudRepository.findAll()).thenReturn(Arrays.asList(solicitudBase));

        // 2. Act: Ejecutamos el endpoint del controlador
        List<SolicitudEntity> resultado = solicitudController.obtenerTodas();

        // 3. Assert: Verificamos tamaño y datos
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("jaciel.soto@duocuc.cl", resultado.get(0).getSolicitanteEmail());
        verify(solicitudRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("POST /api/solicitudes - Debe forzar el estado PENDIENTE al crear una solicitud")
    void testCrearSolicitud() {
        // 1. Arrange: Simulamos el guardado devolviendo el mismo objeto recibido
        when(solicitudRepository.save(any(SolicitudEntity.class))).thenAnswer(invocacion -> {
            SolicitudEntity guardada = invocacion.getArgument(0);
            guardada.setId(1L);
            return guardada;
        });

        // Creamos una solicitud intentando enviarla con otro estado para probar la regla de negocio
        SolicitudEntity nuevaSolicitud = new SolicitudEntity();
        nuevaSolicitud.setEquipoId(101L);
        nuevaSolicitud.setSolicitanteEmail("docente@duocuc.cl");
        nuevaSolicitud.setEstado("ACEPTADA"); // El controlador debe sobrescribir esto a PENDIENTE

        // 2. Act
        SolicitudEntity respuesta = solicitudController.crearSolicitud(nuevaSolicitud);

        // 3. Assert: Comprobamos que el controlador aplicó la regla de negocio
        assertNotNull(respuesta);
        assertEquals("PENDIENTE", respuesta.getEstado());
        verify(solicitudRepository, times(1)).save(any(SolicitudEntity.class));
    }

    @Test
    @DisplayName("PUT /api/solicitudes/{id}/estado - Debe actualizar el estado cuando la solicitud existe")
    void testCambiarEstadoExitoso() {
        // 1. Arrange
        when(solicitudRepository.findById(1L)).thenReturn(Optional.of(solicitudBase));
        when(solicitudRepository.save(any(SolicitudEntity.class))).thenReturn(solicitudBase);

        // 2. Act
        ResponseEntity<?> respuesta = solicitudController.cambiarEstado(1L, "ACEPTADA");

        // 3. Assert
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        SolicitudEntity cuerpo = (SolicitudEntity) respuesta.getBody();
        assertNotNull(cuerpo);
        assertEquals("ACEPTADA", cuerpo.getEstado());
        verify(solicitudRepository).save(solicitudBase);
    }

    @Test
    @DisplayName("PUT /api/solicitudes/{id}/rechazar - Debe rechazar con 400 Bad Request si el motivo está vacío")
    void testRechazarSolicitudSinMotivo() {
        // 1. Arrange & 2. Act: Enviamos espacios en blanco como motivo
        ResponseEntity<?> respuesta = solicitudController.rechazarSolicitud(1L, "   ");

        // 3. Assert: Debe devolver 400 Bad Request y NUNCA llamar a la base de datos
        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertEquals("Error: El motivo de rechazo es obligatorio por regla de negocio.", respuesta.getBody());
        verify(solicitudRepository, never()).save(any());
    }

    @Test
    @DisplayName("PUT /api/solicitudes/{id}/rechazar - Debe cambiar estado a RECHAZADA y guardar el motivo")
    void testRechazarSolicitudValida() {
        // 1. Arrange
        when(solicitudRepository.findById(1L)).thenReturn(Optional.of(solicitudBase));
        when(solicitudRepository.save(any(SolicitudEntity.class))).thenReturn(solicitudBase);

        // 2. Act
        ResponseEntity<?> respuesta = solicitudController.rechazarSolicitud(1L, "No hay stock disponible");

        // 3. Assert
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        SolicitudEntity cuerpo = (SolicitudEntity) respuesta.getBody();
        assertNotNull(cuerpo);
        assertEquals("RECHAZADA", cuerpo.getEstado());
        assertEquals("No hay stock disponible", cuerpo.getMotivoRechazo());
        verify(solicitudRepository, times(1)).save(solicitudBase);
    }
}