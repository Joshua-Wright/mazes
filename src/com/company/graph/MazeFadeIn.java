package com.company.graph;

import java.awt.*;
import java.util.*;

public class MazeFadeIn {
    private CellGrid cellGrid;
    private Graph<XYPair, Double> graph;
    private long stepDelay = 1;

    private static final Color color_target = Color.decode("#F92672");

    public MazeFadeIn(CellGrid cellGrid, Graph<XYPair, Double> graph) {
        this.cellGrid = cellGrid;
        this.graph = graph;
        graph.getVertexes().forEach(v -> cellGrid.put(v, cellGrid.getBackgroundColor()));
        graph.getEdges().forEach(e -> cellGrid.put(e, cellGrid.getBackgroundColor()));
//        cellGrid.removeAll();
    }

    public MazeFadeIn run() {
        Set<Edge<XYPair, Double>> edges = new TreeSet<>(graph.getEdges());
        int count = 0;
        for (Edge<XYPair, Double> edge : edges) {
            cellGrid.put(edge.getSrcVertex(), cellGrid.getForegroundColor());
            cellGrid.put(edge.getDestVertex(), cellGrid.getForegroundColor());
            cellGrid.put(edge, cellGrid.getForegroundColor());
            /*sleep every 3 inputs, so pop in 3 at a time*/
            count++;
            if (count % 3 == 0) {
                sleep(stepDelay);
            }
        }

        return this;
    }

    private static void sleep(long delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public long getStepDelay() {
        return stepDelay;
    }

    public MazeFadeIn setStepDelay(long stepDelay) {
        this.stepDelay = stepDelay;
        return this;
    }
}
