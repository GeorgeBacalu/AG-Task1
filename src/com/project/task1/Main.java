package com.project.task1;

import com.project.task1.component.AppPanel;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // start the graphical execution thread either by implementing Runnable or by creating a Thread instance
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Graph Algorithms");
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setSize(600, 600);
            frame.add(new AppPanel());
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
