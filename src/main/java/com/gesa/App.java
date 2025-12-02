package com.gesa;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;

public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // 👇 AQUÍ AGREGAMOS LA CARPETA "views/"
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("views/LoginView.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 650, 500);
        stage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);
        stage.setResizable(false); // <--- ESTA ES LA LÍNEA MÁGICA


        stage.setTitle("Login GESA");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}