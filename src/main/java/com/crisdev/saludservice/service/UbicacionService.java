package com.crisdev.saludservice.service;

import com.crisdev.saludservice.enums.Pais;
import com.crisdev.saludservice.enums.Provincia;
import com.crisdev.saludservice.exception.MiException;
import com.crisdev.saludservice.model.Ubicacion;
import com.crisdev.saludservice.repository.UbicacionRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UbicacionService {

    final UbicacionRepository ubicacionRepository;

    public UbicacionService(UbicacionRepository ubicacionRepository) {
        this.ubicacionRepository = ubicacionRepository;
    }

    public Ubicacion crearUbicacion(Pais pais, Provincia provincia, String localidad, String codigoPostal, String domicilio) {


        Ubicacion ubicacion = new Ubicacion();
        ubicacion.setPais(pais);
        ubicacion.setProvincia(provincia);
        ubicacion.setLocalidad(localidad);
        ubicacion.setCodigoPostal(codigoPostal);
        ubicacion.setDomicilio(domicilio);

        return ubicacionRepository.save(ubicacion);
    }

    public Ubicacion actualizarUbicacion(String idUbicacion, Pais pais, Provincia provincia, String localidad, String codigoPostal, String domicilio) throws MiException {

        try {
            Ubicacion ubicacion = new Ubicacion();
            if (idUbicacion != null) {
                Optional<Ubicacion> respuesta = ubicacionRepository.findById(idUbicacion);
                if (respuesta.isPresent()) {
                    ubicacion = respuesta.get();
                }
                ubicacion.setPais(pais);
                ubicacion.setProvincia(provincia);
                ubicacion.setLocalidad(localidad);
                ubicacion.setCodigoPostal(codigoPostal);
                ubicacion.setDomicilio(domicilio);

                return ubicacionRepository.save(ubicacion);
            }
        } catch (Exception e) {
            throw new MiException(e.getMessage());
        }
        return null;
    }
}
