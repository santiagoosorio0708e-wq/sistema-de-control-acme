package com.acme.sica.usuario.domain.port;

import com.acme.sica.usuario.domain.model.Usuario;
import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida para la persistencia de Usuarios.
 * Principio SOLID — Dependency Inversion: la capa de aplicación
 * depende de esta abstracción, no de la implementación JDBC.
 */
public interface UsuarioRepository {
    Optional<Usuario> findById(int id);
    Optional<Usuario> findByUsername(String username);
    List<Usuario> findAll();
    List<Usuario> findByRolId(int rolId);
    Usuario save(Usuario usuario);
    void update(Usuario usuario);
    void delete(int id);
}
