package com.acme.sica.acceso.domain.event;

/**
 * Observer Pattern — Interfaz para escuchar eventos de acceso.
 * Los componentes que necesiten reaccionar a cambios en visitas
 * implementarán esta interfaz.
 *
 * Principio SOLID — Interface Segregation: interfaz mínima con un solo método.
 */
@FunctionalInterface
public interface AccesoEventListener {

    /**
     * Maneja un evento de acceso.
     *
     * @param event el evento de acceso generado
     */
    void onAccesoEvent(AccesoEvent event);
}
