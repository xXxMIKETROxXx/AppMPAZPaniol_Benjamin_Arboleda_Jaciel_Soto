package com.proyecto.mpazpaniol.repository;

import com.proyecto.mpazpaniol.entity.SolicitudEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Interface Repository. Hereda de JpaRepository para obtener operaciones CRUD de forma automática.
 */
@Repository
public interface SolicitudRepository extends JpaRepository<SolicitudEntity, Long> {
    
    // Spring Data JPA crea automáticamente la consulta SQL: SELECT * FROM solicitudes WHERE estado = ?
    List<SolicitudEntity> findByEstado(String estado);
}