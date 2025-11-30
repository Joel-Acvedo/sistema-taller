package com.gesa.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField; // <--- IMPORTANTE
import javafx.scene.control.TextField;     // <--- IMPORTANTE
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

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
}