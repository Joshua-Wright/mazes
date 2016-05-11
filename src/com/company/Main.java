package com.company;

import com.company.ArgParser.ArgParser;
import com.company.graph.*;

import javax.swing.*;

public class Main {
    private static String KEY_X = "X";
    private static String KEY_Y = "Y";
    private static String KEY_STEP_DELAY = "STEP_DELAY";
    private static String KEY_FINAL_DELAY = "FINAL_DELAY";

    public static void main(String[] args) {
        ArgParser argParser = new ArgParser()
                .putKeyValue("-x", KEY_X)
                .putKeyValue("-y", KEY_Y)
                .putKeyValue("-w", KEY_X)
                .putKeyValue("-h", KEY_Y)
                .putDefault(KEY_X, "50")
                .putDefault(KEY_Y, "30")
                .putKeyValue("-sd", KEY_STEP_DELAY)
                .putKeyValue("-fd", KEY_FINAL_DELAY)
                .putDefault(KEY_FINAL_DELAY, "1000")
                .putDefault(KEY_STEP_DELAY, "20")
                .parse(args);
        int w = Integer.valueOf(argParser.getValue(KEY_X));
        int h = Integer.valueOf(argParser.getValue(KEY_Y));
        long stepDelay = Long.valueOf(argParser.getValue(KEY_STEP_DELAY));
        long finalDelay = Long.valueOf(argParser.getValue(KEY_FINAL_DELAY));
        int cell_size = 10;
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setBounds(0,0, (w * 2) * cell_size, (h * 2 + 2) * cell_size);
        window.setVisible(true);
        while (true) {
            Graph<XYPair, Double> g = Algorithm.GeneratePlanarGraph(w, h);
            g = Algorithm.MSTKruskals(g);

            CellGrid cellGrid = new CellGrid(w, h, g);

            window.getContentPane().add(cellGrid);
            window.revalidate();
            DFSMazeSolver solver = new DFSMazeSolver(cellGrid, g)
                    .setStepDelay(stepDelay)
                    .setFinalDelay(finalDelay)
                    .run();
            window.remove(cellGrid);
        }
    }
}
