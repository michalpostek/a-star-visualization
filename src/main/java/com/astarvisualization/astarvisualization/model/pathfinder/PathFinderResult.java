package com.astarvisualization.astarvisualization.model.pathfinder;

import java.util.ArrayList;

public record PathFinderResult(ArrayList<PathFindingStep> steps, boolean foundPath) {
}
