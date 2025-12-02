package com.gesa.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class CotizacionController {

    @FXML private ComboBox<?> cmbCliente;
    @FXML private ComboBox<?> cmbVehiculo;
    @FXML private DatePicker dtFecha;

    @FXML private TextField txtDescripcion;
    @FXML private TextField txtCantidad;
    @FXML private TextField txtPrecio;

    @FXML private TableView<?> tablaDetalles;
    @FXML private TableColumn<?, ?> colCantidad;
    @FXML private TableColumn<?, ?> colDescripcion;
    @FXML private TableColumn<?, ?> colPrecio;
    @FXML private TableColumn<?, ?> colImporte;

    @FXML private Label lblSubtotal;
    @FXML private Label lblIVA;
    @FXML private Label lblTotal;

    @FXML
    public void initialize() {
        // Aquí cargaremos los clientes al iniciar
    }

    @FXML
    void agregarItem(ActionEvent event) {
        System.out.println("Agregando producto a la tabla...");
    }
}