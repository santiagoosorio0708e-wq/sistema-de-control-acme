package com.acme.sica.acceso.application.strategy;

import com.acme.sica.acceso.application.NotificacionService;
import com.acme.sica.acceso.domain.event.AccesoEvent;
import com.acme.sica.acceso.domain.model.Visita;
import com.acme.sica.auditoria.application.AuditoriaService;
import com.acme.sica.persona.domain.model.Persona;

public class AccesoPaseTemporalStrategy implements EstrategiaAcceso {

    private final NotificacionService notificacionService;
    private final AuditoriaService auditoriaService;

    public AccesoPaseTemporalStrategy(NotificacionService notificacionService, AuditoriaService auditoriaService) {
        this.notificacionService = notificacionService;
        this.auditoriaService = auditoriaService;
    }

    @Override
    public boolean procesarAcceso(Persona persona, Visita visita) {
        // En un flujo de pase temporal (ej. trabajador olvidó carnet), requiere aprobación de RRHH o su gerente.
        AccesoEvent evento = new AccesoEvent(
                AccesoEvent.TipoEvento.VISITA_PENDIENTE,
                visita.getId(),
                persona.getId(),
                "Solicitud de PASE TEMPORAL pendiente de aprobación para: " + persona.getNombreCompleto(),
                visita.getEmpresaDestinoId()
        );
        notificacionService.notificarEvento(evento);
        
        auditoriaService.registrarAuditoria(
                "FLUJO_ACCESO", "VISITAS", visita.getId(), 
                "Se procesó acceso PASE TEMPORAL y se notificó a la empresa."
        );
        
        return false; // Requiere aprobación
    }
}
