package com.company.graph;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class CellGrid extends JComponent {
    private HashMap<XYPair, Cell> mCells;
    private Graph<XYPair, Double> graph;
    private Color backgroundColor;

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    private int w, h;

    private class Cell {
        public int x, y;
        public Color color;

        public Cell(int x, int y, Color state) {
            this.x = x;
            this.y = y;
            this.color = state;
        }
    }

    public CellGrid(int x, int y, Graph<XYPair, Double> g) {
        super();
        /*data members*/
        this.graph = g;
        this.w = x;
        this.h = y;
        this.mCells = new HashMap<>();
        this.backgroundColor = Color.DARK_GRAY;

        /*insert the vertexes and edges*/
        g.getVertexes().forEach(v -> put(v, Color.WHITE));
        g.getEdges().forEach(e -> put(e, Color.WHITE));

    }

    public void put(Vertex<XYPair, ?> v, Color c) {
        int x = v.getProp().x * 2;
        int y = v.getProp().y * 2;
        mCells.put(new XYPair(x, y), new Cell(x, y, c));
        repaint();
    }

    public void put(Edge<XYPair, ?> e, Color c) {
        Vertex<XYPair, ?> v = graph.getVertex(e.getSrc());
        Vertex<XYPair, ?> u = graph.getVertex(e.getDest());
        int x = (v.getProp().x * 2 + u.getProp().x * 2) / 2;
        int y = (v.getProp().y * 2 + u.getProp().y * 2) / 2;
        mCells.put(new XYPair(x, y), new Cell(x, y, c));
        repaint();
    }

    public void paint(Graphics g) {
        /*use doubles so that the fractional part is preserved when it doesn't divide evenly*/
        double cs_x = getWidth() / (2.0 * w - 1);
        double cs_y = getHeight() / (2.0 * h - 1);
        g.setColor(backgroundColor);
        g.fillRect(0, 0, getWidth(), getHeight());
        mCells.forEach((xyPair, cell) -> {
            g.setColor(cell.color);
            int x = (int) (cell.x * cs_x);
            int y = (int) (cell.y * cs_y);
            /* +1 on cell size so that there are no gaps*/
            g.fillRect(x, y, (int) cs_x + 1, (int) cs_y + 1);
        });
    }

    public void updateColor(XYPair p, Color state) {
        mCells.get(new XYPair(p.x * 2, p.y * 2)).color = state;
        repaint();
    }

    public Color getLabel(XYPair p) {
        return mCells.get(new XYPair(p.x * 2, p.y * 2)).color;
    }

    public enum CellColor {
        UNVISITED,
        BACKTRACK,
        FINAL,
        PROCESSING,
        DONE,
    }

}