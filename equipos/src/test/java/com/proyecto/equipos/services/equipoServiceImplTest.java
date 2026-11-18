package com.proyecto.equipos.services;

import com.proyecto.equipos.entities.Equipo;
import com.proyecto.equipos.repository.equipoRepository;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class equipoServiceImplTest {

    @Mock
    private equipoRepository equipoRepository;

    @InjectMocks
    private equipoServiceImpl equipoService;

    @Test
    void listarDevuelveTodosLosEquipos() {
        List<Equipo> equipos = List.of(equipo("1"));
        when(equipoRepository.findAll()).thenReturn(equipos);

        assertEquals(equipos, equipoService.listar());
    }

    @Test
    void obtenerPorIdDevuelveEquipoExistente() {
        Equipo esperado = equipo("1");
        when(equipoRepository.findById("1")).thenReturn(Optional.of(esperado));

        assertEquals(Optional.of(esperado), equipoService.obtenerPorId("1"));
    }

    @Test
    void obtenerPorIdDevuelveVacioCuandoNoExiste() {
        when(equipoRepository.findById("desconocido")).thenReturn(Optional.empty());

        assertTrue(equipoService.obtenerPorId("desconocido").isEmpty());
    }

    @Test
    void listarPorCategoriaDevuelveCoincidencias() {
        List<Equipo> equipos = List.of(equipo("1"));
        when(equipoRepository.findByCategoria("Computador")).thenReturn(equipos);

        assertEquals(equipos, equipoService.listarPorCategoria("Computador"));
    }

    @Test
    void crearGuardaUnEquipoValido() {
        Equipo nuevo = equipo("1");
        when(equipoRepository.existsById("1")).thenReturn(false);
        when(equipoRepository.save(nuevo)).thenReturn(nuevo);

        assertEquals(nuevo, equipoService.crear(nuevo));
        verify(equipoRepository).save(nuevo);
    }

    @Test
    void crearGeneraUuidCuandoElIdEstaVacio() {
        Equipo nuevo = equipo("");
        when(equipoRepository.save(nuevo)).thenReturn(nuevo);

        Equipo creado = equipoService.crear(nuevo);

        assertNotNull(creado.getId());
        assertTrue(creado.getId().matches(
                "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}"
        ));
        verify(equipoRepository, never()).existsById(any());
        verify(equipoRepository).save(nuevo);
    }

    @Test
    void crearRechazaIdDuplicado() {
        when(equipoRepository.existsById("1")).thenReturn(true);

        ResponseStatusException error = assertThrows(
                ResponseStatusException.class,
                () -> equipoService.crear(equipo("1"))
        );

        assertEquals(409, error.getStatusCode().value());
        verify(equipoRepository, never()).save(any(Equipo.class));
    }

    @Test
    void crearRechazaCamposVacios() {
        Equipo invalido = new Equipo("1", " ", "HP", "ProDesk 400", "Computador");

        ResponseStatusException error = assertThrows(
                ResponseStatusException.class,
                () -> equipoService.crear(invalido)
        );

        assertEquals(400, error.getStatusCode().value());
        verify(equipoRepository, never()).existsById(any());
        verify(equipoRepository, never()).save(any(Equipo.class));
    }

    @Test
    void actualizarGuardaLosDatosDelEquipoExistente() {
        Equipo existente = equipo("1");
        Equipo datos = new Equipo(null, "Notebook Lenovo", "Lenovo", "ThinkPad E14", "Notebook");
        when(equipoRepository.findById("1")).thenReturn(Optional.of(existente));
        when(equipoRepository.save(existente)).thenReturn(existente);

        Optional<Equipo> actualizado = equipoService.actualizar("1", datos);

        assertTrue(actualizado.isPresent());
        assertEquals("1", actualizado.get().getId());
        assertEquals("Notebook Lenovo", actualizado.get().getNombre());
        assertEquals("Lenovo", actualizado.get().getMarca());
        assertEquals("ThinkPad E14", actualizado.get().getModelo());
        assertEquals("Notebook", actualizado.get().getCategoria());
        verify(equipoRepository).save(existente);
    }

    @Test
    void actualizarDevuelveVacioCuandoNoExiste() {
        when(equipoRepository.findById("desconocido")).thenReturn(Optional.empty());

        assertFalse(equipoService.actualizar("desconocido", null).isPresent());
        verify(equipoRepository, never()).save(any(Equipo.class));
    }

    private Equipo equipo(String id) {
        return new Equipo(id, "Computador HP ProDesk", "HP", "ProDesk 400", "Computador");
    }
}
