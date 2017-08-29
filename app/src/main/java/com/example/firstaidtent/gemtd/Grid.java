package com.example.firstaidtent.gemtd;

class Grid {
    private static final int WIDTH = 980;
    private static final int HEIGHT = 680;

    private static final int WIDTH_BOX_MAX = 44;
    private static final int HEIGHT_BOX_MAX = 32;

    private static final int BOX_WIDTH = 20;
    private static final int BOX_HEIGHT = 20;

    static int getClosestBuildPointX(int x) {
        int newX;

        if (x % BOX_WIDTH < BOX_WIDTH / 2) {
            newX = x - (x % BOX_WIDTH);
        } else {
            newX = x + (BOX_WIDTH - x % BOX_WIDTH);
        }

        return newX;
    }

    static int getClosestBuildPointY(int y) {
        int newY;

        if (y % BOX_HEIGHT < BOX_HEIGHT / 2) {
            newY = y - (y % BOX_HEIGHT);
        } else {
            newY = y + (BOX_HEIGHT - y % BOX_HEIGHT);
        }

        return newY;
    }

    static boolean checkValidBuildLocation(int x, int y) {
        for (Tower t : Tower.getTowers()) {
            if (x == t.getCenterX() && y == t.getCenterY()) {
                return false;
            }
        }
        return true;
    }
}
