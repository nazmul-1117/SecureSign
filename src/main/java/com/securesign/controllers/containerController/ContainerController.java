package com.securesign.controllers.containerController;

import com.jfoenix.controls.JFXButton;
import com.securesign.controllers.containerController.FXMLLoadToPane;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import com.securesign.securesign.Main;

import java.net.URL;
import java.util.ResourceBundle;

public class ContainerController implements Initializable {
    public VBox leftMenuBar;

    public JFXButton minimizeButton;
    public JFXButton maximizeButton;
    public JFXButton closeButton;

    public JFXButton homeJFXB;
    public JFXButton manualJFXB;
    public JFXButton documentsJFXB;
    public BorderPane topMenuBarBorderPane;
    public JFXButton rsaKeyJFXB;
    public JFXButton docSigningJFXB;
    public JFXButton verificationJFXB;
    public JFXButton aboutJFXB;
    @FXML BorderPane mainDashboardBorderPane;

    public void setSceneToNewWindow(ActionEvent event, String fxmlFileName){
        System.out.println("You Clicked "+ fxmlFileName);
        FXMLLoadToPane fxmlLoader1 = new FXMLLoadToPane();
        Pane pane = fxmlLoader1.getPane(fxmlFileName);
        mainDashboardBorderPane.setCenter(pane);
    }

    private void setButtonColor(JFXButton buttonColor){
        homeJFXB.setStyle("-fx-background-color: transparent");
        rsaKeyJFXB.setStyle("-fx-background-color: transparent");
        documentsJFXB.setStyle("-fx-background-color: transparent");
        docSigningJFXB.setStyle("-fx-background-color: transparent");
        verificationJFXB.setStyle("-fx-background-color: transparent");
        manualJFXB.setStyle("-fx-background-color: transparent");
        aboutJFXB.setStyle("-fx-background-color: transparent");

        buttonColor.setStyle("-fx-background-color: #00C9CF;"+"-fx-text-fill: #000000;");
    }

    public void leftMenubarButtonHandler(ActionEvent event) {

        setButtonColor((JFXButton) event.getSource());
        if (event.getSource().equals(homeJFXB)){
            setSceneToNewWindow(event, "home.fxml");

        }else if (event.getSource().equals(rsaKeyJFXB)){
            setButtonColor(rsaKeyJFXB);
            setSceneToNewWindow(event, "rsa-key.fxml");

        }else if (event.getSource().equals(documentsJFXB)){
            setSceneToNewWindow(event, "documents.fxml");

        }else if (event.getSource().equals(docSigningJFXB)){
            setSceneToNewWindow(event, "signing.fxml");

        }else if (event.getSource().equals(verificationJFXB)){
            setSceneToNewWindow(event, "verification.fxml");

        }else if (event.getSource().equals(manualJFXB)){
            setSceneToNewWindow(event, "manual.fxml");

        }else if (event.getSource().equals(aboutJFXB)){
            setSceneToNewWindow(event, "about.fxml");

        }
    }

    public void titleBarButtonAction(ActionEvent event) {

        System.out.println("Button called for: " + event.toString());

        Stage tempStage;

        if (event.getSource().equals(closeButton)){
            javafx.application.Platform.exit();
            System.out.println("System Closed");

        }else if (event.getSource().equals(minimizeButton)){
            tempStage = (Stage) ((Button)event.getSource()).getScene().getWindow();
            tempStage.setIconified(true);
            System.out.println("System Minimize");

        }else if(event.getSource().equals(maximizeButton)){
            tempStage = (Stage) ((Button)event.getSource()).getScene().getWindow();
            tempStage.setMaximized(!tempStage.isMaximized());
            System.out.println("Maximized button");
        }
    }

    private void setScene(ActionEvent event){
        try {
            Image icon = new Image("file:src/main/java/images/logo.png");

            Stage stage;
            FXMLLoader fxmlLoader;
            Scene scene;

            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.close();
            stage = new Stage();

            String fxmlName = "hamming.fxml";
            URL fxmlURL = Main.class.getResource(fxmlName);

            fxmlLoader = new FXMLLoader(fxmlURL);

            System.out.println("URL" + fxmlURL);

            scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);

            stage.getIcons().add(icon);
            stage.setTitle("BitGuard!");

            stage.initStyle(StageStyle.UNDECORATED);
            stage.setResizable(true);
            stage.show();

            System.out.println("scene changes");
        }catch (Exception e){
            System.out.println("Scene Load Failed");
        }
    }

    public void logOutButton(ActionEvent event) {
        System.out.println("Logout Button Clicked");
        setScene(event);
        System.out.println("Logout Done");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}