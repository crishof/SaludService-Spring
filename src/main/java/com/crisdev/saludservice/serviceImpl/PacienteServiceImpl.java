package com.crisdev.saludservice.serviceImpl;

import com.crisdev.saludservice.enums.Rol;
import com.crisdev.saludservice.exception.MiException;
import com.crisdev.saludservice.model.Imagen;
import com.crisdev.saludservice.model.Paciente;
import com.crisdev.saludservice.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.Date;

@Service
public class PacienteServiceImpl {

    @Autowired
    UtilServiceImpl utilService;
    @Autowired
    ImagenServiceImpl imagenService;
    @Autowired
    PacienteRepository pacienteRepository;

    public void crearPaciente(String nombre, String apellido, Long dni, String fechaNacimiento, MultipartFile fotoPerfil, String email, String password, String password2) throws ParseException, MiException {

        Date fecha;
        try {
            utilService.validarRegistro(nombre, apellido, fechaNacimiento, dni, email, password, password2);
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
        Paciente.setPassword(password);
        Paciente.setFechaAlta(new Date());

        Imagen imagen = imagenService.guardar(fotoPerfil);
        Paciente.setFotoPerfil(imagen);

        pacienteRepository.save(Paciente);
    }
}
