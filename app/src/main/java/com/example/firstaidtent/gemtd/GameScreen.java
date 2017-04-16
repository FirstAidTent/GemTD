package com.example.firstaidtent.gemtd;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import com.example.firstaidtent.framework.Game;
import com.example.firstaidtent.framework.Graphics;
import com.example.firstaidtent.framework.Input.TouchEvent;
import com.example.firstaidtent.framework.Screen;
import com.example.firstaidtent.framework.implementation.AndroidGame;

import java.util.List;
import java.util.Locale;

class GameScreen extends Screen {
    private enum GameState {
        Ready, Running, Paused, GameOver
    }

    private GameState state = GameState.Running;

    // Variable Setup
    private Background bg;
    private int livesLeft;
    private Paint paint;
    private Paint paint2;
    static Paint paintDebug;

    private int shootTouchId;
    private int jumpTouchId;

    private Level currentLevel;
    private Wave currentWave;

    private Grid grid;

    private Tower[] redTower = new Tower[5];

    // Timer that keeps track of game-time.
    private double timer;

    // Keeps track of when to spawn another enemy.
    private double spawnTimer;

    // Used to print out the game-time
    private int timer_milli;
    private int timer_sec;
    private int timer_min;
    private String timerString;

//    private static int[] JUMP_BTN_POS = {700, 350, 65, 65};
//    private static int[] SHOOT_BTN_POS = {600, 350, 65, 65};
//    private static int[] PAUSE_BTN_POS = {0, 0, 35, 35};

    static final int SCREEN_WIDTH = AndroidGame.getFrameBufferWidth();
    static final int SCREEN_HEIGHT = AndroidGame.getFrameBufferHeight();

    GameScreen(Game game) {
        super(game);

        // Initialize game objects here
        currentLevel = Level.createLevel(1, Assets.gameScreen, new Point(877, -10), new Point(112, 730));
        Progress.setCurrentLevel(currentLevel);
        currentLevel.addTurnPoint(new Point(877, 112));
        currentLevel.addTurnPoint(new Point(427, 112));
        currentLevel.addTurnPoint(new Point(427, 607));
        currentLevel.addTurnPoint(new Point(877, 607));
        currentLevel.addTurnPoint(new Point(877, 352));
        currentLevel.addTurnPoint(new Point(112, 352));

        Wave wave = new Wave(currentLevel);
        wave.addEnemyType(Enemy.EnemyType.YELLOW_ENEMY, 20);

        currentLevel.setCurrentWave(1);
        bg = currentLevel.getBg();

        currentWave = currentLevel.getCurrentWave();
        Progress.setCurrentWave(currentWave);

        grid = new Grid(game.getGraphics());

        // Towers
        redTower[0] = new Tower(720, 90);
        redTower[1] = new Tower(810, 90);
        redTower[2] = new Tower(720, 157);
        redTower[3] = new Tower(810, 157);
        redTower[4] = new Tower(630, 124);


        shootTouchId = -1;
        jumpTouchId = -1;

        livesLeft = 10;

        timer = 0.00;
        timerString = "00:00.000";

        // Defining a paint object
        paint = new Paint();
        paint.setTextSize(30);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);

        paint2 = new Paint();
        paint2.setTextSize(100);
        paint2.setTextAlign(Paint.Align.CENTER);
        paint2.setAntiAlias(true);
        paint2.setColor(Color.WHITE);

