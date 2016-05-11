package com.company.graph;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class CellGrid extends JComponent {
    private Integer cell_size;
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

    public CellGrid(int x, int y, int cell_size, Graph<XYPair, Double> g) {
        super();
        /*data members*/
        this.graph = g;
        this.w = x;
        this.h = y;
        this.mCells = new HashMap<>();
        this.cell_size = cell_size;
        this.backgroundColor = Color.DARK_GRAY;

        /*insert the vertexes and edges*/
        g.getVertexes().forEach((id, v) -> put(v, Color.WHITE));
        g.getEdges().forEach((id, e) -> put(e, Color.WHITE));

    }

    public void put(Graph<XYPair, Double>.Vertex v, Color c) {
        int x = v.getProp().x * 2;
        int y = v.getProp().y * 2;
        mCells.put(new XYPair(x, y), new Cell(x, y, c));
        repaint();
    }

    public void put(Graph<XYPair, Double>.Edge e, Color c) {
        Graph<XYPair, Double>.Vertex v = graph.getVertex(e.getSrc());
        Graph<XYPair, Double>.Vertex u = graph.getVertex(e.getDest());
        int x = (v.getProp().x * 2 + u.getProp().x * 2) / 2;
        int y = (v.getProp().y * 2 + u.getProp().y * 2) / 2;
        mCells.put(new XYPair(x, y), new Cell(x, y, c));
        repaint();
    }

    public void paint(Graphics g) {
        int cs_x = getWidth() / (2*w-1);
        int cs_y = getHeight() / (2*h-1);
//        g.setColor(Color.DARK_GRAY);
        g.setColor(backgroundColor);
        g.fillRect(0, 0, getWidth(), getHeight());
        mCells.forEach((xyPair, cell) -> {
            g.setColor(cell.color);
            g.fillRect(cell.x * cs_x, cell.y * cs_y, cs_x, cs_y);
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