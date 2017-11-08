package com.example.firstaidtent.gemtd;

class Progress {
    private static Level currentLevel;
    private static Wave currentWave;
    private static int livesLeft;

    static Level getCurrentLevel() {
        return currentLevel;
    }

    static void setCurrentLevel(Level currentLevel) {
        if (Level.getLevels().containsValue(currentLevel)) {
            Progress.currentLevel = currentLevel;
        }
    }

    static Wave getCurrentWave() {
        return currentWave;
    }

    static void setCurrentWave(Wave currentWave) {
        Progress.currentWave = currentWave;
    }

    static int getLivesLeft() {
        return livesLeft;
    }

    static void setLivesLeft(int livesLeft) {
        Progress.livesLeft = livesLeft;
    }

    static void loseLife(int n) {
        setLivesLeft(getLivesLeft() - n);
    }

    static void nullify() {
        currentLevel = null;
        currentWave = null;
        livesLeft = 0;
    }
}
