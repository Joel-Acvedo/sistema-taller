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
        // Aquí validaremos que haya seleccionado a alguien de la tabla primero
        // Object seleccionado = tablaClientes.getSelectionModel().getSelectedItem();
        // if (seleccionado != null) {
        //      abrirFormulario("Editar Cliente", seleccionado);
        // } else {
        //      mostrarAlerta("Selecciona un cliente primero");
        // }

        System.out.println("Abriendo editor...");
    }

    // --- ACCIÓN: ELIMINAR ---
    @FXML
    void eliminarCliente(ActionEvent event) {
        System.out.println("Eliminando...");
    }

    // --- MÉTODO MAESTRO PARA ABRIR VENTANAS (POPUP) ---
    private void abrirFormulario(String titulo, Object clienteAEditar) {
        try {
            // 1. Cargar el FXML del Formulario (Asegúrate de crearlo)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/gesa/views/FormularioCliente.fxml"));
            Parent root = loader.load();

            // 2. Crear el Escenario (Stage)
            Stage stage = new Stage();
            stage.setTitle(titulo);
            stage.setScene(new Scene(root));

            // 🔒 BLOQUEO: Esto hace que no puedan tocar la ventana de atrás
            stage.initModality(Modality.APPLICATION_MODAL);

            // 🔒 TAMAÑO FIJO: Aquí respondes tu duda de "que no se modifique el tamaño"
            stage.setResizable(false);

            // 3. Mostrar
            stage.showAndWait(); // Wait significa: "Espera aquí hasta que cierren la ventana"

            // Cuando se cierre la ventana, recargamos la tabla para ver los cambios
            System.out.println("Formulario cerrado, recargando tabla...");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}