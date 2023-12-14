package com.crisdev.saludservice.model;

import com.crisdev.saludservice.enums.DiaSemana;
import com.crisdev.saludservice.repository.ProfesionalRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
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


    public HorarioLaboral(String dia, LocalTime entrada, LocalTime salida, Profesional profesional) {
    }
}
