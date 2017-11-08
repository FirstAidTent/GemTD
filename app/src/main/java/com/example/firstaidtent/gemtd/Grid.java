package com.example.firstaidtent.gemtd;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class Grid {
    private int x;
    private int y;
    private int width;
    private int height;
    private int boxWidth;
    private int boxHeight;

    private List<Point> invalidBuildPoints = new ArrayList<>();

    Grid(int x, int y, int width, int height, int boxW, int boxH) {
        this.x         = x;
        this.y         = y;
        this.width     = width;
        this.height    = height;
        this.boxWidth  = boxW;
        this.boxHeight = boxH;
    }

    boolean addInvalidBuildPointsSquareCenter(int x, int y, int length) {
        return addInvalidBuildPointsRect(x - length / 2, y - length / 2, x + length / 2, y + length / 2);
    }

    boolean addInvalidBuildPointsRect(int x, int y, int x2, int y2) {
        if (x > x2 || y > y2) {
            return false;
        }

        int newX = x;
        int newY = y;
        boolean exists;

        if (x % boxWidth != 0) {
            newX = x + (boxWidth - x % boxWidth);
        }

        if (y % boxHeight != 0) {
            newY = y + (boxHeight - y % boxHeight);
        }

        for (int i = newX; i <= x2; i += boxWidth) {
            for (int j = newY; j <= y2; j += boxHeight) {
                invalidBuildPoints.add(new Point(i, j));
            }
        }

        return true;
    }

    boolean removeInvalidBuildPointsSquareCenter(int x, int y, int length) {
        return removeInvalidBuildPointsRect(x - length / 2, y - length / 2, x + length / 2, y + length / 2);
    }

    boolean removeInvalidBuildPointsRect(int x, int y, int x2, int y2) {
        if (x > x2 || y > y2) {
            return false;
        }

        int newX = x;
        int newY = y;

        if (x % boxWidth != 0) {
            newX = x + (boxWidth - x % boxWidth);
        }

        if (y % boxHeight != 0) {
            newY = y + (boxHeight - y % boxHeight);
        }

        for (int i = newX; i <= x2; i += boxWidth) {
            for (int j = newY; j <= y2; j += boxHeight) {
                Iterator<Point> iter = invalidBuildPoints.iterator();
                while (iter.hasNext()) {
                    Point p = iter.next();
                    if (i == p.x && j == p.y) {
                        iter.remove();
                        break;
                    }
                }
            }
        }

        return true;
    }

    int getClosestBuildPointX(int x) {
        int newX;

        if (x % boxWidth < boxWidth / 2) {
            newX = x - (x % boxWidth);
        } else {
            newX = x + (boxWidth - x % boxWidth);
        }

        if (newX == this.x) {
            newX = this.x + boxWidth;
        }

        if (newX == this.x + width) {
            newX = (this.x + width) - boxWidth;
        }

        return newX;
    }

    int getClosestBuildPointY(int y) {
        int newY;

        if (y % boxHeight < boxHeight / 2) {
            newY = y - (y % boxHeight);
        } else {
            newY = y + (boxHeight - y % boxHeight);
        }

        if (newY == this.y) {
            newY = this.y + boxHeight;
        }

        if (newY == this.y + height) {
            newY = (this.y + height) - boxHeight;
        }

        return newY;
    }

    boolean checkValidBuildLocation(int x, int y) {
        int cx = getClosestBuildPointX(x);
        int cy = getClosestBuildPointY(y);

        for (Point p : invalidBuildPoints) {
            if (cx == p.x && cy == p.y) {
                return false;
            }
        }

        return true;
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

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getBoxWidth() {
        return boxWidth;
    }

    public void setBoxWidth(int boxWidth) {
        this.boxWidth = boxWidth;
    }

    public int getBoxHeight() {
        return boxHeight;
    }

    public void setBoxHeight(int boxHeight) {
        this.boxHeight = boxHeight;
    }

    public List<Point> getInvalidBuildPoints() {
        return invalidBuildPoints;
    }
}
