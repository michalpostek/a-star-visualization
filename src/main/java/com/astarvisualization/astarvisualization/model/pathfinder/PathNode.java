package com.astarvisualization.astarvisualization.model.pathfinder;

import java.util.ArrayList;

public class PathNode {
    int row, col, g, order;
    double h, f;
    PathNode previous;

    public PathNode(int row, int col, int g, double h, PathNode previous, int order) {
        this.row = row;
        this.col = col;
        this.g = g;
        this.h = h;
        this.f = g + h;
        this.previous = previous;
        this.order = order;
    }

    public ArrayList<PathNode> getPath() {
        ArrayList<PathNode> path = new ArrayList<>();
        PathNode current = this;

        while (current != null) {
            path.add(0, current);
            current = current.previous;
        }

        return path;
    }
}
