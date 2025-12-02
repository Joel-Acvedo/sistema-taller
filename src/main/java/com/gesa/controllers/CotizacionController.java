package com.gesa.controllers;

import com.gesa.dao.ClienteDAO;
import com.gesa.dao.AutoDAO;
import com.gesa.models.Cliente;
import com.gesa.models.DetalleCotizacion;
import com.gesa.models.Auto;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.time.LocalDate;

public class CotizacionController {

    // --- ELEMENTOS VISUALES ---
    @FXML private ComboBox<Cliente> cmbCliente;
    @FXML private ComboBox<Auto> cmbVehiculo;
    @FXML private DatePicker dtFecha;

    @FXML private TextField txtDescripcion;
    @FXML private TextField txtCantidad;
    @FXML private TextField txtPrecio;

    @FXML private TableView<DetalleCotizacion> tablaDetalles;
    @FXML private TableColumn<DetalleCotizacion, Integer> colCantidad;
    @FXML private TableColumn<DetalleCotizacion, String> colDescripcion;
    @FXML private TableColumn<DetalleCotizacion, Double> colPrecio;
    @FXML private TableColumn<DetalleCotizacion, Double> colImporte;
    @FXML private TableColumn<DetalleCotizacion, Void> colAccion;

    @FXML private Label lblSubtotal;
    @FXML private Label lblIVA;
    @FXML private Label lblTotal;

    // --- HERRAMIENTAS ---
    private ClienteDAO clienteDAO = new ClienteDAO();
    private AutoDAO autoDAO = new AutoDAO();

    // Listas Base
    private ObservableList<Cliente> listaClientes = FXCollections.observableArrayList();
    private ObservableList<Auto> listaAutos = FXCollections.observableArrayList();
    private ObservableList<DetalleCotizacion> listaDetalles = FXCollections.observableArrayList();

    // Filtro para el buscador
    private FilteredList<Cliente> clientesFiltrados;

    // Ítems Especiales
    private Cliente OPCION_NUEVO_CLIENTE = new Cliente(-1, "➕ AGREGAR NUEVO CLIENTE", "", "", "", "");
    private Auto OPCION_NUEVO_AUTO = new Auto(-1, 0, "➕ AGREGAR NUEVO AUTO", "", 0, "");

