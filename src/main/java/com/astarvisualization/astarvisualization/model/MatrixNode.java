package com.astarvisualization.astarvisualization.model;

public enum MatrixNode {
    WALKABLE(0),
    START(1),
    FINISH(2),
    OPEN_LIST(3),
    CLOSED_LIST(4),
    OBSTACLE(5),
    FINAL_PATH(6);

    private final int value;

    MatrixNode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static MatrixNode fromInt(int i) {
        for (MatrixNode matrixNode : MatrixNode.values()) {
            if (matrixNode.getValue() == i) {
                return matrixNode;
            }
        }

        throw new IllegalArgumentException("Unexpected value: " + i);
    }
}
