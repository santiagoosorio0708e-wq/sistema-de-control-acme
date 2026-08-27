package com.acme.sica.auditoria.domain.model;

/**
 * Entidad que representa un registro de la bitácora de auditoría.
 * Registra cada acción relevante del sistema de manera inmutable.
 */
public class BitacoraAuditoria {
    private Integer id;
    private Integer usuarioId;
    private String usuarioNombre; // Campo auxiliar
    private String accion;
    private String entidad;
    private Integer entidadId;
    private String detalle;
    private String fechaHora;

    public BitacoraAuditoria() {}

    public BitacoraAuditoria(Integer usuarioId, String accion, String entidad,
                              Integer entidadId, String detalle) {
        this.usuarioId = usuarioId;
        this.accion = accion;
        this.entidad = entidad;
        this.entidadId = entidadId;
        this.detalle = detalle;
    }

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Integer usuarioId) { this.usuarioId = usuarioId; }

    public String getUsuarioNombre() { return usuarioNombre; }
    public void setUsuarioNombre(String usuarioNombre) { this.usuarioNombre = usuarioNombre; }

    public String getAccion() { return accion; }
    public void setAccion(String accion) { this.accion = accion; }

    public String getEntidad() { return entidad; }
    public void setEntidad(String entidad) { this.entidad = entidad; }

    public Integer getEntidadId() { return entidadId; }
    public void setEntidadId(Integer entidadId) { this.entidadId = entidadId; }

    public String getDetalle() { return detalle; }
    public void setDetalle(String detalle) { this.detalle = detalle; }

    public String getFechaHora() { return fechaHora; }
    public void setFechaHora(String fechaHora) { this.fechaHora = fechaHora; }

    @Override
    public String toString() {
        return "[" + fechaHora + "] " + accion + " - " + entidad + " #" + entidadId;
    }
}
