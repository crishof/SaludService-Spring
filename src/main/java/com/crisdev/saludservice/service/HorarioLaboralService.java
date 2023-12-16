package com.crisdev.saludservice.service;

import com.crisdev.saludservice.exception.MiException;
import com.crisdev.saludservice.model.HorarioLaboral;
import com.crisdev.saludservice.model.Profesional;
import com.crisdev.saludservice.repository.HorarioLaboralRepository;
import com.crisdev.saludservice.repository.ProfesionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.Optional;

@Service
public class HorarioLaboralService {

    @Autowired
    UtilService utilService;
    @Autowired
    ProfesionalRepository profesionalRepository;
    @Autowired
    ProfesionalService profesionalService;
    @Autowired
    HorarioLaboralRepository horarioLaboralRepository;

    public HorarioLaboral crearHorario(Profesional profesional, String dia, String horaEntrada, String horaSalida) throws MiException {


        System.out.println("TEST CREAR HORARIO");
        System.out.println("profesional nombre = " + profesional.getNombre());
        System.out.println("dia = " + dia);
        System.out.println("horaEntrada = " + horaEntrada);
        System.out.println("horaSalida = " + horaSalida);

            LocalTime entrada = utilService.formatearHora(horaEntrada);
            LocalTime salida = utilService.formatearHora(horaSalida);
            HorarioLaboral horario = new HorarioLaboral();
            System.out.println("TEST CREAR HORARIO EN EL IF");

            return horarioLaboralRepository.save(horario);


    }


}
