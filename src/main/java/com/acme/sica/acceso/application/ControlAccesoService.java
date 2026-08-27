package com.acme.sica.acceso.application;

import com.acme.sica.acceso.application.strategy.*;
import com.acme.sica.acceso.domain.event.AccesoEvent;
import com.acme.sica.acceso.domain.model.Visita;
import com.acme.sica.acceso.domain.port.VisitaRepository;
import com.acme.sica.auditoria.application.AuditoriaService;
import com.acme.sica.persona.domain.model.Persona;
import com.acme.sica.persona.domain.port.PersonaRepository;
import com.acme.sica.shared.domain.exception.EntidadNoEncontradaException;
import com.acme.sica.shared.security.AutorizacionService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class ControlAccesoService {

    private final VisitaRepository visitaRepository;
    private final PersonaRepository personaRepository;
    private final NotificacionService notificacionService;
    private final AutorizacionService autorizacionService;
    private final AuditoriaService auditoriaService;

    public ControlAccesoService(VisitaRepository visitaRepository,
                                PersonaRepository personaRepository,
                                NotificacionService notificacionService,
                                AutorizacionService autorizacionService,
                                AuditoriaService auditoriaService) {
        this.visitaRepository = visitaRepository;
        this.personaRepository = personaRepository;
        this.notificacionService = notificacionService;
        this.autorizacionService = autorizacionService;
        this.auditoriaService = auditoriaService;
    }

    public List<Visita> listarVisitas() {
        return visitaRepository.findAll();
    }

    public Visita registrarVisita(Visita visita) {
        autorizacionService.verificarPermiso("registrar_visita");
        
        Persona persona = personaRepository.findById(visita.getPersonaId())
                .orElseThrow(() -> new EntidadNoEncontradaException("Persona", visita.getPersonaId()));
        
        if (!"ACTIVO".equals(persona.getEstadoAcceso())) {
            throw new RuntimeException("La persona tiene acceso " + persona.getEstadoAcceso());
        }

        // Revisar si ya tiene una visita abierta (Salida Olvidada)
        Optional<Visita> visitaAbierta = visitaRepository.findVisitaAbierta(persona.getId());
        if (visitaAbierta.isPresent()) {
            EstrategiaAcceso estrategia = new AccesoSalidaOlvidadaStrategy(visitaRepository, auditoriaService);
            estrategia.procesarAcceso(persona, visitaAbierta.get());
        }

        // Registrar nueva visita
        Visita guardada = visitaRepository.save(visita);
        
        // Determinar estrategia (No Anunciado o Pre-registrado)
        EstrategiaAcceso estrategia;
        if ("PENDIENTE".equals(visita.getEstado())) {
            estrategia = new AccesoNoAnunciadoStrategy(notificacionService, auditoriaService);
        } else {
            estrategia = new AccesoPreRegistradoStrategy(auditoriaService);
        }
        
        estrategia.procesarAcceso(persona, guardada);
        return guardada;
    }

    public void checkIn(int visitaId) {
        autorizacionService.verificarPermiso("check_in");
        
        Visita visita = visitaRepository.findById(visitaId)
                .orElseThrow(() -> new EntidadNoEncontradaException("Visita", visitaId));
        
        if (!"APROBADO".equals(visita.getEstado())) {
            throw new RuntimeException("Solo se puede hacer check-in a visitas APROBADAS.");
        }
        
        String ahora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        visitaRepository.registrarEntrada(visitaId, ahora);
        
        notificacionService.notificarEvento(new AccesoEvent(
                AccesoEvent.TipoEvento.CHECK_IN,
                visitaId,
                visita.getPersonaId(),
                "Persona ingresó al complejo",
                visita.getEmpresaDestinoId()
        ));
        
        auditoriaService.registrarAuditoria("CHECK_IN", "VISITAS", visitaId, "Ingreso registrado");
    }

    public void checkOutByPersona(int personaId) {
        autorizacionService.verificarPermiso("check_out");
        
        Visita visita = visitaRepository.findVisitaAbierta(personaId)
                .orElseThrow(() -> new RuntimeException("La persona no tiene un ingreso registrado activo."));
        
        String ahora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        visitaRepository.registrarSalida(visita.getId(), ahora);
        
        notificacionService.notificarEvento(new AccesoEvent(
                AccesoEvent.TipoEvento.CHECK_OUT,
                visita.getId(),
                personaId,
                "Persona salió del complejo",
                visita.getEmpresaDestinoId()
        ));
        
        auditoriaService.registrarAuditoria("CHECK_OUT", "VISITAS", visita.getId(), "Salida registrada");
    }

    public void checkOut(int visitaId) {
        autorizacionService.verificarPermiso("check_out");
        
        Visita visita = visitaRepository.findById(visitaId)
                .orElseThrow(() -> new EntidadNoEncontradaException("Visita", visitaId));
        
        if (!"DENTRO".equals(visita.getEstado())) {
            throw new RuntimeException("La visita no está en estado DENTRO.");
        }
        
        String ahora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        visitaRepository.registrarSalida(visitaId, ahora);
        
        notificacionService.notificarEvento(new AccesoEvent(
                AccesoEvent.TipoEvento.CHECK_OUT,
                visitaId,
                visita.getPersonaId(),
                "Persona salió del complejo",
                visita.getEmpresaDestinoId()
        ));
        
        auditoriaService.registrarAuditoria("CHECK_OUT", "VISITAS", visitaId, "Salida registrada");
    }

    public void aprobarVisita(int visitaId, int funcionarioId, String observaciones) {
        autorizacionService.verificarPermiso("aprobar_visita");
        
        visitaRepository.updateEstado(visitaId, "APROBADO", observaciones);
        visitaRepository.updateFuncionario(visitaId, funcionarioId);
        notificacionService.marcarComoResuelta(visitaId);
        
        auditoriaService.registrarAuditoria("APROBAR", "VISITAS", visitaId, "Visita aprobada por funcionario");
    }

    public void rechazarVisita(int visitaId, int funcionarioId, String observaciones) {
        autorizacionService.verificarPermiso("rechazar_visita");
        
        visitaRepository.updateEstado(visitaId, "RECHAZADO", observaciones);
        visitaRepository.updateFuncionario(visitaId, funcionarioId);
        notificacionService.marcarComoResuelta(visitaId);
        
        auditoriaService.registrarAuditoria("RECHAZAR", "VISITAS", visitaId, "Visita rechazada: " + observaciones);
    }
}
