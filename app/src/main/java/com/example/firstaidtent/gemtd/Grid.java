package com.example.firstaidtent.gemtd;

import android.graphics.Color;

import com.example.firstaidtent.framework.Graphics;

import java.util.ArrayList;
import java.util.List;

class Grid {
    private static final int WIDTH = 990;
    private static final int HEIGHT = 720;

    private static final int WIDTH_BOX_MAX = 44;
    private static final int HEIGHT_BOX_MAX = 32;

    private List<Integer> gridX = new ArrayList<>();
    private List<Integer> gridY = new ArrayList<>();
    private List<Integer> gridWidth = new ArrayList<>();
    private List<Integer> gridHeight = new ArrayList<>();

    private boolean visible = false;

    private Graphics graphics;

    Grid(Graphics g) {
        graphics = g;
    }

    void addGridArea(int x, int y, int width, int height) {
        gridX.add(x);
        gridY.add(y);
        gridWidth.add(width);
        gridHeight.add(height);
    }

    void showGrid() {
        visible = true;
    }

    void hideGrid() {
        visible = false;
    }

    void drawGridAll() {
        int lineX;
        int lineY;

        for (int i = 0; i <= WIDTH_BOX_MAX; i++) {
            lineX = i * WIDTH / WIDTH_BOX_MAX;
            lineY = i * HEIGHT / HEIGHT_BOX_MAX;

            if (lineY > 720) {
                lineY = lineY - 720;
            }

            graphics.drawLine(lineX, 0, lineX, HEIGHT, Color.WHITE);
            graphics.drawString(String.valueOf(lineX), lineX, lineY, GameScreen.paintDebug);
        }

        for (int i = 0; i <= HEIGHT_BOX_MAX; i++) {
            lineY = i * HEIGHT / HEIGHT_BOX_MAX;

            graphics.drawLine(0, lineY, WIDTH, lineY, Color.WHITE);
            graphics.drawString(String.valueOf(lineY), 495, lineY, GameScreen.paintDebug);
        }
    }

    void removeGridArea(int x, int y, int width, int height) {

    }
}
