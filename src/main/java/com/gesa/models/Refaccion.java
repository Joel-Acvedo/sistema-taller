package com.gesa.models;

public class Refaccion {
    private int id;
    private String codigoInterno; // codigo_interno en BD
    private String nombre;
    private String descripcion;
    private String compatibilidad;

    // Dineros
    private double costoProveedor; // costo_proveedor
    private int margenGanancia;    // margen_ganancia_porcentaje
    private double precioVenta;    // precio_venta_sugerido (Calculado)

    // Inventario
    private int stockActual;
    private int stockMinimo;
    private boolean eliminado;

    public Refaccion() {}

    public Refaccion(int id, String codigoInterno, String nombre, double costoProveedor, int stockActual) {
        this.id = id;
        this.codigoInterno = codigoInterno;
        this.nombre = nombre;
        this.costoProveedor = costoProveedor;
        this.stockActual = stockActual;
        this.margenGanancia = 30; // Valor por defecto si no se especifica
        calcularPrecioVenta(); // Calculamos el precio público automático
    }

    // Método para calcular precio venta (Igual que en tu BD)
    public void calcularPrecioVenta() {
        this.precioVenta = this.costoProveedor * (1 + (this.margenGanancia / 100.0));
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCodigoInterno() { return codigoInterno; }
    public void setCodigoInterno(String codigoInterno) { this.codigoInterno = codigoInterno; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getCompatibilidad() { return compatibilidad; }
    public void setCompatibilidad(String compatibilidad) { this.compatibilidad = compatibilidad; }

    public double getCostoProveedor() { return costoProveedor; }
    public void setCostoProveedor(double costoProveedor) {
        this.costoProveedor = costoProveedor;
        calcularPrecioVenta(); // Recalcular si cambia el costo
    }

    public int getMargenGanancia() { return margenGanancia; }
    public void setMargenGanancia(int margenGanancia) {
        this.margenGanancia = margenGanancia;
        calcularPrecioVenta(); // Recalcular si cambia el margen
    }

    public double getPrecioVenta() { return precioVenta; }
    public void setPrecioVenta(double precioVenta) { this.precioVenta = precioVenta; }

    public int getStockActual() { return stockActual; }
    public void setStockActual(int stockActual) { this.stockActual = stockActual; }

    public int getStockMinimo() { return stockMinimo; }
    public void setStockMinimo(int stockMinimo) { this.stockMinimo = stockMinimo; }

    public boolean isEliminado() { return eliminado; }
    public void setEliminado(boolean eliminado) { this.eliminado = eliminado;}

    @Override
    public String toString() {
        return nombre + " (" + codigoInterno + ")";
    }
}