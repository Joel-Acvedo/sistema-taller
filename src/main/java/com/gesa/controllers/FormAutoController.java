package com.gesa.controllers;

import com.gesa.dao.AutoDAO;     // <--- YA ACTUALIZADO
import com.gesa.dao.ClienteDAO;
import com.gesa.models.Auto;     // <--- YA ACTUALIZADO
import com.gesa.models.Cliente;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class FormAutoController {

    // --- ELEMENTOS VISUALES ---
    @FXML private Label lblTitulo;
    @FXML private ComboBox<Cliente> cmbCliente;

    @FXML private TextField txtMarca;
    @FXML private TextField txtModelo;
    @FXML private TextField txtAnio;
    @FXML private TextField txtColor;
    @FXML private TextField txtPlacas;
    @FXML private TextField txtVin;
    @FXML private TextField txtKilometraje;
    @FXML private TextField txtMotor;

    // --- HERRAMIENTAS ---
    private AutoDAO autoDAO = new AutoDAO(); // <--- USA AutoDAO
    private ClienteDAO clienteDAO = new ClienteDAO();
    private ObservableList<Cliente> listaClientes = FXCollections.observableArrayList();

    // --- ARRANQUE ---
    @FXML
    public void initialize() {
        cargarClientes();
    }

    private void cargarClientes() {
        listaClientes.clear();
        listaClientes.addAll(clienteDAO.listar());
        cmbCliente.setItems(listaClientes);

        // Converter para ver nombre bonito
        cmbCliente.setConverter(new StringConverter<Cliente>() {
            @Override
            public String toString(Cliente c) { return (c == null) ? "" : c.toString(); }
            @Override
            public Cliente fromString(String string) { return null; }
        });
    }

    // Recibir cliente desde Cotización
    public void setClientePreseleccionado(Cliente c) {
        if (c != null) {
            for (Cliente item : cmbCliente.getItems()) {
                if (item.getId() == c.getId()) {
                    cmbCliente.getSelectionModel().select(item);
                    cmbCliente.setDisable(true);
                    break;
                }
            }
        }
    }

    // --- BOTÓN GUARDAR ---
    @FXML
    void actionGuardar(ActionEvent event) {
        // 1. Validaciones
        if (cmbCliente.getValue() == null) {
            mostrarAlerta("Error", "Debes seleccionar un dueño (Cliente).");
            return;
        }
        if (txtMarca.getText().isEmpty() || txtModelo.getText().isEmpty() || txtPlacas.getText().isEmpty()) {
            mostrarAlerta("Error", "Marca, Modelo y Placas son obligatorios.");
            return;
        }

        try {
            // 2. Crear Objeto Auto
            Auto nuevoAuto = new Auto(); // <--- USA Auto
            nuevoAuto.setClienteId(cmbCliente.getValue().getId());
            nuevoAuto.setMarca(txtMarca.getText());
            nuevoAuto.setModelo(txtModelo.getText());
            nuevoAuto.setPlacas(txtPlacas.getText());
            nuevoAuto.setColor(txtColor.getText());
            nuevoAuto.setVin(txtVin.getText());

            // Validar Año
            int anio = txtAnio.getText().isEmpty() ? 0 : Integer.parseInt(txtAnio.getText());
            nuevoAuto.setAnio(anio);

            // --- CORRECCIÓN KILOMETRAJE (Texto -> int) ---
            String textoKilo = txtKilometraje.getText();
            int kilo = 0;
            if (textoKilo != null && !textoKilo.isEmpty()) {
                // Quitamos comas "120,000" -> "120000"
                String limpio = textoKilo.replace(",", "").replace(".", "").trim();
                kilo = Integer.parseInt(limpio);
            }
            nuevoAuto.setKilometraje(kilo); // <--- Guarda int limpio
            // ---------------------------------------------

            // Campos obligatorios BD
            String motor = txtMotor.getText().isEmpty() ? "4 Cilindros" : txtMotor.getText();
            nuevoAuto.setMotorCategoria(motor);
            nuevoAuto.setCombustible("Gasolina");

            // 3. Guardar usando AutoDAO
            boolean exito = autoDAO.guardar(nuevoAuto);

            if (exito) {
                mostrarAlerta("Éxito", "Auto registrado correctamente.");
                cerrarVentana();
            } else {
                mostrarAlerta("Error", "No se pudo guardar el auto.");
            }

        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "El Año y Kilometraje deben ser números válidos.");
        }
    }

    // --- BOTÓN CANCELAR ---
    @FXML
    void actionCancelar(ActionEvent event) {
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) txtMarca.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String titulo, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(titulo);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}