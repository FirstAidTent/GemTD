package com.example.firstaidtent.gemtd;

import com.example.firstaidtent.framework.Image;

class Background {

    private int bgX;
    private int bgY;
    private Image image;

    public Background(int x, int y, Image image) {
        bgX = x;
        bgY = y;
        this.image = image;
    }

    public int getBgX() {
        return bgX;
    }

    public void setBgX(int bgX) {
        this.bgX = bgX;
    }

    public int getBgY() {
        return bgY;
    }

    public void setBgY(int bgY) {
        this.bgY = bgY;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}