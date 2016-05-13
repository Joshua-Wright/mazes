package com.company.graph;

import java.util.*;

public class Algorithm {

    public static Graph<XYPair, Double> GeneratePlanarGraph(int w, int h) {
        Graph<XYPair, Double> output = new AdjListGraph<>();
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                Vertex v1 = output.insertVertex(new XYPair(x, y));
                if (x > 0) {
                    Vertex v2 = output.getVertex(new XYPair(x - 1, y));
                    output.insertEdgeUndirected(v1.getId(), v2.getId(), Math.random());
                }
                if (y > 0) {
                    Vertex v2 = output.getVertex(new XYPair(x, y - 1));
                    output.insertEdgeUndirected(v1.getId(), v2.getId(), Math.random());
                }
            }
        }
        return output;
    }

    public static <VP, EP extends Comparable> Graph<VP, EP> RandomBFS(Graph<VP, EP> g) {
        Graph<VP, EP> output = new AdjListGraph<VP, EP>();
        HashMap<Integer, Integer> parentMap = new HashMap<>();
        TreeMap<Double, Vertex> toProcess = new TreeMap<>();
        HashMap<Vertex, Boolean> processed = new HashMap<>();
        g.getVertexes().forEach(v -> {
            processed.put(v, false);
            output.insertVertex(v.getProp(), v.getId());
        });
        /*start at random vertex*/
        int startV;
        do {
            startV = (int) Math.ceil(Math.random() * g.getLastVertexId());
        } while (g.getVertex(startV) == null);
        toProcess.put(0.0, g.getVertex(startV));

        while (!toProcess.isEmpty()) {
            /*get a random thing to look at*/
            Vertex<?, ?> v = toProcess.pollFirstEntry().getValue();

            /*mark this node as done*/
            processed.put(v, true);

            /*add the adjacent unexplored nodes*/
            v.getEdges().forEach(e -> {
                Vertex w = g.getVertex(e.opposite(v.getId()));
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

    public static <VP> AdjListGraph<VP, Double> MSTPrimJarnik(Graph<VP, Double> g) {
        AdjListGraph<VP, Double> output = new AdjListGraph<>();
        final HashMap<Integer, Double> D = new HashMap<>();
        final HashMap<Integer, Integer> P = new HashMap<>();
        Comparator<Vertex> comparator = (Vertex v1, Vertex v2) -> {
            /*make sure that unequal vertexes with equal distances don't compare equal*/
            int comp = D.get(v1.getId()).compareTo(D.get(v2.getId()));
            if (comp == 0 && !v1.equals(v2)) {
                return -1;
            } else {
                return comp;
            }
        };
        final TreeSet<Vertex<VP, Double>> vertex_pq = new TreeSet<>(comparator);
        g.getVertexes().forEach(v -> {
            D.put(v.getId(), Double.POSITIVE_INFINITY);
            output.insertVertex(v.getProp(), v.getId());
            vertex_pq.add(v);
        });
        /*start at node 1*/
        vertex_pq.remove(g.getVertex(1));
        D.put(1, 0.0);
        vertex_pq.add(g.getVertex(1));

        while (!vertex_pq.isEmpty()) {
            Vertex<VP, Double> v = vertex_pq.pollFirst();

            v.getEdges().forEach(e -> {
                /*get the opposite edge*/
                Vertex<VP, Double> z = g.getVertex(e.opposite(v.getId()));
                /*check each edge*/
                if (e.getProperty() < D.get(z.getId())) {
                    /*remove and re-add to update the key*/
                    vertex_pq.remove(z);
                    D.put(z.getId(), e.getProperty());
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

    private static void MergeClusters(HashMap<Integer, HashSet<Integer>> C, int u, int v) {
        /*merge both into cluster at u. If that is bad size-wise, just swap them*/
        if (C.get(u).size() < C.get(v).size()) {
            int c = u;
            u = v;
            v = c;
        }
        /*merge the set itself*/
        HashSet<Integer> newset = C.get(u);
        newset.addAll(C.get(v));
        /*update all the old references*/
        C.get(v).forEach(a -> C.put(a, newset));
    }

    public static <VP> AdjListGraph<VP, Double> MSTKruskals(Graph<VP, Double> g) {
        AdjListGraph<VP, Double> output = new AdjListGraph<>();
        HashMap<Integer, HashSet<Integer>> C = new HashMap<>();
        g.getVertexes().forEach(v -> {
            /*add all vertexes to graph*/
            output.insertVertex(v.getProp(), v.getId());
            /*put each vertex in a unique set*/
            C.put(v.getId(), new HashSet<>());
            C.get(v.getId()).add(v.getId());
        });
        PriorityQueue<Edge<?, Double>> PQ = new PriorityQueue<>();
        g.getEdges().forEach(PQ::add);

        while (!PQ.isEmpty()) {
            Edge<?, Double> e = PQ.poll();
            int u = e.getSrc();
            int v = e.getDest();
            if (C.get(u) != C.get(v)) { /*nodes from different cluster*/
                /*insert this edge to the spanning tree*/
                output.insertEdgeUndirected(u, v, e.getProperty());
                MergeClusters(C, u, v);
            }
        }
        return output;
    }
}
