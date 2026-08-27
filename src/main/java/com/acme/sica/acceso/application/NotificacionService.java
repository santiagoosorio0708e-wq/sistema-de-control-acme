package com.acme.sica.acceso.application;

import com.acme.sica.acceso.domain.event.AccesoEvent;
import com.acme.sica.acceso.domain.event.AccesoEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Observer Pattern — Gestor de notificaciones de acceso (Subject).
 * Recibe eventos de acceso y notifica a los listeners registrados.
 */
public class NotificacionService {

    private final List<AccesoEventListener> listeners = new ArrayList<>();
    
    // Lista en memoria para simular notificaciones no leídas en la interfaz web (Polling)
    private final List<AccesoEvent> notificacionesActivas = new ArrayList<>();

    public void registrarListener(AccesoEventListener listener) {
        listeners.add(listener);
    }

    public void notificarEvento(AccesoEvent event) {
        // Guardar para el polling de la web
        notificacionesActivas.add(event);
        
        // Notificar a observadores si los hay (ej. logs, websockets en el futuro)
        for (AccesoEventListener listener : listeners) {
            listener.onAccesoEvent(event);
        }
    }
    
    /**
     * Obtiene notificaciones para una empresa específica (simulación de buzón web).
     */
    public List<AccesoEvent> getNotificacionesParaEmpresa(int empresaId) {
        List<AccesoEvent> result = new ArrayList<>();
        for (AccesoEvent e : notificacionesActivas) {
            if (e.getEmpresaDestinoId() != null && e.getEmpresaDestinoId() == empresaId) {
                result.add(e);
            }
        }
        return result;
    }
    
    /**
     * Limpia una notificación (marcar como leída/resuelta).
     */
    public void marcarComoResuelta(int visitaId) {
        notificacionesActivas.removeIf(e -> e.getVisitaId() != null && e.getVisitaId() == visitaId);
    }
}
