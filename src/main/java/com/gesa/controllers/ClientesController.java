package com.gesa.controllers;

import com.gesa.dao.ClienteDAO;
import com.gesa.models.Cliente;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class ClientesController {

    @FXML private TableView<Cliente> tablaClientes;
    @FXML private TextField txtBuscar;

    // Columnas
    @FXML private TableColumn<Cliente, Integer> colId;
    @FXML private TableColumn<Cliente, String> colNombre;
    @FXML private TableColumn<Cliente, String> colApellidos;
    @FXML private TableColumn<Cliente, String> colTelefono;
    @FXML private TableColumn<Cliente, String> colEmail;
    @FXML private TableColumn<Cliente, String> colRFC;
    @FXML private TableColumn<Cliente, String> colMembresia;
    @FXML private TableColumn<Cliente, String> colRazonSocial;
    @FXML private TableColumn<Cliente, String> colRegimen;
    @FXML private TableColumn<Cliente, String> colCP;
    @FXML private TableColumn<Cliente, String> colUsoCFDI;

    // Herramientas
    private ClienteDAO dao = new ClienteDAO();

    // 1. La Lista Base (Donde viven los datos)
    private ObservableList<Cliente> listaClientes = FXCollections.observableArrayList();

    // 2. La Lista Filtrada (La envoltura inteligente)
    private FilteredList<Cliente> listaFiltrada;

    @FXML
    public void initialize() {
        configurarColumnas();

        // Primero configuramos el buscador para que esté listo antes de cargar datos
        configurarBuscador();

        cargarDatos();
    }

    private void configurarColumnas() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colRFC.setCellValueFactory(new PropertyValueFactory<>("rfc"));
        colMembresia.setCellValueFactory(new PropertyValueFactory<>("textoMembresia"));
        colRazonSocial.setCellValueFactory(new PropertyValueFactory<>("razonSocialFiscal"));
        colRegimen.setCellValueFactory(new PropertyValueFactory<>("regimenFiscal"));
        colCP.setCellValueFactory(new PropertyValueFactory<>("cpFiscal"));
        colUsoCFDI.setCellValueFactory(new PropertyValueFactory<>("usoCfdi"));
    }

    // BUSCADOR 👇👇
    private void configurarBuscador() {
        // 1. Envolvemos la lista original en una FilteredList
        listaFiltrada = new FilteredList<>(listaClientes, b -> true);

        // 2. Le decimos al TextField qué hacer cuando escribes
        txtBuscar.textProperty().addListener((observable, oldValue, newValue) -> {
            listaFiltrada.setPredicate(cliente -> {
                // Si no hay texto, mostramos a todos
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Convertimos a minúsculas para que "Juan" y "juan" sean iguales
                String lowerCaseFilter = newValue.toLowerCase();

                // 3. Reglas del Juego: ¿En dónde buscamos?
                if (cliente.getNombre().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Encontrado en Nombre
                } else if (cliente.getApellidos().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Encontrado en Apellidos
                } else if (cliente.getRfc() != null && cliente.getRfc().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Encontrado en RFC
                } else if (cliente.getTelefono() != null && cliente.getTelefono().contains(lowerCaseFilter)) {
                    return true; // Encontrado en Teléfono
                }

                return false; // No coincide con nada
            });
        });

        // 4. Envolvemos la FilteredList en una SortedList (para no perder el ordenamiento)
        SortedList<Cliente> sortedData = new SortedList<>(listaFiltrada);

        // 5. Vinculamos el comparador de la tabla con la lista ordenada
        sortedData.comparatorProperty().bind(tablaClientes.comparatorProperty());

        // 6. ¡Metemos la lista final a la tabla!
        tablaClientes.setItems(sortedData);
    }

    private void cargarDatos() {
        listaClientes.clear(); // Limpiamos la base
        List<Cliente> clientesDB = dao.listar();
        listaClientes.addAll(clientesDB); // Al llenar la base, la filtrada y ordenada se actualizan solas
    }

    // --- ACCIONES (Igual que antes) ---

    @FXML
    void nuevoCliente(ActionEvent event) {
        abrirFormulario("Nuevo Cliente", null);
    }

    @FXML
    void editarCliente(ActionEvent event) {
        Cliente seleccionado = tablaClientes.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            abrirFormulario("Editar Cliente", seleccionado);
        } else {
            mostrarAlerta("Atención", "Selecciona un cliente de la tabla para editar.");
        }
    }

    @FXML
    void eliminarCliente(ActionEvent event) {
        Cliente seleccionado = tablaClientes.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            boolean exito = dao.eliminar(seleccionado.getId());
            if (exito) {
                System.out.println("🗑️ Cliente eliminado");
                cargarDatos();
            } else {
                mostrarAlerta("Error", "No se pudo eliminar al cliente.");
            }
        } else {
            mostrarAlerta("Atención", "Selecciona un cliente para eliminar.");
        }
    }

    private void abrirFormulario(String titulo, Cliente clienteAEditar) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/gesa/views/FormularioCLiente.fxml"));
            Parent root = loader.load();

            FormClienController controller = loader.getController();
            controller.setCliente(clienteAEditar);

            Stage stage = new Stage();
            stage.setTitle(titulo);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);

            stage.showAndWait();

            cargarDatos();

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error Crítico", "No se pudo abrir el formulario: " + e.getMessage());
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}