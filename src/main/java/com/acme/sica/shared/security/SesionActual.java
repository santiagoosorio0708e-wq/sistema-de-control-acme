package com.acme.sica.shared.security;

import com.acme.sica.usuario.domain.model.Usuario;

/**
 * Almacena la sesión actual del usuario autenticado.
 * Utiliza ThreadLocal para soportar múltiples hilos si es necesario.
 */
public final class SesionActual {

    private static final ThreadLocal<Usuario> usuarioActual = new ThreadLocal<>();

    private SesionActual() {
        // Utilidad no instanciable
    }

    /**
     * Establece el usuario autenticado en la sesión actual.
     */
    public static void setUsuario(Usuario usuario) {
        usuarioActual.set(usuario);
    }

    /**
     * Obtiene el usuario autenticado de la sesión actual.
     *
     * @return el usuario actual, o null si no hay sesión activa.
     */
    public static Usuario getUsuario() {
        return usuarioActual.get();
    }

    /**
     * Verifica si hay un usuario autenticado.
     */
    public static boolean isAutenticado() {
        return usuarioActual.get() != null;
    }

    /**
     * Cierra la sesión actual, eliminando el usuario del ThreadLocal.
     */
    public static void cerrarSesion() {
        usuarioActual.remove();
    }

    /**
     * Obtiene el ID del usuario actual, o null si no hay sesión.
     */
    public static Integer getUsuarioId() {
        Usuario u = usuarioActual.get();
        return u != null ? u.getId() : null;
    }

    /**
     * Obtiene el nombre del usuario actual para auditoría.
     */
    public static String getNombreUsuario() {
        Usuario u = usuarioActual.get();
        return u != null ? u.getUsername() : "SISTEMA";
    }
}
