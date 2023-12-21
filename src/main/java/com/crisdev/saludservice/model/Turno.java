package com.crisdev.saludservice.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
public class Turno {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private LocalDate fecha;
    private LocalTime hora;
    @OneToOne
    private Profesional profesional;
    @OneToOne
    private Paciente paciente;
    private boolean disponible;
    private boolean atendido;
}
