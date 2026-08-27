package com.acme.sica.usuario.infrastructure.persistence;

import com.acme.sica.usuario.domain.model.Permiso;
import com.acme.sica.usuario.domain.port.PermisoRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PermisoRepositoryImpl implements PermisoRepository {

    private final Connection connection;

    public PermisoRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Permiso> findByRolId(int rolId) {
        String sql = "SELECT p.* FROM permisos p " +
                     "JOIN rol_permiso rp ON p.id = rp.permiso_id " +
                     "WHERE rp.rol_id = ? ORDER BY p.modulo, p.clave";
        List<Permiso> list = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, rolId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar permisos por rol: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public List<Permiso> findAll() {
        List<Permiso> list = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM permisos ORDER BY modulo, clave")) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar permisos: " + e.getMessage(), e);
        }
        return list;
    }

    private Permiso mapRow(ResultSet rs) throws SQLException {
        return new Permiso(rs.getInt("id"), rs.getString("clave"),
                          rs.getString("descripcion"), rs.getString("modulo"));
    }
}
