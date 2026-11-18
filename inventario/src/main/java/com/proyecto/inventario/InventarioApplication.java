package com.proyecto.inventario;

import com.proyecto.inventario.entities.Inventario;
import com.proyecto.inventario.repository.inventarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class InventarioApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventarioApplication.class, args);
    }

    @Bean
    CommandLineRunner cargarInventario(inventarioRepository inventarioRepository) {
        return args -> {
            if (inventarioRepository.count() == 0) {
                inventarioRepository.saveAll(List.of(
                        new Inventario("1", 8, 5, "Disponible", "Estante A1"),
                        new Inventario("2", 10, 4, "Reservado", "Estante A2"),
                        new Inventario("3", 5, 3, "Prestado", "Estante B1"),
                        new Inventario("4", 6, 6, "En Mantenimiento", "Estante C1"),
                        new Inventario("5", 4, 0, "Fuera de Servicio", "Estante D1")
                ));
            }
        };
    }
}
