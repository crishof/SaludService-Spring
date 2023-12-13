package com.crisdev.saludservice.model;

import com.crisdev.saludservice.enums.Pais;
import com.crisdev.saludservice.enums.Provincia;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ubicacion {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Enumerated(EnumType.STRING)
    private Pais pais;
    @Enumerated(EnumType.STRING)
    private Provincia provincia;
    private String localidad;
    private String codigoPostal;
    private String domicilio;

    public Ubicacion(Pais pais, Provincia provincia, String localidad, String codigoPostal, String domicilio) {
        this.pais = pais;
        this.provincia = provincia;
        this.localidad = localidad;
        this.codigoPostal = codigoPostal;
        this.domicilio = domicilio;
    }
}
