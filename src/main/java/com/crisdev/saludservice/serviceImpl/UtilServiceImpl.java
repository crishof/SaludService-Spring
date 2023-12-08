package com.crisdev.saludservice.serviceImpl;

import com.crisdev.saludservice.exception.MiException;
import com.crisdev.saludservice.service.UtilService;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class UtilServiceImpl implements UtilService {

    public void validarRegistro(String nombre, String apellido, String fecha, Long dni, Long matricula,
                                String email, String password, String password2) throws MiException {

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
        if (matricula == null) {
            throw new MiException("El número de matrícula no puede estar vacío");
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

    public Date formatearFecha(String fechaStr) throws ParseException {

        Date fecha;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        fecha = dateFormat.parse(fechaStr);
        return fecha;
    }
}

