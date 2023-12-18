package com.crisdev.saludservice.service;

import com.crisdev.saludservice.model.HorarioLaboral;
import com.crisdev.saludservice.model.Profesional;
import com.crisdev.saludservice.model.Turno;
import com.crisdev.saludservice.repository.HorarioLaboralRepository;
import com.crisdev.saludservice.repository.ProfesionalRepository;
import com.crisdev.saludservice.repository.TurnoRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

@Service
public class TurnoService {

    @Autowired
    TurnoRepository turnoRepository;
    @Autowired
    HorarioLaboralRepository horarioLaboralRepository;
    @Autowired
    private ProfesionalRepository profesionalRepository;

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

        while (dateTimeEntrada.plusMinutes(30).isBefore(LocalDateTime.of(fecha, horaSalida)) ||
                dateTimeEntrada.plusMinutes(30).equals(LocalDateTime.of(fecha, horaSalida))) {
            turnosDia.add(dateTimeEntrada);
            dateTimeEntrada = dateTimeEntrada.plusMinutes(30);
        }

        return turnosDia;
    }


}
