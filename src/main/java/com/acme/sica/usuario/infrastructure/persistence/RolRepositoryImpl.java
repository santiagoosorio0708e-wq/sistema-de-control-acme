package com.acme.sica.usuario.infrastructure.persistence;

import com.acme.sica.usuario.domain.model.Rol;
import com.acme.sica.usuario.domain.port.RolRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RolRepositoryImpl implements RolRepository {

    private final Connection connection;

    public RolRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<Rol> findById(int id) {
        String sql = "SELECT * FROM roles WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar rol: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Rol> findByNombre(String nombre) {
        String sql = "SELECT * FROM roles WHERE nombre = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, nombre);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar rol por nombre: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Rol> findAll() {
        List<Rol> list = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM roles ORDER BY id")) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar roles: " + e.getMessage(), e);
        }
        return list;
    }

    private Rol mapRow(ResultSet rs) throws SQLException {
        return new Rol(rs.getInt("id"), rs.getString("nombre"), rs.getString("descripcion"));
    }
}
