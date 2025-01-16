package com.astarvisualization.astarvisualization.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Objects;
import java.util.Scanner;

public class MatrixModel {
    private static final int SIZE = 20;
    private static final int DEFAULT_START_ROW = SIZE - 1;
    private static final int DEFAULT_START_COL = 0;
    private static final int DEFAULT_FINISH_ROW = 0;
    private static final int DEFAULT_FINISH_COL = SIZE - 1;
    private final MatrixNode[][] matrix;

    public MatrixModel() {
        matrix = createDefaultMatrix();
    }

    public MatrixNode[][] getMatrix() {
        return matrix;
    }

    public void clearAnimation() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (matrix[row][col] == MatrixNode.OPEN_LIST || matrix[row][col] == MatrixNode.CLOSED_LIST || matrix[row][col] == MatrixNode.FINAL_PATH) {
                    matrix[row][col] = MatrixNode.WALKABLE;
                }
            }
        }
    }

    public void updateFinishNode(int newFinishRow, int newFinishCol) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (getNode(row, col) == MatrixNode.FINISH) {
                    swapNodes(row, col, newFinishRow, newFinishCol);
                }
            }
        }
    }

    public MatrixNode getNode(int row, int col) {
        return matrix[row][col];
    }

    public void updateNode(int row, int col, MatrixNode matrixNode) {
        matrix[row][col] = matrixNode;
    }

    public void swapNodes(int sourceRow, int sourceCol, int targetRow, int targetCol) {
        MatrixNode sourceNode = matrix[sourceRow][sourceCol];
        MatrixNode targetNode = matrix[targetRow][targetCol];

        matrix[sourceRow][sourceCol] = targetNode;
        matrix[targetRow][targetCol] = sourceNode;
    }

    public void toggleObstacle(int row, int col) {
        MatrixNode newState = matrix[row][col] == MatrixNode.OBSTACLE ? MatrixNode.WALKABLE : MatrixNode.OBSTACLE;
        matrix[row][col] = newState;
    }

    private MatrixNode[][] createDefaultMatrix() {
        int[][] rawMatrix = parseDefaultMatrixFile();
        MatrixNode[][] matrix = new MatrixNode[SIZE][SIZE];

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                matrix[row][col] = MatrixNode.fromInt(rawMatrix[row][col]);
            }
        }

        matrix[DEFAULT_START_ROW][DEFAULT_START_COL] = MatrixNode.START;
        matrix[DEFAULT_FINISH_ROW][DEFAULT_FINISH_COL] = MatrixNode.FINISH;

        return matrix;
    }

    private static int[][] parseDefaultMatrixFile() {
        try {
            int[][] matrix = new int[SIZE][SIZE];
            String path = Objects.requireNonNull(MatrixModel.class.getClassLoader().getResource("default-matrix.txt")).getPath();
            File file = new File(path);
            Scanner scanner = new Scanner(file);

            int row = 0;

            while (scanner.hasNextLine() && row < SIZE) {
                String line = scanner.nextLine();
                String[] lineItems = line.split(" ");

                for (int col = 0; col < SIZE; col++) {
                    matrix[row][col] = Integer.parseInt(lineItems[col]);
                }

                row++;
            }

            scanner.close();

            return matrix;
        } catch (FileNotFoundException exception) {
            return new int[SIZE][SIZE];
        }
    }
}
