package com.example.firstaidtent.gemtd;

import android.graphics.Point;

import com.example.firstaidtent.framework.Image;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class Level {
    private Background bg;
    private Point spawnPoint;
    private Point endPoint;

    private Wave currentWave;

    private List<Point> turnPoints = new ArrayList<>();
    private List<Wave> waves = new ArrayList<>();
    private static HashMap<Integer, Level> levels = new HashMap<>();

    private Level(int levelNum, Image bg, Point spawnPoint, Point endPoint) {
        this.bg = new Background(0, 0, bg);
        this.spawnPoint = spawnPoint;
        this.endPoint = endPoint;

        levels.put(levelNum, this);
    }

    public static Level createLevel(int levelNum, Image bg, Point spawnPoint, Point endPoint) {
//        if (levels.containsKey(levelNum)) {
//            return levels.get(levelNum);
//        }
        return new Level(levelNum, bg, spawnPoint, endPoint);
    }

    public static Level getLevel(int levelNum) {
        return levels.get(levelNum);
    }

    public void moveEnemies(float deltaTime) {
        List<Enemy> enemies = currentWave.getEnemies();

        for (int i = 0; i < enemies.size(); i++) {
            enemies.get(i).update(deltaTime);
        }
    }

    // TODO: Add check for enemy status
    public void checkEnemyStatus() {

    }

    public void addTurnPoint(Point p) {
        turnPoints.add(p);
    }

    public void removeTurnPoint(Point p) {
        turnPoints.remove(p);
    }
    
    public void addWave(Wave w) {
        waves.add(w);
    }

    public void removeWave(Wave w) {
        waves.remove(w);
    }

    public Background getBg() {
        return bg;
    }

    public void setBg(Background bg) {
        this.bg = bg;
    }

    public void setBgImg(Image img) {
        bg.setImage(img);
    }

    public Point getSpawnPoint() {
        return spawnPoint;
    }

    public void setSpawnPoint(Point spawnPoint) {
        this.spawnPoint = spawnPoint;
    }

    public Point getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(Point endPoint) {
        this.endPoint = endPoint;
    }

    public Wave getCurrentWave() {
        return currentWave;
    }

    public void setCurrentWave(Wave currentWave) {
        this.currentWave = currentWave;
    }

    public void setCurrentWave(int waveNum) {
        if (waveNum > 0) {
            this.currentWave = waves.get(waveNum - 1);
        } else {
            this.currentWave = waves.get(0);
        }
    }

    public List<Point> getTurnPoints() {
        return turnPoints;
    }

    public List<Wave> getWaves() {
        return waves;
    }

    public static HashMap<Integer, Level> getLevels() {
        return levels;
    }
}