        paintDebug = new Paint();
        paintDebug.setTextSize(30);
        paintDebug.setTextAlign(Paint.Align.CENTER);
        paintDebug.setAntiAlias(true);
        paintDebug.setColor(Color.RED);
    }

    @Override
    public void update(float deltaTime) {
        List touchEvents = game.getInput().getTouchEvents();

        // We have four separate update methods in this example.
        // Depending on the state of the game, we call different update methods.
        // Refer to Unit 3's code. We did a similar thing without separating the
        // update methods.

        if (state == GameState.Ready) {
            updateReady(touchEvents);
        }
        if (state == GameState.Running) {
            updateRunning(touchEvents, deltaTime);
        }
        if (state == GameState.Paused) {
            updatePaused(touchEvents);
        }
        if (state == GameState.GameOver) {
            updateGameOver(touchEvents);
        }
    }

    private void updateReady(List touchEvents) {

        // This example starts with a "Ready" screen.
        // When the user touches the screen, the game begins.
        // state now becomes GameState.Running.
        // Now the updateRunning() method will be called!

        if (touchEvents.size() > 0) {
            state = GameState.Running;
        }
    }

    private void updateRunning(List touchEvents, float deltaTime) {
        timer += deltaTime * 1000;
        timer_milli = (int) timer % 1000;
        timer_sec = (int) (timer / 1000) % 60;
        timer_min = (int) (timer / 60000) % 60;
        timerString = String.format(Locale.ENGLISH, "%02d:%02d.%03d", timer_min, timer_sec, timer_milli);
        spawnTimer += deltaTime;


        /*// All touch inputs are handled here:
        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = (TouchEvent) touchEvents.get(i);

            // Checks if a button is pressed down and applies the appropriate action
            if (event.type == TouchEvent.TOUCH_DOWN) {
                if (inBoundsRect(event, JUMP_BTN_POS[0], JUMP_BTN_POS[1], JUMP_BTN_POS[2], JUMP_BTN_POS[3]) && jumpTouchId == -1) { // JUMP
                    jumpTouchId = event.pointer;
                    if (joystick.getDirection() == Joystick.DIRECTION.DOWN) {
                        tank.jumpPhase();
                    } else {
                        tank.jump();
                    }
                }

                // If the player presses the shoot button, the tank will shoot
                if (inBoundsRect(event, SHOOT_BTN_POS[0], SHOOT_BTN_POS[1], SHOOT_BTN_POS[2], SHOOT_BTN_POS[3]) && shootTouchId == -1) { // SHOOT
                    shootTouchId = event.pointer;
                    tank.startShoot();
                }

                if (inBoundsCircle(event, joystick.getCenterX(), joystick.getCenterY(), joystick.getRadius() + 30) && !joystick.isTouched()) { // Joystick
                    joystick.setTouched(true);
                    joystick.setTouchId(event.pointer);
                    debugStringJoystick = joystick.update(event.x, event.y).toString();
                }
            }

            // If the player releases a button, the appropriate action will stop
            if (event.type == TouchEvent.TOUCH_UP) {
                if (jumpTouchId == event.pointer) { // JUMP
                    jumpTouchId = -1;
                    tank.stopJump();
                }

                if (shootTouchId == event.pointer) { // SHOOT
                    shootTouchId = -1;
                    tank.stopShoot();
                }

                if (inBoundsRect(event, PAUSE_BTN_POS[0], PAUSE_BTN_POS[1], PAUSE_BTN_POS[2], PAUSE_BTN_POS[3])) { // PAUSE
                    pause();
                }

                if (joystick.isTouched() && event.pointer == joystick.getTouchId()) {
                    joystick.setTouched(false);
                    joystick.reset();
                }
            }

            if (event.type == TouchEvent.TOUCH_DRAGGED) {
                if (inBoundsRect(event, JUMP_BTN_POS[0], JUMP_BTN_POS[1], JUMP_BTN_POS[2], JUMP_BTN_POS[3]) && jumpTouchId == -1) { // JUMP
                    jumpTouchId = event.pointer;
                    if (joystick.getDirection() == Joystick.DIRECTION.DOWN) {
                        tank.jumpPhase();
                    } else {
                        tank.jump();
                    }
                } else if (!inBoundsRect(event, JUMP_BTN_POS[0], JUMP_BTN_POS[1], JUMP_BTN_POS[2], JUMP_BTN_POS[3]) && jumpTouchId == event.pointer) {
                    jumpTouchId = -1;
                    tank.stopJump();
                }

                if (inBoundsRect(event, SHOOT_BTN_POS[0], SHOOT_BTN_POS[1], SHOOT_BTN_POS[2], SHOOT_BTN_POS[3]) && shootTouchId == -1) { // SHOOT
                    shootTouchId = event.pointer;
                    tank.startShoot();
                } else if (!inBoundsRect(event, SHOOT_BTN_POS[0], SHOOT_BTN_POS[1], SHOOT_BTN_POS[2], SHOOT_BTN_POS[3]) && shootTouchId == event.pointer) {
                    shootTouchId = -1;
                    tank.stopShoot();
                }

                if (inBoundsCircle(event, joystick.getCenterX(), joystick.getCenterY(), joystick.getRadius() + 30) && !joystick.isTouched()) {
                    joystick.setTouched(true);
                    joystick.setTouchId(event.pointer);
                    debugStringJoystick = joystick.update(event.x, event.y).toString();
                } else if (joystick.isTouched() && event.pointer == joystick.getTouchId()) {
                    debugStringJoystick = joystick.update(event.x, event.y).toString();
                }
            }
        }*/

        // Losing all your lives means game over
        if (livesLeft <= 0) {
            state = GameState.GameOver;
        }

        // Call individual update() methods here.
        // From here on is where all the game updates happen.
        if (spawnTimer >= currentWave.getSpawnRate()) {
            currentWave.spawnNextEnemy();
            spawnTimer = 0.00;
        }

        if (currentWave.getEnemies().size() <= 0 && Enemy.getEnemies().size() <= 0) {
            currentWave.addEnemyType(Enemy.EnemyType.YELLOW_ENEMY, 20);
        }

        updateTowers(deltaTime);
        updateProjectiles(deltaTime);
        updateEnemies(deltaTime);
    }

    private void updateTowers(float deltaTime) {

        List<Tower> towers = Tower.getTowers();
        for (int i = 0; i < towers.size(); i++) {
            Tower t = towers.get(i);
            t.update(deltaTime);
        }

    }

    private void updateProjectiles(float deltaTime) {

        List<Projectile> projectiles = Projectile.getProjectiles();
        for (int i = 0; i < projectiles.size(); i++) {
            Projectile p = projectiles.get(i);
            p.update(deltaTime);
        }

    }

    private void updateEnemies(float deltaTime) {

        List<Enemy> enemies = Enemy.getEnemies();
        for (int i = 0; i < enemies.size(); i++) {
            Enemy e = enemies.get(i);
            e.update(deltaTime);
        }

    }

    private boolean inBoundsRect(TouchEvent event, int x, int y, int width,
                                 int height) {
        return (event.x > x && event.x < x + width - 1 && event.y > y
                && event.y < y + height - 1);
    }

    private boolean inBoundsCircle(TouchEvent event, int x, int y, double radius) {
        return Math.sqrt(Math.pow((event.x - x), 2) + Math.pow((event.y - y), 2)) <= radius;
    }

    private void updatePaused(List touchEvents) {
        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = (TouchEvent) touchEvents.get(i);
            if (event.type == TouchEvent.TOUCH_UP) {
                if (inBoundsRect(event, 0, 0, 800, 240)) {

                    if (!inBoundsRect(event, 0, 0, 35, 35)) {
                        resume();
                    }
                }

                if (inBoundsRect(event, 0, 240, 800, 240)) {
                    nullify();
                    goToMenu();
                }
            }
        }
    }

    private void updateGameOver(List touchEvents) {
        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = (TouchEvent) touchEvents.get(i);
            if (event.type == TouchEvent.TOUCH_DOWN) {
                if (inBoundsRect(event, 0, 0, 800, 480)) {
                    nullify();
                    goToMenu();
                    return;
                }
            }
        }

    }

