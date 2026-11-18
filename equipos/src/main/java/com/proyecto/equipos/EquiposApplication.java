package com.proyecto.equipos;

import com.proyecto.equipos.entities.Equipo;
import com.proyecto.equipos.repository.equipoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class EquiposApplication {

    public static void main(String[] args) {
        SpringApplication.run(EquiposApplication.class, args);
    }

    @Bean
    CommandLineRunner cargarEquipos(equipoRepository equipoRepository) {
        return args -> {
            if (equipoRepository.count() == 0) {
                equipoRepository.saveAll(List.of(
                        new Equipo("1", "Computador HP ProDesk", "HP", "ProDesk 400", "Computador"),
                        new Equipo("2", "Notebook Lenovo ThinkPad", "Lenovo", "ThinkPad E14", "Notebook"),
                        new Equipo("3", "Proyector Epson EB-X06", "Epson", "EB-X06", "Proyector"),
                        new Equipo("4", "Kit Arduino UNO", "Arduino", "UNO R3", "Robótica"),
                        new Equipo("5", "Switch TP-Link", "TP-Link", "TL-SG1024", "Equipamiento de red")
                ));
            }
        };
    }
}
