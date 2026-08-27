package com.acme.sica.acceso.infrastructure.persistence;

import com.acme.sica.acceso.domain.model.Visita;
import com.acme.sica.acceso.domain.port.VisitaRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VisitaRepositoryImpl implements VisitaRepository {

    private final Connection connection;

    public VisitaRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    private String getBaseQuery() {
        return "SELECT v.*, p.nombre as p_nombre, p.apellido as p_apellido, p.documento as p_doc, " +
               "e.nombre as emp_nombre, u.nombre_completo as func_nombre " +
               "FROM visitas v " +
               "JOIN personas p ON v.persona_id = p.id " +
               "LEFT JOIN empresas e ON v.empresa_destino_id = e.id " +
               "LEFT JOIN usuarios u ON v.funcionario_autoriza_id = u.id ";
    }

    @Override
    public Optional<Visita> findById(int id) {
        String sql = getBaseQuery() + "WHERE v.id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar visita por ID", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Visita> findAll() {
        List<Visita> list = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(getBaseQuery() + "ORDER BY v.fecha_creacion DESC")) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar visitas", e);
        }
        return list;
    }

    @Override
    public List<Visita> findByPersonaId(int personaId) {
        String sql = getBaseQuery() + "WHERE v.persona_id = ? ORDER BY v.fecha_creacion DESC";
        return executeQueryWithParam(sql, personaId);
    }

    @Override
    public List<Visita> findByEstado(String estado) {
        String sql = getBaseQuery() + "WHERE v.estado = ? ORDER BY v.fecha_creacion DESC";
        return executeQueryWithStringParam(sql, estado);
    }

    @Override
    public List<Visita> findByEmpresaDestinoId(int empresaId) {
        String sql = getBaseQuery() + "WHERE v.empresa_destino_id = ? ORDER BY v.fecha_creacion DESC";
        return executeQueryWithParam(sql, empresaId);
    }

    @Override
    public List<Visita> findPendientes() {
        return findByEstado("PENDIENTE");
    }

    @Override
    public List<Visita> findPendientesByEmpresaId(int empresaId) {
        String sql = getBaseQuery() + "WHERE v.estado = 'PENDIENTE' AND v.empresa_destino_id = ? ORDER BY v.fecha_creacion ASC";
        return executeQueryWithParam(sql, empresaId);
    }

    @Override
    public List<Visita> findDentro() {
        return findByEstado("DENTRO");
    }

    @Override
    public Optional<Visita> findVisitaAbierta(int personaId) {
        String sql = getBaseQuery() + "WHERE v.persona_id = ? AND v.estado = 'DENTRO' LIMIT 1";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, personaId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar visita abierta", e);
        }
        return Optional.empty();
    }

    @Override
    public Visita save(Visita visita) {
        String sql = "INSERT INTO visitas (persona_id, empresa_destino_id, funcionario_autoriza_id, motivo, estado, observaciones) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, visita.getPersonaId());
            
            if (visita.getEmpresaDestinoId() != null) ps.setInt(2, visita.getEmpresaDestinoId());
            else ps.setNull(2, Types.INTEGER);
            
            if (visita.getFuncionarioAutorizaId() != null) ps.setInt(3, visita.getFuncionarioAutorizaId());
            else ps.setNull(3, Types.INTEGER);
            
            ps.setString(4, visita.getMotivo());
            ps.setString(5, visita.getEstado());
            ps.setString(6, visita.getObservaciones());
            
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) visita.setId(keys.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar visita", e);
        }
        return visita;
    }

    @Override
    public void updateEstado(int id, String estado, String observaciones) {
        String sql = "UPDATE visitas SET estado = ?, observaciones = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, estado);
            ps.setString(2, observaciones);
            ps.setInt(3, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar estado de visita", e);
        }
    }

    @Override
    public void registrarEntrada(int id, String fechaHoraEntrada) {
        String sql = "UPDATE visitas SET fecha_hora_entrada = ?, estado = 'DENTRO' WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, fechaHoraEntrada);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al registrar entrada", e);
        }
    }

    @Override
    public void registrarSalida(int id, String fechaHoraSalida) {
        String sql = "UPDATE visitas SET fecha_hora_salida = ?, estado = 'CERRADA' WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, fechaHoraSalida);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al registrar salida", e);
        }
    }

    @Override
    public void updateFuncionario(int visitaId, int funcionarioId) {
        String sql = "UPDATE visitas SET funcionario_autoriza_id = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, funcionarioId);
            ps.setInt(2, visitaId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar funcionario de la visita", e);
        }
    }

    private List<Visita> executeQueryWithParam(String sql, int param) {
        List<Visita> list = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, param);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error ejecutando consulta de visitas", e);
        }
        return list;
    }
    
    private List<Visita> executeQueryWithStringParam(String sql, String param) {
        List<Visita> list = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, param);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error ejecutando consulta de visitas", e);
        }
        return list;
    }

    private Visita mapRow(ResultSet rs) throws SQLException {
        Visita v = new Visita();
        v.setId(rs.getInt("id"));
        v.setPersonaId(rs.getInt("persona_id"));
        v.setPersonaNombre(rs.getString("p_nombre") + " " + rs.getString("p_apellido"));
        v.setPersonaDocumento(rs.getString("p_doc"));
        
        int empId = rs.getInt("empresa_destino_id");
        if (!rs.wasNull()) {
            v.setEmpresaDestinoId(empId);
            v.setEmpresaDestinoNombre(rs.getString("emp_nombre"));
        }
        
        int funcId = rs.getInt("funcionario_autoriza_id");
        if (!rs.wasNull()) {
            v.setFuncionarioAutorizaId(funcId);
            v.setFuncionarioNombre(rs.getString("func_nombre"));
        }
        
        v.setMotivo(rs.getString("motivo"));
        v.setFechaHoraEntrada(rs.getString("fecha_hora_entrada"));
        v.setFechaHoraSalida(rs.getString("fecha_hora_salida"));
        v.setEstado(rs.getString("estado"));
        v.setObservaciones(rs.getString("observaciones"));
        v.setFechaCreacion(rs.getString("fecha_creacion"));
        return v;
    }
}
