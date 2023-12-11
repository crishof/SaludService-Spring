package com.crisdev.saludservice.model;

import com.crisdev.saludservice.enums.DiaSemana;
import com.crisdev.saludservice.repository.ProfesionalRepository;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalTime;


@Entity
public class HorarioLaboral {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String diaSemana;
    private LocalTime horaEntrada;
    private LocalTime horaSalida;
    @ManyToOne
    private Profesional profesional;


}
