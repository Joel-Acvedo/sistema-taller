package com.gesa.controllers;

import com.gesa.dao.RefaccionDAO;
import com.gesa.models.Refaccion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

    @FXML private TableView<Refaccion> tablaInventario;
    @FXML private TextField txtBuscar;

    // Columnas
    @FXML private TableColumn<Refaccion, Integer> colId;
    @FXML private TableColumn<Refaccion, String> colNombre;
    @FXML private TableColumn<Refaccion, String> colCodigo;
    @FXML private TableColumn<Refaccion, Double> colCosto;
    @FXML private TableColumn<Refaccion, Double> colPrecio;
    @FXML private TableColumn<Refaccion, Integer> colStock;
    @FXML private TableColumn<Refaccion, String> colDescripcion;

    private RefaccionDAO dao = new RefaccionDAO();
    private ObservableList<Refaccion> listaRefacciones = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        configurarColumnas();
        cargarDatos();
    }

    private void configurarColumnas() {
        // Asegúrate que estos strings coincidan con Refaccion.java
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigoInterno"));
        colCosto.setCellValueFactory(new PropertyValueFactory<>("costoProveedor"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precioVenta"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stockActual"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
    }

    private void cargarDatos() {
        listaRefacciones.clear();
        List<Refaccion> datos = dao.listar();
        listaRefacciones.addAll(datos);
        tablaInventario.setItems(listaRefacciones);
    }

    @FXML
    void nuevoInv(ActionEvent event) {
        abrirFormulario("Nueva Refacción", null);
    }

    @FXML
    void editarInv(ActionEvent event) {
        Refaccion seleccionado = tablaInventario.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            abrirFormulario("Editar Refacción", seleccionado);
        } else {
            System.out.println("Selecciona algo");
        }
    }

    // Método Maestro
    private void abrirFormulario(String titulo, Refaccion refaccionAEditar) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/gesa/views/FormularioInventario.fxml"));
            Parent root = loader.load();

            FormInvController controller = loader.getController();
            controller.setRefaccion(refaccionAEditar);

            Stage stage = new Stage();
            stage.setTitle(titulo);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.showAndWait();

            cargarDatos(); // Recargar al volver

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}