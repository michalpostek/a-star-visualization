package com.astarvisualization.astarvisualization.controller;

import com.astarvisualization.astarvisualization.model.*;
import com.astarvisualization.astarvisualization.view.GridView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class GridController {
    private PathFindingState state = PathFindingState.CUSTOMIZING;
    private final MatrixModel matrixModel;
    private final GridView gridView;

    public GridController() {
        matrixModel = new MatrixModel();
        gridView = new GridView(matrixModel.getMatrix());
        registerGridViewEventHandlers();
    }

    public GridPane getView() {
        return this.gridView.getGridPane();
    }

    public void registerSceneEventHandlers(Scene scene) {
        scene.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.SPACE) {
                if (state == PathFindingState.COMPLETED) {
                    matrixModel.clearAnimation();
                    gridView.syncGridView(matrixModel.getMatrix());
                    state = PathFindingState.CUSTOMIZING;

                    return;
                }

                try {
                    handleRunPathFinding();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void handleRunPathFinding() throws Exception {
        state = PathFindingState.RUNNING;
        PathFinder pathFinder = new PathFinder(matrixModel.getMatrix());
        PathFinderResult result = pathFinder.getSteps();
        runAnimationAndUpdateState(result);
    }

    private void runAnimationAndUpdateState(PathFinderResult result) {
        Timeline timeline = new Timeline();

        int offset = 0;
        for (Step step : result.steps()) {
            KeyFrame keyFrame = new KeyFrame(Duration.seconds(offset++ * 0.01), event -> {
                matrixModel.updateCell(step.row(), step.col(), step.matrixNode());
                gridView.syncGridView(matrixModel.getMatrix());
            });
            timeline.getKeyFrames().add(keyFrame);
        }

        timeline.setCycleCount(1);
        timeline.play();
        timeline.setOnFinished(event -> {
            if (result.foundPath()) {
                state = PathFindingState.COMPLETED;
            } else {
                state = PathFindingState.FAILED;
            }
        });
    }

    private void registerGridViewEventHandlers() {
        gridView.getGridPane().getChildren().forEach(node -> {
            int row = GridPane.getRowIndex(node);
            int col = GridPane.getColumnIndex(node);
            Rectangle gridCell = gridView.getCell(row, col);

            gridCell.setOnMouseClicked(event -> {
                if (state == PathFindingState.CUSTOMIZING) {
                    matrixModel.handleCellClick(row, col);
                    gridView.syncGridView(matrixModel.getMatrix());
                } else if (state == PathFindingState.FAILED && matrixModel.getCell(row, col) == MatrixNode.CLOSED_LIST) {
                    matrixModel.updateFinishCell(row, col);
                    matrixModel.clearAnimation();
                    gridView.syncGridView(matrixModel.getMatrix());
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
                if (state != PathFindingState.CUSTOMIZING) {
                    return;
                }

                Rectangle source = (Rectangle) event.getGestureSource();
                int sourceRow = GridPane.getRowIndex(source);
                int sourceCol = GridPane.getColumnIndex(source);

                matrixModel.handleCellDrag(sourceRow, sourceCol, row, col);
                gridView.syncGridView(matrixModel.getMatrix());
                event.consume();
            });
        });
    }
}
