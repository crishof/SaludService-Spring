package com.crisdev.saludservice.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Entity
public class Consulta {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private LocalDate fecha;
    private LocalTime hora;
    private String motivo;
    private String antecedentes;
    private String diagnostico;
    private String indicaciones;
    private String observaciones;
    @ManyToOne
    private Profesional profesional;
    @ManyToOne
    private Paciente paciente;
    private int valoracion;
    private boolean atendido;

}
