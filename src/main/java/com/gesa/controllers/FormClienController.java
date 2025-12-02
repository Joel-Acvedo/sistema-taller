package com.gesa.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class FormClienController {

    // 1. Elementos visuales
    @FXML private Label lblTitulo;
    @FXML private TextField txtNombre;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtEmail;
    // Agrega aquí los demás IDs si te faltan en el FXML (txtRFC, etc.)

    // 2. Variable GENÉRICA (Cambiamos Cliente por Object)
    private Object clienteEnEdicion;

    // 3. El Método Lógico (Adaptado para no usar Cliente por ahora)
    public void setCliente(Object c) {
        this.clienteEnEdicion = c; // Guardamos la referencia genérica

        if (c != null) {
            // --- MODO EDICIÓN ---
            if(lblTitulo != null) lblTitulo.setText("Editar Cliente");

            // ⚠️ COMENTADO TEMPORALMENTE:
            // Como 'c' ahora es un Object genérico, Java no sabe que tiene métodos getNombre().
            // Cuando volvamos a conectar el modelo, descomentamos esto.

            // txtNombre.setText(c.getNombre());
            // txtTelefono.setText(c.getTelefono());

            System.out.println("Simulando carga de datos para edición...");

        } else {
            // --- MODO NUEVO ---
            if(lblTitulo != null) lblTitulo.setText("Nuevo Cliente");

            // Limpiamos las cajitas (Validamos que no sean null para que no truene si te falta algún ID)
            if(txtNombre != null) txtNombre.clear();
            if(txtApellidos != null) txtApellidos.clear();
            if(txtTelefono != null) txtTelefono.clear();
            if(txtEmail != null) txtEmail.clear();
        }
    }
}