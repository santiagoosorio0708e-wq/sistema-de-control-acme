package com.acme.sica.persona.infrastructure.persistence;

import com.acme.sica.persona.domain.model.Persona;
import com.acme.sica.persona.domain.port.PersonaRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PersonaRepositoryImpl implements PersonaRepository {

    private final Connection connection;

    public PersonaRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<Persona> findById(int id) {
        String sql = "SELECT p.*, e.nombre as empresa_nombre FROM personas p " +
                     "LEFT JOIN empresas e ON p.empresa_id = e.id WHERE p.id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar persona por ID", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Persona> findByDocumento(String documento) {
        String sql = "SELECT p.*, e.nombre as empresa_nombre FROM personas p " +
                     "LEFT JOIN empresas e ON p.empresa_id = e.id WHERE p.documento = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, documento);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar persona por documento", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Persona> findAll() {
        String sql = "SELECT p.*, e.nombre as empresa_nombre FROM personas p " +
                     "LEFT JOIN empresas e ON p.empresa_id = e.id ORDER BY p.nombre, p.apellido";
        List<Persona> list = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar personas", e);
        }
        return list;
    }

    @Override
    public List<Persona> findByTipoPersona(String tipoPersona) {
        String sql = "SELECT p.*, e.nombre as empresa_nombre FROM personas p " +
                     "LEFT JOIN empresas e ON p.empresa_id = e.id WHERE p.tipo_persona = ? ORDER BY p.nombre";
        List<Persona> list = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, tipoPersona);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar personas por tipo", e);
        }
        return list;
    }

    @Override
    public List<Persona> findByEmpresaId(int empresaId) {
        String sql = "SELECT p.*, e.nombre as empresa_nombre FROM personas p " +
                     "LEFT JOIN empresas e ON p.empresa_id = e.id WHERE p.empresa_id = ? ORDER BY p.nombre";
        List<Persona> list = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, empresaId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar personas por empresa", e);
        }
        return list;
    }

    @Override
    public Persona save(Persona persona) {
        String sql = "INSERT INTO personas (documento, tipo_documento, nombre, apellido, tipo_persona, empresa_id, foto_url, estado_acceso, telefono, email) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, persona.getDocumento());
            ps.setString(2, persona.getTipoDocumento());
            ps.setString(3, persona.getNombre());
            ps.setString(4, persona.getApellido());
            ps.setString(5, persona.getTipoPersona());
            if (persona.getEmpresaId() != null) ps.setInt(6, persona.getEmpresaId());
            else ps.setNull(6, Types.INTEGER);
            ps.setString(7, persona.getFotoUrl());
            ps.setString(8, persona.getEstadoAcceso());
            ps.setString(9, persona.getTelefono());
            ps.setString(10, persona.getEmail());
            
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) persona.setId(keys.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar persona", e);
        }
        return persona;
    }

    @Override
    public void update(Persona persona) {
        String sql = "UPDATE personas SET documento=?, tipo_documento=?, nombre=?, apellido=?, tipo_persona=?, " +
                     "empresa_id=?, foto_url=?, estado_acceso=?, telefono=?, email=? WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, persona.getDocumento());
            ps.setString(2, persona.getTipoDocumento());
            ps.setString(3, persona.getNombre());
            ps.setString(4, persona.getApellido());
            ps.setString(5, persona.getTipoPersona());
            if (persona.getEmpresaId() != null) ps.setInt(6, persona.getEmpresaId());
            else ps.setNull(6, Types.INTEGER);
            ps.setString(7, persona.getFotoUrl());
            ps.setString(8, persona.getEstadoAcceso());
            ps.setString(9, persona.getTelefono());
            ps.setString(10, persona.getEmail());
            ps.setInt(11, persona.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar persona", e);
        }
    }

    @Override
    public void delete(int id) {
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM personas WHERE id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar persona", e);
        }
    }

    private Persona mapRow(ResultSet rs) throws SQLException {
        Persona p = new Persona();
        p.setId(rs.getInt("id"));
        p.setDocumento(rs.getString("documento"));
        p.setTipoDocumento(rs.getString("tipo_documento"));
        p.setNombre(rs.getString("nombre"));
        p.setApellido(rs.getString("apellido"));
        p.setTipoPersona(rs.getString("tipo_persona"));
        
        int empId = rs.getInt("empresa_id");
        if (!rs.wasNull()) {
            p.setEmpresaId(empId);
            p.setEmpresaNombre(rs.getString("empresa_nombre"));
        }
        
        p.setFotoUrl(rs.getString("foto_url"));
        p.setEstadoAcceso(rs.getString("estado_acceso"));
        p.setTelefono(rs.getString("telefono"));
        p.setEmail(rs.getString("email"));
        return p;
    }
}
