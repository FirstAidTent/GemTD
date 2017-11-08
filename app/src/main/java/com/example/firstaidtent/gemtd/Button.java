package com.example.firstaidtent.gemtd;

import com.example.firstaidtent.framework.Graphics;
import com.example.firstaidtent.framework.Image;

import java.util.ArrayList;
import java.util.List;

abstract class Button {
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected Image img;

    protected boolean visible;
    protected boolean active;

    protected int priority;
    protected int priorityCounter = 0;

    protected static List<Button> buttons = new ArrayList<>();

    Button(int x, int y, int width, int height, Image img) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.img = img;

        visible = true;
        active = false;

        priority = priorityCounter;
        priorityCounter++;

        buttons.add(this);
    }

    abstract void actions(GameScreen game);

    synchronized void draw(Graphics g) {
        if (isVisible()) {
            g.drawImage(img, x, y);
        }
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

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Image getImg() {
        return img;
    }

    public void setImg(Image img) {
        this.img = img;
    }

    public static List<Button> getButtons() {
        return buttons;
    }
}
