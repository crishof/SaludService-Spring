package com.crisdev.saludservice.serviceImpl;

import com.crisdev.saludservice.exception.MiException;
import com.crisdev.saludservice.model.Profesional;
import com.crisdev.saludservice.repository.ProfesionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;


@Service
public class ProfesionalServiceImpl {

    @Autowired
    UtilServiceImpl utilService;
    @Autowired
    ProfesionalRepository profesionalRepository;
    public void crearProfesional(String nombre, String apellido, String email, String password, String password2) throws MiException{

        try {
            utilService.validarRegistro(nombre, apellido, email, password, password2);
        } catch (MiException e) {
            throw new MiException(e.getMessage());
        }

        Profesional profesional = new Profesional();
        profesional.setNombre(nombre);
        profesional.setApellido(apellido);
        profesional.setEmail(email);
        profesional.setPassword(password);
        profesional.setFechaAlta(new Date());

        profesionalRepository.save(profesional);


    }
}
