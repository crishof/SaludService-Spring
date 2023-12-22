package com.crisdev.saludservice.repository;

import com.crisdev.saludservice.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PacienteRepository extends JpaRepository<Paciente, String> {


}
