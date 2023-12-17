package com.crisdev.saludservice.service;

import com.crisdev.saludservice.enums.DiaSemana;
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

    public static List<HorarioLaboral> ordenarPorDiaSemana(List<HorarioLaboral> horarios) {
        // Define un comparador personalizado basado en el orden de los días de la semana
        Comparator<HorarioLaboral> comparador = Comparator.comparingInt(horario -> {
            DiaSemana diaSemana = horario.getDiaSemana();
            // Asigna un valor numérico a cada día de la semana para la comparación
            return switch (diaSemana) {
                case LUNES -> 1;
                case MARTES -> 2;
                case MIERCOLES -> 3;
                case JUEVES -> 4;
                case VIERNES -> 5;
            };
        });
        // Ordena la lista usando el comparador
        horarios.sort(comparador);
        return horarios;
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
            DiaSemana nuevoDia = horario.getDiaSemana();
            int indiceExistente = -1;

            for (int i = 0; i < horarios.size(); i++) {
                DiaSemana diaExistente = horarios.get(i).getDiaSemana();
                if (diaExistente.equals(nuevoDia)) {
                    indiceExistente = i;
                    break;
                }
            }

            int indiceInsercion = 0;
            while (indiceInsercion < horarios.size() && horarios.get(indiceInsercion).getDiaSemana().compareTo(nuevoDia) < 0) {
                indiceInsercion++;
            }
            horarios.add(indiceInsercion, horario);
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
}


