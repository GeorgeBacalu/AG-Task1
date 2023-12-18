package com.project.task1.component;

import javax.swing.*;

public class GraphTypeComboBox<T> extends JComboBox<T> {
    private boolean isDirected = false;

    public GraphTypeComboBox(T[] items) {
        super(items);
        this.addActionListener(event -> isDirected = this.getSelectedItem() == items[0]);
    }

    public boolean isDirected() {
        return isDirected;
    }
}
