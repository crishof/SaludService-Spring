package com.crisdev.saludservice.service;

import com.crisdev.saludservice.enums.Especialidad;
import com.crisdev.saludservice.exception.MiException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class UtilService {

    public void validarRegistroProfesional(String nombre, String apellido, String fecha, Long dni, Especialidad especialidad, Long matricula,
                                           String email, String password, String password2) throws MiException {

        validarRegistro(nombre, apellido, fecha, dni, email, password, password2);

        if (matricula == null) {
            throw new MiException("El número de matrícula no puede estar vacío");
        }
        if (especialidad == null) {
            throw new MiException("Debe seleccionar su especialidad médica");
        }
    }

    public LocalDate formatearFecha(String fechaStr) throws MiException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate fecha;

        try {
            fecha = LocalDate.parse(fechaStr, formatter);
        } catch (Exception e) {
            throw new MiException("Error al dar formato a la fecha " + e.getMessage());
        }

        return fecha;
    }

    public void validarRegistro(String nombre, String apellido, String fecha, Long dni, String email,
                                String password, String password2) throws MiException {
        if (nombre.isEmpty()) {
            throw new MiException("El nombre no puede estar vacío");
        }
        if (apellido.isEmpty()) {
            throw new MiException("El apellido no puede estar vacío");
        }
        if (dni == null) {
            throw new MiException("El dni no puede estar vacío");
        }
        if (fecha.isEmpty()) {
            throw new MiException("La fecha no puede estar vacía");
        }
        if (email.isEmpty()) {
            throw new MiException("El email no puede estar vacío");
        }
        if (password == null || password.length() < 6) {
            throw new MiException("La contraseña debe contener 6 dígitos");
        }
        if (!password.equals(password2)) {
            throw new MiException("Las contraseñas no coinciden");
        }
    }
}

