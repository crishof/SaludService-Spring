package com.crisdev.saludservice.repository;

import com.crisdev.saludservice.model.HorarioLaboral;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HorarioLaboralRepository extends JpaRepository<HorarioLaboral, String> {
//    List<HorarioLaboral> listarPorIdProfesional(String id);

    @Query(value = "SELECT hl.* FROM horario_laboral hl " +
            "JOIN usuario_horario_laboral uhl ON hl.id = uhl.horario_laboral_id " +
            "WHERE uhl.profesional_id = :profesionalId " +
            "ORDER BY hl.dia_semana", nativeQuery = true)
    List<HorarioLaboral> obtenerHorariosPorProfesionalOrdenadosNativo(@Param("profesionalId") String id);

}
