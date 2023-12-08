package com.crisdev.saludservice.model;

import com.crisdev.saludservice.enums.Rol;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class Usuario {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    private String nombre;
    private String apellido;
    private long dni;
    private Date fechaNacimiento;
    private String email;
    private String password;
    private String domicilio;
    @Enumerated(EnumType.STRING)
    private Rol rol;
    private Date fechaAlta;
    private boolean activo;
    @OneToOne
    private Imagen fotoPerfil;
}
