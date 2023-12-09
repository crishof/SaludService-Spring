package com.crisdev.saludservice.service;

import com.crisdev.saludservice.model.Imagen;
import com.crisdev.saludservice.repository.ImagenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ImagenService {

    @Autowired
    ImagenRepository imagenRepository;

    public Imagen guardar(MultipartFile archivo) {

        if (archivo != null) {
            try {
                Imagen imagen = new Imagen();
                imagen.setMime(archivo.getContentType());
                imagen.setNombre(archivo.getName());
                imagen.setContenido(archivo.getBytes());

                return imagenRepository.save(imagen);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }
}
