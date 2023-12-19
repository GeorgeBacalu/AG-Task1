package com.project.task1.component;

import com.project.task1.util.FileUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class AdjacencyMatrixButton extends JButton {
    private static AdjacencyMatrixButton instance = null;

    private AdjacencyMatrixButton(String text) {
        super(text);
        setFocusable(false);
        addActionListener(event -> {
            setEnabled(false);

            JTextArea textArea = new JTextArea();
            textArea.setFont(new Font("Arial", Font.PLAIN, 20));
            textArea.setEditable(false);
            readFromFile(textArea);

            JButton updateButton = new JButton("Update");
            updateButton.setFocusable(false);
            updateButton.setAlignmentX(CENTER_ALIGNMENT);
            updateButton.addActionListener(e -> readFromFile(textArea));

            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
            panel.add(Box.createRigidArea(new Dimension(0, 5)));
            panel.add(updateButton);
            panel.add(Box.createRigidArea(new Dimension(0, 5)));
            panel.add(new JScrollPane(textArea));

            JFrame frame = new JFrame("Adjacency Matrix");
            frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            frame.setSize(600, 600);
            frame.add(panel);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    AdjacencyMatrixButton.this.setEnabled(true);
                }
            });
        });
    }

    public static AdjacencyMatrixButton getInstance(String text) {
        if (instance == null) {
            instance = new AdjacencyMatrixButton(text);
        }
        return instance;
    }

    private static void readFromFile(JTextArea textArea) {
        try {
            textArea.setText(FileUtil.readFromFile());
        } catch (IOException exception) {
            JOptionPane.showMessageDialog(null, "Error reading from file!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
