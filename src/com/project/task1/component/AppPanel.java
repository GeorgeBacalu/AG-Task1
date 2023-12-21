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
    private final GraphTypeComboBox graphTypeComboBox = GraphTypeComboBox.getInstance(new String[]{"directed", "undirected"});
    private boolean isDirected = graphTypeComboBox.isDirected();

    public AppPanel() {
        initializeComponents();
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent event) {
                start = event.getPoint();
                if (event.isShiftDown()) {
                    draggedNode = nodes.stream().filter(node -> isPointOnNode(start, node)).findAny().orElse(null);
                }
                if (event.isAltDown()) {
                    nodes.stream()
                            .filter(node -> isPointOnNode(start, node))
                            .findAny()
                            .ifPresentOrElse(AppPanel.this::deleteNodeAndAdjacentArcs, () -> arcs.stream()
                                    .filter(arc -> isPointNearArc(start, arc))
                                    .findAny()
                                    .ifPresent(AppPanel.this::deleteArc));
                    writeToFile();
                    repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent event) {
                if (!isDragging) {
                    if (isNodePositionValid(event, null) && !event.isAltDown()) {
                        nodes.add(new Node(event.getX(), event.getY(), nodeNumber++));
                        graphTypeComboBox.setEnabled(false);
                        FileUtil.addRowColumnAndInitialize();
                        writeToFile();
                    }
                } else {
                    isDirected = graphTypeComboBox.isDirected();
                    nodes.stream()
                            .filter(node -> isPointOnNode(start, node))
                            .findFirst()
                            .ifPresent(startNode -> nodes.stream()
                                    .filter(node -> isPointOnNode(end, node) && (isDirected || node != startNode))
                                    .findFirst()
                                    .ifPresent(endNode -> {
                                        updateEnds(startNode, endNode);
                                        arcs.add(new Arc(start, end, startNode, endNode, isDirected, nodeDiameter));
                                        FileUtil.updateMatrix(startNode.getValue() - 1, endNode.getValue() - 1, isDirected, true);
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
        add(graphTypeComboBox);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

    private void writeToFile() {
        try {
            FileUtil.writeToFile();
        } catch (IOException exception) {
            JOptionPane.showMessageDialog(null, "Error writing to file!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean isPointOnNode(Point point, Node node) {
        int radius = nodeDiameter / 2, centerX = node.getX() + radius, centerY = node.getY() + radius;
        int distance = (int) Math.sqrt(Math.pow(point.x - centerX, 2) + Math.pow(point.y - centerY, 2));
        return distance <= radius;
    }

    private boolean isPointNearArc(Point point, Arc arc) {
        if (arc.getStartNode() != arc.getEndNode()) {
            Point startLine = arc.getStart(), endLine = arc.getEnd();
            int deltaX = endLine.x - startLine.x, deltaY = endLine.y - startLine.y;
            double projectionFactor = ((point.x - startLine.x) * deltaX + (point.y - startLine.y) * deltaY) / (Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
            Point closestPoint = projectionFactor < 0 ? startLine : projectionFactor > 1 ? endLine :
                    new Point(startLine.x + (int) (projectionFactor * deltaX), startLine.y + (int) (projectionFactor * deltaY));
            return point.distance(closestPoint) < 10;
        }
        int centerX = arc.getStart().x + nodeDiameter / 2, centerY = arc.getStart().y + nodeDiameter / 2, loopDiameter = nodeDiameter;
        return new Rectangle(centerX - loopDiameter / 2, centerY - loopDiameter / 2, loopDiameter, loopDiameter).contains(point);
    }

    private void deleteNodeAndAdjacentArcs(Node nodeToDelete) {
        arcs.removeIf(arc -> arc.getStartNode().getValue() == nodeToDelete.getValue() || arc.getEndNode().getValue() == nodeToDelete.getValue());
        nodes.remove(nodeToDelete);
        nodes.stream().filter(node -> node.getValue() > nodeToDelete.getValue()).forEach(node -> node.setValue(node.getValue() - 1));
        --nodeNumber;
        FileUtil.removeRowColumn(nodeToDelete.getValue() - 1);
    }

    private void deleteArc(Arc arcToDelete) {
        arcs.remove(arcToDelete);
        FileUtil.updateMatrix(arcToDelete.getStartNode().getValue() - 1, arcToDelete.getEndNode().getValue() - 1, isDirected, false);
    }

    private boolean isNodePositionValid(MouseEvent event, Node draggedNode) {
        int minDistance = 2 * nodeDiameter;
        return nodes.stream().filter(node -> node != draggedNode).noneMatch(node ->
                event.getX() > node.getX() - minDistance && event.getX() < node.getX() + minDistance &&
                event.getY() > node.getY() - minDistance && event.getY() < node.getY() + minDistance);
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
