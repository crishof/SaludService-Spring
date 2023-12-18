package com.crisdev.saludservice.service;

import com.crisdev.saludservice.enums.DiaSemana;
import com.crisdev.saludservice.exception.MiException;
import com.crisdev.saludservice.model.HorarioLaboral;
import com.crisdev.saludservice.model.Profesional;
import com.crisdev.saludservice.repository.HorarioLaboralRepository;
import com.crisdev.saludservice.repository.ProfesionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class HorarioLaboralService {

    @Autowired
    UtilService utilService;
    @Autowired
    ProfesionalRepository profesionalRepository;
    @Autowired
    HorarioLaboralRepository horarioLaboralRepository;

    public HorarioLaboral crearHorario(DiaSemana dia, String horaEntrada, String horaSalida) throws MiException {

        LocalTime entrada = utilService.formatearHora(horaEntrada);
        LocalTime salida = utilService.formatearHora(horaSalida);
        HorarioLaboral horario = new HorarioLaboral();
        horario.setHoraEntrada(entrada);
        horario.setHoraSalida(salida);
        horario.setDiaSemana(dia);

        return horarioLaboralRepository.save(horario);

    }

    public List<HorarioLaboral> listarHorariosProfesional(String id) {

        return horarioLaboralRepository.obtenerHorariosPorProfesionalOrdenadosNativo(id);
    }


}