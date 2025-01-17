package com.astarvisualization.astarvisualization;

import com.astarvisualization.astarvisualization.controller.GridController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage stage) {
        VBox appContainer = new VBox(20);
        GridController controller = new GridController();
        appContainer.getChildren().add(controller.getView());

        Scene scene = new Scene(appContainer, 600, 600);
        controller.registerSceneEventHandlers(scene);

        stage.setTitle("A* search algorithm visualization");
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}