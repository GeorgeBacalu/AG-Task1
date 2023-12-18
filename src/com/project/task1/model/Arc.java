package com.project.task1.model;

import java.awt.*;

public class Arc {
    private Point start;
    private Point end;

    public Arc(Point start, Point end) {
        this.start = start;
        this.end = end;
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
        if(start != null) {
            graphics.setColor(Color.BLACK);
            graphics.drawLine(start.x, start.y, end.x, end.y);
        }
    }
}
