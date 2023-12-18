package com.project.task1.component;

import javax.swing.*;

public class GraphTypeComboBox<T> extends JComboBox<T> {
    private static GraphTypeComboBox instance = null;
    private boolean isDirected = true;

    private GraphTypeComboBox(T[] items) {
        super(items);
        this.addActionListener(event -> isDirected = this.getSelectedItem() == items[0]);
    }

    public static <T> GraphTypeComboBox getInstance(T[] items) {
        if (instance == null) {
            instance = new GraphTypeComboBox<>(items);
        }
        return instance;
    }

    public boolean isDirected() {
        return isDirected;
    }
}
