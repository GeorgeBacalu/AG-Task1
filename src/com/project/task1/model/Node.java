package com.project.task1.model;

import java.awt.*;

public class Node {
    private int x;
    private int y;
    private int value;

    public Node(int x, int y, int value) {
        this.x = x;
        this.y = y;
        this.value = value;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void drawNode(Graphics graphics, int nodeDiameter) {
        graphics.setColor(Color.RED);
        graphics.setFont(new Font("TimesRoman", Font.BOLD, 15));
        graphics.fillOval(x, y, nodeDiameter, nodeDiameter);
        graphics.setColor(Color.WHITE);
        graphics.drawOval(x, y, nodeDiameter, nodeDiameter);
        graphics.drawString(String.valueOf(value), x + (value < 10 ? 13 : 8), y + 20);
    }
}
