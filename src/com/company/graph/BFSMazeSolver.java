package com.company.graph;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class BFSMazeSolver {
    private CellGrid cellGrid;
    private Graph<XYPair, Double> graph;
    private int start, target;
    private long stepDelay;
    private long finalDelay;
    private boolean doFullMaze;

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
    private HashMap<Object, CellLabel> DFSLabels;
    private HashMap<Object, CellLabel> labels;
    private HashSet<Vertex> visited;
    private HashSet<Integer> pathSet;

    public BFSMazeSolver(CellGrid cellGrid, Graph<XYPair, Double> graph) {
        this.cellGrid = cellGrid;
        this.graph = graph;
        this.doFullMaze = true;
        /*try vertex IDs until we find one that exists*/
        do {
            this.start = (int) Math.ceil(Math.random() * graph.getLastVertexId());
        } while (graph.getVertex(this.start) == null);
        do {
            this.target = (int) Math.ceil(Math.random() * graph.getLastVertexId());
        } while (graph.getVertex(this.target) == null);

        this.labels = new HashMap<Object, CellLabel>();
        this.DFSLabels = new HashMap<Object, CellLabel>();
        this.visited = new HashSet<>();
        graph.getVertexes().forEach(v -> DFSLabels.put(v, CellLabel.UNVISITED));
        graph.getEdges().forEach(e -> DFSLabels.put(e, CellLabel.UNVISITED));

        /*defaults*/
        this.stepDelay = 50;
        this.finalDelay = 500;
        cellGrid.setBackgroundColor(Color.decode("#524F52"));
    }

    /**
     * @param vertex_id: id of starting vertex
     * @return sequence of vertexes that lead from start to end
     */
    private List<Integer> DFSFindPath(int vertex_id) {
        /*base-case: we found the path because we are there*/
        if (vertex_id == target) {
            List<Integer> list = new LinkedList<>();
            list.add(0, vertex_id);
            return list;
        }
        Vertex<XYPair, ?> v = graph.getVertex(vertex_id);
        /*set the label*/
        DFSLabels.put(v, CellLabel.VISITED);
        for (Edge<XYPair, ?> e : v.getEdges()) {

            Vertex<XYPair, ?> w = graph.getVertex(e.opposite(v.getId()));

            if (DFSLabels.get(w).equals(CellLabel.UNVISITED)) {
                DFSLabels.put(e, CellLabel.DISCOVERY_EDGE);

                /*add to the current path we've traversed*/
                List<Integer> list = DFSFindPath(w.getId());
                if (list != null) {
                    list.add(0, vertex_id);
                    return list;
                }
            }
        }
        /*did not find a path*/
        return null;
    }

    public BFSMazeSolver run() {
        cellGrid.put(graph.getVertex(target), color_target);
        List<Integer> sequence = DFSFindPath(start);
        pathSet = new HashSet<>(sequence);
        /*add the first node*/
        visited.add(graph.getVertex(sequence.get(0)));
        cellGrid.put(graph.getVertex(sequence.get(0)), color_forward);

        if (doFullMaze) {
            while (visited.size() < graph.getVertexes().size()) {
                addEdges();
                sleep(stepDelay);
                addVertexes();
                sleep(stepDelay);
            }
        } else {
            for (int i = 0; i < sequence.size(); i++) {
                addEdges();
                sleep(stepDelay);
                addVertexes();
                sleep(stepDelay);
            }
        }

        sleep(finalDelay);
        return this;
    }

    private void addEdges() {
        graph.getEdges().parallelStream()
                .filter((e) -> visited.contains(e.getSrcVertex()) && !visited.contains(e.getDestVertex()))
                .collect(Collectors.toList())
                .forEach((e) -> {
                    cellGrid.put(e, color_back);
                    if (pathSet.contains(e.getSrc()) && pathSet.contains(e.getDest())) {
                        cellGrid.put(e, color_forward);
                    }
                });
    }

    private void addVertexes() {
        graph.getEdges().parallelStream()
                .filter((e) -> visited.contains(e.getSrcVertex()) && !visited.contains(e.getDestVertex()))
                .map(Edge::getDestVertex)
                .collect(Collectors.toList())
                .forEach((v) -> {
                    visited.add(v);
                    if (pathSet.contains(v.getId())) {
                        cellGrid.put(v, color_forward);
                    } else {
                        cellGrid.put(v, color_back);
                    }
                });
    }

    private static void sleep(long delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public long getFinalDelay() {
        return finalDelay;
    }

    public BFSMazeSolver setFinalDelay(long finalDelay) {
        this.finalDelay = finalDelay;
        return this;
    }

    public long getStepDelay() {
        return stepDelay;
    }

    public BFSMazeSolver setStepDelay(long stepDelay) {
        this.stepDelay = stepDelay;
        return this;
    }

    public boolean isDoFullMaze() {
        return doFullMaze;
    }

    public BFSMazeSolver setDoFullMaze(boolean doFullMaze) {
        this.doFullMaze = doFullMaze;
        return this;
    }
}
