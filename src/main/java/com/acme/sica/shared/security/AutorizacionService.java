package com.acme.sica.shared.security;

import com.acme.sica.shared.domain.exception.AccesoDenegadoException;
import com.acme.sica.usuario.domain.model.Permiso;
import com.acme.sica.usuario.domain.model.Usuario;
import com.acme.sica.usuario.domain.port.PermisoRepository;

import java.util.List;

/**
 * Servicio de autorización basado en RBAC.
 * Verifica si el usuario actual tiene el permiso requerido
 * antes de ejecutar una operación.
 *
 * Principio SOLID — Single Responsibility: Solo se encarga de autorización.
 * Principio SOLID — Dependency Inversion: Depende de la interfaz PermisoRepository.
 */
public class AutorizacionService {

    private final PermisoRepository permisoRepository;

    public AutorizacionService(PermisoRepository permisoRepository) {
        this.permisoRepository = permisoRepository;
    }

    /**
     * Verifica si el usuario actual tiene el permiso indicado.
     * Lanza AccesoDenegadoException si no lo tiene.
     *
     * @param clavePermiso la clave del permiso requerido
     * @throws AccesoDenegadoException si el usuario no tiene el permiso
     */
    public void verificarPermiso(String clavePermiso) {
        Usuario usuario = SesionActual.getUsuario();
        if (usuario == null) {
            throw new AccesoDenegadoException("No hay sesión activa.", clavePermiso);
        }

        List<Permiso> permisos = permisoRepository.findByRolId(usuario.getRolId());
        boolean tienePermiso = permisos.stream()
                .anyMatch(p -> p.getClave().equals(clavePermiso));

        if (!tienePermiso) {
            throw new AccesoDenegadoException(clavePermiso);
        }
    }

    /**
     * Verifica si el usuario actual tiene el permiso indicado (sin lanzar excepción).
     *
     * @param clavePermiso la clave del permiso
     * @return true si el usuario tiene el permiso
     */
    public boolean tienePermiso(String clavePermiso) {
        Usuario usuario = SesionActual.getUsuario();
        if (usuario == null) {
            return false;
        }

        List<Permiso> permisos = permisoRepository.findByRolId(usuario.getRolId());
        return permisos.stream()
                .anyMatch(p -> p.getClave().equals(clavePermiso));
    }
}
