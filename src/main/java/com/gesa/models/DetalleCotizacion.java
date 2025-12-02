package com.gesa.models;

public class DetalleCotizacion {
    private int id;
    private int cotizacionId;
    private String descripcion; // Guardamos el texto por si borran el producto después
    private int cantidad;
    private double precioUnitario;
    private double importe; // (cantidad * precio)

    public DetalleCotizacion() {}

    public DetalleCotizacion(String descripcion, int cantidad, double precioUnitario) {
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.importe = cantidad * precioUnitario;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getCotizacionId() { return cotizacionId; }
    public void setCotizacionId(int cotizacionId) { this.cotizacionId = cotizacionId; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
        this.importe = this.cantidad * this.precioUnitario; // Recalcular solo
    }
    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
        this.importe = this.cantidad * this.precioUnitario; // Recalcular solo
    }
    public double getImporte() { return importe; }
}