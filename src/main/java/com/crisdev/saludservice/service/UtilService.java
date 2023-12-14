package com.crisdev.saludservice.service;

import com.crisdev.saludservice.enums.Especialidad;
import com.crisdev.saludservice.enums.Pais;
import com.crisdev.saludservice.enums.Provincia;
import com.crisdev.saludservice.exception.MiException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Service
public class UtilService {

    public void validarUsuario(String nombre, String apellido, String fecha, Long dni, String email) throws MiException {
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
    }
    public void validarPassword(String password, String password2) throws MiException {
        if (password == null || password.length() < 6) {
            throw new MiException("La contraseña debe contener 6 dígitos");
        }
        if (!password.equals(password2)) {
            throw new MiException("Las contraseñas no coinciden");
        }
    }
    public void validarProfesional (Especialidad especialidad, Long matricula) throws MiException {

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
    public LocalTime formatearHora(String hora) {
        // Crear un formateador para el formato de la cadena de tiempo
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        // Convertir la cadena de tiempo a LocalTime
        return LocalTime.parse(hora, formatter);

    }
    public void validarUbicacion(Pais pais, Provincia provincia, String localidad, String codigoPostal, String domicilio) throws MiException {

        if (pais == null) {
            throw new MiException("Debe seleccionar un país");
        }
        if (provincia == null) {
            throw new MiException("Debe seleccionar una provincia");
        }
        if (localidad.isEmpty()) {
            throw new MiException("La localidad no puede estar vacía");
        }
        if (codigoPostal.isEmpty()) {
            throw new MiException("El codigo postal no puede estar vacío");
        }
        if (domicilio.isEmpty()) {
            throw new MiException("El domicilio no puede estar vacío");
        }
    }
    public void validarHorario(String dia, String horaEntrada, String horaSalida) throws MiException {

        System.out.println("TEST VALIDAR");
        if (dia.isEmpty()) {
            throw new MiException("Debe seleccionar un día");
        }
        if (horaEntrada.isEmpty()) {
            throw new MiException("Debe seleccionar la hora de entrada");
        }
        if (horaSalida.isEmpty()) {
            throw new MiException("Debe seleccionar la hora de salida");
        }

    }
}

