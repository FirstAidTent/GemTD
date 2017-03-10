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

    // The sprite of the enemy. In case you have an animation, you should set sprite to the
    // first frame of your animation. spriteX and spriteY is where the upper-left corner of your
    // image will be. They are usually (sprite width / 2) from the center.
    // The spriteX and spriteY are also used even if you only have an animation.
    private Image sprite;
    private int spriteX;
    private int spriteY;

    // The width and height of the tower in the grid.
    private int width;
    private int height;

    private static List<Tower> towers = new ArrayList<>();

    Tower(int x, int y) {
        centerX = x;
        centerY = y;

        attackDamage = 1;
        attackRate = 0.10;
        attackRange = 300.00;

        attackCD = 0.00;

        sprite = Assets.towerRed;

        towers.add(this);
    }

    Tower(int x, int y, int damage, double rate, double range) {
        centerX = x;
        centerY = y;

        attackDamage = damage;
        attackRate = rate;
        attackRange = range;

        attackCD = 0.00;

        sprite = Assets.towerRed;

        towers.add(this);
    }

    void update(float deltaTime) {
        attackCD -= deltaTime;

        updateSprite();

        if (currentTarget != null) {
            if (currentTarget.isDead() || targetOutOfRange()) {
                searchTargetClosest();
            } else {
                attack(deltaTime);
            }
        } else {
            searchTargetClosest();
        }
    }

    private void updateSprite() {
        spriteX = (int) Math.round(centerX - sprite.getWidth() / 2);
        spriteY = (int) Math.round(centerY - sprite.getHeight() / 2);
    }

    private void attack(float deltaTime) {
        if (attackCD <= 0.00) {
            attackCD = attackRate;
            Projectile p = new Projectile(centerX, centerY, currentTarget);
        }
    }

    private boolean searchTargetClosest() {
        List<Enemy> enemies = Enemy.getEnemies();
        double distance;
        double min_dist = 2000.00;
        Enemy target = null;
        Enemy e;

        for (int i = 0; i < enemies.size(); i++) {
            e = enemies.get(i);
            distance = Math.sqrt(Math.pow((e.getCenterX() - centerX), 2) + Math.pow((e.getCenterY() - centerY), 2));
            if (distance < min_dist && distance <= attackRange) {
                min_dist = distance;
                target = e;
            }
        }

        if (target != null) {
            currentTarget = target;
            return true;
        }

        currentTarget = null;
        return false;
    }

    private boolean searchTargetFurthest() {
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
            return true;
        }

        currentTarget = null;
        return false;
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