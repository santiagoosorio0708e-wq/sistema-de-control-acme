package com.acme.sica.acceso.application.strategy;

import com.acme.sica.acceso.domain.model.Visita;
import com.acme.sica.acceso.domain.port.VisitaRepository;
import com.acme.sica.auditoria.application.AuditoriaService;
import com.acme.sica.persona.domain.model.Persona;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AccesoSalidaOlvidadaStrategy implements EstrategiaAcceso {

    private final VisitaRepository visitaRepository;
    private final AuditoriaService auditoriaService;

    public AccesoSalidaOlvidadaStrategy(VisitaRepository visitaRepository, AuditoriaService auditoriaService) {
        this.visitaRepository = visitaRepository;
        this.auditoriaService = auditoriaService;
    }

    @Override
    public boolean procesarAcceso(Persona persona, Visita visitaAbiertaPrevia) {
        // Cerrar la visita previa por el sistema
        String ahora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        visitaRepository.registrarSalida(visitaAbiertaPrevia.getId(), ahora);
        visitaRepository.updateEstado(visitaAbiertaPrevia.getId(), "CERRADA_POR_SISTEMA", "Sistema detectó nuevo ingreso sin salida previa.");
        
        auditoriaService.registrarAuditoria(
                "SALIDA_OLVIDADA", "VISITAS", visitaAbiertaPrevia.getId(), 
                "Se cerró visita automáticamente por nuevo ingreso de " + persona.getDocumento()
        );
        
        return true;
    }
}
