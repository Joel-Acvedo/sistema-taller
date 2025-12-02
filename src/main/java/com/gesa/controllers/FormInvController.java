package com.gesa.controllers;

import com.gesa.dao.RefaccionDAO;
import com.gesa.models.Refaccion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class FormInvController {

    // 1. Elementos Visuales (Fx:id)
    @FXML private Label lblTitulo;
    @FXML private TextField txtNombre;
    @FXML private TextField txtCodigo;
    @FXML private TextField txtCosto;
    @FXML private TextField txtStock;
    @FXML private TextArea txtDescripcion;

    // 2. Herramientas
    private Refaccion refaccionEnEdicion;
    private RefaccionDAO dao = new RefaccionDAO();

    // 3. Recibir Datos (Setear)
    public void setRefaccion(Refaccion r) {
        this.refaccionEnEdicion = r;

        if (r != null) {
            // --- MODO EDICIÓN ---
            lblTitulo.setText("Editar Pieza");
            setTextoSeguro(txtNombre, r.getNombre());
            setTextoSeguro(txtCodigo, r.getCodigoInterno());
            setTextoSeguro(txtCosto, String.valueOf(r.getCostoProveedor()));
            setTextoSeguro(txtStock, String.valueOf(r.getStockActual()));
            if (txtDescripcion != null) txtDescripcion.setText(r.getDescripcion());
        } else {
            // --- MODO NUEVO ---
            lblTitulo.setText("Agregar Pieza");
            limpiar();
        }
    }

    private void setTextoSeguro(TextField campo, String valor) {
        if (campo != null) campo.setText(valor != null ? valor : "");
    }

    // 4. BOTÓN GUARDAR (Aquí estaba el detalle)
    @FXML
    void actionGuardar(ActionEvent event) {
        try {
            // A. Validaciones
            if (txtNombre.getText().isEmpty() || txtCosto.getText().isEmpty()) {
                mostrarAlerta("Error", "Nombre y Costo son obligatorios, master.");
                return;
            }

            // B. Crear Objeto con los datos nuevos
            Refaccion refaccionGuardar = new Refaccion();
            refaccionGuardar.setNombre(txtNombre.getText());
            refaccionGuardar.setCodigoInterno(txtCodigo.getText());
            refaccionGuardar.setDescripcion(txtDescripcion.getText());

            // Convertir números (Cuidado con el texto vacío)
            double costo = Double.parseDouble(txtCosto.getText());
            int stock = txtStock.getText().isEmpty() ? 0 : Integer.parseInt(txtStock.getText());

            refaccionGuardar.setCostoProveedor(costo);
            refaccionGuardar.setStockActual(stock);

            boolean exito;

            // C. Decidir si es INSERT o UPDATE
            if (refaccionEnEdicion == null) {
                // NUEVO
                exito = dao.guardar(refaccionGuardar);
            } else {
                // EDICIÓN (¡ESTO FALTABA!)
                // 1. Pasamos el ID original al objeto nuevo
                refaccionGuardar.setId(refaccionEnEdicion.getId());

                // 2. Llamamos al DAO real
                exito = dao.editar(refaccionGuardar);
            }

            // D. Resultado
            if (exito) {
                mostrarAlerta("Éxito", "Inventario actualizado correctamente.");
                cerrarVentana();
            } else {
                mostrarAlerta("Error", "No se pudo guardar en la BD.");
            }

        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "El costo y stock deben ser números (ej. 150.50).");
        }
    }

    @FXML
    void actionCancelar(ActionEvent event) {
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) txtNombre.getScene().getWindow();
        stage.close();
    }

    private void limpiar() {
        if(txtNombre != null) txtNombre.clear();
        if(txtCodigo != null) txtCodigo.clear();
        if(txtCosto != null) txtCosto.clear();
        if(txtStock != null) txtStock.clear();
        if(txtDescripcion != null) txtDescripcion.clear();
    }

    private void mostrarAlerta(String titulo, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(titulo);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}