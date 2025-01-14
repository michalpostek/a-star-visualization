package com.astarvisualization.astarvisualization.model;

import javafx.scene.paint.Color;

public enum MatrixNode {
//    FIXME move color away from model
    WALKABLE(0, Color.LIGHTGREY),
    START(1, Color.GREEN),
    FINISH(2, Color.RED),
    OPEN_LIST(3, Color.BLUE),
    CLOSED_LIST(4, Color.ORANGE),
    OBSTACLE(5, Color.BLACK),
    FINAL_PATH(6, Color.YELLOW);

    private final int value;
    private final Color color;

    MatrixNode(int value, Color color) {
        this.value = value;
        this.color = color;
    }

    public int getValue() {
        return value;
    }

    public Color getColor() {
        return color;
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
