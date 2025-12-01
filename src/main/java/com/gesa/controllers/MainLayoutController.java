package com.gesa.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import java.io.IOException;

public class MainLayoutController {

    // Se conecta con el fx:id="mainPane" del FXML
    @FXML
    private BorderPane mainPane;

    // --- ACCIONES DE LOS BOTONES ---

    @FXML
    void mostrarInicio(ActionEvent event) {
        System.out.println("🏠 Yendo a Inicio");
        cargarVista("InicioView");
    }

    @FXML
    void mostrarClientes(ActionEvent event) {
        System.out.println("👥 Yendo a Clientes");
        cargarVista("ClientesView");
    }

    @FXML
    void mostrarInventario(ActionEvent event) {
        System.out.println("📦 Yendo a Inventario");
        cargarVista("InventarioView");
    }

    @FXML
    void mostrarCotizacion(ActionEvent event) {
        System.out.println("📝 Yendo a Cotización");
        cargarVista("CotizacionView"); // Asegúrate de crear este archivo FXML
    }

    // --- MÉTODO MAESTRO PARA CAMBIAR PANTALLAS ---
    private void cargarVista(String nombreArchivo) {
        try {
            // Carga el archivo desde la carpeta views
            // OJO: La ruta es absoluta (/com/gesa/views/...)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/gesa/views/" + nombreArchivo + ".fxml"));
            Parent vista = loader.load();

            // Pone la nueva vista en el CENTRO
            mainPane.setCenter(vista);

        } catch (IOException e) {
            System.err.println("❌ Error al cargar la vista: " + nombreArchivo);
            e.printStackTrace();
        }
    }
}