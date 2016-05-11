package com.company.graph;

public interface Edge<VP, EP> extends Comparable {

    int getSrc();

    int getDest();

    Vertex<VP, EP> getSrcVertex();

    Vertex<VP, EP> getDestVertex();

    int opposite(@Graph.VertexId int a);

    void setProperty(EP property);

    EP getProperty();
}
