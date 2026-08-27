package com.acme.sica.incidente.domain.model;

/**
 * Entidad que representa un incidente de seguridad en el complejo.
 */
public class Incidente {
    private Integer id;
    private Integer personaId;
    private String personaNombre; // Campo auxiliar
    private Integer visitaId;
    private String tipo;
    private String descripcion;
    private String severidad;
    private Integer reportadoPorId;
    private String reportadoPorNombre; // Campo auxiliar
    private String fechaHora;
    private String estado;

    public Incidente() {}

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getPersonaId() { return personaId; }
    public void setPersonaId(Integer personaId) { this.personaId = personaId; }

    public String getPersonaNombre() { return personaNombre; }
    public void setPersonaNombre(String personaNombre) { this.personaNombre = personaNombre; }

    public Integer getVisitaId() { return visitaId; }
    public void setVisitaId(Integer visitaId) { this.visitaId = visitaId; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getSeveridad() { return severidad; }
    public void setSeveridad(String severidad) { this.severidad = severidad; }

    public Integer getReportadoPorId() { return reportadoPorId; }
    public void setReportadoPorId(Integer reportadoPorId) { this.reportadoPorId = reportadoPorId; }

    public String getReportadoPorNombre() { return reportadoPorNombre; }
    public void setReportadoPorNombre(String reportadoPorNombre) { this.reportadoPorNombre = reportadoPorNombre; }

    public String getFechaHora() { return fechaHora; }
    public void setFechaHora(String fechaHora) { this.fechaHora = fechaHora; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    @Override
    public String toString() {
        return "Incidente #" + id + " [" + severidad + "] " + tipo;
    }
}
