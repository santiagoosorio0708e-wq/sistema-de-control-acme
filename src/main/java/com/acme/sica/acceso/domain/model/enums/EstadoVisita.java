package com.acme.sica.acceso.domain.model.enums;

/**
 * Estados posibles de una visita en el sistema.
 */
public enum EstadoVisita {
    APROBADO("Aprobado - Autorizado para ingreso"),
    PENDIENTE("Pendiente de Aprobación"),
    RECHAZADO("Rechazado - Acceso denegado"),
    DENTRO("Dentro del complejo"),
    CERRADA("Cerrada - Visita finalizada"),
    CERRADA_POR_SISTEMA("Cerrada por Sistema - Salida no registrada");

    private final String descripcion;

    EstadoVisita(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
