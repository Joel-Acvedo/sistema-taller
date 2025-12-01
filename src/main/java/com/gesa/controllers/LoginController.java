package com.gesa.controllers;

import com.gesa.App;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField; // <--- IMPORTANTE
import javafx.scene.control.TextField;     // <--- IMPORTANTE
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LoginController {
    //Conectamos con mi login
    @FXML
    private BorderPane Login;

    // 👇👇 TE FALTABAN ESTOS DOS 👇👇
    // Tienen que llamarse IGUAL que el fx:id del FXML
    @FXML private TextField txtUser;
    @FXML private PasswordField txtPass;


    // ---------------------------------------------------

    @FXML
    void handleLogin(javafx.event.ActionEvent event) {
        System.out.println("¡Botón presionado!");

        // Llamamos mis variables txtUser y txtPass
        String usuario = txtUser.getText();
        String password = txtPass.getText();

        if (usuario.isEmpty() || password.isEmpty()) {
            System.out.println("❌ Campos vacíos");
        } else {
            System.out.println("✅ Intentando entrar con: " + usuario);
            // Aquí llamaremos el DAO más adelante por el momento entra por que entra
            irAlMenuPrincipal();
        }
    }

    // Variables para guardar la posición del mouse
    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    public void initialize() {
        // Cuando haces clic, guardamos dónde agarraste la ventana
        Login.setOnMousePressed((MouseEvent event) -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        // Cuando arrastras el mouse, movemos la ventana
        Login.setOnMouseDragged((MouseEvent event) -> {
            Stage stage = (Stage) Login.getScene().getWindow();
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
    }
    private void irAlMenuPrincipal() {
        try {
            // 1. Cargar el archivo del Menú (Asegúrate que la ruta sea correcta)
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/com/gesa/views/MainView.fxml"));
            javafx.scene.Parent root = loader.load();

            // 2. Crear la nueva escena
            javafx.scene.Scene scene = new javafx.scene.Scene(root);
            javafx.stage.Stage stage = new javafx.stage.Stage();

            stage.setTitle("Sistema GESA Automotriz");

            stage.setScene(scene);
            stage.setResizable(false);

            // 3. Mostrar la nueva y cerrar la vieja
            stage.show();

            // Cerrar el Login usando la referencia de tu panel principal
            javafx.stage.Stage loginStage = (javafx.stage.Stage) Login.getScene().getWindow();
            loginStage.close();









        } catch (Exception e) {
            System.err.println("❌ Error al cargar el Menu Principal: " + e.getMessage());
            e.printStackTrace();
        }
    }
}