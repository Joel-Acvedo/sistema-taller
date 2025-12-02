package com.gesa.models;

public class Usuario {
    private int id;
    private String nombreCompleto;
    private String username;
    private String passwordHash; // En la BD es password_hash
    private String rol;          // Nuevo del script
    private boolean activo;      // Nuevo del script

    public Usuario() {}

    public Usuario(int id, String nombreCompleto, String username, String passwordHash, String rol) {
        this.id = id;
        this.nombreCompleto = nombreCompleto;
        this.username = username;
        this.passwordHash = passwordHash;
        this.rol = rol;
        this.activo = true;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}