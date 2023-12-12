package com.crisdev.saludservice.model;

import com.crisdev.saludservice.enums.Rol;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;

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
    private LocalDate fechaNacimiento;
    private String email;
    private String password;
    @OneToOne
    private Ubicacion ubicacion;
    @Enumerated(EnumType.STRING)
    private Rol rol;
    private LocalDate fechaAlta;
    private boolean activo;
    @OneToOne
    private Imagen fotoPerfil;
}
