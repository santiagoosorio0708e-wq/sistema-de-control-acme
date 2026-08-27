package com.acme.sica.empresa.domain.model;

/**
 * Entidad que representa una empresa del complejo Zona Acme.
 */
public class Empresa {
    private Integer id;
    private String nombre;
    private String nit;
    private String sector;
    private String telefonoContacto;
    private String emailContacto;
    private boolean activa;

    public Empresa() {}

    public Empresa(Integer id, String nombre, String nit, String sector,
                   String telefonoContacto, String emailContacto, boolean activa) {
        this.id = id;
        this.nombre = nombre;
        this.nit = nit;
        this.sector = sector;
        this.telefonoContacto = telefonoContacto;
        this.emailContacto = emailContacto;
        this.activa = activa;
    }

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getNit() { return nit; }
    public void setNit(String nit) { this.nit = nit; }

    public String getSector() { return sector; }
    public void setSector(String sector) { this.sector = sector; }

    public String getTelefonoContacto() { return telefonoContacto; }
    public void setTelefonoContacto(String telefonoContacto) { this.telefonoContacto = telefonoContacto; }

    public String getEmailContacto() { return emailContacto; }
    public void setEmailContacto(String emailContacto) { this.emailContacto = emailContacto; }

    public boolean isActiva() { return activa; }
    public void setActiva(boolean activa) { this.activa = activa; }

    @Override
    public String toString() {
        return nombre + " (NIT: " + nit + ")";
    }
}
