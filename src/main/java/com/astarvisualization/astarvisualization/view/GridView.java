package com.astarvisualization.astarvisualization.view;

import com.astarvisualization.astarvisualization.model.MatrixNode;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class GridView {
    private final GridPane gridPane;
    private final static int GRID_CELL_SIZE = 20;
    private final MatrixNode[][] matrix;

    public GridView(MatrixNode[][] matrix) {
        this.matrix = matrix;
        this.gridPane = createGridPaneFromMatrix(matrix);
    }

    public GridPane getGridPane() {
        return gridPane;
    }

    public void syncGridView() {
        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[row].length; col++) {
                Rectangle gridCell = getCell(row, col);
                gridCell.setFill(getCellColor(matrix[row][col]));
            }
        }
    }

    public Rectangle getCell(int row, int col) {
        for (var child : gridPane.getChildren()) {
            if (GridPane.getRowIndex(child) == row && GridPane.getColumnIndex(child) == col) {
                return (Rectangle) child;
            }
        }

        return null;
    }

    private static GridPane createGridPaneFromMatrix(MatrixNode[][] matrix) {
        GridPane gridPane = new GridPane();

        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[row].length; col++) {
                Rectangle gridCell = new Rectangle(GRID_CELL_SIZE, GRID_CELL_SIZE);

                gridCell.setStroke(Color.WHITE);
                gridCell.setFill(getCellColor(matrix[row][col]));

                gridPane.add(gridCell, col, row);
            }
        }

        return gridPane;
    }

    private static Color getCellColor(MatrixNode matrixNode) {
        return switch (matrixNode) {
            case WALKABLE -> Color.LIGHTGREY;
            case START -> Color.GREEN;
            case FINISH -> Color.RED;
            case OPEN_LIST -> Color.BLUE;
            case CLOSED_LIST -> Color.ORANGE;
            case OBSTACLE -> Color.BLACK;
            case FINAL_PATH -> Color.YELLOW;
        };
    }
}
