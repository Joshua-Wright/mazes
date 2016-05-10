package com.company;

import com.company.graph.Algorithm;
import com.company.graph.CellGrid;
import com.company.graph.Graph;
import com.company.graph.DFSMazeSolver;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        int w = 50;
        int h = 30;
//        int w = 192/2;
//        int h = 108/2;
        int cell_size = 10;
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setBounds(40, 40, (w * 2) * cell_size, (h * 2 + 2) * cell_size);
        window.setVisible(true);
        while (true) {
            Graph g = Algorithm.GeneratePlanarGraph(w, h);
            g = Algorithm.MSTKruskals(g);

            CellGrid cellGrid = new CellGrid(w, h, cell_size, g);

            window.getContentPane().add(cellGrid);
            window.revalidate();
            DFSMazeSolver solver = new DFSMazeSolver(cellGrid, g);
            solver.setStepDelay(2);
            solver.run();
            window.remove(cellGrid);
        }
    }
}
