package com.acme.sica.reporte.application;

import com.acme.sica.acceso.domain.model.Visita;
import com.acme.sica.acceso.domain.port.VisitaRepository;
import com.acme.sica.incidente.domain.model.Incidente;
import com.acme.sica.incidente.domain.port.IncidenteRepository;
import com.acme.sica.shared.security.AutorizacionService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReporteService {

    private final VisitaRepository visitaRepository;
    private final IncidenteRepository incidenteRepository;
    private final AutorizacionService autorizacionService;

    public ReporteService(VisitaRepository visitaRepository, 
                          IncidenteRepository incidenteRepository, 
                          AutorizacionService autorizacionService) {
        this.visitaRepository = visitaRepository;
        this.incidenteRepository = incidenteRepository;
        this.autorizacionService = autorizacionService;
    }

    public Map<String, Long> obtenerVisitasPorEstado() {
        autorizacionService.verificarPermiso("ver_reportes_basicos");
        
        List<Visita> visitas = visitaRepository.findAll();
        
        // Uso de Streams para agrupar y contar
        return visitas.stream()
                .collect(Collectors.groupingBy(Visita::getEstado, Collectors.counting()));
    }

    public Map<String, Long> obtenerVisitasPorEmpresa() {
        autorizacionService.verificarPermiso("ver_reportes_avanzados");
        
        List<Visita> visitas = visitaRepository.findAll();
        
        return visitas.stream()
                .filter(v -> v.getEmpresaDestinoNombre() != null)
                .collect(Collectors.groupingBy(Visita::getEmpresaDestinoNombre, Collectors.counting()));
    }
    
    public Map<String, Long> obtenerIncidentesPorSeveridad() {
        autorizacionService.verificarPermiso("ver_reportes_avanzados");
        
        List<Incidente> incidentes = incidenteRepository.findAll();
        
        return incidentes.stream()
                .collect(Collectors.groupingBy(Incidente::getSeveridad, Collectors.counting()));
    }
}
