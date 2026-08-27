package com.acme.sica.shared.domain.exception;

/**
 * Excepción lanzada cuando no se encuentra una entidad por su identificador.
 */
public class EntidadNoEncontradaException extends RuntimeException {

    private final String entidad;
    private final Object identificador;

    public EntidadNoEncontradaException(String entidad, Object identificador) {
        super(String.format("No se encontró %s con identificador: %s", entidad, identificador));
        this.entidad = entidad;
        this.identificador = identificador;
    }

    public String getEntidad() {
        return entidad;
    }

    public Object getIdentificador() {
        return identificador;
    }
}
