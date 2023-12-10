package com.crisdev.saludservice.service;

import com.crisdev.saludservice.enums.Especialidad;
import com.crisdev.saludservice.enums.Rol;
import com.crisdev.saludservice.exception.MiException;
import com.crisdev.saludservice.model.Imagen;
import com.crisdev.saludservice.model.Profesional;
import com.crisdev.saludservice.repository.ProfesionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;


@Service
public class ProfesionalService {

    @Autowired
    UtilService utilService;
    @Autowired
    ProfesionalRepository profesionalRepository;
    @Autowired
    ImagenService imagenService;

    public void crearProfesional(String nombre, String apellido, Long dni, String fechaNacimiento,
                                 MultipartFile fotoPerfil, Long matricula, MultipartFile diploma,
                                 Especialidad especialidad, String email, String password, String password2) throws MiException, ParseException {

        LocalDate fecha;
        try {
            utilService.validarRegistro(nombre, apellido, fechaNacimiento, dni, especialidad, matricula, email, password, password2);
            fecha = utilService.formatearFecha(fechaNacimiento);
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
        profesional.setFechaAlta(LocalDate.now());

        Imagen imagen = imagenService.guardar(fotoPerfil);
        profesional.setFotoPerfil(imagen);
        Imagen imagen1 = imagenService.guardar(diploma);
        profesional.setDiploma(imagen1);
        profesional.setMatricula(matricula);
        profesional.setEspecialidad(especialidad);

        profesionalRepository.save(profesional);


    }

    public List<Profesional> listarProfesionales() {
        return profesionalRepository.findAll();
    }

    public List<Profesional> listarProfesionales(String especialidad, String columna) {

        Sort sort;
        if (columna == null || columna.isEmpty()) {
            sort = Sort.by("especialidad");
        } else {
            sort = Sort.by(columna);
        }
        return profesionalRepository.findByEspecialidadAndSort(especialidad, columna, sort);
    }
}
