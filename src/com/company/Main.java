package com.company;

import com.company.ArgParser.ArgParser;
import com.company.graph.*;

import javax.swing.*;

public class Main {
    private static int id = 0;
    private static final Integer KEY_X = id++;
    private static final Integer KEY_Y = id++;
    private static final Integer KEY_STEP_DELAY = id++;
    private static final Integer KEY_FINAL_DELAY = id++;
    private static final Integer KEY_DFS = id++;
    private static final Integer KEY_BFS = id++;
    private static final Integer KEY_FADE = id++;
    private static final Integer KEY_NOFADE = id++;

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
                .putDefault(KEY_STEP_DELAY, "10")
                .putIfPresent("-dfs", KEY_DFS)
                .putIfPresent("-DFS", KEY_DFS)
                .putIfPresent("-bfs", KEY_BFS)
                .putIfPresent("-BFS", KEY_BFS)
                .putIfPresent("-fade", KEY_FADE)
                .putIfPresent("-nofade", KEY_NOFADE)
                .parse(args);
        int w = Integer.valueOf(argParser.getValue(KEY_X));
        int h = Integer.valueOf(argParser.getValue(KEY_Y));
        long stepDelay = Long.valueOf(argParser.getValue(KEY_STEP_DELAY));
        long finalDelay = Long.valueOf(argParser.getValue(KEY_FINAL_DELAY));
        int cell_size = 10;
        boolean bfs = argParser.isPresent(KEY_BFS);
        boolean fade = argParser.isPresent(KEY_FADE);
        if (!argParser.isPresent(KEY_FADE) && !argParser.isPresent(KEY_NOFADE)) {
            fade = true;
        }

        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setBounds(30, 30, (w * 2) * cell_size, (h * 2 + 2) * cell_size);
        window.setVisible(true);
        while (true) {
            Graph<XYPair, Double> g = Algorithm.GeneratePlanarGraph(w, h);
            g = Algorithm.MSTKruskals(g);
            CellGrid cellGrid = new CellGrid(w, h, g);

            window.getContentPane().add(cellGrid);
            window.revalidate();

            if (fade) {
                MazeFadeIn fadeIn = new MazeFadeIn(cellGrid, g).run();
            }


            if (bfs) {
                BFSMazeSolver solver2 = new BFSMazeSolver(cellGrid, g)
                        .setStepDelay(20)
                        .setDoFullMaze(true)
                        .run();
            } else {
                DFSMazeSolver solver1 = new DFSMazeSolver(cellGrid, g)
                        .setStepDelay(stepDelay)
                        .setFinalDelay(finalDelay)
                        .run();
            }
            window.remove(cellGrid);
        }
    }
}
