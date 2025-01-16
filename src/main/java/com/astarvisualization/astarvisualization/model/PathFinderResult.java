package com.astarvisualization.astarvisualization.model;

import java.util.ArrayList;

public record PathFinderResult(ArrayList<Step> steps, boolean foundPath) {
}
