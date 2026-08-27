package com.acme.sica.auditoria.infrastructure.persistence;

import com.acme.sica.auditoria.domain.model.BitacoraAuditoria;
import com.acme.sica.auditoria.domain.port.BitacoraRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BitacoraRepositoryImpl implements BitacoraRepository {

    private final Connection connection;

    public BitacoraRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    private String getBaseQuery() {
        return "SELECT b.*, u.nombre_completo as u_nombre FROM bitacora_auditoria b " +
               "LEFT JOIN usuarios u ON b.usuario_id = u.id ";
    }

    @Override
    public void save(BitacoraAuditoria registro) {
        String sql = "INSERT INTO bitacora_auditoria (usuario_id, accion, entidad, entidad_id, detalle) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            if (registro.getUsuarioId() != null) ps.setInt(1, registro.getUsuarioId());
            else ps.setNull(1, Types.INTEGER);
            
            ps.setString(2, registro.getAccion());
            ps.setString(3, registro.getEntidad());
            
            if (registro.getEntidadId() != null) ps.setInt(4, registro.getEntidadId());
            else ps.setNull(4, Types.INTEGER);
            
            ps.setString(5, registro.getDetalle());
            
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) registro.setId(keys.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar en bitácora", e);
        }
    }

    @Override
    public List<BitacoraAuditoria> findAll() {
        List<BitacoraAuditoria> list = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(getBaseQuery() + "ORDER BY b.fecha_hora DESC")) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar bitácora", e);
        }
        return list;
    }

    @Override
    public List<BitacoraAuditoria> findByUsuarioId(int usuarioId) {
        String sql = getBaseQuery() + "WHERE b.usuario_id = ? ORDER BY b.fecha_hora DESC";
        List<BitacoraAuditoria> list = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar bitácora por usuario", e);
        }
        return list;
    }

    @Override
    public List<BitacoraAuditoria> findByEntidad(String entidad) {
        String sql = getBaseQuery() + "WHERE b.entidad = ? ORDER BY b.fecha_hora DESC";
        List<BitacoraAuditoria> list = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, entidad);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar bitácora por entidad", e);
        }
        return list;
    }

    @Override
    public List<BitacoraAuditoria> findRecent(int limit) {
        String sql = getBaseQuery() + "ORDER BY b.fecha_hora DESC LIMIT ?";
        List<BitacoraAuditoria> list = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar bitácora reciente", e);
        }
        return list;
    }

    private BitacoraAuditoria mapRow(ResultSet rs) throws SQLException {
        BitacoraAuditoria b = new BitacoraAuditoria();
        b.setId(rs.getInt("id"));
        
        int uId = rs.getInt("usuario_id");
        if (!rs.wasNull()) {
            b.setUsuarioId(uId);
            b.setUsuarioNombre(rs.getString("u_nombre"));
        }
        
        b.setAccion(rs.getString("accion"));
        b.setEntidad(rs.getString("entidad"));
        
        int eId = rs.getInt("entidad_id");
        if (!rs.wasNull()) b.setEntidadId(eId);
        
        b.setDetalle(rs.getString("detalle"));
        b.setFechaHora(rs.getString("fecha_hora"));
        return b;
    }
}
