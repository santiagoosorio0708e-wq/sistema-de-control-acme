package com.acme.sica.acceso.application.strategy;

import com.acme.sica.acceso.domain.model.Visita;
import com.acme.sica.auditoria.application.AuditoriaService;
import com.acme.sica.persona.domain.model.Persona;

public class AccesoPreRegistradoStrategy implements EstrategiaAcceso {

    private final AuditoriaService auditoriaService;

    public AccesoPreRegistradoStrategy(AuditoriaService auditoriaService) {
        this.auditoriaService = auditoriaService;
    }

    @Override
    public boolean procesarAcceso(Persona persona, Visita visita) {
        // En un flujo pre-registrado, la visita ya está aprobada o se aprueba automáticamente al crearla.
        auditoriaService.registrarAuditoria(
                "FLUJO_ACCESO", "VISITAS", visita.getId(), 
                "Se procesó acceso PRE-REGISTRADO para " + persona.getDocumento()
        );
        return true;
    }
}
