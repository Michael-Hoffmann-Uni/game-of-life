package jpp.gol.ui;

import javafx.scene.shape.Rectangle;

public class CoordRectangle extends Rectangle {
    private int xCoord;
    private int yCoord;

    public CoordRectangle(double width, double height, int xCoord, int yCoord) {
        super(width, height);
        this.xCoord = xCoord;
        this.yCoord = yCoord;
    }

    public int getXCoord() {
        return xCoord;
    }

    public int getYCoord() {
        return yCoord;
    }
}
