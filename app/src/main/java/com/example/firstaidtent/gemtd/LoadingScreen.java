package com.example.firstaidtent.gemtd;

import android.graphics.Bitmap;

import com.example.firstaidtent.framework.Game;
import com.example.firstaidtent.framework.Graphics;
import com.example.firstaidtent.framework.Graphics.ImageFormat;
import com.example.firstaidtent.framework.Screen;
import com.example.firstaidtent.framework.implementation.AndroidImage;

import java.util.Locale;

import static com.example.firstaidtent.framework.Graphics.ImageFormat.ARGB4444;

class LoadingScreen extends Screen {
    LoadingScreen(Game game) {
        super(game);
    }

    @Override
    public void update(float deltaTime) {
        Graphics g = game.getGraphics();

        // Menu
        Assets.menu = g.newImage("menu.png", ImageFormat.RGB565);

        // Level Backgrounds
        Assets.gameScreen = g.newImage("game_screen.png", ImageFormat.RGB565);

        // Enemies
        Assets.yellowEnemy[0] = g.newImage("enemies/enemy1small.png", ARGB4444);
        for (int i = 1; i < 5; i++) {
            Assets.yellowEnemy[i] = g.newImage(String.format(Locale.ENGLISH, "enemies/enemy%d.png", i + 1), ARGB4444);
        }

        // Towers
//        Assets.towerRed = g.newImage("towers/tower_red.png", ImageFormat.ARGB4444);
        Bitmap gems = g.newBitmap("towers/gems.png", ImageFormat.ARGB4444);
        Assets.towerRed = new AndroidImage(Bitmap.createBitmap(gems, 26, 176, 45, 45), ImageFormat.ARGB4444);

        // Projectile
        Assets.projectile = g.newImage("towers/projectiles/projectile.png", ImageFormat.ARGB4444);

        // Stages
//        Level[] level = new Level[3];
//        level[0] = Level.createLevel(1, Assets.stage_bg[0], new Point(0, 150), new Point(720, 400));
//        Wave wave = new Wave(level[0]);
//        wave.addEnemyType(Enemy.EnemyType.YELLOW_ENEMY, 10);

        //This is how you would load a sound if you had one.
        //Assets.click = game.getAudio().createSound("explode.ogg");

        game.setScreen(new MainMenuScreen(game));

    }

    private void loadGems() {

    }

    @Override
    public void paint(float deltaTime) {
        Graphics g = game.getGraphics();
        g.drawImage(Assets.splash, 0, 0);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public void backButton() {

    }
}