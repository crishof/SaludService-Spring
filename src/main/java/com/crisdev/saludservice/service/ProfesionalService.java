package com.crisdev.saludservice.service;

import com.crisdev.saludservice.enums.Especialidad;
import com.crisdev.saludservice.enums.Rol;
import com.crisdev.saludservice.exception.MiException;
import com.crisdev.saludservice.model.HorarioLaboral;
import com.crisdev.saludservice.model.Imagen;
import com.crisdev.saludservice.model.Profesional;
import com.crisdev.saludservice.model.Ubicacion;
import com.crisdev.saludservice.repository.ProfesionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class ProfesionalService {

    final UtilService utilService;
    final ProfesionalRepository profesionalRepository;
    final ImagenService imagenService;

    @Autowired
    UbicacionService ubicacionService;

    public ProfesionalService(UtilService utilService, ProfesionalRepository profesionalRepository, ImagenService imagenService) {
        this.utilService = utilService;
        this.profesionalRepository = profesionalRepository;
        this.imagenService = imagenService;
    }

    public void crearProfesional(String nombre, String apellido, Long dni, String fechaNacimiento, MultipartFile fotoPerfil, Long matricula, MultipartFile diploma, Especialidad especialidad, String email, String password, String password2) throws MiException, ParseException {

        LocalDate fecha;
        try {
            utilService.validarUsuario(nombre, apellido, fechaNacimiento, dni, email);
            utilService.validarPassword(password, password2);
            utilService.validarProfesional(especialidad, matricula);
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
        profesional.setPassword(new BCryptPasswordEncoder().encode(password));
        profesional.setFechaAlta(LocalDate.now());

        Imagen imagen = imagenService.guardar(fotoPerfil);
        profesional.setFotoPerfil(imagen);
        Imagen imagen1 = imagenService.guardar(diploma);
        profesional.setDiploma(imagen1);

        String str = "";
        Ubicacion ubicacion = ubicacionService.crearUbicacion(null, null, str, str, str);
        profesional.setUbicacion(ubicacion);

        profesional.setMatricula(matricula);
        profesional.setEspecialidad(especialidad);
        profesional.setHorarioLaboral(new ArrayList<>());

        profesionalRepository.save(profesional);
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

    public void modificarProfesional(String id, String nombre, String apellido, String email) throws MiException {

        Optional<Profesional> respuesta = profesionalRepository.findById(id);
        if (respuesta.isPresent()) {
            Profesional profesional = respuesta.get();
            profesional.setNombre(nombre);
            profesional.setApellido(apellido);
            profesional.setEmail(email);
            profesionalRepository.save(profesional);
        } else {
            throw new MiException("Profesional no encontrado");
        }
    }

    public void agregarHorario(Profesional profesional, HorarioLaboral horario) {

        System.out.println("TEST AGREGAR HORARIO");

        var horas = new ArrayList<HorarioLaboral>();
        horas.add(horario);

        for (HorarioLaboral laboral : horas) {
            System.out.println("dia = " + laboral.getDiaSemana());
            System.out.println("entrada = " + laboral.getHoraEntrada());
            System.out.println("salida = " + laboral.getHoraSalida());
        }

        if (profesional.getHorarioLaboral() == null) {
            var horarioLaboral = new ArrayList<HorarioLaboral>();
            horarioLaboral.add(horario);
            for (HorarioLaboral laboral : horarioLaboral) {
                System.out.println("dia = " + laboral.getDiaSemana());
                System.out.println("entrada = " + laboral.getHoraEntrada());
                System.out.println("salida = " + laboral.getHoraSalida());
            }
            profesional.setHorarioLaboral(horarioLaboral);
            profesionalRepository.save(profesional);
        } else {
            System.out.println("TEST AGREGAR HORARIO EN EL ELSE");
            profesional.getHorarioLaboral().add(horario);
            profesionalRepository.save(profesional);
        }
    }
}


