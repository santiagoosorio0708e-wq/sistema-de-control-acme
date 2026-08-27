package com.acme.sica.incidente.infrastructure.persistence;

import com.acme.sica.incidente.domain.model.Incidente;
import com.acme.sica.incidente.domain.port.IncidenteRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class IncidenteRepositoryImpl implements IncidenteRepository {

    private final Connection connection;

    public IncidenteRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    private String getBaseQuery() {
        return "SELECT i.*, p.nombre as p_nombre, p.apellido as p_apellido, u.nombre_completo as u_nombre " +
               "FROM incidentes i " +
               "LEFT JOIN personas p ON i.persona_id = p.id " +
               "LEFT JOIN usuarios u ON i.reportado_por_id = u.id ";
    }

    @Override
    public Optional<Incidente> findById(int id) {
        String sql = getBaseQuery() + "WHERE i.id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar incidente por ID", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Incidente> findAll() {
        List<Incidente> list = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(getBaseQuery() + "ORDER BY i.fecha_hora DESC")) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar incidentes", e);
        }
        return list;
    }

    @Override
    public List<Incidente> findByPersonaId(int personaId) {
        String sql = getBaseQuery() + "WHERE i.persona_id = ? ORDER BY i.fecha_hora DESC";
        List<Incidente> list = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, personaId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar incidentes por persona", e);
        }
        return list;
    }

    @Override
    public List<Incidente> findByEstado(String estado) {
        String sql = getBaseQuery() + "WHERE i.estado = ? ORDER BY i.fecha_hora DESC";
        List<Incidente> list = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, estado);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar incidentes por estado", e);
        }
        return list;
    }

    @Override
    public Incidente save(Incidente incidente) {
        String sql = "INSERT INTO incidentes (persona_id, visita_id, tipo, descripcion, severidad, reportado_por_id, estado) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            if (incidente.getPersonaId() != null) ps.setInt(1, incidente.getPersonaId());
            else ps.setNull(1, Types.INTEGER);
            
            if (incidente.getVisitaId() != null) ps.setInt(2, incidente.getVisitaId());
            else ps.setNull(2, Types.INTEGER);
            
            ps.setString(3, incidente.getTipo());
            ps.setString(4, incidente.getDescripcion());
            ps.setString(5, incidente.getSeveridad());
            
            if (incidente.getReportadoPorId() != null) ps.setInt(6, incidente.getReportadoPorId());
            else ps.setNull(6, Types.INTEGER);
            
            ps.setString(7, incidente.getEstado());
            
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) incidente.setId(keys.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar incidente", e);
        }
        return incidente;
    }

    @Override
    public void updateEstado(int id, String estado) {
        String sql = "UPDATE incidentes SET estado = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, estado);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar estado del incidente", e);
        }
    }

    private Incidente mapRow(ResultSet rs) throws SQLException {
        Incidente i = new Incidente();
        i.setId(rs.getInt("id"));
        
        int pId = rs.getInt("persona_id");
        if (!rs.wasNull()) {
            i.setPersonaId(pId);
            i.setPersonaNombre(rs.getString("p_nombre") + " " + rs.getString("p_apellido"));
        }
        
        int vId = rs.getInt("visita_id");
        if (!rs.wasNull()) i.setVisitaId(vId);
        
        i.setTipo(rs.getString("tipo"));
        i.setDescripcion(rs.getString("descripcion"));
        i.setSeveridad(rs.getString("severidad"));
        
        int uId = rs.getInt("reportado_por_id");
        if (!rs.wasNull()) {
            i.setReportadoPorId(uId);
            i.setReportadoPorNombre(rs.getString("u_nombre"));
        }
        
        i.setFechaHora(rs.getString("fecha_hora"));
        i.setEstado(rs.getString("estado"));
        return i;
    }
}
