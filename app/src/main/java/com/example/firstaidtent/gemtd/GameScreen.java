package com.example.firstaidtent.gemtd;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import com.example.firstaidtent.framework.Game;
import com.example.firstaidtent.framework.Graphics;
import com.example.firstaidtent.framework.Input.TouchEvent;
import com.example.firstaidtent.framework.Screen;
import com.example.firstaidtent.framework.implementation.AndroidGame;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;

class GameScreen extends Screen {
    // The GameState controls what happens in the game and what is drawn.
    private enum GameState {
        Ready, // The state before waves come (building phase).
        Running, // The state during waves.
        Paused, // The state when the game is paused.
        GameOver // The state when you have lost all your lives and get game over.
    }

    private GameState gameState;
    private GameState saveGameState;

    // Variable Setup
    private Background bg;
    private Background border;
    private Paint paint;
    private Paint paint2;
    static Paint paintDebug;

    private int selectTowerTouchId;
    private Point selectTowerStartPoint;
    private Point selectTowerEndPoint;

    private int jumpTouchId;

    private Level currentLevel;
    private Wave currentWave;

    // Timer that keeps track of game-time.
    private double timer;

    // Keeps track of when to spawn another enemy.
    private double spawnTimer;
    private int enemyHealth;

    // Used to print out the game-time
    private int timer_milli;
    private int timer_sec;
    private int timer_min;
    private String timerString;
    private String textMsg;
    private String debugString;
    static String debugString2;
    private float debugFloat = 0.00f;

    private Button btnPlaceGem;
    private Button btnRemoveGem;
    private Button btnNextWave;
    private Button btnPause;

//    private static int[] JUMP_BTN_POS = {700, 350, 65, 65};
//    private static int[] SHOOT_BTN_POS = {600, 350, 65, 65};
//    private static int[] PAUSE_BTN_POS = {0, 0, 35, 35};

    static final int SCREEN_WIDTH = AndroidGame.getFrameBufferWidth();
    static final int SCREEN_HEIGHT = AndroidGame.getFrameBufferHeight();

