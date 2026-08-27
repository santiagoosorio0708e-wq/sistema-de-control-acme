package com.acme.sica.usuario.infrastructure.persistence;

import com.acme.sica.usuario.domain.model.Usuario;
import com.acme.sica.usuario.domain.port.UsuarioRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementación JDBC del repositorio de Usuarios para SQLite.
 * Repository Pattern — Adaptador de salida en Arquitectura Hexagonal.
 */
public class UsuarioRepositoryImpl implements UsuarioRepository {

    private final Connection connection;

    public UsuarioRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<Usuario> findById(int id) {
        String sql = "SELECT u.*, r.nombre as rol_nombre FROM usuarios u JOIN roles r ON u.rol_id = r.id WHERE u.id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar usuario por ID: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Usuario> findByUsername(String username) {
        String sql = "SELECT u.*, r.nombre as rol_nombre FROM usuarios u JOIN roles r ON u.rol_id = r.id WHERE u.username = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar usuario por username: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Usuario> findAll() {
        String sql = "SELECT u.*, r.nombre as rol_nombre FROM usuarios u JOIN roles r ON u.rol_id = r.id ORDER BY u.id";
        List<Usuario> list = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar usuarios: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public List<Usuario> findByRolId(int rolId) {
        String sql = "SELECT u.*, r.nombre as rol_nombre FROM usuarios u JOIN roles r ON u.rol_id = r.id WHERE u.rol_id = ?";
        List<Usuario> list = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, rolId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar usuarios por rol: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public Usuario save(Usuario usuario) {
        String sql = "INSERT INTO usuarios (username, password_hash, nombre_completo, rol_id, activo) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, usuario.getUsername());
            ps.setString(2, usuario.getPasswordHash());
            ps.setString(3, usuario.getNombreCompleto());
            ps.setInt(4, usuario.getRolId());
            ps.setInt(5, usuario.isActivo() ? 1 : 0);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) usuario.setId(keys.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar usuario: " + e.getMessage(), e);
        }
        return usuario;
    }

    @Override
    public void update(Usuario usuario) {
        String sql = "UPDATE usuarios SET username = ?, nombre_completo = ?, rol_id = ?, activo = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, usuario.getUsername());
            ps.setString(2, usuario.getNombreCompleto());
            ps.setInt(3, usuario.getRolId());
            ps.setInt(4, usuario.isActivo() ? 1 : 0);
            ps.setInt(5, usuario.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar usuario: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "UPDATE usuarios SET activo = 0 WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al desactivar usuario: " + e.getMessage(), e);
        }
    }

    private Usuario mapRow(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setId(rs.getInt("id"));
        u.setUsername(rs.getString("username"));
        u.setPasswordHash(rs.getString("password_hash"));
        u.setNombreCompleto(rs.getString("nombre_completo"));
        u.setRolId(rs.getInt("rol_id"));
        u.setRolNombre(rs.getString("rol_nombre"));
        u.setActivo(rs.getInt("activo") == 1);
        u.setFechaCreacion(rs.getString("fecha_creacion"));
        return u;
    }
}
