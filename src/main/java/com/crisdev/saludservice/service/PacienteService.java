package com.crisdev.saludservice.service;

import com.crisdev.saludservice.enums.Pais;
import com.crisdev.saludservice.enums.Rol;
import com.crisdev.saludservice.exception.MiException;
import com.crisdev.saludservice.model.Imagen;
import com.crisdev.saludservice.model.Paciente;
import com.crisdev.saludservice.model.Ubicacion;
import com.crisdev.saludservice.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.time.LocalDate;

@Service
public class PacienteService {

    final UtilService utilService;
    final ImagenService imagenService;
    final PacienteRepository pacienteRepository;
    
    @Autowired
    UbicacionService ubicacionService;

    public PacienteService(UtilService utilService, ImagenService imagenService, PacienteRepository pacienteRepository) {
        this.utilService = utilService;
        this.imagenService = imagenService;
        this.pacienteRepository = pacienteRepository;
    }

    public void crearPaciente(String nombre, String apellido, Long dni, String fechaNacimiento, MultipartFile fotoPerfil, String email, String password, String password2) throws ParseException, MiException {

        LocalDate fecha;
        try {
            utilService.validarUsuario(nombre, apellido, fechaNacimiento, dni, email);
            utilService.validarPassword(password, password2);
            fecha = utilService.formatearFecha(fechaNacimiento);
        } catch (MiException e) {
            throw new MiException(e.getMessage());
        }

        Paciente paciente = new Paciente();
        paciente.setNombre(nombre);
        paciente.setApellido(apellido);
        paciente.setDni(dni);
        paciente.setFechaNacimiento(fecha);
        paciente.setRol(Rol.PACIENTE);
        paciente.setEmail(email);
        paciente.setPassword(new BCryptPasswordEncoder().encode(password));
        paciente.setFechaAlta(LocalDate.now());

        Imagen imagen = imagenService.guardar(fotoPerfil);
        paciente.setFotoPerfil(imagen);
        String str = "";
        Ubicacion ubicacion = ubicacionService.crearUbicacion(null,null,str,str,str);
        paciente.setUbicacion(ubicacion);

        pacienteRepository.save(paciente);
    }

    public void crearPaciente(String nombre, String apellido, Long dni, String fechaNacimiento, MultipartFile fotoPerfil, String email, String password, String password2, Ubicacion ubicacion) throws MiException {
        LocalDate fecha;
        try {
            utilService.validarUsuario(nombre, apellido, fechaNacimiento, dni, email);
            utilService.validarPassword(password, password2);
            fecha = utilService.formatearFecha(fechaNacimiento);
        } catch (MiException e) {
            throw new MiException(e.getMessage());
        }

        Paciente Paciente = new Paciente();
        Paciente.setNombre(nombre);
        Paciente.setApellido(apellido);
        Paciente.setDni(dni);
        Paciente.setFechaNacimiento(fecha);
        Paciente.setRol(Rol.PACIENTE);
        Paciente.setEmail(email);
        Paciente.setPassword(new BCryptPasswordEncoder().encode(password));
        Paciente.setFechaAlta(LocalDate.now());
        Paciente.setUbicacion(ubicacion);

        Imagen imagen = imagenService.guardar(fotoPerfil);
        Paciente.setFotoPerfil(imagen);

        pacienteRepository.save(Paciente);
    }
}