    // --- ARRANQUE ---
    @FXML
    public void initialize() {
        dtFecha.setValue(LocalDate.now());

        configurarTabla();
        inicializarCombos(); // Configuramos los combos UNA sola vez
        cargarClientes();    // Llenamos datos

        // Listener Cliente
        cmbCliente.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                if (newVal.getId() == -1) {
                    // 👇 LA CURA PARA LA LISTA FANTASMA 👇
                    cmbCliente.hide();

                    Platform.runLater(() -> {
                        cmbCliente.getSelectionModel().clearSelection();
                        abrirFormularioCliente();
                    });
                } else {
                    cargarVehiculos(newVal.getId());
                }
            }
        });

        // Listener Vehículo
        cmbVehiculo.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && newVal.getId() == -1) {
                cmbVehiculo.hide(); // También escondemos este por si acaso
                Platform.runLater(() -> {
                    cmbVehiculo.getSelectionModel().clearSelection();
                    abrirFormularioAuto();
                });
            }
        });
    }

    private void inicializarCombos() {
        // --- CONFIGURACIÓN DEL BUSCADOR DE CLIENTES ---
        cmbCliente.setEditable(true);

        clientesFiltrados = new FilteredList<>(listaClientes, p -> true);
        cmbCliente.setItems(clientesFiltrados);

        cmbCliente.setConverter(new StringConverter<Cliente>() {
            @Override
            public String toString(Cliente c) { return (c == null) ? "" : c.toString(); }
            @Override
            public Cliente fromString(String string) {
                return cmbCliente.getItems().stream().filter(c -> c.toString().equals(string)).findFirst().orElse(null);
            }
        });

        // Lógica de filtrado al escribir
        cmbCliente.getEditor().textProperty().addListener((obs, oldVal, newVal) -> {
            Platform.runLater(() -> {
                // 👇 BLINDAJE EXTRA: Si ya seleccionamos uno, no filtramos nada
                if (cmbCliente.getSelectionModel().getSelectedItem() != null) {
                    return;
                }

                // Aplicar filtro
                clientesFiltrados.setPredicate(cliente -> {
                    if (newVal == null || newVal.isEmpty()) return true;
                    String lower = newVal.toLowerCase();

                    // Siempre mostrar la opción de "AGREGAR NUEVO"
                    if (cliente.getId() == -1) return true;

                    return cliente.toString().toLowerCase().contains(lower);
                });

                // Mostrar lista si hay resultados
                if (!clientesFiltrados.isEmpty() && !cmbCliente.isShowing()) {
                    cmbCliente.show();
                }
            });
        });
    }

    // --- LÓGICA DE DATOS ---

    private void cargarClientes() {
        listaClientes.clear();
        listaClientes.addAll(clienteDAO.listar());
        listaClientes.add(OPCION_NUEVO_CLIENTE);
    }

    private void cargarVehiculos(int clienteId) {
        listaAutos.clear();
        listaAutos.addAll(autoDAO.listarPorCliente(clienteId));
        listaAutos.add(OPCION_NUEVO_AUTO);

        cmbVehiculo.setItems(listaAutos);
        cmbVehiculo.setDisable(false);
    }

    private void configurarTabla() {
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precioUnitario"));
        colImporte.setCellValueFactory(new PropertyValueFactory<>("importe"));
        tablaDetalles.setItems(listaDetalles);
    }

    // --- ACCIONES ---

    @FXML
    void agregarItem(ActionEvent event) {
        try {
            String desc = txtDescripcion.getText();
            if (desc.isEmpty()) {
                mostrarAlerta("Error", "Escribe una descripción");
                return;
            }
            int cant = Integer.parseInt(txtCantidad.getText());
            double precio = Double.parseDouble(txtPrecio.getText());

            DetalleCotizacion detalle = new DetalleCotizacion(desc, cant, precio);
            listaDetalles.add(detalle);

            calcularTotales();
            limpiarInputs();

        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "Cantidad y Precio deben ser números.");
        }
    }

    private void calcularTotales() {
        double subtotal = 0;
        for (DetalleCotizacion d : listaDetalles) {
            subtotal += d.getImporte();
        }
        double iva = subtotal * 0.16;
        double total = subtotal + iva;

        lblSubtotal.setText(String.format("$ %.2f", subtotal));
        lblIVA.setText(String.format("$ %.2f", iva));
        lblTotal.setText(String.format("$ %.2f", total));
    }

    // --- POPUPS ---

    private void abrirFormularioCliente() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/gesa/views/FormularioCLiente.fxml"));
            Parent root = loader.load();

            FormClienController controller = loader.getController();
            controller.setCliente(null);

            Stage stage = new Stage();
            stage.setTitle("Nuevo Cliente Rápido");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.showAndWait();

            cargarClientes(); // Recargar lista al volver

        } catch (IOException e) { e.printStackTrace(); }
    }

    private void abrirFormularioAuto() {
        Cliente clienteActual = cmbCliente.getValue();
        if (clienteActual == null || clienteActual.getId() <= 0) {
            mostrarAlerta("Ojo", "Selecciona un cliente válido primero.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/gesa/views/FormularioAuto.fxml"));
            Parent root = loader.load();

            FormAutoController controller = loader.getController();
            controller.setClientePreseleccionado(clienteActual);

            Stage stage = new Stage();
            stage.setTitle("Nuevo Auto");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            cargarVehiculos(clienteActual.getId()); // Recargar autos

        } catch (IOException e) { e.printStackTrace(); }
    }

    private void limpiarInputs() {
        txtDescripcion.clear();
        txtCantidad.setText("1");
        txtPrecio.clear();
        txtDescripcion.requestFocus();
    }

    private void mostrarAlerta(String titulo, String msg) {
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setTitle(titulo);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}