package com.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.nio.file.Paths;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        String fxmlPath = "./src/main/resources/MainView.fxml";
        Parent root = FXMLLoader.load(Paths.get(fxmlPath).toUri().toURL());

        Scene scene = new Scene(root);

        String cssPath = "./src/main/resources/styles.css";
        scene.getStylesheets().add(Paths.get(cssPath).toUri().toURL().toExternalForm());

        primaryStage.setTitle("English Learning Application");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
