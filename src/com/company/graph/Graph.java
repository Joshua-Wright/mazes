package com.company.graph;

import java.util.HashMap;
import java.util.Map;

public class Graph<VertexProperty, EdgeProperty> {
    private int lastVertexId;
    private HashMap<Integer, Vertex> vertexes;
    private HashMap<VertexProperty, Vertex> vertexes_by_prop;
    private HashMap<EdgePair, Edge> edges;

    public int getLastVertexId() {
        return lastVertexId;
    }

    public Graph() {
        this.lastVertexId = 0;
        this.vertexes = new HashMap<>();
        this.edges = new HashMap<>();
        this.vertexes_by_prop = new HashMap<>();
    }

    public Vertex insertVertex(VertexProperty property) {
        lastVertexId++;
        return insertVertex(property, lastVertexId);
    }

    public Vertex insertVertex(VertexProperty property, Integer id) {
        if (id > lastVertexId) {
            lastVertexId = id;
        }
        Vertex v = new Vertex(property, id);
        vertexes.put(id, v);
        vertexes_by_prop.put(property, v);
        return v;
    }

    public Edge insertEdge(Integer src, Integer dest, EdgeProperty property) {
        Edge e = new Edge(src, dest, property);
        edges.put(e.getPair(), e);
        vertexes.get(src).edges.put(e.getPair(), e);
        vertexes.get(dest).edges.put(e.getPair(), e);
        return e;
    }

    public void insertEdgeUndirected(Integer src, Integer dest, EdgeProperty property) {
        this.insertEdge(src, dest, property);
        this.insertEdge(dest, src, property);
    }

    public int getNVertexes() {
        return vertexes.size();
    }

    public int getNEdges() {
        return edges.size();
    }

    public boolean hasEdge(EdgePair edgePair) {
        return edges.containsKey(edgePair);
    }

    public boolean hasEdge(int src, int dest) {
        return edges.containsKey(new EdgePair(src, dest));
    }

    public Vertex getVertex(Integer id) {
        return vertexes.get(id);
    }

    public Vertex getVertex(VertexProperty prop) {
        return vertexes_by_prop.get(prop);
    }

    public Edge getEdge(EdgePair pair) {
        return edges.get(pair);
    }

    public Edge getEdge(int src, int dest) {
        return getEdge(new EdgePair(src, dest));
    }

    public Map<Integer, Vertex> getVertexes() {
        return vertexes;
    }

    public Map<EdgePair, Edge> getEdges() {
        return edges;
    }

    public class Vertex {
        private VertexProperty prop;
        private int id;
        private HashMap<EdgePair, Edge> edges;

        public Vertex(VertexProperty prop, int id) {
            this.prop = prop;
            this.id = id;
            this.edges = new HashMap<>();
        }

        public Map<EdgePair, Edge> getEdges() {
            return edges;
        }

        public int getId() {
            return id;
        }

        public VertexProperty getProp() {
            return prop;
        }

        public void setProp(VertexProperty prop) {
            this.prop = prop;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Vertex vertex = (Vertex) o;

            if (id != vertex.id) return false;
            return prop != null ? prop.equals(vertex.prop) : vertex.prop == null;

        }

        @Override
        public int hashCode() {
            return (int) (id ^ (id >>> 32));
        }
    }

    public static class EdgePair {
        private int src;
        private int dest;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            EdgePair edgePair = (EdgePair) o;

            if (src != edgePair.src) return false;
            return dest == edgePair.dest;

        }

        @Override
        public int hashCode() {
            int result = src;
            result = 31 * result + dest;
            return result;
        }

        public EdgePair(int src, int dest) {
            this.src = src;
            this.dest = dest;
        }

        public int getSrc() {
            return src;
        }

        public int getDest() {
            return dest;
        }

        @Override
        public String toString() {
            return "EdgePair{" +
                    "src=" + src +
                    ", dest=" + dest +
                    '}';
        }
    }

    public class Edge implements Comparable {
        private EdgePair pair;
        private EdgeProperty property;

        public Edge(int src, int dest, EdgeProperty property) {
            this.pair = new EdgePair(src, dest);
            this.property = property;
        }

        public EdgeProperty getProperty() {
            return property;
        }

        public void setProperty(EdgeProperty property) {
            this.property = property;
        }

        public int opposite(int a) {
            if (a == pair.getSrc()) {
                return pair.getDest();
            } else {
                return pair.getSrc();
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Edge edge = (Edge) o;

            if (!pair.equals(edge.pair)) return false;
            return property != null ? property.equals(edge.property) : edge.property == null;

        }

        @Override
        public int hashCode() {
            int result = pair.hashCode();
            result = 31 * result + (property != null ? property.hashCode() : 0);
            return result;
        }

        public int getDest() {
            return pair.getDest();
        }

        public int getSrc() {
            return pair.getSrc();
        }

        public EdgePair getPair() {
            return pair;
        }

        @Override
        public int compareTo(Object o) {
            if (property instanceof Comparable && o instanceof Graph.Edge) {
                Graph.Edge e = (Graph.Edge) o;
                if (this.equals(e)) {
                    return 0;
                }
                int comp = ((Comparable) property).compareTo(e.getProperty());
                if (comp != 0) {
                    return comp;
                } else {
                    return -1;
                }
            }
            return 0;
        }
    }
}
