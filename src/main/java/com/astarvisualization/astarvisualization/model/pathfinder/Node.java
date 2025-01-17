package com.astarvisualization.astarvisualization.model.pathfinder;

import java.util.ArrayList;

public class Node {
    int row, col, g, order;
    double h, f;
    Node previous;

    public Node(int row, int col, int g, double h, Node previous, int order) {
        this.row = row;
        this.col = col;
        this.g = g;
        this.h = h;
        this.f = g + h;
        this.previous = previous;
        this.order = order;
    }

    public ArrayList<Node> getPath() {
        ArrayList<Node> path = new ArrayList<>();
        Node current = this;

        while (current != null) {
            path.add(0, current);
            current = current.previous;
        }

        return path;
    }
}
