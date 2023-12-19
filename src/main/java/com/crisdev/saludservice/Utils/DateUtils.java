package com.crisdev.saludservice.Utils;

import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.util.Locale;

@Component
public class DateUtils {
    public String obtenerNombreDiaSemanaSPA(DayOfWeek dayOfWeek) {
        return dayOfWeek.getDisplayName(java.time.format.TextStyle.FULL, new Locale("es"));
    }
}
