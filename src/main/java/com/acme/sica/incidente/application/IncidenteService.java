package com.acme.sica.incidente.application;

import com.acme.sica.auditoria.application.AuditoriaService;
import com.acme.sica.incidente.domain.model.Incidente;
import com.acme.sica.incidente.domain.port.IncidenteRepository;
import com.acme.sica.shared.domain.exception.EntidadNoEncontradaException;
import com.acme.sica.shared.security.AutorizacionService;

import java.util.List;

public class IncidenteService {

    private final IncidenteRepository incidenteRepository;
    private final AutorizacionService autorizacionService;
    private final AuditoriaService auditoriaService;

    public IncidenteService(IncidenteRepository incidenteRepository, 
                            AutorizacionService autorizacionService, 
                            AuditoriaService auditoriaService) {
        this.incidenteRepository = incidenteRepository;
        this.autorizacionService = autorizacionService;
        this.auditoriaService = auditoriaService;
    }

    public List<Incidente> listarIncidentes() {
        autorizacionService.verificarPermiso("ver_reportes_basicos");
        return incidenteRepository.findAll();
    }

    public Incidente obtenerIncidente(int id) {
        autorizacionService.verificarPermiso("ver_reportes_basicos");
        return incidenteRepository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("Incidente", id));
    }

    public Incidente registrarIncidente(Incidente incidente) {
        autorizacionService.verificarPermiso("registrar_incidente");
        
        if (incidente.getEstado() == null) {
            incidente.setEstado("ABIERTO");
        }
        
        Incidente guardado = incidenteRepository.save(incidente);
        
        auditoriaService.registrarAuditoria(
                "CREAR", "INCIDENTES", guardado.getId(), 
                "Incidente reportado: " + guardado.getTipo()
        );
        
        return guardado;
    }

    public void actualizarEstado(int id, String nuevoEstado) {
        // Asumiendo que solo administradores o jefes de seguridad pueden cerrar incidentes
        autorizacionService.verificarPermiso("gestionar_usuarios"); 
        
        incidenteRepository.updateEstado(id, nuevoEstado);
        
        auditoriaService.registrarAuditoria(
                "ACTUALIZAR", "INCIDENTES", id, 
                "Estado del incidente cambiado a: " + nuevoEstado
        );
    }
}
