package com.company.graph;

import java.util.Collection;
import java.util.Map;

public interface Vertex<VP, EP> {

    int getId();

    Collection<Edge<VP, EP>> getEdges();

    VP getProp();

    void setProp(VP prop);
}
