package com.crisdev.saludservice.model;

import com.crisdev.saludservice.enums.Especialidad;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToOne;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Profesional extends Usuario {
    
    @Enumerated(EnumType.STRING)
    private Especialidad especialidad;
    private long matricula;
    @OneToOne
    private Imagen diploma;


}
