package com.acme.sica.auditoria.application;

import com.acme.sica.auditoria.domain.model.BitacoraAuditoria;
import com.acme.sica.auditoria.domain.port.BitacoraRepository;
import com.acme.sica.shared.security.AutorizacionService;
import com.acme.sica.shared.security.SesionActual;

import java.util.List;

/**
 * Servicio de aplicación para la gestión de auditoría.
 */
public class AuditoriaService {

    private final BitacoraRepository bitacoraRepository;
    private final AutorizacionService autorizacionService;

    public AuditoriaService(BitacoraRepository bitacoraRepository, AutorizacionService autorizacionService) {
        this.bitacoraRepository = bitacoraRepository;
        this.autorizacionService = autorizacionService;
    }

    /**
     * Registra un evento en la bitácora de auditoría.
     * Utiliza el ID de usuario de la sesión actual.
     */
    public void registrarAuditoria(String accion, String entidad, Integer entidadId, String detalle) {
        Integer usuarioId = SesionActual.getUsuarioId();
        
        BitacoraAuditoria registro = new BitacoraAuditoria(
                usuarioId,
                accion,
                entidad,
                entidadId,
                detalle
        );
        
        bitacoraRepository.save(registro);
    }

    /**
     * Obtiene los últimos N registros de la bitácora.
     * Requiere permiso: ver_reportes_basicos o ver_reportes_avanzados
     */
    public List<BitacoraAuditoria> obtenerRegistrosRecientes(int limite) {
        if (!autorizacionService.tienePermiso("ver_reportes_basicos") && 
            !autorizacionService.tienePermiso("ver_reportes_avanzados")) {
            autorizacionService.verificarPermiso("ver_reportes_avanzados"); // Lanzará excepción
        }
        return bitacoraRepository.findRecent(limite);
    }

    /**
     * Obtiene registros filtrados por entidad.
     */
    public List<BitacoraAuditoria> obtenerPorEntidad(String entidad) {
        autorizacionService.verificarPermiso("ver_reportes_avanzados");
        return bitacoraRepository.findByEntidad(entidad);
    }
}
