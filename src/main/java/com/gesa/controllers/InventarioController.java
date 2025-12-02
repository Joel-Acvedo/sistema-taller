package com.gesa.controllers;

import com.gesa.dao.RefaccionDAO;
import com.gesa.models.Refaccion;
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

public class InventarioController {

    // 1. Elementos Visuales
    @FXML private TableView<Refaccion> tablaInventario;
    @FXML private TextField txtBuscar;

    // Columnas (Deben coincidir con los fx:id de tu Inventario.fxml)
    @FXML private TableColumn<Refaccion, Integer> colId;
    @FXML private TableColumn<Refaccion, String> colNombre;
    @FXML private TableColumn<Refaccion, String> colCodigo;
    @FXML private TableColumn<Refaccion, Double> colCosto;
    @FXML private TableColumn<Refaccion, Double> colPrecio;
    @FXML private TableColumn<Refaccion, Integer> colStock;
    @FXML private TableColumn<Refaccion, String> colDescripcion;

    // 2. Herramientas Lógicas
    private RefaccionDAO dao = new RefaccionDAO();
    private ObservableList<Refaccion> listaRefacciones = FXCollections.observableArrayList();
    private FilteredList<Refaccion> listaFiltrada; // Para el buscador

    // 3. ARRANQUE
    @FXML
    public void initialize() {
        configurarColumnas();
        configurarBuscador(); // Preparamos el filtro
        cargarDatos();        // Llenamos la tabla
    }

    private void configurarColumnas() {
        // "codigoInterno" busca getCodigoInterno() en Refaccion.java
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigoInterno"));
        colCosto.setCellValueFactory(new PropertyValueFactory<>("costoProveedor"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precioVenta"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stockActual"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
    }

    // --- MAGIA DEL BUSCADOR ---
    private void configurarBuscador() {
        // 1. Envolver la lista original
        listaFiltrada = new FilteredList<>(listaRefacciones, b -> true);

        // 2. Escuchar lo que escribes
        txtBuscar.textProperty().addListener((observable, oldValue, newValue) -> {
            listaFiltrada.setPredicate(refaccion -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true; // Si está vacío, muestra todo
                }

                String lowerFilter = newValue.toLowerCase();

                // Reglas de búsqueda: Por Nombre o por Código
                if (refaccion.getNombre().toLowerCase().contains(lowerFilter)) {
                    return true;
                } else if (refaccion.getCodigoInterno() != null && refaccion.getCodigoInterno().toLowerCase().contains(lowerFilter)) {
                    return true;
                }

                return false; // No coincide
            });
        });

        // 3. Envolver en lista ordenada (para poder dar click en columnas y ordenar)
        SortedList<Refaccion> sortedData = new SortedList<>(listaFiltrada);
        sortedData.comparatorProperty().bind(tablaInventario.comparatorProperty());

        // 4. Setear a la tabla
        tablaInventario.setItems(sortedData);
    }

    private void cargarDatos() {
        listaRefacciones.clear();
        List<Refaccion> datos = dao.listar();
        listaRefacciones.addAll(datos);
        // La tabla se actualiza sola gracias al ObservableList
    }

    // --- ACCIONES ---

    @FXML
    void nuevoInv(ActionEvent event) {
        System.out.println("✨ Nueva Refacción");
        abrirFormulario("Nueva Refacción", null);
    }

    @FXML
    void editarInv(ActionEvent event) {
        Refaccion seleccionado = tablaInventario.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            System.out.println("📝 Editando: " + seleccionado.getNombre());
            abrirFormulario("Editar Refacción", seleccionado);
        } else {
            mostrarAlerta("Atención", "Selecciona una pieza para editar.");
        }
    }

    @FXML
    void eliminarInv(ActionEvent event) { // Asegúrate que en el FXML el botón diga onAction="#eliminarInv"
        Refaccion seleccionado = tablaInventario.getSelectionModel().getSelectedItem();

        if (seleccionado != null) {
            // Soft Delete
            boolean exito = dao.eliminar(seleccionado.getId());
            if (exito) {
                System.out.println("🗑️ Refacción eliminada");
                cargarDatos(); // Recargar para que desaparezca
            } else {
                mostrarAlerta("Error", "No se pudo eliminar la refacción.");
            }
        } else {
            mostrarAlerta("Atención", "Selecciona una pieza para eliminar.");
        }
    }

    // --- ABRIR VENTANA (POPUP) ---
    private void abrirFormulario(String titulo, Refaccion refaccionAEditar) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/gesa/views/FormularioInventario.fxml"));
            Parent root = loader.load();

            // Pasamos los datos al controlador hijo
            FormInvController controller = loader.getController();
            controller.setRefaccion(refaccionAEditar);

            Stage stage = new Stage();
            stage.setTitle(titulo);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL); // Bloquea la de atrás
            stage.setResizable(false);

            stage.showAndWait(); // Espera a que cierren...

            cargarDatos(); // Actualiza la tabla al volver

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error Crítico", "No se pudo abrir el formulario: " + e.getMessage());
        }
    }

    private void mostrarAlerta(String titulo, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}