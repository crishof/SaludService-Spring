package com.crisdev.saludservice.enums;

public enum DiaSemana {
    LUNES(1),
    MARTES(2),
    MIERCOLES(3),
    JUEVES(4),
    VIERNES(5);

    private final int valorNumerico;

    DiaSemana(int valorNumerico) {
        this.valorNumerico = valorNumerico;
    }

    public int getValorNumerico() {
        return valorNumerico;
    }
}
