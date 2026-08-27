package com.acme.sica.usuario.domain.port;

import com.acme.sica.usuario.domain.model.Rol;
import java.util.List;
import java.util.Optional;

public interface RolRepository {
    Optional<Rol> findById(int id);
    Optional<Rol> findByNombre(String nombre);
    List<Rol> findAll();
}
