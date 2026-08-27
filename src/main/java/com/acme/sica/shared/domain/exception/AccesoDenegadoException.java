package com.acme.sica.shared.domain.exception;

/**
 * Excepción lanzada cuando un usuario intenta realizar una acción
 * para la cual no tiene el permiso requerido (RBAC).
 */
public class AccesoDenegadoException extends RuntimeException {

    private final String permisoRequerido;

    public AccesoDenegadoException(String permisoRequerido) {
        super("Acceso denegado. No tiene el permiso requerido: " + permisoRequerido);
        this.permisoRequerido = permisoRequerido;
    }

    public AccesoDenegadoException(String mensaje, String permisoRequerido) {
        super(mensaje);
        this.permisoRequerido = permisoRequerido;
    }

    public String getPermisoRequerido() {
        return permisoRequerido;
    }
}
