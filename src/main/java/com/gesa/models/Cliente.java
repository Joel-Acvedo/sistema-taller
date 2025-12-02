package com.gesa.models;

public class Cliente {
    private int id;
    private String nombre;
    private String apellidos;
    private String telefono;
    private String email;
    private String rfc;

    // Campos Fiscales (Coinciden con tu DB)
    private String razonSocialFiscal;
    private String regimenFiscal;
    private String cpFiscal;
    private String usoCfdi;
    private String emailFacturacion;

    private boolean eliminado; // Tu soft delete

    public Cliente() {}

    // Constructor Básico
    public Cliente(int id, String nombre, String apellidos, String telefono, String email, String rfc) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.email = email;
        this.rfc = rfc;
        this.eliminado = false;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRfc() { return rfc; }
    public void setRfc(String rfc) { this.rfc = rfc; }

    // Fiscales
    public String getRazonSocialFiscal() { return razonSocialFiscal; }
    public void setRazonSocialFiscal(String razonSocialFiscal) { this.razonSocialFiscal = razonSocialFiscal; }

    public String getRegimenFiscal() { return regimenFiscal; }
    public void setRegimenFiscal(String regimenFiscal) { this.regimenFiscal = regimenFiscal; }

    public String getCpFiscal() { return cpFiscal; }
    public void setCpFiscal(String cpFiscal) { this.cpFiscal = cpFiscal; }

    public String getUsoCfdi() { return usoCfdi; }
    public void setUsoCfdi(String usoCfdi) { this.usoCfdi = usoCfdi; }

    public String getEmailFacturacion() { return emailFacturacion; }
    public void setEmailFacturacion(String emailFacturacion) { this.emailFacturacion = emailFacturacion; }

    public boolean isEliminado() { return eliminado; }
    public void setEliminado(boolean eliminado) { this.eliminado = eliminado; }
    private String textoMembresia;

    public void setTextoMembresia(String texto) {
        this.textoMembresia = texto;
    }

    public String getTextoMembresia() {
        if (textoMembresia == null || textoMembresia.isEmpty()) {
            return "Sin Membresía"; // <--- AQUÍ PONEMOS EL TEXTO POR DEFECTO
        }
        return textoMembresia;
    }

    @Override
    public String toString() {
        return nombre + " " + apellidos;
    }
}