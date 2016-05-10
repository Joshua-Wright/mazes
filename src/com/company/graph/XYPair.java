package com.company.graph;

public class XYPair {
    public int x, y;

    public XYPair(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        XYPair xyPair = (XYPair) o;

        if (x != xyPair.x) return false;
        return y == xyPair.y;

    }

    @Override
    public String toString() {
        return "XYPair{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }
}
