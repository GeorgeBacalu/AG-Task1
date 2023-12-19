package com.project.task1.model;

import java.awt.*;

public class Arc {
    private Point start;
    private Point end;
    private final Node startNode;
    private final Node endNode;
    private final boolean isDirected;
    private final int nodeDiameter;
    private static final int ARROW_HEAD_LENGTH = 10;
    private static final int ARROW_HEAD_WIDTH = 5;

    public Arc(Point start, Point end, Node startNode, Node endNode, boolean isDirected, int nodeDiameter) {
        this.start = start;
        this.end = end;
        this.startNode = startNode;
        this.endNode = endNode;
        this.isDirected = isDirected;
        this.nodeDiameter = nodeDiameter;
    }

    public Point getStart() {
        return start;
    }

    public void setStart(Point start) {
        this.start = start;
    }

    public Point getEnd() {
        return end;
    }

    public void setEnd(Point end) {
        this.end = end;
    }

    public Node getStartNode() {
        return startNode;
    }

    public Node getEndNode() {
        return endNode;
    }

    public void drawArc(Graphics graphics) {
        if (start != null) {
            graphics.setColor(Color.BLACK);
            if (isDirected && startNode == endNode) {
                int centerX = start.x + nodeDiameter / 2, centerY = start.y + nodeDiameter / 2, loopDiameter = nodeDiameter, loopX = centerX - loopDiameter / 2, loopY = centerY - loopDiameter / 2;
                graphics.drawArc(loopX, loopY, loopDiameter, loopDiameter, 0, 360);
                drawLoopArrowHead(graphics, centerX, centerY);
            } else if (isDirected) {
                graphics.drawLine(start.x, start.y, end.x, end.y);
                drawLineArrowHead(graphics);
            } else if (startNode != endNode) {
                graphics.drawLine(start.x, start.y, end.x, end.y);
            }
        }
    }

    private void drawLineArrowHead(Graphics graphics) {
        double angle = Math.atan2(end.y - start.y, end.x - start.x), x = end.x - ARROW_HEAD_LENGTH * Math.cos(angle), y = end.y - ARROW_HEAD_LENGTH * Math.sin(angle);
        int[] xPoints = new int[]{end.x, (int) (x + ARROW_HEAD_WIDTH * Math.sin(angle)), (int) (x - ARROW_HEAD_WIDTH * Math.sin(angle))};
        int[] yPoints = new int[]{end.y, (int) (y - ARROW_HEAD_WIDTH * Math.cos(angle)), (int) (y + ARROW_HEAD_WIDTH * Math.cos(angle))};
        graphics.fillPolygon(xPoints, yPoints, 3);
    }

    private void drawLoopArrowHead(Graphics graphics, int centerX, int centerY) {
        int arrowPosX = centerX - nodeDiameter / 2, arrowPosY = centerY - nodeDiameter / 2;
        double rotationAngle = Math.PI, arrowTipX = arrowPosX - ARROW_HEAD_LENGTH * Math.cos(rotationAngle), arrowTipY = arrowPosY - ARROW_HEAD_LENGTH * Math.sin(rotationAngle);
        int[] xPoints = new int[]{arrowPosX, (int) (arrowTipX + ARROW_HEAD_WIDTH * Math.sin(rotationAngle)), (int) (arrowTipX - ARROW_HEAD_WIDTH * Math.sin(rotationAngle))};
        int[] yPoints = new int[]{arrowPosY, (int) (arrowTipY - ARROW_HEAD_WIDTH * Math.cos(rotationAngle)), (int) (arrowTipY + ARROW_HEAD_WIDTH * Math.cos(rotationAngle))};
        graphics.fillPolygon(xPoints, yPoints, 3);
    }
}
