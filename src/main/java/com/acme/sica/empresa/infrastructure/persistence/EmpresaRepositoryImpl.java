package com.acme.sica.empresa.infrastructure.persistence;

import com.acme.sica.empresa.domain.model.Empresa;
import com.acme.sica.empresa.domain.port.EmpresaRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmpresaRepositoryImpl implements EmpresaRepository {

    private final Connection connection;

    public EmpresaRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<Empresa> findById(int id) {
        String sql = "SELECT * FROM empresas WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar empresa: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Empresa> findByNit(String nit) {
        String sql = "SELECT * FROM empresas WHERE nit = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, nit);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar empresa por NIT: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Empresa> findAll() {
        List<Empresa> list = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM empresas ORDER BY nombre")) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar empresas: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public List<Empresa> findActivas() {
        List<Empresa> list = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM empresas WHERE activa = 1 ORDER BY nombre")) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar empresas activas: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public Empresa save(Empresa empresa) {
        String sql = "INSERT INTO empresas (nombre, nit, sector, telefono_contacto, email_contacto, activa) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, empresa.getNombre());
            ps.setString(2, empresa.getNit());
            ps.setString(3, empresa.getSector());
            ps.setString(4, empresa.getTelefonoContacto());
            ps.setString(5, empresa.getEmailContacto());
            ps.setInt(6, empresa.isActiva() ? 1 : 0);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) empresa.setId(keys.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar empresa: " + e.getMessage(), e);
        }
        return empresa;
    }

    @Override
    public void update(Empresa empresa) {
        String sql = "UPDATE empresas SET nombre=?, nit=?, sector=?, telefono_contacto=?, email_contacto=?, activa=? WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, empresa.getNombre());
            ps.setString(2, empresa.getNit());
            ps.setString(3, empresa.getSector());
            ps.setString(4, empresa.getTelefonoContacto());
            ps.setString(5, empresa.getEmailContacto());
            ps.setInt(6, empresa.isActiva() ? 1 : 0);
            ps.setInt(7, empresa.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar empresa: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(int id) {
        try (PreparedStatement ps = connection.prepareStatement("UPDATE empresas SET activa = 0 WHERE id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al desactivar empresa: " + e.getMessage(), e);
        }
    }

    private Empresa mapRow(ResultSet rs) throws SQLException {
        Empresa e = new Empresa();
        e.setId(rs.getInt("id"));
        e.setNombre(rs.getString("nombre"));
        e.setNit(rs.getString("nit"));
        e.setSector(rs.getString("sector"));
        e.setTelefonoContacto(rs.getString("telefono_contacto"));
        e.setEmailContacto(rs.getString("email_contacto"));
        e.setActiva(rs.getInt("activa") == 1);
        return e;
    }
}
