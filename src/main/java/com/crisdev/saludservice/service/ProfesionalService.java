package com.crisdev.saludservice.service;

import com.crisdev.saludservice.enums.Especialidad;
import com.crisdev.saludservice.enums.Rol;
import com.crisdev.saludservice.exception.MiException;
import com.crisdev.saludservice.model.HorarioLaboral;
import com.crisdev.saludservice.model.Imagen;
import com.crisdev.saludservice.model.Profesional;
import com.crisdev.saludservice.model.Ubicacion;
import com.crisdev.saludservice.repository.ProfesionalRepository;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.*;


@Service
public class ProfesionalService {

    final UtilService utilService;
    final ProfesionalRepository profesionalRepository;
    final ImagenService imagenService;
    final
    UbicacionService ubicacionService;

    public ProfesionalService(UtilService utilService, ProfesionalRepository profesionalRepository, ImagenService imagenService, UbicacionService ubicacionService) {
        this.utilService = utilService;
        this.profesionalRepository = profesionalRepository;
        this.imagenService = imagenService;
        this.ubicacionService = ubicacionService;
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

    public void agregarHorario(Profesional profesional, HorarioLaboral horario) {

        List<HorarioLaboral> horarios = profesional.getHorarioLaboral();

        if (horarios == null) {
            var horarioLaboral = new ArrayList<HorarioLaboral>();
            horarioLaboral.add(horario);
            profesional.setHorarioLaboral(horarioLaboral);
        } else {

            horarios.add(horario);
            profesional.setHorarioLaboral(horarios);
        }
        profesionalRepository.save(profesional);

    }

    public Profesional buscarPorId(String id) {
        Optional<Profesional> respuesta = profesionalRepository.findById(id);
        return respuesta.orElse(null);
    }

    public Profesional buscarPorEmail(String email) {

        return profesionalRepository.buscarPorEmail(email);
    }

    public void actualizarPrecioConsulta(String id, double precio) throws MiException {

        Optional<Profesional> respuesta = profesionalRepository.findById(id);

        try{
        if (respuesta.isPresent()) {
            Profesional profesional = respuesta.get();
            profesional.setPrecioConsulta(precio);
            profesionalRepository.save(profesional);
        }
        }catch (Exception e){
            throw new MiException(e.getMessage());
        }
    }
}


