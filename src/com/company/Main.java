package com.company;

import com.company.ArgParser.ArgParser;
import com.company.graph.Algorithm;
import com.company.graph.CellGrid;
import com.company.graph.Graph;
import com.company.graph.DFSMazeSolver;

import javax.swing.*;

public class Main {
    private static String KEY_X = "x";
    private static String KEY_Y = "y";

    public static void main(String[] args) {
        ArgParser argParser = new ArgParser()
                .putKeyValue("-x", KEY_X)
                .putKeyValue("-y", KEY_Y)
                .putKeyValue("-w", KEY_X)
                .putKeyValue("-h", KEY_Y)
                .putDefault(KEY_X, "50")
                .putDefault(KEY_Y, "30")
                .parse(args);
        int w = Integer.valueOf(argParser.getValue(KEY_X));
        int h = Integer.valueOf(argParser.getValue(KEY_Y));
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
