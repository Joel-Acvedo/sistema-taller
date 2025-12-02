package com.gesa.models;

import java.time.LocalDate;

public class Vehiculo {
    private int id;
    private int clienteId;
    private String marca;
    private String modelo;
    private int anio; // "ano" en la BD, "anio" en Java

    // Campos Nuevos del Script V6.0
    private String motorCategoria; // Ej: "4 CIL", "V6"
    private String combustible;    // Ej: "Gasolina", "Diesel"

    private String placas;
    private String vin;

    // El Secretario Automático (Fechas)
    private LocalDate fechaUltimoServicio;
    private LocalDate fechaProximoServicio;

    private boolean eliminado;

    public Vehiculo() {}

    public Vehiculo(int id, int clienteId, String marca, String modelo, int anio, String placas) {
        this.id = id;
        this.clienteId = clienteId;
        this.marca = marca;
        this.modelo = modelo;
        this.anio = anio;
        this.placas = placas;
        this.eliminado = false;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getClienteId() { return clienteId; }
    public void setClienteId(int clienteId) { this.clienteId = clienteId; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public int getAnio() { return anio; }
    public void setAnio(int anio) { this.anio = anio; }

    public String getMotorCategoria() { return motorCategoria; }
    public void setMotorCategoria(String motorCategoria) { this.motorCategoria = motorCategoria; }

    public String getCombustible() { return combustible; }
    public void setCombustible(String combustible) { this.combustible = combustible; }

    public String getPlacas() { return placas; }
    public void setPlacas(String placas) { this.placas = placas; }

    public String getVin() { return vin; }
    public void setVin(String vin) { this.vin = vin; }

    public LocalDate getFechaUltimoServicio() { return fechaUltimoServicio; }
    public void setFechaUltimoServicio(LocalDate fechaUltimoServicio) { this.fechaUltimoServicio = fechaUltimoServicio; }

    public LocalDate getFechaProximoServicio() { return fechaProximoServicio; }
    public void setFechaProximoServicio(LocalDate fechaProximoServicio) { this.fechaProximoServicio = fechaProximoServicio; }

    public boolean isEliminado() { return eliminado; }
    public void setEliminado(boolean eliminado) { this.eliminado = eliminado; }

    @Override
    public String toString() {
        return marca + " " + modelo + " (" + placas + ")";
    }
}