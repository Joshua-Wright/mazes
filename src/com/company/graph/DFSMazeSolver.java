package com.company.graph;

import java.awt.*;
import java.util.HashMap;

public class DFSMazeSolver {
    private CellGrid cellGrid;
    private Graph<XYPair, Double> graph;
    private int start, target;
    private long stepDelay;
    private long finalDelay;

    private enum CellLabel {
        UNVISITED,
        VISITED,
        BACK_EDGE,
        DISCOVERY_EDGE,
        BAD_EDGE,
        BAD_NODE,
    }

    private static final Color color_forward = Color.decode("#A6E22E");
    private static final Color color_back = Color.decode("#66D9EF");
    private static final Color color_target = Color.decode("#F92672");

    /*labels for both the vertexes and the edges*/
    private HashMap<Object, CellLabel> labels;

    private class FoundException extends Exception {
        /*throw this when we finally find the target, to unwind the stack*/
    }

    public DFSMazeSolver(CellGrid grid, Graph<XYPair, Double> graph) {
        this.cellGrid = grid;
        this.graph = graph;
        /*try vertex IDs until we find one that exists*/
        do {
            this.start = (int) Math.ceil(Math.random() * graph.getLastVertexId());
        } while (graph.getVertex(this.start) == null);
        do {
            this.target = (int) Math.ceil(Math.random() * graph.getLastVertexId());
        } while (graph.getVertex(this.target) == null);

        this.labels = new HashMap<Object, CellLabel>();
        graph.getVertexes().forEach(v -> labels.put(v, CellLabel.UNVISITED));
        graph.getEdges().forEach(e -> labels.put(e, CellLabel.UNVISITED));

        /*defaults*/
        this.stepDelay = 50;
        this.finalDelay = 500;

        cellGrid.setBackgroundColor(Color.decode("#524F52"));

    }

    public long getStepDelay() {
        return stepDelay;
    }

    public DFSMazeSolver setStepDelay(long stepDelay) {
        this.stepDelay = stepDelay;
        return this;
    }

    private void DFS(int vertex_id) throws InterruptedException, FoundException {
        if (vertex_id == target) {
            Thread.sleep(finalDelay);
            throw new FoundException();
        }

        Vertex<XYPair, ?> v = graph.getVertex(vertex_id);
        /*set the label*/
        labels.put(v, CellLabel.VISITED);
        cellGrid.put(v, color_forward);
        for (Edge<XYPair, ?> e : v.getEdges()) {

            Vertex<XYPair, ?> w = graph.getVertex(e.opposite(v.getId()));

            if (labels.get(w).equals(CellLabel.UNVISITED)) {

                labels.put(e, CellLabel.DISCOVERY_EDGE);
                cellGrid.put(w, color_forward);
                cellGrid.put(e, color_forward);
                Thread.sleep(stepDelay);
                DFS(w.getId());
                cellGrid.put(w, color_back);
                cellGrid.put(e, color_back);
            }
        }
        Thread.sleep(stepDelay);
    }

    public DFSMazeSolver run() {
        cellGrid.put(graph.getVertex(target), color_target);
        try {
            DFS(start);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (FoundException e) {
            return this;
        }
        return this;
    }

    public long getFinalDelay() {
        return finalDelay;
    }

    public DFSMazeSolver setFinalDelay(long finalDelay) {
        this.finalDelay = finalDelay;
        return this;
    }
}
