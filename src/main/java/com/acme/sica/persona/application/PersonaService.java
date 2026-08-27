package com.acme.sica.persona.application;

import com.acme.sica.auditoria.application.AuditoriaService;
import com.acme.sica.persona.domain.model.Persona;
import com.acme.sica.persona.domain.port.PersonaRepository;
import com.acme.sica.shared.domain.exception.EntidadNoEncontradaException;
import com.acme.sica.shared.security.AutorizacionService;

import java.util.List;

public class PersonaService {

    private final PersonaRepository personaRepository;
    private final AutorizacionService autorizacionService;
    private final AuditoriaService auditoriaService;

    public PersonaService(PersonaRepository personaRepository, 
                          AutorizacionService autorizacionService, 
                          AuditoriaService auditoriaService) {
        this.personaRepository = personaRepository;
        this.autorizacionService = autorizacionService;
        this.auditoriaService = auditoriaService;
    }

    public List<Persona> listarPersonas() {
        return personaRepository.findAll();
    }

    public Persona obtenerPersona(int id) {
        return personaRepository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("Persona", id));
    }

    public Persona buscarPorDocumento(String documento) {
        return personaRepository.findByDocumento(documento)
                .orElseThrow(() -> new EntidadNoEncontradaException("Persona", documento));
    }

    public Persona crearPersona(Persona persona) {
        autorizacionService.verificarPermiso("crear_persona");
        
        // Asignar estado por defecto
        if (persona.getEstadoAcceso() == null) {
            persona.setEstadoAcceso("ACTIVO");
        }
        
        Persona guardada = personaRepository.save(persona);
        
        auditoriaService.registrarAuditoria(
                "CREAR", "PERSONAS", guardada.getId(), 
                "Persona registrada: " + guardada.getDocumento()
        );
        
        return guardada;
    }

    public void actualizarPersona(int id, Persona actualizacion) {
        autorizacionService.verificarPermiso("editar_persona");
        
        Persona existente = obtenerPersona(id);
        existente.setDocumento(actualizacion.getDocumento());
        existente.setTipoDocumento(actualizacion.getTipoDocumento());
        existente.setNombre(actualizacion.getNombre());
        existente.setApellido(actualizacion.getApellido());
        existente.setTipoPersona(actualizacion.getTipoPersona());
        existente.setEmpresaId(actualizacion.getEmpresaId());
        existente.setTelefono(actualizacion.getTelefono());
        existente.setEmail(actualizacion.getEmail());
        
        personaRepository.update(existente);
        
        auditoriaService.registrarAuditoria(
                "ACTUALIZAR", "PERSONAS", id, 
                "Persona actualizada: " + existente.getDocumento()
        );
    }

    public void cambiarEstadoAcceso(int id, String nuevoEstado) {
        autorizacionService.verificarPermiso("cambiar_estado_acceso");
        
        Persona existente = obtenerPersona(id);
        existente.setEstadoAcceso(nuevoEstado);
        
        personaRepository.update(existente);
        
        auditoriaService.registrarAuditoria(
                "CAMBIAR_ESTADO", "PERSONAS", id, 
                "Estado cambiado a " + nuevoEstado
        );
    }
}
