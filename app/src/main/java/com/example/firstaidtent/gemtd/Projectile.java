package com.example.firstaidtent.gemtd;

import android.graphics.Rect;

import com.example.firstaidtent.framework.Image;

import java.util.ArrayList;
import java.util.List;

class Projectile {

    private double centerX;
    private double centerY;
    private int spriteX;
    private int spriteY;

    private Enemy target;

    private double speed;
    private double angle;
    private boolean visible;
    private Image sprite;
    private Rect AoE = new Rect();

    private static List<Projectile> projectiles = new ArrayList<>();

    Projectile(double startX, double startY) {
        centerX = startX;
        centerY = startY;

        target = null;

        speed = 400.00;
        visible = true;
        sprite = Assets.projectile;

        updateSprite();
        updateHitbox();

        projectiles.add(this);
    }

    Projectile(double startX, double startY, Enemy target) {
        this(startX, startY);

        this.target = target;
    }

    public void update(float deltaTime) {
        if ((centerX > -20 && centerX <= GameScreen.SCREEN_WIDTH + 20) && (centerY > -20 && centerY <= GameScreen.SCREEN_HEIGHT + 20)) {
            move(deltaTime);
            updateSprite();
            updateHitbox();
            checkCollisionCenter(deltaTime);
        } else {
            visible = false;
            AoE = null;
        }

        if (!visible) {
            projectiles.remove(this);
        }
    }

    private void move(float deltaTime) {
        if (target != null) {
            double angle = Math.atan2(target.getCenterY() - centerY, target.getCenterX() - centerX);

            double speedX = speed * Math.cos(angle) * deltaTime;
            double speedY = speed * Math.sin(angle) * deltaTime;

            centerX += speedX;
            centerY += speedY;
        }
    }

    private void updateSprite() {
        spriteX = (int) Math.round(centerX - sprite.getWidth() / 2);
        spriteY = (int) Math.round(centerY - sprite.getHeight() / 2);
    }

    private void updateHitbox() {
        int cornerX = (int) Math.round(centerX - sprite.getWidth() / 2);
        int cornerY = (int) Math.round(centerY - sprite.getHeight() / 2);

        AoE.set(cornerX, cornerY, cornerX + sprite.getWidth(), cornerY + sprite.getHeight());
    }


    private void checkCollisionHitbox() {
        if (Rect.intersects(AoE, target.getHitbox())) {
            visible = false;

            if (target.getHealth() > 0) {
                target.setHealth(target.getHealth() - 1);
            }
        }
    }

    private void checkCollisionCenter(float deltaTime) {
        Double distance = Math.sqrt(Math.pow((target.getCenterX() - centerX), 2) + Math.pow((target.getCenterY() - centerY), 2));
        if (distance <= speed * deltaTime / 2.00) {
            visible = false;

            if (target.getHealth() > 0) {
                target.setHealth(target.getHealth() - 1);
            }
        }
    }


    public double getCenterX() {
        return centerX;
    }

    public void setCenterX(double centerX) {
        this.centerX = centerX;
    }

    public double getCenterY() {
        return centerY;
    }

    public void setCenterY(double centerY) {
        this.centerY = centerY;
    }

    public int getSpriteX() {
        return spriteX;
    }

    public void setSpriteX(int spriteX) {
        this.spriteX = spriteX;
    }

    public int getSpriteY() {
        return spriteY;
    }

    public void setSpriteY(int spriteY) {
        this.spriteY = spriteY;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Image getSprite() {
        return sprite;
    }

    public void setSprite(Image sprite) {
        this.sprite = sprite;
    }

    public static List<Projectile> getProjectiles() {
        return projectiles;
    }
}