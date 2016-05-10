package com.company.graph;

import java.util.*;

public class Algorithm {

    public static Graph<XYPair, Double> GeneratePlanarGraph(int w, int h) {
        Graph<XYPair, Double> output = new Graph<>();
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                Graph.Vertex v1 = output.insertVertex(new XYPair(x, y));
                if (x > 0) {
                    Graph.Vertex v2 = output.getVertex(new XYPair(x - 1, y));
                    output.insertEdgeUndirected(v1.getId(), v2.getId(), Math.random());
                }
                if (y > 0) {
                    Graph.Vertex v2 = output.getVertex(new XYPair(x, y - 1));
                    output.insertEdgeUndirected(v1.getId(), v2.getId(), Math.random());
                }
            }
        }
        return output;
    }

    public static <VP, EP> Graph<VP, EP> RandomBFS(Graph<VP, EP> g) {
        /*starts at node 1*/
        HashMap<Integer, Integer> parentMap = new HashMap<>();
        Graph<VP, EP> output = new Graph<VP, EP>();
        TreeMap<Double, Graph<VP, EP>.Vertex> toProcess = new TreeMap<>();
        HashMap<Graph<VP, EP>.Vertex, Boolean> processed = new HashMap<>();
        g.getVertexes().forEach((id,v) -> {
            processed.put(v, false);
            output.insertVertex(v.getProp(), id);
        });
        toProcess.put(0.0, g.getVertex(1));

        while (!toProcess.isEmpty()) {
            /*get a random thing to look at*/
            Graph<VP, EP>.Vertex v = toProcess.pollFirstEntry().getValue();

            /*mark this node as done*/
            processed.put(v, true);

            /*add the adjacent unexplored nodes*/
            v.getEdges().forEach((ep, e) -> {
                Graph<VP, EP>.Vertex w = g.getVertex(e.opposite(v.getId()));
                if (!processed.get(w)) {
                    toProcess.put(Math.random(), w);
                    parentMap.put(w.getId(), v.getId());
                }
            });
        }

        parentMap.forEach((k, v) -> {
            output.insertEdgeUndirected(k, v, g.getEdge(k, v).getProperty());
        });
        return output;
    }

    public static <VP> Graph<VP, Double> MSTPrimJarnik(Graph<VP, Double> g) {
        Graph<VP, Double> output = new Graph<>();
        final HashMap<Integer, Double> D = new HashMap<>();
        final HashMap<Integer, Integer> P = new HashMap<>();
        Comparator<Graph<VP, Double>.Vertex> comparator = (Graph<VP, Double>.Vertex v1, Graph<VP, Double>.Vertex v2) -> {
            /*make sure that unequal vertexes with equal distances don't compare equal*/
            int comp = D.get(v1.getId()).compareTo(D.get(v2.getId()));
            if (comp == 0 && !v1.equals(v2)) {
                return -1;
            } else {
                return comp;
            }
        };
        final TreeSet<Graph<VP, Double>.Vertex> vertex_pq = new TreeSet<Graph<VP, Double>.Vertex>(comparator);
        g.getVertexes().forEach((id,v)-> {
            D.put(id, Double.POSITIVE_INFINITY);
            output.insertVertex(v.getProp(), v.getId());
            vertex_pq.add(v);
        });
        /*start at node 1*/
        vertex_pq.remove(g.getVertex(1));
        D.put(1, 0.0);
        vertex_pq.add(g.getVertex(1));

        while (!vertex_pq.isEmpty()) {
            Graph<VP, Double>.Vertex v = vertex_pq.pollFirst();

            v.getEdges().forEach((ep, edge) -> {
                /*get the opposite edge*/
                Graph<VP, Double>.Vertex z = g.getVertex(edge.opposite(v.getId()));
                /*check each edge*/
                if (edge.getProperty() < D.get(z.getId())) {
                    /*remove and re-add to update the key*/
                    vertex_pq.remove(z);
                    D.put(z.getId(), edge.getProperty());
                    P.put(z.getId(), v.getId());
                    vertex_pq.add(z);
                }
            });
        }

        P.forEach((k, v) -> {
            output.insertEdgeUndirected(k, v, g.getEdge(k, v).getProperty());
        });

        return output;
    }

    private static void MergeClusters(HashMap<Integer, HashSet<Integer>> C, Integer u, Integer v) {
        /*merge both into cluster at u. If that is bad size-wise, just swap them*/
        if (C.get(u).size() < C.get(v).size()) {
            Integer c = u;
            u = v;
            v = c;
        }
        /*merge the set itself*/
        HashSet<Integer> newset = C.get(u);
        newset.addAll(C.get(v));
        /*update all the old references*/
        C.get(v).forEach(a -> C.put(a, newset));
    }

    public static <VP> Graph<VP, Double> MSTKruskals(Graph<VP, Double> g) {
        Graph<VP, Double> output = new Graph<>();
        HashMap<Integer, HashSet<Integer>> C = new HashMap<>();
        g.getVertexes().forEach((id,v)-> {
            /*add all vertexes to graph*/
            output.insertVertex(v.getProp(), v.getId());
            /*put each vertex in a unique set*/
            C.put(v.getId(), new HashSet<>());
            C.get(v.getId()).add(v.getId());
        });
        PriorityQueue<Graph<VP, Double>.Edge> PQ = new PriorityQueue<Graph<VP, Double>.Edge>();
        g.getEdges().forEach((ep,e) -> PQ.add(e));

        while (!PQ.isEmpty()) {
            Graph<VP, Double>.Edge e = PQ.poll();
            Integer u = e.getSrc();
            Integer v = e.getDest();
//            if (C.get(u) != C.get(v)) { /*nodes from different cluster*/
            if (!C.get(u).equals(C.get(v))) { /*nodes from different cluster*/
                /*insert this edge to the spanning tree*/
                output.insertEdgeUndirected(u, v, e.getProperty());
                MergeClusters(C, u, v);
            }
        }
        return output;
    }

    public static void PrintPlanarGraph(Graph<XYPair, Double> g) {
        final String edgeVertical = "##";
        final String edgeHorizontal = "##";
        final String vertexLabel = "##";
        final String spacer = "  ";
        Graph<XYPair, Double>.Vertex v = g.getVertex(g.getLastVertexId());
        int w = v.getProp().x;
        int h = v.getProp().y;
        for (int y = 0; y < h; y++) {
            /*print even lines*/
            for (int x = 0; x < w; x++) {
                /*print the vertex*/
                System.out.print(vertexLabel);
                /*print the edge, if it exists*/
                Graph.Vertex v1 = g.getVertex(new XYPair(x, y));
                Graph.Vertex v2 = g.getVertex(new XYPair(x + 1, y));
                if (g.hasEdge(v1.getId(), v2.getId())) {
                    System.out.print(edgeHorizontal);
                } else {
                    System.out.print(spacer);
                }
            }
            /*print the last node on the end, and the newline*/
            System.out.println(vertexLabel);
            /*print edge-only lines*/
            for (int x = 0; x < (w + 1); x++) {
                Graph.Vertex v1 = g.getVertex(new XYPair(x, y));
                Graph.Vertex v2 = g.getVertex(new XYPair(x, y + 1));
                if (g.hasEdge(v1.getId(), v2.getId())) {
                    System.out.print(edgeVertical);
                } else {
                    System.out.print(spacer);
                }
                System.out.print(spacer);
            }
            System.out.println("");
        }
        /*print the last row*/
        int y = h;
        for (int x = 0; x < w; x++) {
                /*print the vertex*/
            System.out.print(vertexLabel);
                /*print the edge, if it exists*/
            Graph.Vertex v1 = g.getVertex(new XYPair(x, y));
            Graph.Vertex v2 = g.getVertex(new XYPair(x + 1, y));
            if (g.hasEdge(v1.getId(), v2.getId())) {
                System.out.print(edgeHorizontal);
            } else {
                System.out.print(spacer);
            }
        }
        System.out.println(vertexLabel);
    }
}
