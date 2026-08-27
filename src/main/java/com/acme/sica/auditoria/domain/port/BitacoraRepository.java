package com.acme.sica.auditoria.domain.port;

import com.acme.sica.auditoria.domain.model.BitacoraAuditoria;
import java.util.List;

public interface BitacoraRepository {
    void save(BitacoraAuditoria registro);
    List<BitacoraAuditoria> findAll();
    List<BitacoraAuditoria> findByUsuarioId(int usuarioId);
    List<BitacoraAuditoria> findByEntidad(String entidad);
    List<BitacoraAuditoria> findRecent(int limit);
}
