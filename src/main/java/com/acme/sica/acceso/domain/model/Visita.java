package com.acme.sica.acceso.domain.model;

/**
 * Entidad que representa una visita (registro de entrada/salida) al complejo.
 */
public class Visita {
    private Integer id;
    private Integer personaId;
    private String personaNombre;   // Campo auxiliar
    private String personaDocumento; // Campo auxiliar
    private Integer empresaDestinoId;
    private String empresaDestinoNombre; // Campo auxiliar
    private Integer funcionarioAutorizaId;
    private String funcionarioNombre;    // Campo auxiliar
    private String motivo;
    private String fechaHoraEntrada;
    private String fechaHoraSalida;
    private String estado;
    private String observaciones;
    private String fechaCreacion;

    public Visita() {}

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getPersonaId() { return personaId; }
    public void setPersonaId(Integer personaId) { this.personaId = personaId; }

    public String getPersonaNombre() { return personaNombre; }
    public void setPersonaNombre(String personaNombre) { this.personaNombre = personaNombre; }

    public String getPersonaDocumento() { return personaDocumento; }
    public void setPersonaDocumento(String personaDocumento) { this.personaDocumento = personaDocumento; }

    public Integer getEmpresaDestinoId() { return empresaDestinoId; }
    public void setEmpresaDestinoId(Integer empresaDestinoId) { this.empresaDestinoId = empresaDestinoId; }

    public String getEmpresaDestinoNombre() { return empresaDestinoNombre; }
    public void setEmpresaDestinoNombre(String empresaDestinoNombre) { this.empresaDestinoNombre = empresaDestinoNombre; }

    public Integer getFuncionarioAutorizaId() { return funcionarioAutorizaId; }
    public void setFuncionarioAutorizaId(Integer funcionarioAutorizaId) { this.funcionarioAutorizaId = funcionarioAutorizaId; }

    public String getFuncionarioNombre() { return funcionarioNombre; }
    public void setFuncionarioNombre(String funcionarioNombre) { this.funcionarioNombre = funcionarioNombre; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public String getFechaHoraEntrada() { return fechaHoraEntrada; }
    public void setFechaHoraEntrada(String fechaHoraEntrada) { this.fechaHoraEntrada = fechaHoraEntrada; }

    public String getFechaHoraSalida() { return fechaHoraSalida; }
    public void setFechaHoraSalida(String fechaHoraSalida) { this.fechaHoraSalida = fechaHoraSalida; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public String getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(String fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    @Override
    public String toString() {
        return "Visita #" + id + " - " + personaNombre + " → " + empresaDestinoNombre + " [" + estado + "]";
    }
}
