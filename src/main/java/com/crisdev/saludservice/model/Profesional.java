package com.crisdev.saludservice.model;

import com.crisdev.saludservice.enums.Especialidad;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Profesional extends Usuario {
    private Especialidad especialidad;
    private long matricula;
    @OneToOne
    private Imagen diploma;


}
