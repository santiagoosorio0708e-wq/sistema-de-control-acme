package com.acme.sica.usuario.domain.model;

/**
 * Entidad que representa un permiso granular del sistema (RBAC).
 * Cada permiso corresponde a una acción específica del sistema.
 */
public class Permiso {
    private Integer id;
    private String clave;
    private String descripcion;
    private String modulo;

    public Permiso() {}

    public Permiso(Integer id, String clave, String descripcion, String modulo) {
        this.id = id;
        this.clave = clave;
        this.descripcion = descripcion;
        this.modulo = modulo;
    }

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getClave() { return clave; }
    public void setClave(String clave) { this.clave = clave; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getModulo() { return modulo; }
    public void setModulo(String modulo) { this.modulo = modulo; }

    @Override
    public String toString() {
        return clave + " (" + modulo + ")";
    }
}
