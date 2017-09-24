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

    private GameState gameState = GameState.Ready;

    // Variable Setup
    private Background bg;
    private Background border;
    private Paint paint;
    private Paint paint2;
    static Paint paintDebug;

    private int shootTouchId;
    private int jumpTouchId;

    private Level currentLevel;
    private Wave currentWave;

//    private Tower[] redTower = new Tower[5];

    // Timer that keeps track of game-time.
    private double timer;

    // Keeps track of when to spawn another enemy.
    private double spawnTimer;

    // Used to print out the game-time
    private int timer_milli;
    private int timer_sec;
    private int timer_min;
    private String timerString;
    private String textMsg;

    private Button btnPlaceGem;
    private Button btnPause;

//    private static int[] JUMP_BTN_POS = {700, 350, 65, 65};
//    private static int[] SHOOT_BTN_POS = {600, 350, 65, 65};
//    private static int[] PAUSE_BTN_POS = {0, 0, 35, 35};

    static final int SCREEN_WIDTH = AndroidGame.getFrameBufferWidth();
    static final int SCREEN_HEIGHT = AndroidGame.getFrameBufferHeight();

    GameScreen(Game game) {
        super(game);

        // Initialize game objects here

        // Progress, Lives, Level etc.
        currentLevel = Level.createLevel(1, Assets.gameScreen, new Point(880, -10), new Point(120, 730));
        Progress.setCurrentLevel(currentLevel);
        currentLevel.addTurnPoint(new Point(880, 120));
        currentLevel.addTurnPoint(new Point(440, 120));
        currentLevel.addTurnPoint(new Point(440, 600));
        currentLevel.addTurnPoint(new Point(880, 600));
        currentLevel.addTurnPoint(new Point(880, 360));
        currentLevel.addTurnPoint(new Point(120, 360));

        Wave wave = new Wave(currentLevel);
        wave.addEnemyType(Enemy.EnemyType.YELLOW_ENEMY, 20);

        currentLevel.setCurrentWave(1);
        bg = currentLevel.getBg();
        border = new Background(0, 0, Assets.gameScreenBorder);

        currentWave = currentLevel.getCurrentWave();

        Progress.setCurrentWave(currentWave);
        Progress.setLivesLeft(10);

        // Towers
//        redTower[0] = Tower.createTower(520, 120);
//        redTower[1] = Tower.createTower(480, 160);
//        redTower[2] = Tower.createTower(440, 200);
//        redTower[3] = Tower.createTower(400, 160);
//        redTower[4] = Tower.createTower(360, 120);

        // Buttons
        btnPlaceGem = new PlaceGemButton(1045, 180);
        btnPause = new PauseButton(1230, 0);


        // Grid Invalid Build Points
        Grid grid = new Grid(20, 20, 980, 680, 20, 20);
        currentLevel.setGrid(grid);

        grid.addInvalidBuildPointsRect(860, 60, 900, 180);  // Turnpoint 1
        grid.addInvalidBuildPointsRect(820, 100, 860, 140); // Turnpoint 1

        grid.addInvalidBuildPointsRect(380, 100, 500, 140);  // Turnpoint 2
        grid.addInvalidBuildPointsRect(420, 140, 460, 180); // Turnpoint 2

        grid.addInvalidBuildPointsRect(420, 540, 460, 660);  // Turnpoint 3
        grid.addInvalidBuildPointsRect(460, 580, 500, 620); // Turnpoint 3

        grid.addInvalidBuildPointsRect(820, 580, 940, 620);  // Turnpoint 4
        grid.addInvalidBuildPointsRect(860, 540, 900, 580); // Turnpoint 4

        grid.addInvalidBuildPointsRect(860, 300, 900, 420);  // Turnpoint 5
        grid.addInvalidBuildPointsRect(820, 340, 860, 380); // Turnpoint 5

        grid.addInvalidBuildPointsRect(60, 340, 180, 380);  // Turnpoint 6
        grid.addInvalidBuildPointsRect(100, 380, 140, 420); // Turnpoint 6

        shootTouchId = -1;
        jumpTouchId = -1;

        timer = 0.00;
        timerString = "00:00.000";
        textMsg = "";

        // Defining a paint object for drawing the text.
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

        // We have four separate update methods.
        // Depending on the gameState of the game, the appropriate update method is called.

        if (gameState == GameState.Ready) {
            updateReady(touchEvents);
        }
        if (gameState == GameState.Running) {
            updateRunning(touchEvents, deltaTime);
        }
        if (gameState == GameState.Paused) {
            updatePaused(touchEvents);
        }
        if (gameState == GameState.GameOver) {
            updateGameOver(touchEvents);
        }
    }

    private void updateReady(List touchEvents) {

        // This example starts with a "Ready" screen.
        // When the user touches the screen, the game begins.
        // gameState now becomes GameState.Running.
        // Now the updateRunning() method will be called!


    }

    private void updateRunning(List touchEvents, float deltaTime) {
        timer += deltaTime * 1000;
        timer_milli = (int) timer % 1000;
        timer_sec = (int) (timer / 1000) % 60;
        timer_min = (int) (timer / 60000) % 60;
        timerString = String.format(Locale.ENGLISH, "%02d:%02d.%03d", timer_min, timer_sec, timer_milli);
        spawnTimer += deltaTime;

        updateTouchEvents(touchEvents, deltaTime);

        /*int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = (TouchEvent) touchEvents.get(i);

            if (event.type == TouchEvent.TOUCH_UP) {
                if (!textMsg.isEmpty()) {
                    textMsg = "";
                }

                if (btnPlaceGem.isActive()) {
                    if (inBoundsRect(event, 40, 40, 919, 639)) {
                        if (currentLevel.grid.checkValidBuildLocation(event.x, event.y)) {
                            textMsg = "";
                            Tower.createTower(event.x, event.y);
                        } else {
                            if (!textMsg.equals("Invalid build location")) {
                                textMsg = "Invalid build location";
                            }
                        }
                    }

                }

                for (Button button : Button.getButtons()) {
                    if (button.isVisible() && inBoundsRect(event, button.getX(), button.getY(), button.getWidth(), button.getHeight())) {
                        button.actions(this);
                    }
                }
            }

        }*/

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
        if (Progress.getLivesLeft() <= 0) {
            gameState = GameState.GameOver;
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

    private void updateTouchEvents(List touchEvents, float deltaTime) {
        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = (TouchEvent) touchEvents.get(i);
            Grid grid = currentLevel.getGrid();

            if (event.type == TouchEvent.TOUCH_UP) {
                if (!textMsg.isEmpty()) {
                    textMsg = "";
                }

                if (btnPlaceGem.isActive()) {
                    if (inBoundsRect(event, 40, 40, 919, 639)) {
                        if (grid.checkValidBuildLocation(event.x, event.y)) {
                            textMsg = "";
                            Tower.createTower(event.x, event.y);
                        } else {
                            if (!textMsg.equals("Invalid build location")) {
                                textMsg = "Invalid build location";
                            }
                        }
                    }

                }

                for (Button button : Button.getButtons()) {
                    if (button.isVisible() && inBoundsRect(event, button.getX(), button.getY(), button.getWidth(), button.getHeight())) {
                        button.actions(this);
                    }
                }
            }

        }
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

    private boolean inBoundsGrid(TouchEvent event, Grid grid) {
        return (event.x > grid.getX() && event.x < grid.getX() + grid.getWidth() - 1 && event.y > grid.getY()
                && event.y < grid.getY() + grid.getHeight() - 1);
    }

    private void updatePaused(List touchEvents) {
        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = (TouchEvent) touchEvents.get(i);
            if (event.type == TouchEvent.TOUCH_UP) {
                if (inBoundsRect(event, 440, 180, 400, 100)) {
                    resume();
                }

                if (inBoundsRect(event, 490, 460, 300, 100)) {
                    nullify();
                    goToMenu();
                    return;
                }
            }
        }
    }

    private void updateGameOver(List touchEvents) {
        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = (TouchEvent) touchEvents.get(i);
            if (event.type == TouchEvent.TOUCH_DOWN) {
                if (inBoundsRect(event, 0, 0, 1280, 720)) {
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

    // Draws everything on the screen. The objects will be drawn above each other in the order they
    // are placed here.
    @Override
    public void paint(float deltaTime) {
        if (gameState == GameState.Ready || gameState == GameState.Running) {
            drawRunningUI();
        }

        if (gameState == GameState.Paused) {
            drawRunningUI();
            drawPausedUI();
        }
        if (gameState == GameState.GameOver) {
            drawGameOverUI();
        }

    }

    private void drawReadyUI() {
        Graphics g = game.getGraphics();

        g.drawARGB(155, 0, 0, 0);
        g.drawString("Tap to Start", 400, 240, paint);

    }

    private void drawRunningUI() {
        Graphics g = game.getGraphics();

        // The Background
        g.drawImage(bg.getImage(), bg.getBgX(), bg.getBgY());

        // The Towers
        List<Tower> towers = Tower.getTowers();
        for (int i = 0; i < towers.size(); i++) {
            Tower t = towers.get(i);
            g.drawImage(t.getSprite(), t.getSpriteX(), t.getSpriteY());
//            g.drawCircle((int) t.getCenterX(), (int) t.getCenterY(), t.getAttackRange(), Color.WHITE, false);
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

        // The Border
        g.drawImage(border.getImage(), border.getBgX(), border.getBgY());

        // Buttons
        for (Button button : Button.getButtons()) {
            g.drawImage(button.getImg(), button.getX(), button.getY());
        }

        // Text Messages
        if (!textMsg.isEmpty()) {
            g.drawString(textMsg, 300, 650, paintDebug);
        }

        // Debug Stuff
//            if (!debugString.equals("")) {
//                g.drawString(debugString, 150, 60, paintDebug);
//                g.drawString(debugString2, 150, 110, paintDebug);
//                g.drawString(debugString3, 150, 160, paintDebug);
//                g.drawString("FPS: " + Math.round(1000.00f / debugFloat), 700, 60, paintDebug);
//                g.drawString(timerString, 400, 60, paintDebug);
//                g.drawString(debugStringJoystick, 600, 110, paintDebug);
//            }
    }

    private void drawPausedUI() {
        Graphics g = game.getGraphics();
        // Darken the entire screen so you can display the Paused screen.
        g.drawARGB(155, 0, 0, 0);
        g.drawString("Resume", 640, 220, paint2);
        g.drawString("Menu", 640, 500, paint2);

    }

    private void drawGameOverUI() {
        Graphics g = game.getGraphics();
        g.drawRect(0, 0, 1280, 720, Color.BLACK);
        g.drawString("GAME OVER", 640, 360, paint2);
        g.drawString("Tap to return", 640, 410, paint);
    }

    private void nullify() {

        // Set all variables to null. You will be recreating them in the constructor.

        paint = null;
        bg = null;
        border = null;
        currentLevel = null;
        currentWave = null;

        Enemy.getEnemies().clear();
        Projectile.getProjectiles().clear();
        Tower.getTowers().clear();
        Button.getButtons().clear();

        // Call garbage collector to clean up memory.
        System.gc();

    }

    @Override
    public void pause() {
        if (gameState == GameState.Running) {
            gameState = GameState.Paused;
        }

    }

    @Override
    public void resume() {
        if (gameState == GameState.Paused) {
            gameState = GameState.Running;
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