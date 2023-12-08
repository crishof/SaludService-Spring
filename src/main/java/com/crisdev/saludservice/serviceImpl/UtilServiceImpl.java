package com.crisdev.saludservice.serviceImpl;

import com.crisdev.saludservice.exception.MiException;
import com.crisdev.saludservice.service.UtilService;
import org.springframework.stereotype.Service;

@Service
public class UtilServiceImpl implements UtilService {

    public void validarRegistro(String nombre, String apellido, String email, String password, String password2) throws MiException {

        if (nombre.isEmpty()) {
            throw new MiException("El nombre no puede estar vacío");
        }
        if (apellido.isEmpty()) {
            throw new MiException("El apellido no puede estar vacío");
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
