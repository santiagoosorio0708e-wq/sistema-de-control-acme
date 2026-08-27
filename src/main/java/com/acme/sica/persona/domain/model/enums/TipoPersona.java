package com.acme.sica.persona.domain.model.enums;

/**
 * Tipos de persona en el complejo.
 */
public enum TipoPersona {
    TRABAJADOR("Trabajador"),
    VISITANTE("Visitante");

    private final String descripcion;

    TipoPersona(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
