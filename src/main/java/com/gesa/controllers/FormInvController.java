package com.gesa.controllers;

import com.gesa.dao.RefaccionDAO;
import com.gesa.models.Refaccion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class FormInvController {

    @FXML private Label lblTitulo;
    @FXML private TextField txtNombre;
    @FXML private TextField txtCodigo;
    @FXML private TextField txtCosto;
    @FXML private TextField txtStock;
    @FXML private TextArea txtDescripcion;

    private Refaccion refaccionEnEdicion;
    private RefaccionDAO dao = new RefaccionDAO();

    // Recibir datos
    public void setRefaccion(Refaccion r) {
        this.refaccionEnEdicion = r;

        if (r != null) {
            // EDICIÓN
            lblTitulo.setText("Editar Pieza");
            txtNombre.setText(r.getNombre());
            txtCodigo.setText(r.getCodigoInterno());
            txtCosto.setText(String.valueOf(r.getCostoProveedor()));
            txtStock.setText(String.valueOf(r.getStockActual()));
            txtDescripcion.setText(r.getDescripcion());
        } else {
            // NUEVO
            lblTitulo.setText("Agregar Pieza");
            limpiar();
        }
    }

    @FXML
    void actionGuardar(ActionEvent event) {
        try {
            // Validaciones
            if (txtNombre.getText().isEmpty() || txtCosto.getText().isEmpty()) {
                mostrarAlerta("Error", "Nombre y Costo son obligatorios");
                return;
            }

            // Crear Objeto
            Refaccion nuevaRef = new Refaccion();
            nuevaRef.setNombre(txtNombre.getText());
            nuevaRef.setCodigoInterno(txtCodigo.getText());
            nuevaRef.setDescripcion(txtDescripcion.getText());

            // Convertir textos a números
            double costo = Double.parseDouble(txtCosto.getText());
            int stock = txtStock.getText().isEmpty() ? 0 : Integer.parseInt(txtStock.getText());

            nuevaRef.setCostoProveedor(costo);
            nuevaRef.setStockActual(stock);
            // El precio venta se calcula solo en el modelo

            boolean exito;
            if (refaccionEnEdicion == null) {
                exito = dao.guardar(nuevaRef);
            } else {
                nuevaRef.setId(refaccionEnEdicion.getId());
                // exito = dao.editar(nuevaRef); // Tienes que crear este método en tu DAO
                exito = true; // Temporal
            }

            if (exito) cerrarVentana();

        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "El costo y stock deben ser números válidos");
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
        txtNombre.clear();
        txtCodigo.clear();
        txtCosto.clear();
        txtStock.clear();
        txtDescripcion.clear();
    }

    private void mostrarAlerta(String titulo, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(titulo);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}