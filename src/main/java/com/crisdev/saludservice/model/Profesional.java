package com.crisdev.saludservice.model;

import com.crisdev.saludservice.enums.Especialidad;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Profesional extends Usuario {

    @Enumerated(EnumType.STRING)
    private Especialidad especialidad;
    private long matricula;
    @OneToOne
    private Imagen diploma;
    @OneToMany
    private List<HorarioLaboral> horarioLaboral;


}
