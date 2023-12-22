package com.crisdev.saludservice.repository;

import com.crisdev.saludservice.model.HorarioLaboral;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HorarioLaboralRepository extends JpaRepository<HorarioLaboral, String> {

    @Query(value = "SELECT hl.* FROM horario_laboral hl " + "JOIN usuario_horario_laboral uhl ON hl.id = uhl.horario_laboral_id " + "WHERE uhl.profesional_id = :profesionalId " + "ORDER BY FIELD(hl.dia_semana, 'LUNES', 'MARTES', 'MIERCOLES', 'JUEVES', 'VIERNES')", nativeQuery = true)
    List<HorarioLaboral> obtenerHorariosPorProfesionalOrdenadosNativo(@Param("profesionalId") String id);

    @Query(value = "SELECT hl.id, hl.dia_semana, hl.hora_entrada, hl.hora_salida " + "FROM horario_laboral hl " + "JOIN usuario_horario_laboral uhl ON hl.id = uhl.horario_laboral_id " + "WHERE uhl.profesional_id = :profesionalId " + "AND hl.dia_semana = :dia", nativeQuery = true)
    List<HorarioLaboral> obtenerHorarioPorDiaProfesional(@Param("profesionalId") String profesionalId, @Param("dia") String dia);

}
