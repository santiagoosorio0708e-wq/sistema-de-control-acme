package com.acme.sica.usuario.domain.model;

/**
 * Entidad que representa un usuario del sistema.
 * Cada usuario tiene un rol asignado que define sus permisos.
 */
public class Usuario {
    private Integer id;
    private String username;
    private String passwordHash;
    private String nombreCompleto;
    private Integer rolId;
    private String rolNombre; // Campo auxiliar para mostrar el nombre del rol
    private boolean activo;
    private String fechaCreacion;

    public Usuario() {}

    public Usuario(Integer id, String username, String passwordHash, String nombreCompleto,
                   Integer rolId, boolean activo, String fechaCreacion) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.nombreCompleto = nombreCompleto;
        this.rolId = rolId;
        this.activo = activo;
        this.fechaCreacion = fechaCreacion;
    }

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public Integer getRolId() { return rolId; }
    public void setRolId(Integer rolId) { this.rolId = rolId; }

    public String getRolNombre() { return rolNombre; }
    public void setRolNombre(String rolNombre) { this.rolNombre = rolNombre; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public String getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(String fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    @Override
    public String toString() {
        return username + " (" + nombreCompleto + ")";
    }
}
