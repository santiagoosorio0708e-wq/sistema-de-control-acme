package com.acme.sica.usuario.domain.port;

import com.acme.sica.usuario.domain.model.Permiso;
import java.util.List;

public interface PermisoRepository {
    List<Permiso> findByRolId(int rolId);
    List<Permiso> findAll();
}