//    private void updateProjectile() {
//
//        ArrayList projectiles = Projectile.getProjectiles();
//        for (int i = 0; i < projectiles.size(); i++) {
//            Projectile p = (Projectile) projectiles.get(i);
//
//            // A projectile going out of the visible area is removed from the game
//            if (p.isVisible()) {
//                p.update();
//            }
//        }
//
//    }

    // Draws everything on the screen
    @Override
    public void paint(float deltaTime) {
        Graphics g = game.getGraphics();

        // The Background
        g.drawImage(bg.getImage(), bg.getBgX(), bg.getBgY());

        // The Towers
        List<Tower> towers = Tower.getTowers();
        for (int i = 0; i < towers.size(); i++) {
            Tower t = towers.get(i);
            g.drawImage(t.getSprite(), t.getSpriteX(), t.getSpriteY());
            g.drawCircle((int) t.getCenterX(), (int) t.getCenterY(), t.getAttackRange(), Color.WHITE, false);
        }

        // The Projectiles
        List<Projectile> projectiles = Projectile.getProjectiles();
        for (int i = 0; i < projectiles.size(); i++) {
            Projectile p = projectiles.get(i);
            g.drawImage(p.getSprite(), p.getSpriteX(), p.getSpriteY());
        }

        // The Enemies
        List<Enemy> enemies = Enemy.getEnemies();
        for (int i = 0; i < enemies.size(); i++) {
            Enemy e = enemies.get(i);
            if ((e.getSpriteX() > -100 && e.getSpriteX() < SCREEN_WIDTH + 100) &&
                    (e.getSpriteY() > -100 && e.getSpriteY() < SCREEN_HEIGHT + 100) &&
                    e.isSpawned()) {

                g.drawImage(e.getSprite(), e.getSpriteX(), e.getSpriteY());

            }
        }

        //grid.drawGridAll();

//        // Debug Stuff
//        if (!debugString.equals("")) {
//            g.drawString(debugString, 150, 60, paintDebug);
//            g.drawString(debugString2, 150, 110, paintDebug);
//            g.drawString(debugString3, 150, 160, paintDebug);
//            g.drawString("FPS: " + Math.round(1000.00f / debugFloat), 700, 60, paintDebug);
            g.drawString(timerString, 400, 60, paintDebug);
//            g.drawString(debugStringJoystick, 600, 110, paintDebug);
//        }

        // Draws the UI above the other things
//        if (state == GameState.Ready) {
//            drawReadyUI();
//        }
//        if (state == GameState.Running) {
//            drawRunningUI();
//        }
//        if (state == GameState.Paused) {
//            drawPausedUI();
//        }
//        if (state == GameState.GameOver) {
//            drawGameOverUI();
//        }

    }

    private void nullify() {

        // Set all variables to null. You will be recreating them in the
        // constructor.

        paint = null;
        bg = null;
        currentLevel = null;
        currentWave = null;

        Enemy.getEnemies().clear();
//        Projectile.getProjectiles().clear();

        // Call garbage collector to clean up memory.
        System.gc();

    }

    private void drawReadyUI() {
        Graphics g = game.getGraphics();

        g.drawARGB(155, 0, 0, 0);
        g.drawString("Tap to Start", 400, 240, paint);

    }

