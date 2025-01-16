package com.astarvisualization.astarvisualization.model;

import java.util.*;

public class PathFinder {
    private static final int[][] MOVES = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
    private static final int MOVE_COST = 1;
    private final MatrixNode[][] matrix;
    private final ArrayList<Step> steps;

    public PathFinder(MatrixNode[][] matrix) {
        this.steps = new ArrayList<>();
        this.matrix = copyMatrix(matrix);
    }

    public ArrayList<Step> getSteps() throws Exception {
        int counter = 0;

        PriorityQueue<Node> openList = new PriorityQueue<>(Comparator
                .comparingDouble((Node node) -> node.f)
                .thenComparingInt((Node node) -> -node.order)
        );

        int startRow = getStartRow();
        int startCol = getStartCol();

        Node startNode = new Node(startRow, startCol, 0, getEuclideanDistance(startRow, startCol), null, counter++);
        openList.add(startNode);
        matrix[startRow][startCol] = MatrixNode.OPEN_LIST;

        while (!openList.isEmpty()) {
            Node currentNode = openList.poll();

            if (isFinish(currentNode.row, currentNode.col)) {
                currentNode.getPath().forEach((Node node) -> addStep(node.row, node.col, MatrixNode.FINAL_PATH));

                return steps;
            }

            matrix[currentNode.row][currentNode.col] = MatrixNode.CLOSED_LIST;
            addStep(currentNode.row, currentNode.col, MatrixNode.CLOSED_LIST);

            for (int[] move : MOVES) {
                int newRow = currentNode.row + move[0];
                int newCol = currentNode.col + move[1];

                if (!isWithinMapRange(newRow, newCol) || matrix[newRow][newCol] == MatrixNode.OBSTACLE || matrix[newRow][newCol] == MatrixNode.CLOSED_LIST) {
                    continue;
                }

                Node node = new Node(newRow, newCol, currentNode.g + MOVE_COST, getEuclideanDistance(newRow, newCol), currentNode, counter++);
                Optional<Node> targetNode = getTargetNode(openList, newRow, newCol);

                if (targetNode.isEmpty()) {
                    openList.add(node);
                    addStep(node.row, node.col, MatrixNode.OPEN_LIST);
                } else if (targetNode.get().f > node.f) {
                    openList.remove(targetNode.get());
                    openList.add(node);
                }
            }
        }

        return steps;
    }

    private void addStep(int row, int col, MatrixNode matrixNode) {
        Step step = new Step(row, col, matrixNode);
        steps.add(step);
    }

    private boolean isWithinMapRange(int row, int col) {
        return row >= 0 && row < matrix.length && col >= 0 && col < matrix[row].length;
    }

    private double getEuclideanDistance(int row, int col) throws Exception {
        return Math.sqrt(Math.pow(getFinishRow() - row, 2) + Math.pow(getFinishCol() - col, 2));
    }

    private int getStartRow() throws Exception {
        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[row].length; col++) {
                if (isStart(row, col)) {
                    return row;
                }
            }
        }

        throw new Exception("No starting point marked");
    }

    private int getStartCol() throws Exception {
        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[row].length; col++) {
                if (isStart(row, col)) {
                    return col;
                }
            }
        }

        throw new Exception("No starting point marked");
    }

    private int getFinishRow() throws Exception {
        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[row].length; col++) {
                if (isFinish(row, col)) {
                    return row;
                }
            }
        }

        throw new Exception("No finish point marked");
    }

    private int getFinishCol() throws Exception {
        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[row].length; col++) {
                if (isFinish(row, col)) {
                    return col;
                }
            }
        }

        throw new Exception("No finish point marked");
    }

    private boolean isStart(int row, int col) {
        return matrix[row][col] == MatrixNode.START;
    }

    private boolean isFinish(int row, int col) {
        return matrix[row][col] == MatrixNode.FINISH;
    }

    private static Optional<Node> getTargetNode(PriorityQueue<Node> openList, int row, int col) {
        Optional<Node> targetNode = Optional.empty();

        for (Node node : openList) {
            if (row == node.row && col == node.col) {
                targetNode = Optional.of(node);
            }
        }

        return targetNode;
    }

    private static MatrixNode[][] copyMatrix(MatrixNode[][] originalMatrix) {
        return Arrays.stream(originalMatrix)
            .map(MatrixNode[]::clone)
            .toArray(MatrixNode[][]::new);
    }
}
