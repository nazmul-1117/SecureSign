package com.securesign.securesign;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        Image icon = new Image("file:src/main/resources/com/securesign/securesign/images/logo.png");

        String fxmlURL = "main-container.fxml";
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(fxmlURL));
        Scene scene = new Scene(fxmlLoader.load(), 640, 480);

        stage.getIcons().add(icon);
        stage.setTitle("SecureSign");
        stage.setScene(scene);

        stage.initStyle(StageStyle.UNDECORATED);
        stage.setResizable(true);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}