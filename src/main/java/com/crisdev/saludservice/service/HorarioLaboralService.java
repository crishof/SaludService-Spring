package com.crisdev.saludservice.service;

import com.crisdev.saludservice.exception.MiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HorarioLaboralService {

    @Autowired
    UtilService utilService;

    public void crearHorario(String id, String dia, String horaEntrada, String horaSalida) throws MiException {


    }


}
