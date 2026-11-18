package com.proyecto.inventario.services;

import com.proyecto.inventario.entities.Inventario;
import com.proyecto.inventario.repository.inventarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class inventarioServiceImplTest {

    @Mock
    private inventarioRepository inventarioRepository;

    @InjectMocks
    private inventarioServiceImpl inventarioService;

    @Test
    void listarDevuelveTodosLosRegistros() {
        List<Inventario> inventarios = List.of(inventario("1"));
        when(inventarioRepository.findAll()).thenReturn(inventarios);

        assertEquals(inventarios, inventarioService.listar());
    }

    @Test
    void obtenerPorIdDevuelveRegistroExistente() {
        Inventario esperado = inventario("1");
        when(inventarioRepository.findById("1")).thenReturn(Optional.of(esperado));

        assertEquals(Optional.of(esperado), inventarioService.obtenerPorId("1"));
    }

    @Test
    void obtenerPorIdDevuelveVacioCuandoNoExiste() {
        when(inventarioRepository.findById("desconocido")).thenReturn(Optional.empty());

        assertTrue(inventarioService.obtenerPorId("desconocido").isEmpty());
    }

    @Test
    void crearGuardaUnRegistroValido() {
        Inventario nuevo = inventario("1");
        when(inventarioRepository.existsById("1")).thenReturn(false);
        when(inventarioRepository.save(nuevo)).thenReturn(nuevo);

        assertEquals(nuevo, inventarioService.crear(nuevo));
        verify(inventarioRepository).save(nuevo);
    }

    @Test
    void crearRechazaEquipoIdDuplicado() {
        when(inventarioRepository.existsById("1")).thenReturn(true);

        ResponseStatusException error = assertThrows(
                ResponseStatusException.class,
                () -> inventarioService.crear(inventario("1"))
        );

        assertEquals(409, error.getStatusCode().value());
        verify(inventarioRepository, never()).save(any(Inventario.class));
    }

    @Test
    void crearRechazaStockDisponibleMayorQueTotal() {
        Inventario invalido = new Inventario("1", 5, 6, "Disponible", "Estante A1");

        ResponseStatusException error = assertThrows(
                ResponseStatusException.class,
                () -> inventarioService.crear(invalido)
        );

        assertEquals(400, error.getStatusCode().value());
        verify(inventarioRepository, never()).existsById(any());
    }

    @Test
    void crearRechazaEstadoInvalido() {
        Inventario invalido = new Inventario("1", 5, 3, "En Reparación", "Estante A1");

        ResponseStatusException error = assertThrows(
                ResponseStatusException.class,
                () -> inventarioService.crear(invalido)
        );

        assertEquals(400, error.getStatusCode().value());
        verify(inventarioRepository, never()).existsById(any());
    }

    @Test
    void cambiarEstadoGuardaElNuevoEstado() {
        Inventario existente = inventario("1");
        when(inventarioRepository.findById("1")).thenReturn(Optional.of(existente));
        when(inventarioRepository.save(existente)).thenReturn(existente);

        Optional<Inventario> actualizado = inventarioService.cambiarEstado("1", "Prestado");

        assertTrue(actualizado.isPresent());
        assertEquals("Prestado", actualizado.get().getEstado());
        verify(inventarioRepository).save(existente);
    }

    @Test
    void actualizarStockRechazaValorSobreElTotal() {
        when(inventarioRepository.findById("1")).thenReturn(Optional.of(inventario("1")));

        ResponseStatusException error = assertThrows(
                ResponseStatusException.class,
                () -> inventarioService.actualizarStock("1", 9)
        );

        assertEquals(400, error.getStatusCode().value());
        verify(inventarioRepository, never()).save(any(Inventario.class));
    }

    @Test
    void darDeBajaCambiaElEstadoYGuarda() {
        Inventario existente = inventario("1");
        when(inventarioRepository.findById("1")).thenReturn(Optional.of(existente));
        when(inventarioRepository.save(existente)).thenReturn(existente);

        Optional<Inventario> actualizado = inventarioService.darDeBaja("1");

        assertTrue(actualizado.isPresent());
        assertEquals("Fuera de Servicio", actualizado.get().getEstado());
        verify(inventarioRepository).save(existente);
    }

    @Test
    void actualizarNoRequiereEquipoIdEnElCuerpo() {
        Inventario existente = inventario("1");
        Inventario datos = new Inventario(null, 10, 4, "Reservado", "Estante A2");
        when(inventarioRepository.findById("1")).thenReturn(Optional.of(existente));
        when(inventarioRepository.save(existente)).thenReturn(existente);

        Optional<Inventario> actualizado = inventarioService.actualizar("1", datos);

        assertTrue(actualizado.isPresent());
        assertEquals("1", actualizado.get().getEquipoId());
        assertEquals(10, actualizado.get().getStockTotal());
        assertEquals("Estante A2", actualizado.get().getUbicacionFisica());
        verify(inventarioRepository).save(existente);
    }

    @Test
    void actualizarInexistenteDevuelveVacio() {
        when(inventarioRepository.findById("desconocido")).thenReturn(Optional.empty());

        assertFalse(inventarioService.actualizar("desconocido", null).isPresent());
    }

    private Inventario inventario(String equipoId) {
        return new Inventario(equipoId, 8, 5, "Disponible", "Estante A1");
    }
}
