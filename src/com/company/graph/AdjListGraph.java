package com.company.graph;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AdjListGraph<VP, EP extends Comparable> implements Graph<VP, EP> {
    private int lastVertexId;
    private HashMap<Integer, Vertex<VP, EP>> vertexes;
    private HashMap<VP, Vertex<VP, EP>> vertexes_by_prop;
    private HashMap<EdgePair, Edge<VP, EP>> edges;

    public int getLastVertexId() {
        return lastVertexId;
    }

    public AdjListGraph() {
        this.lastVertexId = 0;
        this.vertexes = new HashMap<>();
        this.edges = new HashMap<>();
        this.vertexes_by_prop = new HashMap<>();
    }

    public Vertex<VP, EP> insertVertex(VP property) {
        lastVertexId++;
        return insertVertex(property, lastVertexId);
    }

    public Vertex<VP, EP> insertVertex(VP property, Integer id) {
        if (id > lastVertexId) {
            lastVertexId = id;
        }
        Vertex<VP, EP> v = new AdjListVertex(property, id);
        vertexes.put(id, v);
        vertexes_by_prop.put(property, v);
        return v;
    }

    public Edge<VP, EP> insertEdge(Integer src, Integer dest, EP property) {
        AdjListEdge e = new AdjListEdge(src, dest, property);
        edges.put(e.getPair(), e);
        ((AdjListVertex) vertexes.get(src)).edges.put(e.getPair(), e);
        ((AdjListVertex) vertexes.get(dest)).edges.put(e.getPair(), e);
        return e;
    }

    public void insertEdgeUndirected(@VertexId Integer src, @VertexId Integer dest, EP property) {
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

    public Edge<VP, EP> getEdge(EdgePair pair) {
        return edges.get(pair);
    }

    public Edge<VP, EP> getEdge(int src, int dest) {
        return getEdge(new EdgePair(src, dest));
    }

    public Vertex<VP, EP> getVertex(Integer id) {
        return vertexes.get(id);
    }

    public Vertex<VP, EP> getVertex(VP prop) {
        return vertexes_by_prop.get(prop);
    }

    public Collection<Vertex<VP, EP>> getVertexes() {
        return vertexes.values();
    }

    public Collection<Edge<VP, EP>> getEdges() {
        return edges.values();
    }

    private class AdjListVertex implements Vertex<VP, EP> {
        private VP prop;
        private int id;
        private HashMap<EdgePair, Edge<VP, EP>> edges;

        public AdjListVertex(VP prop, int id) {
            this.prop = prop;
            this.id = id;
            this.edges = new HashMap<>();
        }

        public int getId() {
            return id;
        }

        @Override
        public Collection<Edge<VP, EP>> getEdges() {
            return edges.values();
        }

        public VP getProp() {
            return prop;
        }

        public void setProp(VP prop) {
            this.prop = prop;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            AdjListVertex vertex = (AdjListVertex) o;

            if (id != vertex.id) return false;
            return prop != null ? prop.equals(vertex.prop) : vertex.prop == null;

        }

        @Override
        public int hashCode() {
            int result = prop != null ? prop.hashCode() : 0;
            result = 31 * result + id;
            result = 31 * result + (edges != null ? edges.hashCode() : 0);
            return result;
        }
    }

    private class AdjListEdge implements Edge<VP, EP> {
        private EdgePair pair;
        private EP property;

        @Override
        public int compareTo(Object o) {
            Edge<?, EP> e = (Edge<?, EP>) o;
            int cmp = property.compareTo(e.getProperty());
            if (cmp == 0) {
                if (e.getSrc() == pair.getSrc() && e.getDest() == pair.getDest()) {
                    return 0;
                } else {
                    return -1; /*TODO: is this the right return value?*/
                }
            } else {
                return cmp;
            }
        }

        public AdjListEdge(int src, int dest, EP property) {
            this.pair = new EdgePair(src, dest);
            this.property = property;
        }

        public EP getProperty() {
            return property;
        }

        public void setProperty(EP property) {
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

            AdjListEdge edge = (AdjListEdge) o;

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

        @Override
        public Vertex<VP, EP> getSrcVertex() {
            return vertexes.get(pair.getSrc());
        }

        @Override
        public Vertex<VP, EP> getDestVertex() {
            return vertexes.get(pair.getDest());
        }

        public EdgePair getPair() {
            return pair;
        }
    }
}
