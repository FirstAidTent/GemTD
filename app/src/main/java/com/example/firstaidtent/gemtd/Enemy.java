package com.example.firstaidtent.gemtd;

import android.graphics.Point;
import android.graphics.Rect;

import com.example.firstaidtent.framework.Image;

import java.util.ArrayList;
import java.util.List;

/*
 * The common Enemy class. All different kind of enemies you want in the game should extend this class.
 * The necessary fields and methods for an enemy are defined in this class.
 * In your new class, you can add more things like resistance and such.
 * Add your new enemy class to the EnemyType Enum.
 */

abstract class Enemy {
    enum EnemyType {
        YELLOW_ENEMY, NULL
    }

    // Enemy Stats
    private int health;

    // The center of where the enemy will appear.
    private double centerX;
    private double centerY;

    // The speed of the enemy. You should provide the speed in pixels per second.
    private double speed;

    // The point the enemy is moving towards.
    private Point destPoint;

    // Indicates which turn point it is currently moving towards in the level.
    private int turnPointNum;

    private boolean isSpawned;
    private boolean isDead;

    // The sprite of the enemy. In case you have an animation, you should set sprite to the
    // first frame of your animation. spriteX and spriteY is where the upper-left corner of your
    // image will be. They are usually (sprite width / 2) from the center.
    // The spriteX and spriteY are also used even if you only have an animation.
    private Image sprite;
    private int spriteX;
    private int spriteY;

    // Hitbox of the enemy.
    private Rect hitbox;

    // Arraylist where all enemies are stored.
    private static List<Enemy> enemies = new ArrayList<>();

    Enemy(double centerX, double centerY, int health, Image sprite) {
        this.centerX = centerX;
        this.centerY = centerY;

        this.health = health;

        destPoint = new Point(0, 0);
        speed = getOriginalSpeed();

        turnPointNum = 0;

        isSpawned = false;
        isDead = false;

        this.sprite = sprite;

        hitbox = new Rect(0, 0, 0, 0);

        updateSprite();
        updateHitbox();
    }

    // The default update method for behavior methods for enemies.
    // Override it if you want to change enemy behavior.
    void update(float deltaTime) {
        if (isSpawned) {
            if (health > 0) {
                move(deltaTime);
                updateSprite();
                updateHitbox();
            } else {
                die();
            }
        }
    }

    // Set the original speed of the enemy. Is used for calculating speed/slow effects.
    abstract double getOriginalSpeed();

    private void move(float deltaTime) {
        if (destPoint != null) {

            // If distance between the enemy and the destination point is less than some pixels,
            // make the enemy move toward the next turn point
            double distance = Math.sqrt(Math.pow((destPoint.x - centerX), 2) + Math.pow((destPoint.y - centerY), 2));
            if (distance <= getOriginalSpeed() * deltaTime) {
                if (destPoint.equals(Progress.getCurrentLevel().getEndPoint())) {
                    die();
                }

                moveTo(destPoint.x, destPoint.y);

                List<Point> turnPoints = Progress.getCurrentLevel().getTurnPoints();
                Point nextPoint;

                turnPointNum++;

                if (turnPointNum >= turnPoints.size()) {
                    nextPoint = Progress.getCurrentLevel().getEndPoint();
                } else {
                    nextPoint = turnPoints.get(turnPointNum);
                }

                destPoint.set(nextPoint.x, nextPoint.y);
            }

            double angle = Math.atan2(destPoint.y - centerY, destPoint.x - centerX);

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

    // The enemies hitbox needs to be updated every time the enemy moves.
    private void updateHitbox() {
        int cornerX = (int) Math.round(centerX - sprite.getWidth() / 2);
        int cornerY = (int) Math.round(centerY - sprite.getHeight() / 2);

        hitbox.set(cornerX, cornerY, cornerX + sprite.getWidth(), cornerY + sprite.getHeight());
    }

    // Instantly moves the enemy to the given x and y coordinates.
    void moveTo(double x, double y) {
        centerX = x;
        centerY = y;
    }

    // What happens when the enemy dies. By default, it gets removed from the enemy group,
    // which stops the updating and painting of the enemy.
    void die() {
        enemies.remove(this);
        isSpawned = false;
        isDead = true;
    }

    // Getters and Setters
    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public Point getDestPoint() {
        return destPoint;
    }

    public void setDestPoint(Point destPoint) {
        this.destPoint.set(destPoint.x, destPoint.y);
    }

    public int getTurnPointNum() {
        return turnPointNum;
    }

    public boolean isSpawned() {
        return isSpawned;
    }

    public void setSpawned(boolean spawned) {
        isSpawned = spawned;
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
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

    public Image getSprite() {
        return sprite;
    }

    public void setSprite(Image sprite) {
        this.sprite = sprite;
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

    public Rect getHitbox() {
        return hitbox;
    }

    public void setHitbox(Rect hitbox) {
        this.hitbox = hitbox;
    }

    public static List<Enemy> getEnemies() {
        return enemies;
    }
}