//    private void drawRunningUI() {
//        Graphics g = game.getGraphics();
//
//        // Draws the Buttons for the UI.
//        // g.drawImage(srcImg, x, y, srcX, srcY, width, height)
//
//        g.drawImage(Assets.button, JUMP_BTN_POS[0], JUMP_BTN_POS[1], 0, 0, JUMP_BTN_POS[2], JUMP_BTN_POS[3]);
//        g.drawImage(Assets.button, SHOOT_BTN_POS[0], SHOOT_BTN_POS[1], 0, 65, SHOOT_BTN_POS[2], SHOOT_BTN_POS[3]);
//        g.drawImage(Assets.button, PAUSE_BTN_POS[0], PAUSE_BTN_POS[1], 0, 195, PAUSE_BTN_POS[2], PAUSE_BTN_POS[3]);
//        g.drawImage(joystick.getSpriteCircle(), joystick.getSpriteCircleX(), joystick.getSpriteCircleY());
//        g.drawImage(joystick.getSpriteMiddle(), joystick.getSpriteMiddleX(), joystick.getSpriteMiddleY());
//    }

    private void drawPausedUI() {
        Graphics g = game.getGraphics();
        // Darken the entire screen so you can display the Paused screen.
        g.drawARGB(155, 0, 0, 0);
        g.drawString("Resume", 400, 165, paint2);
        g.drawString("Menu", 400, 360, paint2);

    }

    private void drawGameOverUI() {
        Graphics g = game.getGraphics();
        g.drawRect(0, 0, 1281, 801, Color.BLACK);
        g.drawString("GAME OVER", 400, 240, paint2);
        g.drawString("Tap to return", 400, 290, paint);

    }

    @Override
    public void pause() {
        if (state == GameState.Running) {
            state = GameState.Paused;
        }

    }

    @Override
    public void resume() {
        if (state == GameState.Paused) {
            state = GameState.Running;
        }
    }

    @Override
    public void dispose() {

    }

    @Override
    public void backButton() {
        pause();
    }

    private void goToMenu() {
        game.setScreen(new MainMenuScreen(game));
    }

}