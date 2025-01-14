package com.astarvisualization.astarvisualization.controller;

import com.astarvisualization.astarvisualization.model.MatrixModel;
import com.astarvisualization.astarvisualization.view.GridView;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;

public class GridController {
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

    private void registerGridViewEventHandlers() {
        gridView.getGridPane().getChildren().forEach(node -> {
            int row = GridPane.getRowIndex(node);
            int col = GridPane.getColumnIndex(node);
            Rectangle gridCell = gridView.getCell(row, col);

            gridCell.setOnMouseClicked(event -> {
                if (matrixModel.isStart(row, col) || matrixModel.isFinish(row, col)) {
                    return;
                }

                matrixModel.toggleObstacle(row, col);
                gridView.syncGridView(matrixModel.getMatrix());
            });

            gridCell.setOnDragDetected((MouseEvent event) -> {
                gridCell.startFullDrag();
                event.consume();
            });

            gridCell.setOnMouseDragReleased((MouseDragEvent event) -> {
                Rectangle source = (Rectangle) event.getGestureSource();
                int sourceRow = GridPane.getRowIndex(source);
                int sourceCol = GridPane.getColumnIndex(source);

                matrixModel.swapNodes(sourceRow, sourceCol, row, col);
                gridView.syncGridView(matrixModel.getMatrix());
                event.consume();
            });
        });
    }
}
