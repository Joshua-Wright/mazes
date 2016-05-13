package com.company.graph;

import java.lang.annotation.Documented;
import java.util.Collection;

public interface Graph<VP, EP> {

    @Documented
    public @interface VertexId {
    }

    /**
     * @param property property that the new vertex will hold
     * @return the vertex that was inserted
     */
    Vertex<VP, EP> insertVertex(VP property);

    /**
     * @param property property that the new vertex will hold
     * @param id       id for the node (clobbered if it already exists)
     * @return the vertex that was inserted
     */
    Vertex<VP, EP> insertVertex(VP property, Integer id);

    /**
     * @param src      id of the source vertex
     * @param dest     id of the destination vertex
     * @param property property for the edge to hold
     * @return the edge that was inserted
     */
    Edge<VP, EP> insertEdge(@VertexId Integer src, @VertexId Integer dest, EP property);

    /**
     * inserts the same edge in both directions
     *
     * @param src      id of the source vertex
     * @param dest     id of the destination vertex
     * @param property property for the edge to hold
     */
    void insertEdgeUndirected(@VertexId Integer src, @VertexId Integer dest, EP property);

    int getNVertexes();

    int getNEdges();

    int getLastVertexId();

    Edge<VP, EP> getEdge(EdgePair pair);

    Edge<VP, EP> getEdge(@VertexId int src, @VertexId int dest);

    Vertex<VP, EP> getVertex(VP prop);

    Vertex<VP, EP> getVertex(Integer id);

    Collection<Vertex<VP, EP>> getVertexes();

    Collection<Edge<VP, EP>> getEdges();

    class EdgePair implements Comparable {
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

        @Override
        public int compareTo(Object o) {
            EdgePair e = (EdgePair) o;
            if (Integer.compare(src,e.src) != 0) {
                return Integer.compare(src,e.src);
            } else {
                return Integer.compare(dest,e.dest);
            }
        }
    }

}

