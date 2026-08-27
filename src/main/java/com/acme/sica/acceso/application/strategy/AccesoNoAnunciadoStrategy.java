package com.acme.sica.acceso.application.strategy;

import com.acme.sica.acceso.application.NotificacionService;
import com.acme.sica.acceso.domain.event.AccesoEvent;
import com.acme.sica.acceso.domain.model.Visita;
import com.acme.sica.auditoria.application.AuditoriaService;
import com.acme.sica.persona.domain.model.Persona;

public class AccesoNoAnunciadoStrategy implements EstrategiaAcceso {

    private final NotificacionService notificacionService;
    private final AuditoriaService auditoriaService;

    public AccesoNoAnunciadoStrategy(NotificacionService notificacionService, AuditoriaService auditoriaService) {
        this.notificacionService = notificacionService;
        this.auditoriaService = auditoriaService;
    }

    @Override
    public boolean procesarAcceso(Persona persona, Visita visita) {
        // En un flujo no anunciado, se requiere aprobación.
        // Se emite evento para notificar al funcionario de la empresa destino.
        AccesoEvent evento = new AccesoEvent(
                AccesoEvent.TipoEvento.VISITA_PENDIENTE,
                visita.getId(),
                persona.getId(),
                "Visita no anunciada pendiente de aprobación: " + persona.getNombreCompleto(),
                visita.getEmpresaDestinoId()
        );
        notificacionService.notificarEvento(evento);
        
        auditoriaService.registrarAuditoria(
                "FLUJO_ACCESO", "VISITAS", visita.getId(), 
                "Se procesó acceso NO ANUNCIADO y se notificó a la empresa."
        );
        
        return false; // Requiere aprobación
    }
}
