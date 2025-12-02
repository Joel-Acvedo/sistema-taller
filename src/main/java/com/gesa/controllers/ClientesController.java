package com.gesa.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;

public class ClientesController {

    // 1. Lo dejamos genérico <?> para que acepte lo que sea
    @FXML private TableView<?> tablaClientes;
    @FXML private TextField txtBuscar;

    // --- ACCIÓN: NUEVO CLIENTE ---
    @FXML
    void nuevoCliente(ActionEvent event) {
        abrirFormulario("Nuevo Cliente", null);
    }

    // --- ACCIÓN: EDITAR CLIENTE ---
    @FXML
    void editarCliente(ActionEvent event) {
        // 2. Usamos Object (Objeto genérico)
        Object seleccionado = tablaClientes.getSelectionModel().getSelectedItem();

        if (seleccionado != null) {
            System.out.println("Abriendo editor (Genérico)...");
            abrirFormulario("Editar Cliente", seleccionado);
        } else {
            System.out.println("❌ Selecciona algo primero");
        }
    }

    @FXML
    void eliminarCliente(ActionEvent event) {
        System.out.println("Eliminando...");
    }

    // --- MÉTODO MAESTRO (MODIFICADO PARA USAR OBJECT) ---
    private void abrirFormulario(String titulo, Object clienteAEditar) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/gesa/views/FormularioCLiente.fxml"));
            Parent root = loader.load();

            // 👇👇 AQUÍ ESTÁ EL CAMBIO 👇👇
            // Pedimos el controlador
            FormClienController controller = loader.getController();

            // ⚠️ COMENTÉ ESTO TEMPORALMENTE:
            // Como no queremos usar la clase Cliente ahorita, desactivamos el pase de datos.
            // La ventana se abrirá, pero los campos saldrán vacíos siempre.

            // if (clienteAEditar != null) {
            //      controller.setCliente( (Cliente) clienteAEditar );
            // }

            Stage stage = new Stage();
            stage.setTitle(titulo);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.showAndWait();

            System.out.println("Formulario cerrado.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}