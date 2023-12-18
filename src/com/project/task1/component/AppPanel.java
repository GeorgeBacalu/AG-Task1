package com.project.task1.component;

import com.project.task1.model.*;
import com.project.task1.util.FileUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AppPanel extends JPanel {
    private int nodeNumber = 1;
    private final int nodeDiameter = 30;
    private final List<Node> nodes = new ArrayList<>();
    private final List<Arc> arcs = new ArrayList<>();
    private Point start = null;
    private Point end = null;
    private boolean isDragging = false;
    private Node draggedNode = null;
    private final GraphTypeComboBox comboBox = GraphTypeComboBox.getInstance(new String[]{"directed", "undirected"});

    public AppPanel() {
        initializeComponents();
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent event) {
                start = event.getPoint();
                if (event.isShiftDown()) {
                    draggedNode = nodes.stream().filter(node -> isPointOnNode(start, node)).findAny().orElse(null);
                }
            }

            @Override
            public void mouseReleased(MouseEvent event) {
                if (!isDragging) {
                    if (isNodePositionValid(event, null)) {
                        nodes.add(new Node(event.getX(), event.getY(), nodeNumber++));
                        comboBox.setEnabled(false);
                        FileUtil.addRowColumnAndInitialize();
                        writeToFile();
                    }
                } else {
                    boolean isDirected = comboBox.isDirected();
                    nodes.stream()
                            .filter(node -> isPointOnNode(start, node))
                            .findFirst()
                            .ifPresent(startNode -> nodes.stream()
                                    .filter(node -> isPointOnNode(end, node) && (isDirected || node != startNode))
                                    .findFirst()
                                    .ifPresent(endNode -> {
                                        updateEnds(startNode, endNode);
                                        arcs.add(new Arc(start, end, startNode, endNode, isDirected));
                                        FileUtil.updateMatrix(startNode.getValue() - 1, endNode.getValue() - 1, isDirected);
                                        writeToFile();
                                    }));
                }
                start = null;
                isDragging = false;
                draggedNode = null;
                repaint();
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent event) {
                if (draggedNode != null) {
                    if (isNodePositionValid(event, draggedNode)) {
                        updateArcsPositions(event);
                    }
                } else {
                    end = event.getPoint();
                    isDragging = true;
                }
                repaint();
            }
        });
    }

    private void initializeComponents() {
        add(AdjacencyMatrixButton.getInstance("Show adjacency matrix"));
        add(new JLabel("Choose graph type: "));
        add(comboBox);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

    private void writeToFile() {
        try {
            FileUtil.writeToFile();
        } catch (IOException exception) {
            JOptionPane.showMessageDialog(null, "Error reading from file!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean isNodePositionValid(MouseEvent event, Node draggedNode) {
        int minDistance = 2 * nodeDiameter;
        return nodes.stream().filter(node -> node != draggedNode).noneMatch(node ->
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

    private void updateArcsPositions(MouseEvent event) {
        int radius = nodeDiameter / 2;
        draggedNode.setX(event.getX());
        draggedNode.setY(event.getY());
        for (Arc arc : arcs) {
            if (arc.getStartNode() == draggedNode) {
                arc.setStart(new Point(event.getX() + radius, event.getY() + radius));
            } else if (arc.getEndNode() == draggedNode) {
                arc.setEnd(new Point(event.getX() + radius, event.getY() + radius));
            }
        }
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
