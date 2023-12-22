package com.crisdev.saludservice.service;

import com.crisdev.saludservice.exception.MiException;
import com.crisdev.saludservice.model.Consulta;
import com.crisdev.saludservice.model.Paciente;
import com.crisdev.saludservice.model.Profesional;
import com.crisdev.saludservice.repository.ConsultaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class ConsultaService {

    final ConsultaRepository consultaRepository;

    public ConsultaService(ConsultaRepository consultaRepository) {
        this.consultaRepository = consultaRepository;
    }

    public void crearConsulta(Paciente paciente, String motivo, String antecedentes, String diagnostico, String indicaciones, String observaciones, Profesional profesional) throws MiException {

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

    public void valorarConsulta(String idConsulta, int estrellas) {

        Consulta consulta = findConsultaById(idConsulta);
        consulta.setValoracion(estrellas);
        consultaRepository.save(consulta);
    }

    public Consulta findConsultaById(String id) {

        Optional<Consulta> respuesta = consultaRepository.findById(id);

        return respuesta.orElse(null);
    }

    public List<Consulta> findAllByProfesional(Profesional profesional) {

        return consultaRepository.findAllByProfesionalId(profesional.getId());
    }
}
