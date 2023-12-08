package com.crisdev.saludservice.serviceImpl;

import com.crisdev.saludservice.enums.Rol;
import com.crisdev.saludservice.exception.MiException;
import com.crisdev.saludservice.model.Imagen;
import com.crisdev.saludservice.model.Profesional;
import com.crisdev.saludservice.repository.ProfesionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.Date;


@Service
public class ProfesionalServiceImpl {

    @Autowired
    UtilServiceImpl utilService;
    @Autowired
    ProfesionalRepository profesionalRepository;
    @Autowired
    ImagenServiceImpl imagenService;

    public void crearProfesional(String nombre, String apellido, Long dni, String fechaNacimiento,
                                 MultipartFile fotoPerfil, Long matricula, MultipartFile diploma,
                                 String email, String password, String password2) throws MiException, ParseException {

        Date fecha = utilService.formatearFecha(fechaNacimiento);
        try {
            utilService.validarRegistro(nombre, apellido, fechaNacimiento, dni, matricula, email, password, password2);
        } catch (MiException e) {
            throw new MiException(e.getMessage());
        }

        Profesional profesional = new Profesional();
        profesional.setNombre(nombre);
        profesional.setApellido(apellido);
        profesional.setDni(dni);
        profesional.setFechaNacimiento(fecha);
        profesional.setRol(Rol.PROFESIONAL);
        profesional.setEmail(email);
        profesional.setPassword(password);
        profesional.setFechaAlta(new Date());

        Imagen imagen = imagenService.guardar(fotoPerfil);
        profesional.setFotoPerfil(imagen);
        Imagen imagen1 = imagenService.guardar(diploma);
        profesional.setDiploma(imagen1);
        profesional.setMatricula(matricula);

        profesionalRepository.save(profesional);


    }
}
