package com.example.firstaidtent.gemtd;

import android.graphics.Color;

import com.example.firstaidtent.framework.Graphics;

import java.util.ArrayList;
import java.util.List;

class Gridbox {
    private static final int WIDTH = GameScreen.SCREEN_WIDTH;
    private static final int HEIGHT = GameScreen.SCREEN_HEIGHT;

    private static final int WIDTH_BOX_MAX = 16;
    private static final int HEIGHT_BOX_MAX = 9;

    private List<Integer> gridX = new ArrayList<>();
    private List<Integer> gridY = new ArrayList<>();
    private List<Integer> gridWidth = new ArrayList<>();
    private List<Integer> gridHeight = new ArrayList<>();

    private boolean visible = false;

    private Graphics graphics;

    Gridbox(Graphics g) {
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
        for (int i = 0; i <= WIDTH_BOX_MAX; i++) {
            if (i == WIDTH_BOX_MAX) {
                graphics.drawLine(i * WIDTH / WIDTH_BOX_MAX - 1, 0, i * WIDTH / WIDTH_BOX_MAX - 1, HEIGHT, Color.WHITE);
            } else {
                graphics.drawLine(i * WIDTH / WIDTH_BOX_MAX, 0, i * WIDTH / WIDTH_BOX_MAX, HEIGHT, Color.WHITE);
            }
        }

        for (int i = 0; i <= HEIGHT_BOX_MAX; i++) {
            if (i == HEIGHT_BOX_MAX) {
                graphics.drawLine(0, i * HEIGHT / HEIGHT_BOX_MAX - 1, WIDTH, i * HEIGHT / HEIGHT_BOX_MAX - 1, Color.WHITE);
            } else {
                graphics.drawLine(0, i * HEIGHT / HEIGHT_BOX_MAX, WIDTH, i * HEIGHT / HEIGHT_BOX_MAX, Color.WHITE);
            }
        }
    }

    void removeGridArea(int x, int y, int width, int height) {

    }
}
