package com.gesa.models;

public class Cliente {
    
    private int id;
    private String nombre;
    private String telefono;
    private String email;
    private String rfc;

    // Constructor vacío (necesario)
    public Cliente() {}

    // Constructor con datos
    public Cliente(int id, String nombre, String telefono, String email, String rfc) {
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
        this.rfc = rfc;
    }

    // Getters y Setters (Para que la tabla pueda leer los datos)
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRfc() { return rfc; }
    public void setRfc(String rfc) { this.rfc = rfc; }
}