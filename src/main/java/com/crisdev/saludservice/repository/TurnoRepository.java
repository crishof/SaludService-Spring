package com.crisdev.saludservice.repository;

import com.crisdev.saludservice.model.Turno;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TurnoRepository extends JpaRepository<Turno, String> {
    @Query("SELECT t FROM Turno t WHERE t.paciente.id = :pacienteId AND t.atendido = false ")
    List<Turno> findNoAtendidoByPacienteId(@Param("pacienteId") String pacienteId);

    @Query("SELECT t FROM Turno t WHERE t.profesional.id = :profesionalId AND t.disponible = false AND t.atendido = false")
    List<Turno> findTurnosReservadosProfesional(String profesionalId);

    Page<Turno> findByDisponible(boolean disponible, PageRequest pageable);
}
