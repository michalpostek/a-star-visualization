package com.astarvisualization.astarvisualization.view;

import com.astarvisualization.astarvisualization.model.MatrixNode;
import javafx.beans.property.ObjectProperty;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class GridView {
    private final GridPane gridPane;
    private final static int GRID_CELL_SIZE = 20;

    public GridView(ObjectProperty<MatrixNode>[][] matrix) {
        this.gridPane = createGridPaneFromMatrix(matrix);
    }

    public GridPane getGridPane() {
        return gridPane;
    }

    public Rectangle getCell(int row, int col) {
        for (var child : gridPane.getChildren()) {
            if (GridPane.getRowIndex(child) == row && GridPane.getColumnIndex(child) == col) {
                return (Rectangle) child;
            }
        }

        return null;
    }

    private static GridPane createGridPaneFromMatrix(ObjectProperty<MatrixNode>[][] matrix) {
        GridPane gridPane = new GridPane();

        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[row].length; col++) {
                Rectangle gridCell = new Rectangle(GRID_CELL_SIZE, GRID_CELL_SIZE);

                gridCell.setStroke(Color.WHITE);
                gridCell.setFill(matrix[row][col].get().getColor());

                matrix[row][col].addListener((observable, oldValue, newValue) -> gridCell.setFill(newValue.getColor()));

                gridPane.add(gridCell, col, row);
            }
        }

        return gridPane;
    }
}
