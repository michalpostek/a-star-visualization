package com.astarvisualization.astarvisualization.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Objects;
import java.util.Scanner;

public class MatrixModel {
    private PathFindingState state = PathFindingState.CUSTOMIZING;
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

    public boolean hasFinished() {
        return state == PathFindingState.COMPLETED || state == PathFindingState.FAILED;
    }

    public void runPathFinding() {
        state = PathFindingState.RUNNING;
    }

    public void handleCellDrag(int sourceRow, int sourceCol, int targetRow, int targetCol) {
        if (state != PathFindingState.CUSTOMIZING) {
            return;
        }

        MatrixNode sourceNode = matrix[sourceRow][sourceCol];
        MatrixNode targetNode = matrix[targetRow][targetCol];

        matrix[sourceRow][sourceCol] = targetNode;
        matrix[targetRow][targetCol] = sourceNode;
    }

    public void handleCellClick(int row, int col) {
        if (state != PathFindingState.CUSTOMIZING || isStart(row, col) || isFinish(row, col)) {
            return;
        }

        MatrixNode newState = matrix[row][col] == MatrixNode.OBSTACLE ? MatrixNode.WALKABLE : MatrixNode.OBSTACLE;
        matrix[row][col] = newState;
    }

    private boolean isStart(int row, int col) {
        return matrix[row][col] == MatrixNode.START;
    }

    private boolean isFinish(int row, int col) {
        return matrix[row][col] == MatrixNode.FINISH;
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
