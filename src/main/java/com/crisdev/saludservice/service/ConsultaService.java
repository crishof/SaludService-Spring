package com.crisdev.saludservice.service;

import com.crisdev.saludservice.exception.MiException;
import com.crisdev.saludservice.model.Consulta;
import com.crisdev.saludservice.model.Paciente;
import com.crisdev.saludservice.model.Profesional;
import com.crisdev.saludservice.model.Turno;
import com.crisdev.saludservice.repository.ConsultaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class ConsultaService {

    @Autowired
    PacienteService pacienteService;

   @Autowired
   ConsultaRepository consultaRepository;

    public void crearConsulta(String idTurno, String motivo, String antecedentes, String diagnostico, String indicaciones, String observaciones, Profesional profesional) throws MiException {

        Paciente paciente = pacienteService.buscarPacientePorIdTurno(idTurno);

        Consulta consulta = new Consulta();
        consulta.setPaciente(paciente);
        consulta.setMotivo(motivo);
        consulta.setAntecedentes(antecedentes);
        consulta.setDiagnostico(diagnostico);
        consulta.setIndicaciones(indicaciones);
        consulta.setObservaciones(observaciones);
        consulta.setProfesional(profesional);

        LocalDate fecha = LocalDate.now();
        LocalTime hora = LocalTime.now();

        consulta.setFecha(fecha);
        consulta.setHora(hora);

        consulta.setAtendido(true);

        consultaRepository.save(consulta);

    }

    public List<Consulta> listarConsultasPaciente(Paciente paciente) {
        return consultaRepository.findAllByPacienteId(paciente.getId());
    }
}
