package com.gesa.models;

public class Servicio {
    private int id;
    private String nombre;
    private String descripcion;
    private int frecuenciaMeses; // frecuencia_recomendada_meses
    private boolean esPremium;   // es_premium

    public Servicio() {}

    public Servicio(int id, String nombre, int frecuenciaMeses) {
        this.id = id;
        this.nombre = nombre;
        this.frecuenciaMeses = frecuenciaMeses;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public int getFrecuenciaMeses() { return frecuenciaMeses; }
    public void setFrecuenciaMeses(int frecuenciaMeses) { this.frecuenciaMeses = frecuenciaMeses; }

    public boolean isEsPremium() { return esPremium; }
    public void setEsPremium(boolean esPremium) { this.esPremium = esPremium; }

    @Override
    public String toString() {
        return nombre;
    }
}