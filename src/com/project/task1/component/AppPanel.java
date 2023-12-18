package com.project.task1.component;

import com.project.task1.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class AppPanel extends JPanel {
    private int nodeNumber = 1;
    private final int nodeDiameter = 30;
    private final List<Node> nodes = new ArrayList<>();
    private final List<Arc> arcs = new ArrayList<>();
    Point start = null;
    Point end = null;
    boolean isDragging = false;

    public AppPanel() {
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent event) {
                start = event.getPoint();
            }

            @Override
            public void mouseReleased(MouseEvent event) {
                if (!isDragging) {
                    nodes.add(new Node(event.getX(), event.getY(), nodeNumber++));
                } else {
                    arcs.add(new Arc(start, end));
                }
                start = null;
                isDragging = false;
                repaint();
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent event) {
                end = event.getPoint();
                isDragging = true;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        nodes.forEach(node -> node.drawNode(graphics, nodeDiameter));
        arcs.forEach(arc -> arc.drawArc(graphics));
        if (start != null) {
            graphics.setColor(Color.BLACK);
            graphics.drawLine(start.x, start.y, end.x, end.y);
        }
    }
}
