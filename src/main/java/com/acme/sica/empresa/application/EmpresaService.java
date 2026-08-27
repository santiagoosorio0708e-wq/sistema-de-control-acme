package com.acme.sica.empresa.application;

import com.acme.sica.auditoria.application.AuditoriaService;
import com.acme.sica.empresa.domain.model.Empresa;
import com.acme.sica.empresa.domain.port.EmpresaRepository;
import com.acme.sica.shared.domain.exception.EntidadNoEncontradaException;
import com.acme.sica.shared.security.AutorizacionService;

import java.util.List;

public class EmpresaService {

    private final EmpresaRepository empresaRepository;
    private final AutorizacionService autorizacionService;
    private final AuditoriaService auditoriaService;

    public EmpresaService(EmpresaRepository empresaRepository, 
                          AutorizacionService autorizacionService, 
                          AuditoriaService auditoriaService) {
        this.empresaRepository = empresaRepository;
        this.autorizacionService = autorizacionService;
        this.auditoriaService = auditoriaService;
    }

    public List<Empresa> listarEmpresas() {
        return empresaRepository.findAll();
    }

    public List<Empresa> listarEmpresasActivas() {
        return empresaRepository.findActivas();
    }

    public Empresa obtenerEmpresa(int id) {
        return empresaRepository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("Empresa", id));
    }

    public Empresa crearEmpresa(Empresa empresa) {
        autorizacionService.verificarPermiso("gestionar_empresas");
        empresa.setActiva(true);
        
        Empresa guardada = empresaRepository.save(empresa);
        
        auditoriaService.registrarAuditoria(
                "CREAR", "EMPRESAS", guardada.getId(), 
                "Empresa creada: " + guardada.getNombre()
        );
        
        return guardada;
    }

    public void actualizarEmpresa(int id, Empresa actualizacion) {
        autorizacionService.verificarPermiso("gestionar_empresas");
        
        Empresa existente = obtenerEmpresa(id);
        existente.setNombre(actualizacion.getNombre());
        existente.setNit(actualizacion.getNit());
        existente.setSector(actualizacion.getSector());
        existente.setTelefonoContacto(actualizacion.getTelefonoContacto());
        existente.setEmailContacto(actualizacion.getEmailContacto());
        existente.setActiva(actualizacion.isActiva());
        
        empresaRepository.update(existente);
        
        auditoriaService.registrarAuditoria(
                "ACTUALIZAR", "EMPRESAS", id, 
                "Empresa actualizada: " + existente.getNombre()
        );
    }
}
