package com.example.firstaidtent.gemtd;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

class Wave {
    private double spawnRate = 1.00; // Default spawn rate in seconds

    private List<Enemy> enemies;
    private Level level;

    Wave(int levelNum) {
        enemies = new ArrayList<>(100);

        level = Level.getLevels().get(levelNum);

        level.getWaves().add(this);
    }

    Wave(Level lvl) {
        enemies = new ArrayList<>(100);

        level = lvl;

        level.getWaves().add(this);
    }

    boolean addEnemy(Enemy e) {
        return enemies.add(e);
    }

    void addEnemyType(Class<? extends Enemy> type, int n) {
        Enemy e;
        Constructor<? extends Enemy> cons;

        try {
            cons = type.getDeclaredConstructor(double.class, double.class);
        } catch (NoSuchMethodException ex) {
            ex.printStackTrace();
            return;
        }

        for (int i = 0; i < n; i++) {
            try {
                e = cons.newInstance(level.getSpawnPoint().x, level.getSpawnPoint().y);
            } catch (Exception ex) {
                ex.printStackTrace();
                return;
            }
            e.setDestPoint(level.getTurnPoints().get(0));
            enemies.add(e);
        }
    }

    void addEnemyType(Class<? extends Enemy> type, int n, int health) {
        Enemy e;
        Constructor<? extends Enemy> cons;

        try {
            cons = type.getDeclaredConstructor(double.class, double.class, int.class);
        } catch (NoSuchMethodException ex) {
            ex.printStackTrace();
            return;
        }

        for (int i = 0; i < n; i++) {
            try {
                e = cons.newInstance(level.getSpawnPoint().x, level.getSpawnPoint().y, health);
            } catch (Exception ex) {
                ex.printStackTrace();
                return;
            }
            e.setDestPoint(level.getTurnPoints().get(0));
            enemies.add(e);
        }
    }

    Enemy removeEnemy(int index) {
        return enemies.remove(index);
    }

    boolean removeEnemy(Enemy e) {
        return enemies.remove(e);
    }

    void removeEnemy(int from, int to) {
        for (int i = from; i <= to; i++) {
            enemies.remove(i);
        }
    }

    void spawnNextEnemy() {
        if (enemies.size() > 0) {
            Enemy e = enemies.get(0);

            Enemy.getEnemies().add(e);
            e.setSpawned(true);
            enemies.remove(0);
        }
    }

    double getSpawnRate() {
        return spawnRate;
    }

    List<Enemy> getEnemies() {
        return enemies;
    }
}
