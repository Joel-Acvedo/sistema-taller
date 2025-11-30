package com.gesa.models;
//Este no va en dao ni en db , va en models porque es un modelo de datos
//modelo de datos es una clase que representa una tabla de la base de datos
//Se tiene que trabajr esto despues , por ahora nomas dejare esta asi
public class Usuario {
    
    // Atributos (Igualitos a tu tabla de PostgreSQL)
    private int id;
    private String nombreCompleto;
    private String username;
    private String passwordHash;
    private String rol;
    private boolean activo;

    // Constructor Vacio
    public Usuario() {}

    // Constructor Completo
    public Usuario(int id, String nombreCompleto, String username, String passwordHash, String rol, boolean activo) {
        this.id = id;
        this.nombreCompleto = nombreCompleto;
        this.username = username;
        this.passwordHash = passwordHash;
        this.rol = rol;
        this.activo = activo;
    }

    // Getters y Setters (Para poder leer y escribir los datos)
    // Tip: En VS Code puedes generarlos click derecho -> Source Action -> Generate Getters and Setters
    
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
    
    @Override
    public String toString() {
        return nombreCompleto + " (" + rol + ")";
    }
    
}