    // This is the game screen that will be shown after the main menu.
    // The game initializations are done in the separate Initialization class.
    GameScreen(Game game) {
        super(game);

        // Initialize game objects here
        gameState = GameState.Ready;
        saveGameState = GameState.Ready;

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
//        wave.addEnemyType(Enemy.EnemyType.YELLOW_ENEMY, 20);
        wave.addEnemyType(YellowEnemy.class, 20, 50);
        enemyHealth = 50;

        currentLevel.setCurrentWave(wave);
        bg = currentLevel.getBg();
        border = new Background(0, 0, Assets.gameScreenBorder);

        currentWave = currentLevel.getCurrentWave();

        Progress.setCurrentWave(currentWave);
        Progress.setLivesLeft(10);

        // Buttons
        btnPlaceGem  = new PlaceGemButton(Constants.BTN_PLACE_GEM[0], Constants.BTN_PLACE_GEM[1]);
        btnRemoveGem = new RemoveGemButton(Constants.BTN_REMOVE_GEM[0], Constants.BTN_REMOVE_GEM[1]);
        btnNextWave  = new NextWaveButton(Constants.BTN_NEXT_WAVE[0], Constants.BTN_NEXT_WAVE[1]);
        btnPause     = new PauseButton(Constants.BTN_PAUSE[0], Constants.BTN_PAUSE[1]);

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

        selectTowerTouchId = -1;
        selectTowerStartPoint = new Point(-1, -1);
        selectTowerEndPoint = new Point(-1, -1);

        jumpTouchId = -1;

        timer = 0.00;
        timerString = "00:00.000";
        textMsg = "";
        debugString = "HP: " + enemyHealth;
        debugString2 = "Lives: " + Progress.getLivesLeft();

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

    /**
     * The main update method. Updates all the objects in the game.
     * There are several separate update methods in this main method.
     * Depending on the game state, the appropriate update method is called and different things are updated.
     *
     * @param deltaTime
     *        The time since the last update.
     *        Used to ensure the same speed on enemies even with different FPS.
     */
    @Override
    public void update(float deltaTime) {

        if (gameState == GameState.Ready) {
            updateReady(deltaTime);
        } else if (gameState == GameState.Running) {
            updateRunning(deltaTime);
        } else if (gameState == GameState.Paused) {
            updatePaused(deltaTime);
        } else if (gameState == GameState.GameOver) {
            updateGameOver(deltaTime);
        }

    }

    /**
     * Update method for the Ready game state.
     */
    private void updateReady(float deltaTime) {
        debugFloat = deltaTime;

        // Update fewer things while getting ready for a wave.
        updateTouchEvents(deltaTime);
        updateTowers(deltaTime);
        updateProjectiles(deltaTime);
    }

    /**
     * Update method for the Running game state.
     */
    private void updateRunning(float deltaTime) {
        debugFloat = deltaTime;

        // Losing all your lives means game over.
        if (Progress.getLivesLeft() <= 0) {
            gameState = GameState.GameOver;
        }

        // All the game updates happen while a wave is running.
        updateTouchEvents(deltaTime);
        updateTimer(deltaTime);
        updateWaveSpawn(deltaTime);
        updateTowers(deltaTime);
        updateProjectiles(deltaTime);
        updateEnemies(deltaTime);
    }

    /**
     * Updates the in-game timer.
     */
    private void updateTimer(float deltaTime) {
        timer += deltaTime * 1000;
        timer_milli = (int) timer % 1000;
        timer_sec = (int) (timer / 1000) % 60;
        timer_min = (int) (timer / 60000) % 60;
        timerString = String.format(Locale.ENGLISH, "%02d:%02d.%03d", timer_min, timer_sec, timer_milli);
    }

    /**
     * Controls the spawning of enemies and when wave ends.
     */
    private void updateWaveSpawn(float deltaTime) {
        spawnTimer += deltaTime;

        if (spawnTimer >= currentWave.getSpawnRate()) {
            currentWave.spawnNextEnemy();
            spawnTimer = 0.00;
        }

        if (currentWave.getEnemies().size() <= 0 && Enemy.getEnemies().size() <= 0) {
            endWave();
            currentWave.addEnemyType(YellowEnemy.class, 20, enemyHealth);
            enemyHealth += 50;
            debugString = "HP: " + enemyHealth;
        }
    }

    /**
     * Updates all the touch events registered.
     */
    private void updateTouchEvents(float deltaTime) {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        Grid grid = currentLevel.getGrid();

        for (TouchEvent event : touchEvents) {

            // Triggers when touching the screen.
            if (event.type == TouchEvent.TOUCH_DOWN) {

                // The user will be able to select multiple towers at once by holding and dragging the finger across the play area,
                // forming a rectangular area where towers inside are all selected at once.
                if (inBoundsRect(event, Constants.SELECTABLE_AREA) && selectTowerTouchId == -1) {
                    // Saves the touch id to know if the same finger being lifted later
                    // is the same as the one that triggered this touch down event.
                    selectTowerTouchId = event.pointer;

                    // Saves the x and y coordinates of where the touch down event started.
                    selectTowerStartPoint.set(event.x, event.y);
                }//end if
            }//end if TOUCH_DOWN

            // Triggers every time the finger moves across the screen.
            if (event.type == TouchEvent.TOUCH_DRAGGED) {
                // Checks if the triggering touch id is equal to the one used to select towers.
                if (event.pointer == selectTowerTouchId) {
                    // Sets the end point of the rectangle that selects towers.
                    selectTowerEndPoint.set(event.x, event.y);
                }//end if
            }//end if TOUCH_DRAGGED

            // Triggers when lifting the finger from the screen.
            if (event.type == TouchEvent.TOUCH_UP) {

                // Tapping anywhere removes all warning text from the screen.
                // A new text is created further down in this method if needed.
                if (!textMsg.isEmpty()) {
                    textMsg = "";
                }//end if

                // Checks if the triggering touch id is equal to the one used to select towers.
                // Also checks if the user really wanted to select towers.
                // A normal tap on the screen won't trigger the multiple selection of towers
                int selectWidth = Math.abs(selectTowerEndPoint.x - selectTowerStartPoint.x);
                int selectHeight = Math.abs(selectTowerEndPoint.y - selectTowerStartPoint.y);
                if (event.pointer == selectTowerTouchId && (selectTowerEndPoint.x != -1 && selectTowerEndPoint.y != -1) && (selectWidth > 40 || selectHeight > 40)) {
                    Tower.deselectAll();
                    for (Tower t : Tower.getTowers()) {
                        if (inBoundsRect((int) t.getCenterX(), (int) t.getCenterY(), selectTowerStartPoint.x, selectTowerStartPoint.y, selectTowerEndPoint.x, selectTowerEndPoint.y)) {
                            t.select();
                        }//end if
                    }//end for
                } else {
                    if (inBoundsRect(event, Constants.BUILD_AREA)) {
                        Tower.deselectAll();
                        if (btnPlaceGem.isActive()) {
                            if (grid.checkValidBuildLocation(event.x, event.y)) {
                                Tower.createTower(event.x, event.y);
                            } else {
                                if (!textMsg.equals("Invalid build location")) {
                                    textMsg = "Invalid build location";
                                }//end if
                            }//end if
                        } else {
                            for (Tower t : Tower.getTowers()) {
                                if (inBoundsRect(event, Math.round(t.getSpriteX()), Math.round(t.getSpriteY()), t.getSprite().getWidth(), t.getSprite().getHeight())) {
                                    t.select();
                                }//end if
                            }//end for
                        }//end if isActive()
                    }//end if BUILD_AREA

                    for (Button button : Button.getButtons()) {
                        if (button.isVisible() && inBoundsRect(event, button.getX(), button.getY(), button.getWidth(), button.getHeight())) {
                            button.actions(this);
                        }//end if
                    }//end for
                }//end if selectTowerTouchId

                // Reset the select tower id and points
                if (event.pointer == selectTowerTouchId) {
                    selectTowerTouchId = -1;
                    selectTowerStartPoint.set(-1, -1);
                    selectTowerEndPoint.set(-1, -1);
                }//end if
            }//end if TOUCH_UP
        }
    }

    /**
     * Calls each tower's update method to make them attack enemies.
     */
    private void updateTowers(float deltaTime) {

        Iterator<Tower> iter = Tower.getTowers().iterator();
        while (iter.hasNext()) {
            Tower t = iter.next();
            t.update(deltaTime);
            if (t.isRemoved()) {
                iter.remove();
                Tower.getSelectedTowers().remove(t);
            }
        }

    }

    /**
     * Calls each projectile's update method to make the projectiles fly towards its target.
     */
    private void updateProjectiles(float deltaTime) {

        Iterator<Projectile> iter = Projectile.getProjectiles().iterator();
        while (iter.hasNext()) {
            Projectile p = iter.next();
            p.update(deltaTime);
            if (!p.isVisible()) {
                iter.remove();
            }
        }

    }

    /**
     * Calls each enemy's update method to make the enemy run towards the goal.
     */
    private void updateEnemies(float deltaTime) {

        Iterator<Enemy> iter = Enemy.getEnemies().iterator();
        while (iter.hasNext()) {
            Enemy e = iter.next();
            e.update(deltaTime);
            if (e.isDead()) {
                iter.remove();
            }
        }

    }

    /**
     * Checks if a touch event point is inside a rectangular area.
     *
     * @param event
     *        The touch event that is to be checked.<p>
     *
     * @param area
     *        The area in an int array. The array should contain four int with these properties:<p>
     *
     *        area[0] = x of the top-left corner of the rectangle.<p>
     *        area[1] = y of the top-left corner of the rectangle.<p>
     *        area[2] = width of the rectangle.<p>
     *        area[3] = height of the rectangle.
     *
     * @return True if the touch point is inside the area. Otherwise, false.
     */
    private boolean inBoundsRect(TouchEvent event, int[] area) {
        return inBoundsRect(event, area[0], area[1], area[2], area[3]);
    }


    /**
     * Checks if a touch event point is inside a rectangular area. boxX and boxX2 are interchangeable,
     * meaning boxX can be the bottom-right corner and boxX2 can be top-left corner. The same goes for boxY and boxY2.
     *
     * @param x
     *        x of the point to be checked.
     *
     * @param y
     *        y of the point to be checked.
     *
     * @param boxX
     *        x of the top-left corner of the rectangle.
     *
     * @param boxY
     *        y of the top-left corner of the rectangle.
     *
     * @param boxX2
     *        x of the bottom-right corner of the rectangle.
     *
     * @param boxY2
     *        y of the bottom-right corner of the rectangle.
     *
     * @return True if the point is inside the area. Otherwise, false.
     */
    private boolean inBoundsRect(int x, int y, int boxX, int boxY, int boxX2, int boxY2) {
        int temp;

        if (boxX > boxX2) {
            temp = boxX;
            boxX = boxX2;
            boxX2 = temp;
        }

        if (boxY > boxY2) {
            temp = boxY;
            boxY = boxY2;
            boxY2 = temp;
        }

        return ((x > boxX && x < boxX2) && (y > boxY && y < boxY2));
    }

    /**
     * Checks if a touch event point is inside a rectangular area.
     *
     * @param event
     *        The touch event that is to be checked.
     *
     * @param x
     *        x of the top-left corner of the rectangle.
     *
     * @param y
     *        y of the top-left corner of the rectangle.
     *
     * @param width
     *        Width of the rectangle.
     *
     * @param height
     *        Height of the rectangle.
     *
     * @return True if the touch point is inside the area. Otherwise, false.
     */
    private boolean inBoundsRect(TouchEvent event, int x, int y, int width, int height) {
        return (event.x > x && event.x < x + width && event.y > y && event.y < y + height);
    }

    private boolean inBoundsCircle(TouchEvent event, int x, int y, double radius) {
        return Math.sqrt(Math.pow((event.x - x), 2) + Math.pow((event.y - y), 2)) <= radius;
    }

    private void updatePaused(float deltaTime) {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        for (TouchEvent event : touchEvents) {
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

    private void updateGameOver(float deltaTime) {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        for (TouchEvent event : touchEvents) {
            if (event.type == TouchEvent.TOUCH_DOWN) {
                nullify();
                goToMenu();
                return;
            }
        }

    }

    // Draws everything on the screen. The objects will be drawn above each other in the order they are placed here.
    @Override
    public void paint(float deltaTime) {
        switch (gameState) {
            case Ready:
            case Running:
                drawGameUI();
                break;
            case Paused:
                drawGameUI();
                drawPausedUI();
                break;
            case GameOver:
                drawGameOverUI();
                break;
            default:
                drawGameUI();
        }

    }

    private void drawGameUI() {
        Graphics g = game.getGraphics();

        // The Background
        g.drawImage(bg.getImage(), bg.getBgX(), bg.getBgY());

        // The Towers
        List<Tower> towers = Tower.getTowers();
        for (Tower t : towers) {
            t.draw(g);
        }

        // The Tower attack range circle on selected towers
        for (Tower t : Tower.getSelectedTowers()){
            g.drawCircle((int) t.getCenterX(), (int) t.getCenterY(), t.getAttackRange(), Color.WHITE, false);
        }

        // The Projectiles
        List<Projectile> projectiles = Projectile.getProjectiles();
        for (Projectile p : projectiles) {
            g.drawImage(p.getSprite(), p.getSpriteX(), p.getSpriteY());
        }

        // The Enemies
        List<Enemy> enemies = Enemy.getEnemies();
        for (Enemy e : enemies) {
            e.draw(g);
        }

        // Selection Box
        if ((selectTowerStartPoint.x != -1 && selectTowerStartPoint.y != -1) && (selectTowerEndPoint.x != -1 && selectTowerEndPoint.y != -1)) {
            int startPointX = selectTowerStartPoint.x;
            int startPointY = selectTowerStartPoint.y;
            int endPointX = selectTowerEndPoint.x;
            int endPointY = selectTowerEndPoint.y;
            int width = Math.abs(endPointX - startPointX);
            int height = Math.abs(endPointY - startPointY);

            if (startPointX > endPointX) {
                startPointX = endPointX;
            }

            if (startPointY > endPointY) {
                startPointY = endPointY;
            }

            if (width > 40 || height > 40) {
                g.drawRect(startPointX, startPointY, width, height, Color.WHITE, false);
            }
        }

        // The Border
        g.drawImage(border.getImage(), border.getBgX(), border.getBgY());

        // Buttons
        for (Button button : Button.getButtons()) {
            button.draw(g);
        }

        // Text Messages
        g.drawString(debugString, 150, 60, paintDebug);
        g.drawString(debugString2, 150, 110, paintDebug);
        g.drawString(timerString, 450, 60, paintDebug);
        if (!textMsg.isEmpty()) {
            g.drawString(textMsg, 300, 650, paintDebug);
        }


        // Debug Stuff
//            if (!debugString.equals("")) {
//                g.drawString(debugString, 150, 60, paintDebug);
//                g.drawString(debugString2, 150, 110, paintDebug);
//                g.drawString(debugString3, 150, 160, paintDebug);
//                g.drawString("FPS: " + Math.round(1.00f / debugFloat), 700, 60, paintDebug);
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

        // Set all variables to null. You will be recreating them in the constructor. It is important to nullify all static variables.

        paint = null;
        bg = null;
        border = null;
        currentLevel = null;
        currentWave = null;

        Enemy.getEnemies().clear();
        Projectile.getProjectiles().clear();
        Tower.getTowers().clear();
        Button.getButtons().clear();
        Progress.nullify();

        // Call garbage collector to clean up memory.
        System.gc();

    }

    public void endWave() {
        if (gameState == GameState.Running) {
            gameState = GameState.Ready;
            saveGameState = GameState.Ready;
            btnNextWave.setVisible(true);
        }
    }

    public void startWave() {
        if (gameState == GameState.Ready) {
            gameState = GameState.Running;
            saveGameState = GameState.Running;
        }
    }

    @Override
    public void pause() {
        if (gameState == GameState.Ready || gameState == GameState.Running) {
            gameState = GameState.Paused;
        }
    }

    @Override
    public void resume() {
        if (gameState == GameState.Paused) {
            gameState = saveGameState;
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