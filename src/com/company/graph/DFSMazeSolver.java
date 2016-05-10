package com.company.graph;

import java.awt.*;
import java.util.HashMap;

public class DFSMazeSolver {
    private CellGrid cellGrid;
    private Graph<XYPair, Double> graph;
    private int start, target;
    private long stepDelay;

    public long getFinalDelay() {
        return finalDelay;
    }

    public void setFinalDelay(long finalDelay) {
        this.finalDelay = finalDelay;
    }

    private long finalDelay;

    private enum CellLabel {
        UNVISITED,
        VISITED,
        BACK_EDGE,
        DISCOVERY_EDGE,
        BAD_EDGE,
        BAD_NODE,
    }

    private Color color_forward = Color.GREEN;
    private Color color_back = Color.BLUE;
    private Color color_target = Color.RED;

    ;
    /*labels for both the vertexes and the edges*/
    private HashMap<Object, CellLabel> labels;

    public DFSMazeSolver(CellGrid grid, Graph<XYPair, Double> graph) {
        this.cellGrid = grid;
        this.graph = graph;
        /*weird math because 0 isn't a valid vertex id*/
        this.start = (int) (Math.random() * (graph.getLastVertexId() - 1) + 1);
        this.target = (int) (Math.random() * (graph.getLastVertexId() - 1) + 1);

        this.labels = new HashMap<Object, CellLabel>();
        graph.getVertexes().forEach((id, v) -> labels.put(v, CellLabel.UNVISITED));
        graph.getEdges().forEach((ep, e) -> labels.put(e, CellLabel.UNVISITED));

        this.stepDelay = 50;
        this.finalDelay = 500;
    }

    public long getStepDelay() {
        return stepDelay;
    }

    public void setStepDelay(long stepDelay) {
        this.stepDelay = stepDelay;
    }

    private class FoundException extends Exception {
    }

    private void DFS(int vertex_id) throws InterruptedException, FoundException {
//        System.out.println("id: " + String.valueOf(vertex_id));
        if (vertex_id == target) {
            Thread.sleep(finalDelay);
            throw new FoundException();
        }

        Graph<XYPair, Double>.Vertex v = graph.getVertex(vertex_id);
        /*set the label*/
        labels.put(v, CellLabel.VISITED);
        cellGrid.put(v, color_forward);
        for (Graph<XYPair, Double>.Edge e : v.getEdges().values()) {

            Graph<XYPair, Double>.Vertex w = graph.getVertex(e.opposite(v.getId()));

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

    public void run() {
        cellGrid.put(graph.getVertex(target), color_target);
        try {
            DFS(start);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (FoundException e) {
            return;
        }
    }
}
