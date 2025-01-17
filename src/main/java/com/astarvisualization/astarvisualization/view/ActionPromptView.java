package com.astarvisualization.astarvisualization.view;

import com.astarvisualization.astarvisualization.controller.State;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ActionPromptView  extends VBox {
    public ActionPromptView(State state) {
        updateState(state);
    }

    public void updateState(State state) {
        getChildren().clear();

        switch (state) {
            case CUSTOMIZING:
                addPrompt("Press Space to start pathfinding.");
                addPrompt("Click on a cell to toggle an obstacle.");
                addPrompt("Drag the Start or Finish point to reposition them.");
                break;

            case RUNNING:
                addPrompt("The algorithm is currently running.");
                break;

            case COMPLETED:
                addPrompt("Press Space to reset and customize.");
                break;

            case FAILED:
                addPrompt("Click on a closed cell to select alternative Finish point.");
                break;

            default:
                throw new IllegalArgumentException("Unknown state: " + state);
        }
    }

    private void addPrompt(String text) {
        Text promptText = new Text("• " + text);
        promptText.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-fill: #333;");

        getChildren().add(promptText);
    }
}
