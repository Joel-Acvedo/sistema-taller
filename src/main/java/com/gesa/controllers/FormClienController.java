package com.gesa.controllers;

import com.gesa.dao.ClienteDAO;
import com.gesa.models.Cliente;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class FormClienController {

    // 1. Elementos Visuales
    @FXML private Label lblTitulo;

    @FXML private TextField txtNombre;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtEmail;

    // Campos Fiscales
    @FXML private TextField txtRFC;
    @FXML private TextField txtRazonSocial;
    @FXML private TextField txtCP;
    @FXML private TextField txtUsoCFDI;
    @FXML private TextField txtEmailFacturacion;

    // 👇👇 NUEVO CAMPO AGREGADO 👇👇
    @FXML private TextField txtRegimen;

    // 2. Herramientas Lógicas
    private Cliente clienteEnEdicion;
    private ClienteDAO dao = new ClienteDAO();

    // 3. RECIBIR DATOS (Al abrir la ventana)
    public void setCliente(Cliente c) {
        this.clienteEnEdicion = c;

        if (c != null) {
            // --- MODO EDICIÓN ---
            if(lblTitulo != null) lblTitulo.setText("Editar Cliente");

            setTextoSeguro(txtNombre, c.getNombre());
            setTextoSeguro(txtApellidos, c.getApellidos());
            setTextoSeguro(txtTelefono, c.getTelefono());
            setTextoSeguro(txtEmail, c.getEmail());

            // Fiscales
            setTextoSeguro(txtRFC, c.getRfc());
            setTextoSeguro(txtRazonSocial, c.getRazonSocialFiscal());
            setTextoSeguro(txtCP, c.getCpFiscal());
            setTextoSeguro(txtUsoCFDI, c.getUsoCfdi());
            setTextoSeguro(txtEmailFacturacion, c.getEmailFacturacion());

            // 👇 NUEVO: Llenar Régimen
            setTextoSeguro(txtRegimen, c.getRegimenFiscal());

        } else {
            // --- MODO NUEVO ---
            if(lblTitulo != null) lblTitulo.setText("Nuevo Cliente");
            limpiarCampos();
        }
    }

    // Método auxiliar para evitar NullPointerException
    private void setTextoSeguro(TextField campo, String valor) {
        if (campo != null) {
            campo.setText(valor != null ? valor : "");
        }
    }

    // 4. BOTÓN GUARDAR
    @FXML
    void actionGuardar(ActionEvent event) {
        // A. Validaciones
        if (txtNombre.getText().isEmpty() || txtApellidos.getText().isEmpty()) {
            mostrarAlerta("Error", "El nombre y apellidos son obligatorios.");
            return;
        }

        // B. Crear objeto
        Cliente clienteGuardar = new Cliente();

        clienteGuardar.setNombre(txtNombre.getText());
        clienteGuardar.setApellidos(txtApellidos.getText());
        clienteGuardar.setTelefono(txtTelefono.getText());
        clienteGuardar.setEmail(txtEmail.getText());

        // Fiscales (Validando que existan los TextFields en el FXML)
        if(txtRFC != null) clienteGuardar.setRfc(txtRFC.getText());
        if(txtRazonSocial != null) clienteGuardar.setRazonSocialFiscal(txtRazonSocial.getText());
        if(txtCP != null) clienteGuardar.setCpFiscal(txtCP.getText());
        if(txtUsoCFDI != null) clienteGuardar.setUsoCfdi(txtUsoCFDI.getText());
        if(txtEmailFacturacion != null) clienteGuardar.setEmailFacturacion(txtEmailFacturacion.getText());

        // 👇 NUEVO: Guardar Régimen
        if(txtRegimen != null) clienteGuardar.setRegimenFiscal(txtRegimen.getText());

        boolean exito;

        // C. Guardar o Editar
        if (clienteEnEdicion == null) {
            exito = dao.guardar(clienteGuardar);
        } else {
            clienteGuardar.setId(clienteEnEdicion.getId());
            exito = dao.editar(clienteGuardar);
        }

        // D. Resultado
        if (exito) {
            mostrarAlerta("Éxito", "Cliente guardado correctamente.");
            cerrarVentana();
        } else {
            mostrarAlerta("Error", "No se pudo guardar en la base de datos.");
        }
    }

    // 5. BOTÓN CANCELAR
    @FXML
    void actionCancelar(ActionEvent event) {
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) txtNombre.getScene().getWindow();
        stage.close();
    }

    private void limpiarCampos() {
        if(txtNombre != null) txtNombre.clear();
        if(txtApellidos != null) txtApellidos.clear();
        if(txtTelefono != null) txtTelefono.clear();
        if(txtEmail != null) txtEmail.clear();
        if(txtRFC != null) txtRFC.clear();
        if(txtRazonSocial != null) txtRazonSocial.clear();
        if(txtCP != null) txtCP.clear();
        if(txtUsoCFDI != null) txtUsoCFDI.clear();
        if(txtEmailFacturacion != null) txtEmailFacturacion.clear();
        // 👇 NUEVO: Limpiar Régimen
        if(txtRegimen != null) txtRegimen.clear();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}