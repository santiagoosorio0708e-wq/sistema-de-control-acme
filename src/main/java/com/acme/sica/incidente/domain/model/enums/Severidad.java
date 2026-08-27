package com.acme.sica.incidente.domain.model.enums;

/**
 * Nivel de severidad de un incidente de seguridad.
 */
public enum Severidad {
    BAJA("Baja"),
    MEDIA("Media"),
    ALTA("Alta"),
    CRITICA("Crítica");

    private final String descripcion;

    Severidad(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
