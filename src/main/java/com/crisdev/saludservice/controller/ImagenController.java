package com.crisdev.saludservice.controller;

import com.crisdev.saludservice.exception.MiException;
import com.crisdev.saludservice.model.Usuario;
import com.crisdev.saludservice.service.ProfesionalService;
import com.crisdev.saludservice.service.UsuarioService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/imagen")
public class ImagenController {

    final ProfesionalService profesionalService;

    final UsuarioService usuarioService;

    public ImagenController(ProfesionalService profesionalService, UsuarioService usuarioService) {
        this.profesionalService = profesionalService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/perfil/{id}")
    public ResponseEntity<byte[]> imagenUsuario(@PathVariable String id) throws MiException {

        Usuario usuario = usuarioService.buscarUsuario(id);
        byte[] imagen = usuario.getFotoPerfil().getContenido();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);

        return new ResponseEntity<>(imagen, headers, HttpStatus.OK);
    }

}
