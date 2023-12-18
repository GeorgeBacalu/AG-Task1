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
                    if (isNodePositionValid(event)) {
                        nodes.add(new Node(event.getX(), event.getY(), nodeNumber++));
                    }
                } else {
                    nodes.stream()
                            .filter(node -> isPointOnNode(start, node))
                            .findFirst()
                            .ifPresent(startNode -> nodes.stream()
                                    .filter(node -> isPointOnNode(end, node) && node != startNode)
                                    .findFirst()
                                    .ifPresent(endNode -> {
                                        updateEnds(startNode, endNode);
                                        arcs.add(new Arc(start, end));
                                    }));
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

    private boolean isNodePositionValid(MouseEvent event) {
        int minDistance = 2 * nodeDiameter;
        return nodes.stream().noneMatch(node ->
                event.getX() > node.getX() - minDistance && event.getX() < node.getX() + minDistance &&
                event.getY() > node.getY() - minDistance && event.getY() < node.getY() + minDistance);
    }

    private boolean isPointOnNode(Point point, Node node) {
        int radius = nodeDiameter / 2, centerX = node.getX() + radius, centerY = node.getY() + radius;
        int distance = (int) Math.sqrt(Math.pow(point.x - centerX, 2) + Math.pow(point.y - centerY, 2));
        return distance <= radius;
    }

    private void updateEnds(Node startNode, Node endNode) {
        int radius = nodeDiameter / 2;
        start.x = startNode.getX() + radius;
        start.y = startNode.getY() + radius;
        end.x = endNode.getX() + radius;
        end.y = endNode.getY() + radius;
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
