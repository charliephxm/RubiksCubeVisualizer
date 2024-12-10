package com.rubikssolver.rubikscubevisualizer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/rubikssolver/rubikscubevisualizer/VisualizerLayout.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);

        stage.setTitle("Rubik's Cube Algorithm Visualizer");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}