package com.proyecto.mpazpaniol;

import com.proyecto.mpazpaniol.repository.SolicitudRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class MpazpaniolApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SolicitudRepository solicitudRepository;

    @Test
    void contextoWebCargaYExponeSolicitudesSinConectarAUnaBaseDeDatos() throws Exception {
        when(solicitudRepository.findAll()).thenReturn(java.util.List.of());

        mockMvc.perform(get("/api/solicitudes"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(solicitudRepository).findAll();
    }

    @Test
    void crearSolicitudConservaElIdDeEquipoComoTexto() throws Exception {
        when(solicitudRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(post("/api/solicitudes")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "equipoId": "1",
                                  "solicitanteEmail": "alumno@duocuc.cl",
                                  "cargo": "Alumno",
                                  "sala": "Sala 402",
                                  "fechaReserva": "2026-11-18 18:30",
                                  "estado": "ACEPTADA"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.equipoId").value("1"))
                .andExpect(jsonPath("$.estado").value("PENDIENTE"));

        verify(solicitudRepository).save(any());
    }
}
