package com.example.firstaidtent.gemtd;

import android.graphics.Color;

import com.example.firstaidtent.framework.Graphics;
import com.example.firstaidtent.framework.Image;

import java.util.ArrayList;
import java.util.List;


class Tower {
    // Tower Stats
    private int attackDamage;
    private double attackRate;
    private double attackRange;

    private Enemy currentTarget = null;
    private double attackCD;

    // The x and y of the center of the tower.
    private double centerX;
    private double centerY;

    // The sprite of the tower. spriteX and spriteY is where the upper-left corner of your
    // image will be. They are usually (sprite width / 2) from the center.
    // spriteX and spriteY will be set automatically and does not need to be changed manually.
    private Image sprite;
    private int spriteX;
    private int spriteY;

    private boolean isRemoved = false;

    private static List<Tower> selectedTowers = new ArrayList<>();
    private static List<Tower> towers = new ArrayList<>();

    private Tower(int x, int y) {
        centerX = x;
        centerY = y;

        attackDamage = 1;
        attackRate = 0.25;
        attackRange = 200.00; // range = (Warcraft range) / 3

        attackCD = 0.00;

        sprite = Assets.towerRed;

        towers.add(this);
    }

    private Tower(int x, int y, int damage, double rate, double range) {
        centerX = x;
        centerY = y;

        attackDamage = damage;
        attackRate = rate;
        attackRange = range;

        attackCD = 0.00;

        sprite = Assets.towerRed;

        towers.add(this);
    }

    static Tower createTower(int x, int y) {
        Level level = Progress.getCurrentLevel();
        int newX = level.getGrid().getClosestBuildPointX(x);
        int newY = level.getGrid().getClosestBuildPointY(y);

        Tower t = new Tower(newX, newY);

        level.getGrid().addInvalidBuildPointsSquareCenter(newX, newY, t.getSprite().getWidth());

        return t;
    }

    synchronized void update(float deltaTime) {
        attackCD -= deltaTime;

        updateSprite();

        if (currentTarget != null) {
            if (currentTarget.isDead() || targetOutOfRange()) {
                currentTarget = searchTarget();
            } else {
                attack();
            }
        } else {
            currentTarget = searchTarget();
        }
    }

    synchronized void draw(Graphics g) {
        g.drawImage(getSprite(), getSpriteX(), getSpriteY());

        if (selectedTowers.contains(this)) {
            g.drawCircle((int) getCenterX(), (int) getCenterY(), getAttackRange(), Color.WHITE, false);
        }
    }

    void select() {
        selectedTowers.add(this);
    }

    static void deselectAll() {
        selectedTowers.clear();
    }

    void remove() {
        Level level = Progress.getCurrentLevel();
        int newX = level.getGrid().getClosestBuildPointX((int) centerX);
        int newY = level.getGrid().getClosestBuildPointY((int) centerY);

        level.getGrid().removeInvalidBuildPointsSquareCenter(newX, newY, sprite.getWidth());

        isRemoved = true;
    }

    protected void attack() {
        if (attackCD <= 0.00) {
            attackCD = attackRate;

            // Create a projectile, which will automatically move against the current target.
            Projectile p = new Projectile(centerX, centerY, currentTarget);
        }
    }

    protected Enemy searchTarget() {
        return searchTargetClosest();
    }

    private void updateSprite() {
        spriteX = (int) Math.round(centerX - sprite.getWidth() / 2);
        spriteY = (int) Math.round(centerY - sprite.getHeight() / 2);
    }

    // Searches for and sets the towers current target to the closest enemy.
    private Enemy searchTargetClosest() {
        List<Enemy> enemies = Enemy.getEnemies();
        double distance;
        double min_dist = 2000.00; // Is used to store the closest distance to an enemy.
        Enemy target = null;

        for (Enemy e : enemies) {
            if (e.isSpawned()) {
                distance = Math.sqrt(Math.pow((e.getCenterX() - centerX), 2) + Math.pow((e.getCenterY() - centerY), 2));

                // Checks if the enemy is closer than the other enemies that have been checked.
                if (distance < min_dist && distance <= attackRange) {
                    min_dist = distance;
                    target = e;
                }
            }
        }

        return target;
    }

    // Works just like searchTargetClosest(), but searches for the target furthest away, but still within attack range.
    private Enemy searchTargetFurthest() {
        List<Enemy> enemies = Enemy.getEnemies();
        double distance;
        double min_dist = 0.00;
        Enemy target = null;

        for (Enemy e : enemies) {
            distance = Math.sqrt(Math.pow((e.getCenterX() - centerX), 2) + Math.pow((e.getCenterY() - centerY), 2));
            if (distance > min_dist && distance <= attackRange) {
                min_dist = distance;
                target = e;
            }
        }

        return target;
    }

    // Checks if there is an enemy within attack range of the tower.
    private boolean checkEnemiesInRange() {
        List<Enemy> enemies = Progress.getCurrentWave().getEnemies();
        double distance;

        for (Enemy e : enemies) {
            distance = Math.sqrt(Math.pow((e.getCenterX() - centerX), 2) + Math.pow((e.getCenterY() - centerY), 2));
            if (distance <= attackRange) {
                return true;
            }
        }
        return false;
    }

    private boolean targetOutOfRange() {
        return Math.sqrt(Math.pow((currentTarget.getCenterX() - centerX), 2) + Math.pow((currentTarget.getCenterY() - centerY), 2)) > attackRange;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public void setAttackDamage(int attackDamage) {
        this.attackDamage = attackDamage;
    }

    public double getAttackRate() {
        return attackRate;
    }

    public void setAttackRate(double attackRate) {
        this.attackRate = attackRate;
    }

    public double getAttackRange() {
        return attackRange;
    }

    public void setAttackRange(double attackRange) {
        this.attackRange = attackRange;
    }

    public Enemy getCurrentTarget() {
        return currentTarget;
    }

    public void setCurrentTarget(Enemy currentTarget) {
        this.currentTarget = currentTarget;
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

    public int getSpriteX() {
        return spriteX;
    }

    public int getSpriteY() {
        return spriteY;
    }

    public boolean isRemoved() {
        return isRemoved;
    }

    public static List<Tower> getSelectedTowers() {
        return selectedTowers;
    }

    public static List<Tower> getTowers() {
        return towers;
    }
}