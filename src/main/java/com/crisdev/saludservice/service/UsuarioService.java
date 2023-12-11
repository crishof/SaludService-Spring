package com.crisdev.saludservice.service;

import com.crisdev.saludservice.model.Usuario;
import com.crisdev.saludservice.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UsuarioService{

    @Autowired
    UsuarioRepository usuarioRepository;

    public Usuario buscarUsuario(String id) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);

        if (optionalUsuario.isPresent()) {
            return optionalUsuario.get();
        } else {
            // Manejar el caso en que el usuario no existe, por ejemplo, lanzar una excepción o devolver un valor predeterminado
            throw new NoSuchElementException("Usuario no encontrado con ID: " + id);
        }
    }

}
