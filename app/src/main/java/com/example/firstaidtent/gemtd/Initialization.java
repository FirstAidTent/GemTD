package com.example.firstaidtent.gemtd;

import android.graphics.Bitmap;

import com.example.firstaidtent.framework.Graphics;
import com.example.firstaidtent.framework.Graphics.ImageFormat;
import com.example.firstaidtent.framework.implementation.AndroidImage;

import java.util.Locale;

class Initialization {
    private static final int[] RED_GEM = Constants.INIT_RED_GEM;

    private Initialization() {
        // Do nothing, since this will be a static only class.
    }

    static void init(Graphics g) {

        // Menu
        Assets.menu = g.newImage("title_screen.png", ImageFormat.ARGB8888);

        // Level Backgrounds
        Assets.gameScreen = g.newImage("game_screen.png", ImageFormat.ARGB8888);
        Assets.gameScreenBorder = g.newImage("game_screen_border.png", ImageFormat.ARGB8888);

        // Enemies
        Assets.yellowEnemy[0] = g.newImage("enemies/enemy1small.png", ImageFormat.ARGB8888);
        for (int i = 1; i < 5; i++) {
            Assets.yellowEnemy[i] = g.newImage(String.format(Locale.ENGLISH, "enemies/enemy%d.png", i + 1), ImageFormat.ARGB8888);
        }

        // Towers
//        Assets.towerRed = g.newImage("towers/tower_red.png", ImageFormat.ARGB8888);
        Bitmap gems = g.newBitmap("towers/gems.png", ImageFormat.ARGB4444);
        Assets.towerRed = new AndroidImage(Bitmap.createBitmap(gems, RED_GEM[0], RED_GEM[1], RED_GEM[2], RED_GEM[3]), ImageFormat.ARGB8888);

        // Projectile
        Assets.projectile = g.newImage("towers/projectiles/projectile.png", ImageFormat.ARGB8888);

        // Button Images
        Assets.btnPlaceGem[0] = g.newImage("buttons/btn_place_gem.png", ImageFormat.ARGB8888);
        Assets.btnPlaceGem[1] = g.newImage("buttons/btn_place_gem_active.png", ImageFormat.ARGB8888);
        Assets.btnRemoveGem = g.newImage("buttons/btn_remove_gem.png", ImageFormat.ARGB8888);
        Assets.btnNextWave = g.newImage("buttons/btn_next_wave.png", ImageFormat.ARGB8888);
        Assets.btnPause = g.newImage("buttons/btn_pause.png", ImageFormat.ARGB8888);

        //This is how you would load a sound if you had one.
        //Assets.click = game.getAudio().createSound("explode.ogg");
    }
}
