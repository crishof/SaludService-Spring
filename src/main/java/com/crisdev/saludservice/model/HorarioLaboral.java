package com.crisdev.saludservice.model;

import com.crisdev.saludservice.enums.DiaSemana;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
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

    @Enumerated(EnumType.STRING)
    private DiaSemana diaSemana;
    private LocalTime horaEntrada;
    private LocalTime horaSalida;

    @Override
    public String toString() {
        return diaSemana +
                ", De: " + horaEntrada +
                " a " + horaSalida;
    }
}
