package com.astarvisualization.astarvisualization;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        VBox appContainer = new VBox(20);
        Scene scene = new Scene(appContainer, 320, 240);

        stage.setTitle("A* search algorithm visualization");
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}