package com.crisdev.saludservice.repository;

import com.crisdev.saludservice.model.Profesional;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfesionalRepository extends JpaRepository<Profesional, String> {


    @Query("SELECT p FROM Profesional p " +
            "WHERE (:especialidad IS NULL OR p.especialidad LIKE %:especialidad%)" +
            "ORDER BY CASE WHEN :columna = 'nombre' THEN p.nombre END," +
            "CASE WHEN :columna = 'apellido' THEN p.apellido END," +
            "CASE WHEN :columna = 'especialidad' THEN p.especialidad END DESC")
    List<Profesional> findByEspecialidadAndSort(@Param("especialidad") String especialidad, @Param("columna") String columna, Sort sort);
}
