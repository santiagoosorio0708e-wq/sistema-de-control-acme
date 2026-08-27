package com.acme.sica.persona.domain.model;

/**
 * Entidad que representa una persona (trabajador o visitante)
 * que accede al complejo Zona Acme.
 */
public class Persona {
    private Integer id;
    private String documento;
    private String tipoDocumento;
    private String nombre;
    private String apellido;
    private String tipoPersona;
    private Integer empresaId;
    private String empresaNombre; // Campo auxiliar
    private String fotoUrl;
    private String estadoAcceso;
    private String telefono;
    private String email;

    public Persona() {}

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getDocumento() { return documento; }
    public void setDocumento(String documento) { this.documento = documento; }

    public String getTipoDocumento() { return tipoDocumento; }
    public void setTipoDocumento(String tipoDocumento) { this.tipoDocumento = tipoDocumento; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getNombreCompleto() { return nombre + " " + apellido; }

    public String getTipoPersona() { return tipoPersona; }
    public void setTipoPersona(String tipoPersona) { this.tipoPersona = tipoPersona; }

    public Integer getEmpresaId() { return empresaId; }
    public void setEmpresaId(Integer empresaId) { this.empresaId = empresaId; }

    public String getEmpresaNombre() { return empresaNombre; }
    public void setEmpresaNombre(String empresaNombre) { this.empresaNombre = empresaNombre; }

    public String getFotoUrl() { return fotoUrl; }
    public void setFotoUrl(String fotoUrl) { this.fotoUrl = fotoUrl; }

    public String getEstadoAcceso() { return estadoAcceso; }
    public void setEstadoAcceso(String estadoAcceso) { this.estadoAcceso = estadoAcceso; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() {
        return nombre + " " + apellido + " (" + tipoDocumento + ": " + documento + ")";
    }
}
