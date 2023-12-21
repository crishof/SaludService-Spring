package com.crisdev.saludservice.service;

import com.crisdev.saludservice.exception.MiException;
import com.crisdev.saludservice.model.HorarioLaboral;
import com.crisdev.saludservice.model.Paciente;
import com.crisdev.saludservice.model.Profesional;
import com.crisdev.saludservice.model.Turno;
import com.crisdev.saludservice.repository.HorarioLaboralRepository;
import com.crisdev.saludservice.repository.PacienteRepository;
import com.crisdev.saludservice.repository.ProfesionalRepository;
import com.crisdev.saludservice.repository.TurnoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
public class TurnoService {

    @Autowired
    TurnoRepository turnoRepository;
//    @Autowired
//    PacienteRepository pacienteRepository;
//    @Autowired
//    HorarioLaboralRepository horarioLaboralRepository;
    @Autowired
    ProfesionalRepository profesionalRepository;

    @Autowired
    ProfesionalService profesionalService;

//    public TurnoService(TurnoRepository turnoRepository, HorarioLaboralRepository horarioLaboralRepository, ProfesionalRepository profesionalRepository, PacienteRepository pacienteRepository, PacienteService pacienteService, ProfesionalService profesionalService) {
//        this.turnoRepository = turnoRepository;
//        this.horarioLaboralRepository = horarioLaboralRepository;
//        this.profesionalRepository = profesionalRepository;
//        this.pacienteRepository = pacienteRepository;
//        this.pacienteService = pacienteService;
//        this.profesionalService = profesionalService;
//    }
//
    public void generarTurnos(String idProfesional, HorarioLaboral horario) {


        List<Turno> turnos = new ArrayList<>();

        Optional<Profesional> respuesta = profesionalRepository.findById(idProfesional);

        if (respuesta.isPresent()) {
            Profesional profesional = respuesta.get();

            LocalDate fechaActual = LocalDate.now();
            LocalDate fechaLimite = fechaActual.plusMonths(2);

            while (fechaActual.isBefore(fechaLimite)) {
                DayOfWeek dayOfWeek = fechaActual.getDayOfWeek();
                String diaSemanaActual = obtenerNombreDiaSPA(dayOfWeek).toUpperCase();

                if (horario.getDiaSemana().toString().equalsIgnoreCase(diaSemanaActual)) {

                    List<LocalDateTime> turnosDia = generarTurnosDia(fechaActual, horario.getHoraEntrada(), horario.getHoraSalida());

                    turnos.addAll(crearTurnosParaDia(turnosDia, profesional));

                }

                fechaActual = fechaActual.plusDays(1);

            }

            turnoRepository.saveAll(turnos);
        }
    }

    private String obtenerNombreDiaSPA(DayOfWeek diaSemana) {
        Locale espanol = new Locale("es", "ES");
        return diaSemana.getDisplayName(TextStyle.FULL, espanol);
    }

    private List<Turno> crearTurnosParaDia(List<LocalDateTime> turnosDia, Profesional profesional) {
        List<Turno> turnos = new ArrayList<>();
        boolean disponible = true;

        for (LocalDateTime turnoDateTime : turnosDia) {
            Turno turno = new Turno();
            turno.setFecha(turnoDateTime.toLocalDate());
            turno.setHora(turnoDateTime.toLocalTime());
            turno.setProfesional(profesional);
            turno.setDisponible(disponible);
            turnos.add(turno);
        }
        return turnos;
    }

    private List<LocalDateTime> generarTurnosDia(LocalDate fecha, LocalTime horaEntrada, LocalTime horaSalida) {
        List<LocalDateTime> turnosDia = new ArrayList<>();
        LocalDateTime dateTimeEntrada = LocalDateTime.of(fecha, horaEntrada);

        while (dateTimeEntrada.plusMinutes(30).isBefore(LocalDateTime.of(fecha, horaSalida)) || dateTimeEntrada.plusMinutes(30).equals(LocalDateTime.of(fecha, horaSalida))) {
            turnosDia.add(dateTimeEntrada);
            dateTimeEntrada = dateTimeEntrada.plusMinutes(30);
        }

        return turnosDia;
    }

    public Page<Turno> listarTurnos(PageRequest pageable) {

        return turnoRepository.findAll(pageable);

    }

    public Turno buscarPorId(String id) {

        Optional<Turno> respuesta = turnoRepository.findById(id);

        return respuesta.orElse(null);
    }

    public void confirmarTurno(String idTurno, Paciente paciente) throws MiException {
        Turno turno = buscarPorId(idTurno);

        // Verificar si el turno existe
        if (turno == null) {
            throw new MiException("El turno con ID " + idTurno + " no existe");
        }

        // Verificar si el paciente existe
        if (paciente == null) {
            throw new MiException("El paciente no existe");
        }

        // Asignar el paciente al turno y marcar el turno como no disponible
        turno.setPaciente(paciente);
        turno.setDisponible(false);

        // Guardar el turno actualizado
        turnoRepository.save(turno);
    }

    public List<Turno> listarTurnosPaciente(String id) {
//        try {
//            // Buscar al paciente por su ID
//            Paciente paciente = pacienteService.buscarPacientePorId(id);
//
//            // Verificar si el paciente existe
//            if (paciente == null) {
//                throw new MiException("El paciente con ID " + id + " no existe");
//            }
            // Retornar la lista de turnos del paciente
//            return turnoRepository.findByPacienteId(paciente.getId());
//        } catch (MiException e) {
//            throw new RuntimeException(e);
//        }
            return null;
    }

    public Object listarTurnosReservados(String id) {
        try {
            // Buscar al profesional por su ID
            Profesional profesional = profesionalService.buscarPorId(id);

            // Verificar si el profesional existe
            if (profesional == null) {
                throw new MiException("El profesional con ID " + id + " no existe");
            }

            return turnoRepository.findTurnosReservadosProfesional(profesional.getId());
        } catch (MiException e) {
            throw new RuntimeException(e);
        }
    }

    public void atenderTurno(String idTurno) {

        Turno turno = buscarPorId(idTurno);
        turno.setAtendido(true);
        turnoRepository.save(turno);

    }
}
