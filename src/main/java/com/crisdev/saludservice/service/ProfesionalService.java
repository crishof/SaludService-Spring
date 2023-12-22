package com.crisdev.saludservice.service;

import com.crisdev.saludservice.enums.Especialidad;
import com.crisdev.saludservice.enums.Rol;
import com.crisdev.saludservice.exception.MiException;
import com.crisdev.saludservice.model.*;
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

    @Autowired
    UtilService utilService;
    @Autowired
    ProfesionalRepository profesionalRepository;
    @Autowired
    ImagenService imagenService;
    @Autowired
    UbicacionService ubicacionService;
    @Autowired
    ConsultaService consultaService;

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

        return profesionalRepository.findByEmail(email);
    }

    public void actualizarPrecioConsulta(String id, double precio) throws MiException {

        Profesional profesional = buscarPorId(id);
        profesional.setPrecioConsulta(precio);
        profesionalRepository.save(profesional);
    }

    public List<Paciente> listarPacientesDelProfesional(String id) {

        return profesionalRepository.listarPacientesDelProfesional(id);
    }

    public void valorarProfesional(String idConsulta) {

        Consulta consulta = consultaService.findConsultaById(idConsulta);
        Profesional profesional = buscarPorId(consulta.getProfesional().getId());

        List<Consulta> consultasProfesional = consultaService.findAllByProfesional(profesional);

        consultasProfesional.add(consulta);

        double sumarValoraciones = consultasProfesional.stream().mapToDouble(Consulta::getValoracion).sum();
        double promedio = consultasProfesional.isEmpty() ? 0 : sumarValoraciones / consultasProfesional.size();

        double promedioRedondeado = (double) Math.round(promedio * 100) / 100;

        profesional.setValoracion(promedioRedondeado);

        profesionalRepository.save(profesional);

    }

    public List<Profesional> buscarProfesionales(String texto) {

        return profesionalRepository.findByNombreApellidoEspecialidad(texto);
    }
}


