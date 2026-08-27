package com.acme.sica.persona.domain.model.enums;

/**
 * Estado de acceso de una persona al complejo.
 */
public enum EstadoAcceso {
    ACTIVO("Activo - Acceso permitido"),
    BLOQUEADO("Bloqueado - Acceso denegado"),
    RESTRINGIDO("Restringido - Acceso con restricciones");

    private final String descripcion;

    EstadoAcceso(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
