package com.crisdev.saludservice.repository;

import com.crisdev.saludservice.model.Consulta;
import com.crisdev.saludservice.model.Turno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, String> {

    @Query("SELECT c FROM Consulta c WHERE c.paciente.id = :pacienteId")
    List<Consulta> findAllByPacienteId(String pacienteId);

    @Query("SELECT c FROM Consulta c WHERE c.profesional.id = :profesionalId")
    List<Consulta> findAllByProfesionalId(String profesionalId);
}
