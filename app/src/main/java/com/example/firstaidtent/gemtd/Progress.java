package com.example.firstaidtent.gemtd;

class Progress {
    private static Level currentLevel;
    private static Wave currentWave;

    static Level getCurrentLevel() {
        return currentLevel;
    }

    static void setCurrentLevel(Level currentLevel) {
        Progress.currentLevel = currentLevel;
    }

    static Wave getCurrentWave() {
        return currentWave;
    }

    static void setCurrentWave(Wave currentWave) {
        Progress.currentWave = currentWave;
    }
}
