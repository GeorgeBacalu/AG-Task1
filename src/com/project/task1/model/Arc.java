package com.project.task1.model;

import java.awt.*;

public class Arc {
    private Point start;
    private Point end;
    private final boolean isDirected;
    private static final int ARROW_HEAD_LENGTH = 10;
    private static final int ARROW_HEAD_WIDTH = 5;

    public Arc(Point start, Point end, boolean isDirected) {
        this.start = start;
        this.end = end;
        this.isDirected = isDirected;
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

    public void drawArc(Graphics graphics) {
        if (start != null) {
            graphics.setColor(Color.BLACK);
            graphics.drawLine(start.x, start.y, end.x, end.y);
            if (isDirected) {
                drawArrowHead(graphics);
            }
        }
    }

    private void drawArrowHead(Graphics graphics) {
        double angle = Math.atan2(end.y - start.y, end.x - start.x);
        double x = end.x - ARROW_HEAD_LENGTH * Math.cos(angle);
        double y = end.y - ARROW_HEAD_LENGTH * Math.sin(angle);
        int[] xPoints = new int[3];
        int[] yPoints = new int[3];
        xPoints[0] = end.x;
        yPoints[0] = end.y;
        xPoints[1] = (int) (x + ARROW_HEAD_WIDTH * Math.sin(angle));
        yPoints[1] = (int) (y - ARROW_HEAD_WIDTH * Math.cos(angle));
        xPoints[2] = (int) (x - ARROW_HEAD_WIDTH * Math.sin(angle));
        yPoints[2] = (int) (y + ARROW_HEAD_WIDTH * Math.cos(angle));
        graphics.fillPolygon(xPoints, yPoints, 3);
    }
}
