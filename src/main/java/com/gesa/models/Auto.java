package com.gesa.models;

import java.time.LocalDate;

public class Auto {
    private int id;
    private int clienteId; // Relación con el dueño

    // Datos del Carro
    private String marca;
    private String modelo;
    private int anio; // "ano" en la Base de Datos
    private String color;
    private String placas;
    private int kilometraje; // Lo manejamos como String para flexibilidad ("120,000 km")
    private String vin;

    // Datos Técnicos (Obligatorios en tu BD V6.0)
    private String motorCategoria; // Ej: "4 CIL", "V6"
    private String combustible;    // Ej: "Gasolina"

    // El Secretario Automático (Fechas)
    private LocalDate fechaUltimoServicio;
    private LocalDate fechaProximoServicio;

    // Control
    private boolean eliminado;

    public Auto() {}

    public Auto(int id, int clienteId, String marca, String modelo, int anio, String placas) {
        this.id = id;
        this.clienteId = clienteId;
        this.marca = marca;
        this.modelo = modelo;
        this.anio = anio;
        this.placas = placas;
        this.eliminado = false;
    }

    // --- GETTERS Y SETTERS ---

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

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public String getPlacas() { return placas; }
    public void setPlacas(String placas) { this.placas = placas; }

    public int getKilometraje() { return kilometraje; }
    public void setKilometraje(int kilometraje) { this.kilometraje = kilometraje; }

    public String getVin() { return vin; }
    public void setVin(String vin) { this.vin = vin; }

    public String getMotorCategoria() { return motorCategoria; }
    public void setMotorCategoria(String motorCategoria) { this.motorCategoria = motorCategoria; }

    public String getCombustible() { return combustible; }
    public void setCombustible(String combustible) { this.combustible = combustible; }

    public LocalDate getFechaUltimoServicio() { return fechaUltimoServicio; }
    public void setFechaUltimoServicio(LocalDate fechaUltimoServicio) { this.fechaUltimoServicio = fechaUltimoServicio; }

    public LocalDate getFechaProximoServicio() { return fechaProximoServicio; }
    public void setFechaProximoServicio(LocalDate fechaProximoServicio) { this.fechaProximoServicio = fechaProximoServicio; }

    public boolean isEliminado() { return eliminado; }
    public void setEliminado(boolean eliminado) { this.eliminado = eliminado; }

    // Esto es lo que se ve en el ComboBox: "Nissan Tsuru (ABC-123)"
    @Override
    public String toString() {
        // Si es el botón de "Agregar Nuevo" (ID -1), solo mostramos el texto
        if (this.id == -1) {
            return this.marca;
        }
        // Si es un carro normal, mostramos el formato completo
        return marca + " " + modelo + " (" + placas + ")";
    }
}