package com.astarvisualization.astarvisualization.controller;

import com.astarvisualization.astarvisualization.model.MatrixModel;
import com.astarvisualization.astarvisualization.model.MatrixNode;
import com.astarvisualization.astarvisualization.model.pathfinder.PathFinder;
import com.astarvisualization.astarvisualization.model.pathfinder.PathFinderResult;
import com.astarvisualization.astarvisualization.model.pathfinder.PathFindingStep;
import com.astarvisualization.astarvisualization.view.ActionPromptView;
import com.astarvisualization.astarvisualization.view.GridView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class GridController {
    private State state = State.CUSTOMIZING;
    private final MatrixModel matrixModel;
    private final GridView gridView;
    private final ActionPromptView actionPromptView;

    public GridController() {
        matrixModel = new MatrixModel();
        gridView = new GridView(matrixModel.getMatrix());
        actionPromptView = new ActionPromptView(state);
        registerGridViewEventHandlers();
    }

    public VBox getView() {
        VBox container = new VBox(20);
        container.getChildren().addAll(this.gridView.getGridPane(), this.actionPromptView);

        return container;
    }

    public void registerSceneEventHandlers(Scene scene) {
        scene.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.SPACE) {
                if (state == State.COMPLETED) {
                    matrixModel.clearAnimation();
                    gridView.syncGridView();
                    updateState(State.CUSTOMIZING);
                } else if (state == State.CUSTOMIZING) {
                    try {
                        handleRunPathFinding();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    private void handleRunPathFinding() throws Exception {
        updateState(State.RUNNING);
        PathFinder pathFinder = new PathFinder(matrixModel.getMatrix());
        PathFinderResult result = pathFinder.getSteps();
        runAnimationAndUpdateState(result);
    }

    private void runAnimationAndUpdateState(PathFinderResult result) {
        Timeline timeline = new Timeline();

        int offset = 0;
        for (PathFindingStep step : result.steps()) {
            KeyFrame keyFrame = new KeyFrame(Duration.seconds(offset++ * 0.01), event -> {
                MatrixNode matrixNode = matrixModel.getNode(step.row(), step.col());

                if (matrixNode == MatrixNode.START || matrixNode == MatrixNode.FINISH) {
                    return;
                }

                matrixModel.updateNode(step.row(), step.col(), step.matrixNode());
                gridView.syncGridView();
            });
            timeline.getKeyFrames().add(keyFrame);
        }

        timeline.setCycleCount(1);
        timeline.setOnFinished(event -> {
            if (result.foundPath()) {
                updateState(State.COMPLETED);
            } else {
                updateState(State.FAILED);
            }
        });
        timeline.play();
    }

    private void registerGridViewEventHandlers() {
        gridView.getGridPane().getChildren().forEach(node -> {
            int row = GridPane.getRowIndex(node);
            int col = GridPane.getColumnIndex(node);
            Rectangle gridCell = gridView.getCell(row, col);

            gridCell.setOnMouseClicked(event -> {
                if (state == State.CUSTOMIZING) {
                    MatrixNode targetNode = matrixModel.getNode(row, col);

                    if (targetNode == MatrixNode.START || targetNode == MatrixNode.FINISH) {
                        return;
                    }

                    matrixModel.toggleObstacle(row, col);
                    gridView.syncGridView();
                } else if (state == State.FAILED && matrixModel.getNode(row, col) == MatrixNode.CLOSED_LIST) {
                    matrixModel.updateFinishNode(row, col);
                    matrixModel.clearAnimation();
                    gridView.syncGridView();
                    try {
                        handleRunPathFinding();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }

                event.consume();
            });

            gridCell.setOnDragDetected((MouseEvent event) -> {
                gridCell.startFullDrag();
                event.consume();
            });

            gridCell.setOnMouseDragReleased((MouseDragEvent event) -> {
                if (state != State.CUSTOMIZING) {
                    return;
                }

                Rectangle source = (Rectangle) event.getGestureSource();
                int sourceRow = GridPane.getRowIndex(source);
                int sourceCol = GridPane.getColumnIndex(source);

                MatrixNode sourceNode = matrixModel.getNode(sourceRow, sourceCol);
                MatrixNode targetNode = matrixModel.getNode(row, col);

                if (sourceNode == MatrixNode.WALKABLE || targetNode == MatrixNode.START || targetNode == MatrixNode.FINISH) {
                    return;
                }

                matrixModel.swapNodes(sourceRow, sourceCol, row, col);
                gridView.syncGridView();
                event.consume();
            });
        });
    }

    private void updateState(State state) {
        this.state = state;
        this.actionPromptView.updateState(state);
    }
}
