package com.example.firstaidtent.gemtd;

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
        return new Tower(x, y);
    }

    void update(float deltaTime) {
        attackCD -= deltaTime;

        updateSprite();

        if (currentTarget != null) {
            if (currentTarget.isDead() || targetOutOfRange()) {
                searchTargetClosest();
            } else {
                attack();
            }
        } else {
            searchTargetClosest();
        }
    }

    private void updateSprite() {
        spriteX = (int) Math.round(centerX - sprite.getWidth() / 2);
        spriteY = (int) Math.round(centerY - sprite.getHeight() / 2);
    }

    private void attack() {
        if (attackCD <= 0.00) {
            attackCD = attackRate;

            // Create a projectile, which will automatically move against the current target.
            Projectile p = new Projectile(centerX, centerY, currentTarget);
        }
    }

    // Searches for and sets the towers current target to the closest enemy.
    private void searchTargetClosest() {
        List<Enemy> enemies = Enemy.getEnemies();
        double distance;
        double min_dist = 2000.00; // Is used to store the closest distance to an enemy.
        Enemy target = null;
        Enemy e;

        for (int i = 0; i < enemies.size(); i++) {
            e = enemies.get(i);
            distance = Math.sqrt(Math.pow((e.getCenterX() - centerX), 2) + Math.pow((e.getCenterY() - centerY), 2));

            // Checks if the enemy is closer than the other enemies that have been checked.
            if (distance < min_dist && distance <= attackRange) {
                min_dist = distance;
                target = e;
            }
        }

        // If an enemy was found within the attack range, set the current target to the closest enemy found.
        if (target != null) {
            currentTarget = target;
            return;
        }

        currentTarget = null;
    }

    // Works just like searchTargetClosest(), but searches for the target furthest away, but still within attack range.
    private void searchTargetFurthest() {
        List<Enemy> enemies = Enemy.getEnemies();
        double distance;
        double min_dist = 0.00;
        Enemy target = null;
        Enemy e;

        for (int i = 0; i < enemies.size(); i++) {
            e = enemies.get(i);
            distance = Math.sqrt(Math.pow((e.getCenterX() - centerX), 2) + Math.pow((e.getCenterY() - centerY), 2));
            if (distance > min_dist && distance <= attackRange) {
                min_dist = distance;
                target = e;
            }
        }

        if (target != null) {
            currentTarget = target;
            return;
        }

        currentTarget = null;
    }

    private boolean checkEnemiesInRange() {
        List<Enemy> enemies = Progress.getCurrentWave().getEnemies();
        Enemy e;
        double distance;

        for (int i = 0; i < enemies.size(); i++) {
            e = enemies.get(i);
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

    public static List<Tower> getTowers() {
        return towers;
    }
}