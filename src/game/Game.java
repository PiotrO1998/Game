package game;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * Game instance, actual game play and rendering takes place in this class.
 * Once the Game starts, this Class is responsible for rendering everything exactly as the Map intended.
 *
 * @author Pedro Caetano
 * @author Artur Zinnurov
 * @author James Henry
 * @author Piotr Obara
 * @version 9.0
 */
public class Game extends Application {
    private static final int WINDOW_WIDTH = 525;
    private static final int WINDOW_HEIGHT = 540;
    private static int GRID_CELL_WIDTH = 60;
    private static int GRID_CELL_HEIGHT = 60;
    private static int FOV = 9; //must be odd could be changed to see difference
    private static MediaPlayer musicPlayer;
    // load sounds
    private final File gameMusicFile = new File("src/audio/music.mp3");
    private final Media gameMusic = new Media(gameMusicFile.toURI().toString());
    private final GameSounds sound = new GameSounds();
    // X and Y location of player
    private int playerX;
    private int playerY;
    private int currentTick = 0;
    // Rotation of player
    private int playerRotation = 0;
    // Loaded images
    private Image player;
    private Image playerFlip;
    private Image ground;
    private Image snowGround;
    private Image wall;
    private Image snowWall;
    private Image wind;
    private Image rain;
    private Image redDoor;
    private Image blueDoor;
    private Image greenDoor;
    private Image tokenDoor;
    private Image redDoorOpen;
    private Image blueDoorOpen;
    private Image greenDoorOpen;
    private Image tokenDoorOpen;
    private Image enemyStraight;
    private Image enemyDumb;
    private Image enemySmart;
    private Image enemyWall;
    private Image token;
    private Image sword;
    private Image rainUmbrella;
    private Image windJacket;
    private Image teleporter;
    private Image target;
    private Image redKey;
    private Image greenKey;
    private Image blueKey;
    private Image ice;
    private Image iceCubes;
    private Image pickaxe;
    private Log log;
    private Map currentMap;
    private Map reducedMap;
    private GameManager gameManager;
    private Player currentPlayer;
    private Image umbrellaOn;
    private Image jacketOn;
    private Image jacketOnFlip;
    private Canvas canvas;
    private BorderPane pane;
    private Stage primaryStage;
    //important variables for level management
    private int levelNumber;
    private Map[] mapsArray;
    private int stepCount = -1;
    private String season = "";

    /**
     * Initialises javaFX and starts the game.
     *
     * @param primaryStage Stage that game will be rendered in
     */
    public void start(Stage primaryStage) {
        log = new Log(getClass().getSimpleName());
        // Build the GUI
        BorderPane root = buildGUI();
        this.pane = root;
        Scene scene = new Scene(root, 594, 610);

        // Load images
        loadImages();
        // Register an event handler for key presses
        scene.addEventFilter(KeyEvent.KEY_PRESSED, this::processKeyEvent);
        // Display the scene on the stage
        drawGame();
        primaryStage.setScene(scene);
        this.primaryStage = primaryStage;
        primaryStage.show();
       }

    /**
     * Method used to start the game from an exterior Class.
     *
     * @param primaryStage Stage that game will be rendered in
     * @param profile      User Profile playing the game
     * @param maps         Array of Maps that will be played
     * @param currentLevel Current level within Array of Maps
     * @param fov          FOV of the Game, how much of the Map is shown around the Player
     */
    public void playGame(Stage primaryStage, Profile profile, Map[] maps, int currentLevel, int fov) {
        FOV = fov;
        //adjust GRID_CELL_* to fit FOV and Window
        GRID_CELL_WIDTH = WINDOW_WIDTH / FOV;
        GRID_CELL_HEIGHT = WINDOW_HEIGHT / FOV;
        //store variables required for running of the game
        mapsArray = maps;
        levelNumber = currentLevel;
        //init and start game
        initGame(profile, maps[levelNumber]);
        start(primaryStage);
    }

    /**
     * Initialises a new level.
     *
     * @param profile User profile playing the game
     * @param map     Map to play
     */
    private void initGame(Profile profile, Map map) {
        Game.musicPlayer = new MediaPlayer(gameMusic);
        musicPlayer.setVolume(0.3);
        musicPlayer.play();
        //set variables global to the Class
        currentMap = map;
        gameManager = new GameManager();
        currentPlayer = new Player(profile, gameManager);
        //load step count if required
        if (stepCount != -1) {
            currentPlayer.setSteps(stepCount - 1);
        } else {
            stepCount = 0;
        }
        //setup base references for inter class communication to work
        gameManager.setCurrentMap(currentMap);
        gameManager.setCurrentPlayer(currentPlayer);
        Cell currentCell = currentMap.getSpawnCell();
        gameManager.updatePlayerLocation(currentCell.getX(), currentCell.getY());
        playerX = currentPlayer.getX();
        playerY = currentPlayer.getY();
    }

    /**
     * Loading level.
     *
     * @param c Cells of of level to be load.
     */
    private void initGame(Cell[][] c) {
        Game.musicPlayer = new MediaPlayer(gameMusic);
        musicPlayer.setVolume(0.3);
        musicPlayer.play();
        currentMap.setMap(c);
        currentTick = 0;
        gameManager = new GameManager();
        currentPlayer = currentMap.getPlayer();
        gameManager.setCurrentMap(currentMap);
        gameManager.setCurrentPlayer(currentPlayer);
        gameManager.updatePlayerLocation(currentPlayer.getX(), currentPlayer.getY());
        playerX = currentPlayer.getX();
        playerY = currentPlayer.getY();
    }

    /**
     * Reduces the large Cell[][] to one correctly fit for the window/FOV.
     *
     * @param map Map to reduce
     * @return Reduced Cell[][]
     */
    private Cell[][] reduceMap(Map map) {
        Cell[][] reducedMap;
        //corrects FOV to stop crashes
        if (FOV > Math.min(map.getSizeX(), map.getSizeY())) {
            FOV = Math.min(map.getSizeX(), map.getSizeY()) / 2;
            GRID_CELL_WIDTH = WINDOW_WIDTH / FOV;
            GRID_CELL_HEIGHT = WINDOW_HEIGHT / FOV;
        }
        //gets player location and shortens the map around it
        int startX = currentPlayer.getX() - ((FOV - 1) / 2);
        int startY = currentPlayer.getY() - ((FOV - 1) / 2);
        reducedMap = get2DSubArray(currentMap.getMap(), startX, startX + FOV - 1, startY, startY + FOV - 1);
        return reducedMap;
    }

    /**
     * Gets a subarray of a 2D array.
     *
     * @param largeArray       Array from which to extract subarray from
     * @param rowStartIndex    Start row of extraction
     * @param rowEndIndex      End row of extraction
     * @param columnStartIndex Start column of extraction
     * @param columnEndIndex   End column of extraction
     * @return A subarray of largeArray
     */
    private Cell[][] get2DSubArray(Cell[][] largeArray, int rowStartIndex,
                                   int rowEndIndex, int columnStartIndex,
                                   int columnEndIndex) {
        int index = 0;
        //corrects row/column start/end indexes to prevent OutOfBounds errors and ensure proper game appearance
        if (columnStartIndex < 0) {
            columnStartIndex = 0;
            columnEndIndex = FOV - 1;
        }
        if (columnEndIndex >= largeArray[0].length) {
            columnEndIndex = largeArray[0].length - 1;
            columnStartIndex = columnEndIndex - (FOV - 1);
        }
        if (rowStartIndex < 0) {
            rowStartIndex = 0;
            rowEndIndex = FOV - 1;
        }
        if (rowEndIndex > largeArray.length - 1) {
            rowEndIndex = largeArray.length - 1;
            rowStartIndex = rowEndIndex - (FOV - 1);
        }

        log.add("StartRow: " + rowStartIndex);
        log.add("EndRow: " + rowEndIndex);
        log.add("StartColumn: " + columnStartIndex);
        log.add("EndColumn: " + columnEndIndex);
        Cell[][] subArray = new Cell[FOV][FOV];

        //extract the subarray
        for (int row = rowStartIndex; row <= rowEndIndex; row++) {
            subArray[index++] = Arrays.copyOfRange(largeArray[row], columnStartIndex, columnEndIndex + 1);
        }
        return subArray;
    }

    /**
     * Load any images from file.
     */
    private void loadImages() {
        player = new Image("images/player2.png");
        playerFlip = new Image("images/player2Flip.png");
        umbrellaOn = new Image("images/umbrellaOn.png");
        jacketOn = new Image("images/jacketOn.png");
        jacketOnFlip = new Image("images/jacketOnFlip.png");

        enemyDumb = new Image("images/enemyDumb.png");
        enemyStraight = new Image("images/enemyStraight.png");
        enemySmart = new Image("images/enemySmart.png");
        enemyWall = new Image("images/enemyWall.png");

        ground = new Image("images/ground.png");
        snowGround = new Image("images/SnowGround.png");
        wall = new Image("images/hedge.png");
        snowWall = new Image("images/SnowWall.png");

        wind = new Image("images/wind.png");
        rain = new Image("images/rain.png");
        teleporter = new Image("images/teleporter.png");
        target = new Image("images/target.png");
        ice = new Image("images/ice.png");


        redDoor = new Image("images/redDoor.png");
        greenDoor = new Image("images/greenDoor.png");
        blueDoor = new Image("images/blueDoor.png");
        tokenDoor = new Image("images/tokenDoor.png");
        tokenDoorOpen = new Image("images/tokenDoorOpen.png");

        redDoorOpen = new Image("images/redDoorOpen.png");
        greenDoorOpen = new Image("images/greenDoorOpen.png");
        blueDoorOpen = new Image("images/blueDoorOpen.png");

        redKey = new Image("images/redKey.png");
        greenKey = new Image("images/greenKey.png");
        blueKey = new Image("images/blueKey.png");

        token = new Image("images/token.png");
        windJacket = new Image("images/jacket.png");
        rainUmbrella = new Image("images/umbrella.png");

        sword = new Image("images/Sword.png");

        pickaxe = new Image("images/Pickaxe.png");
        iceCubes = new Image("images/iceCubes.png");
    }

    /**
     * Process a key event due to a key being pressed to move the Player.
     *
     * @param event The key event that was pressed.
     */
    private void processKeyEvent(KeyEvent event) {
        boolean moveValid = true;
        //checks first to stop play continuing after a game win/death
        if (currentPlayer.isAlive() && currentPlayer.isPlaying()) {
            playerX = currentPlayer.getX();
            playerY = currentPlayer.getY();
            log.add("Player is alive, can move", 0);


            if (event.getCode() == KeyCode.S) {
                Data data = createDataForSaving();
                String name = currentPlayer.getCurrentProfile().getName();
                try {
                    SaveData.save(data, name + "1.save");
                    System.out.println("Save");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else if (event.getCode() == KeyCode.L) {
                try {
                    String name = currentPlayer.getCurrentProfile().getName();
                    Data data = (Data) SaveData.load(name + "1.save");
                    loadData(Objects.requireNonNull(data));
                    System.out.println("Load");
                } catch (Exception ex) {
                    System.out.println("Profile don't have any save file");
                }
            } else {


                gameManager.getCurrentMap().getCell(playerX, playerY).setCharacter(null);
                // Do nothing
                if (event.getCode() == KeyCode.RIGHT) { // Right key was pressed. So move the player right by one cell.
                    if (gameManager.checkPlayerMove(playerX + 1, playerY)) {
                        playerX = playerX + 1;
                        log.add("Move RIGHT", 1);
                    } else {
                        sound.playThump();
                        log.add("Invalid RIGHT move", 1);
                    }
                    playerRotation = 90;
                } else if (event.getCode() == KeyCode.LEFT) { // Right key was pressed. So move the player right by one cell.
                    if (gameManager.checkPlayerMove(playerX - 1, playerY)) {
                        playerX = playerX - 1;
                        log.add("Move LEFT", 1);
                    } else {
                        sound.playThump();
                        log.add("Invalid LEFT move", 1);
                    }
                    playerRotation = 270;
                } else if (event.getCode() == KeyCode.UP) { // Right key was pressed. So move the player right by one cell.
                    if (gameManager.checkPlayerMove(playerX, playerY - 1)) {
                        playerY = playerY - 1;
                        log.add("Move UP", 1);
                    } else {
                        sound.playThump();
                        log.add("Invalid UP move", 1);
                    }
                    playerRotation = 0;
                } else if (event.getCode() == KeyCode.DOWN) { // Right key was pressed. So move the player right by one cell.
                    if (gameManager.checkPlayerMove(playerX, playerY + 1)) {
                        playerY = playerY + 1;
                        log.add("Move DOWN", 1);
                    } else {
                        sound.playThump();
                        log.add("Invalid DOWN move", 1);
                    }
                    playerRotation = 180;
                } else {
                    moveValid = false;
                    //correct state after a miss press
                    currentTick -= 1;
                    currentPlayer.setSteps(currentPlayer.getCurrentSteps() - 1);
                }
                //performs actions after a movement
                gameManager.updatePlayerLocation(playerX, playerY);
                if (moveValid) {
                    gameManager.postMovement();
                    if (gameManager.getCurrentMap().getCell(playerX, playerY) instanceof Ice) {
                        //repeats key if ice cell
                        Timeline timeline = new Timeline(
                                new KeyFrame(Duration.seconds(.35), e -> processKeyEvent(event)));
                        timeline.setCycleCount(1);
                        timeline.play();
                    }
                    winCheck();
                }
            }
            if (currentPlayer.isPlaying()) {
                if (currentPlayer.isAlive()) {
                    drawGame();
                } else {
                    musicPlayer.stop();
                    log.add("Player is dead, movement not allowed", 1);
                    restartGame();
                }
            }
        }
        event.consume();
    }

    /**
     * Checks if the level has been won, and begins a chain of actions to start the next level/game.
     */
    private void winCheck() {
        if (currentPlayer.getCell() instanceof Target) {
            stepCount = currentPlayer.getCurrentSteps();
            log.add("Player has won the game", 2);
            /*
                if (levelNumber > currentPlayer.getCurrentProfile().getBestLevel()){
                currentPlayer.getCurrentProfile().setBestLevel(levelNumber);
                log.add("Player has beaten this level for the first time",1);
            } else {
                log.add("Not setting this as Best Level", 1);
            }
            */

            musicPlayer.stop();
            currentPlayer.win();
            saveData(levelNumber + 1, currentPlayer.getCurrentSteps());
            //check if there are any maps left to play
            if (levelNumber == mapsArray.length - 1) {
                saveData(levelNumber + 1, currentPlayer.getCurrentSteps());
                winScreen();
            } else {
                saveData(levelNumber + 1, currentPlayer.getCurrentSteps());
                levelNumber += 1;
                initGame(currentPlayer.getCurrentProfile(), mapsArray[levelNumber]);
            }
        }
    }

    /**
     * Draws the Winning screen once the game is won.
     */
    private void winScreen() {
        try {
            this.primaryStage.hide();
            Parent winParent = FXMLLoader.load(getClass().getResource("WinGame.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(winParent, 600, 400));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Draw the game on the canvas.
     * Iterates through Map and creates a Cell as well as any Items/Characters within it
     */
     public void drawGame() {
        reducedMap = new Map(reduceMap(currentMap));

        GraphicsContext gc = canvas.getGraphicsContext2D();
        //draw interface
        drawMenu(this.pane);

        // Clear canvas
        Image ground2 = new Image("images/bay-campus-aerial-m-(1).png");
        gc.drawImage(ground2, 0, 0, canvas.getWidth(), canvas.getHeight());
        gc.strokeRect(0, 0, canvas.getWidth(), canvas.getHeight());

        setSeason(currentMap.getCell(0, 0));

        //render each Cell
        for (int y = 0; y < reducedMap.getSizeY(); y++) {
            for (int x = 0; x < reducedMap.getSizeX(); x++) {
                drawCurrent(gc, x, y);
            }
        }
    }

    /**
     * Draws any Object on the screen.
     *
     * @param gc GraphicsContext the playable game is held in
     * @param x  X location of the Cell to render in
     * @param y  Y location of the Cell to render in
     */
    private void drawCurrent(GraphicsContext gc, int x, int y) {
        Cell currentCell = reducedMap.getCell(x, y);
        if (currentCell != null) {
            String cellName = currentCell.getClass().getSimpleName();
            log.add(cellName + " at " + x + ", " + y, 0);
            drawCell(gc, currentCell, x, y);

            if (currentCell.getItem() != null) {
                Item currentItem = currentCell.getItem();
                String itemName = currentItem.getClass().getSimpleName();
                log.add("^ has " + itemName, 0);
                drawItem(gc, currentItem, x, y);
            }

            if (currentCell.getOccupier() != null) {
                Character currentCharacter = currentCell.getOccupier();
                String characterName = currentCharacter.getClass().getSimpleName();
                drawCharacter(gc, currentCharacter, x, y);
                log.add("^ has " + characterName, 0);
            }
        }
    }

    /**
     * Draws any Cell objects on the screen.
     *
     * @param gc   GraphicsContext the playable game is held in
     * @param cell Cell to render
     * @param x    X location of the Cell to render
     * @param y    Y location of the Cell to render
     */
    private void drawCell(GraphicsContext gc, Cell cell, int x, int y) {
        Image currentCell;
        //selects image file dependant on object
        if (cell instanceof Wall) {
            drawSeason(gc, cell, x, y);
            currentCell = wall;
        } else if (cell instanceof Wind) {
            drawSeason(gc, cell, x, y);
            currentCell = wind;
        } else if (cell instanceof Rain) {
            drawSeason(gc, cell, x, y);
            currentCell = rain;
        } else if (cell instanceof Door) {
            drawSeason(gc, cell, x, y);
            currentCell = drawDoor((Door) cell);
        } else if (cell instanceof Target) {
            drawSeason(gc, cell, x, y);
            currentCell = target;
        } else if (cell instanceof Teleporter) {
            drawSeason(gc, cell, x, y);
            currentCell = teleporter;
        } else if (cell instanceof Ice) {
            drawSeason(gc, cell, x, y);
            currentCell = ice;
        } else if (cell instanceof Ground) {
            if (season.equals("summer")) {
                currentCell = ground;
            } else {
                currentCell = snowGround;
            }
        } else if (cell instanceof SnowWall){
            drawSeason(gc, cell, x, y);
            currentCell = snowWall;
        } else if (cell instanceof IceCubes) {
            drawSeason(gc, cell, x, y);
            currentCell = iceCubes;
        } else {
            currentCell = snowGround;
        }
        //adjust and draw it to screen
        Image toRender = transform(currentCell, 0);
        gc.drawImage(toRender, x * GRID_CELL_WIDTH, y * GRID_CELL_HEIGHT);
    }

    /**
     * Draws any Item objects on the screen.
     *
     * @param gc   GraphicsContext the playable game is held in
     * @param item Item to render
     * @param x    X location of the Cell to render in
     * @param y    Y location of the Cell to render in
     */
    private void drawItem(GraphicsContext gc, Item item, int x, int y) {
        Image currentItem = null;
        if (item instanceof Token) {
            currentItem = token;
        } else if (item instanceof RainUmbrella) {
            currentItem = rainUmbrella;
        } else if (item instanceof WindJacket) {
            currentItem = windJacket;
        } else if (item instanceof Key) {
            //get correct Key image based on colour
            Key key = (Key) reducedMap.getCell(x, y).getItem();
            ColorEnum colorEnum = key.getColorEnum();
            Color colour = colorEnum.getColor(colorEnum);
            if (colour == Color.RED) {
                currentItem = redKey;
            } else if (colour == Color.GREEN) {
                currentItem = greenKey;
            } else if (colour == Color.BLUE) {
                currentItem = blueKey;
            }
        }
        else if (item instanceof Sword) {
            currentItem = sword;
        } else if (item instanceof Pickaxe) {
            currentItem = pickaxe;
        }
        if (currentItem != null) {
            Image toRender = transform(currentItem, 0);
            gc.drawImage(toRender, x * GRID_CELL_WIDTH, y * GRID_CELL_HEIGHT);
        }
    }

    /**
     * Checks whether a Door is unlocked, and renders it correctly if so.
     *
     * @param door Door to render
     * @return Correct Image of the Door
     */
    private Image drawDoor(Door door) {
        Image currentCell = null;
        if (door instanceof KeyDoor) {
            KeyDoor keyDoor = (KeyDoor) door;
            ColorEnum colorEnum = keyDoor.getColorEnum();
            Color colour = colorEnum.getColor(colorEnum);
            if (colour == Color.RED) {
                if (keyDoor.isPassable()) {
                    currentCell = redDoorOpen;
                } else {
                    currentCell = redDoor;
                }
            } else if (colour == Color.GREEN) {
                if (keyDoor.isPassable()) {
                    currentCell = greenDoorOpen;
                } else {
                    currentCell = greenDoor;
                }
            } else if (colour == Color.BLUE) {
                if (keyDoor.isPassable()) {
                    currentCell = blueDoorOpen;
                } else {
                    currentCell = blueDoor;
                }
            } else {
                currentCell = ground;
            }
        } else if (door instanceof TokenDoor) {
            if (door.isPassable()) {
                currentCell = tokenDoorOpen;
            } else {
                currentCell = tokenDoor;
            }
        }

        return currentCell;
    }

    /**
     * Draws any Character objects on the screen.
     *
     * @param gc        GraphicsContext the playable game is held in
     * @param character Character to render
     * @param x         X location of the Cell to render in
     * @param y         Y location of the Cell to render in
     */
    private void drawCharacter(GraphicsContext gc, Character character, int x, int y) {
        Image currentCharacter = null;
        int angle;
        if (character instanceof Player) {
            //if a player, rotate/flip image accordingly
            if (currentTick == 0) {
                currentCharacter = player;
                currentTick += 1;
            } else {
                currentCharacter = playerFlip;
                currentTick -= 1;
            }
            angle = playerRotation;
        } else {
            Enemy enemy = (Enemy) character;
            angle = 0;
            if (character instanceof WallFollow) {
                currentCharacter = enemyWall;
            } else if (character instanceof SmartTarget) {
                currentCharacter = enemySmart;
            } else if (character instanceof DumbTarget) {
                currentCharacter = enemyDumb;
            } else if (character instanceof StraightLine) {
                currentCharacter = enemyStraight;
            }
        }
        if (currentCharacter != null) {
            Image toRender = transform(currentCharacter, angle);
            gc.drawImage(toRender, x * GRID_CELL_WIDTH, y * GRID_CELL_HEIGHT);
        }
        //if it is a Player, draw any Wearables (must be after initial Character render)
        if (character instanceof Player) {
            drawWearables(gc, x, y);
        }
    }

    /**
     * Overlays any Wearables that the Player is currently in possession of.
     *
     * @param gc GraphicsContext the playable game is held in
     * @param x  X location of the Cell to render in
     * @param y  Y location of the Cell to render in
     */
    private void drawWearables(GraphicsContext gc, int x, int y) {
        Image currentWearable;
        Image toRender;
        if (countWearables(currentPlayer.getWearables())[0]) {
            if (currentTick == 0) {
                currentWearable = jacketOnFlip;
            } else {
                currentWearable = jacketOn;
            }
            toRender = transform(currentWearable, playerRotation);
            gc.drawImage(toRender, x * GRID_CELL_WIDTH, y * GRID_CELL_HEIGHT);
        }
        if (countWearables(currentPlayer.getWearables())[1]) {
            currentWearable = umbrellaOn;
            toRender = transform(currentWearable, playerRotation);
            gc.drawImage(toRender, x * GRID_CELL_WIDTH, y * GRID_CELL_HEIGHT);
        }
        if (countWearables(currentPlayer.getWearables())[2]){
            currentWearable = sword;
            toRender = transform(currentWearable, playerRotation);
            gc.drawImage(toRender, x * GRID_CELL_WIDTH, y * GRID_CELL_HEIGHT);
        }
        if (countWearables(currentPlayer.getWearables())[3]){
            currentWearable = pickaxe;
            toRender = transform(currentWearable, playerRotation);
            gc.drawImage(toRender, x * GRID_CELL_WIDTH, y * GRID_CELL_HEIGHT);
        }
    }

    /**
     * Transforms and performs modifications to an Image file.
     *
     * @param source Image to modify
     * @param angle  Angle to rotate Image by
     * @return Modified Image
     */
    private Image transform(Image source, int angle) {
        ImageView imageView = new ImageView(source);
        imageView.setPreserveRatio(false);
        imageView.setFitWidth(Game.GRID_CELL_WIDTH);
        imageView.setFitHeight(Game.GRID_CELL_HEIGHT);
        imageView.setRotate(angle);
        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        return imageView.snapshot(parameters, null);
    }

    /**
     * Restart the game using same options as the current one.
     */
    public void restartGame() {
        log.add("Game restarting", 0);
        initGame(currentPlayer.getCurrentProfile(), currentMap.getOriginalMap());
        drawGame();
    }

    /**
     * Create the GUI.
     *
     * @return The panel that contains the created GUI.
     */
    private BorderPane buildGUI() {
        // Create top-level pane that will hold all GUI
        BorderPane root = new BorderPane();

        // Create canvas
        canvas = new Canvas(594, 594);//this value should not be changed at all
        root.setCenter(canvas);

        // Render GUI around game
        drawMenu(root);

        return root;
    }

    /**
     * Draws GUI around the game.
     *
     * @param root Pane to work on
     */
    @SuppressWarnings("CssUnknownTarget")
    private void drawMenu(BorderPane root) {
        int redKeyCount = countKey(currentPlayer.getInventory())[0];
        int blueKeyCount = countKey(currentPlayer.getInventory())[1];
        int greenKeyCount = countKey(currentPlayer.getInventory())[2];
        boolean[] wearables = countWearables((currentPlayer.getWearables()));
        String windJacketValue = wearables[0] ? "ON" : "OFF";
        String rainUmbrellaValue = wearables[1] ? "ON" : "OFF";

        int tokenCount = currentPlayer.getTokenCount();

        Label col1 = new Label(Integer.toString(redKeyCount));
        Label col2 = new Label(Integer.toString(greenKeyCount));
        Label col3 = new Label(Integer.toString(blueKeyCount));
        Label steps = new Label(Integer.toString(currentPlayer.getCurrentSteps()));
        Label stepsText = new Label("Steps ");
        Label slash = new Label("/");
        Label totalTokens = new Label(Integer.toString(currentMap.getTokenCount()));

        Label token = new Label(Integer.toString(tokenCount));
        Label waterFlipper = new Label(rainUmbrellaValue);
        Label fireBoot = new Label(windJacketValue);

        try {
            Font font = Font.loadFont(new FileInputStream(new File("src/fonts/PixelMiners.otf")), 14);
            col1.setFont(font);
            col2.setFont(font);
            col3.setFont(font);
            token.setFont(font);
            waterFlipper.setFont(font);
            fireBoot.setFont(font);
            steps.setFont(font);
            stepsText.setFont(new Font(font.getName(), 12));
            slash.setFont(font);
            totalTokens.setFont(font);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        VBox toolbar2 = new VBox();
        toolbar2.setStyle("-fx-background-image: url(\"https://imgur.com/MXmRg6v.png\");\n");
        toolbar2.setSpacing(10);
        toolbar2.setPadding(new Insets(10, 10, 10, 10));
        toolbar2.prefHeight(594);
        toolbar2.prefWidth(78);
        root.setLeft(toolbar2);

        Button restartButton = new Button("Restart");
        restartButton.setStyle(" -fx-text-fill:Blue;");
        restartButton.setTranslateX(4);
        toolbar2.getChildren().add(restartButton);

        stepsText.setTranslateY(10);
        stepsText.setTranslateX(6);
        steps.setTranslateY(18);
        steps.setTranslateX(20);
        col1.setTranslateX(22);
        col1.setTranslateY(120);
        col2.setTranslateX(22);
        col2.setTranslateY(230);
        col3.setTranslateX(22);
        col3.setTranslateY(360);

        toolbar2.getChildren().addAll(stepsText, steps, col1, col2, col3);

        restartButton.setOnAction(e -> restartGame());

        HBox toolbar = new HBox();
        toolbar.setSpacing(20);
        toolbar.setPadding(new Insets(30, 30, 30, 20));
        toolbar.setStyle("-fx-background-image: url(\"https://imgur.com/e46VZQj.png\");\n");
        toolbar.prefHeight(594);
        toolbar.prefWidth(78);
        root.setTop(toolbar);

        fireBoot.setTranslateX(127);
        fireBoot.setTranslateY(5);
        fireBoot.setTextFill(changeColour(fireBoot));

        waterFlipper.setTranslateX(260);
        waterFlipper.setTranslateY(5);
        waterFlipper.setTextFill(changeColour(waterFlipper));

        token.setTranslateX(400);
        token.setTranslateY(2);

        slash.setTranslateX(380);
        slash.setTranslateY(2);

        totalTokens.setTranslateX(360);
        totalTokens.setTranslateY(2);

        toolbar.getChildren().addAll(fireBoot, waterFlipper, token, slash, totalTokens);

    }

    /**
     * Counts number of Keys and returns a relevant Int[].
     *
     * @param itemArrayList ArrayList to search
     * @return Completed Int[]
     */
    private int[] countKey(ArrayList<Item> itemArrayList) {
        int[] allKeys = new int[3];

        for (Item i : itemArrayList) {
            if (i instanceof Key) {
                ColorEnum colorEnum = ((Key) i).getColorEnum();
                Color colour = colorEnum.getColor(colorEnum);
                if (colour == Color.RED) {
                    allKeys[0] = +1;
                } else if (colour == Color.BLUE) {
                    allKeys[1] = +1;
                } else if (colour == Color.GREEN) {
                    allKeys[2] += 1;
                }
            }
        }
        return allKeys;
    }

    /**
     * Get colour of label given text.
     *
     * @param wearable Label to read
     * @return Color for the label
     */
    private Color changeColour(Label wearable) {
        if (wearable.getText().equals("OFF")) {
            return Color.RED;
        } else if (wearable.getText().equals("ON")) {
            return Color.web("#00ff44");
        }
        return null;
    }

    /**
     * Returns a Boolean array that indicates whether a Player has certain Wearables.
     *
     * @param itemArrayList ArrayList of Items to search
     * @return Completed Boolean[] if Wearables are present
     */
    private boolean[] countWearables(ArrayList<Wearable> itemArrayList) {
        boolean[] wear = new boolean[4];
        for (Item i : itemArrayList) {
            if (i instanceof WindJacket) {
                wear[0] = true;
            } else if (i instanceof RainUmbrella) {
                wear[1] = true;
            } else if (i instanceof Sword) {
                wear[2] = true;
            } else if (i instanceof Pickaxe) {
                wear[3] = true;
        }
        }
        return wear;
    }

    /**
     * Saves the current Profile progress.
     *
     * @param level level the user is playing
     * @param steps Steps taken so far
     */
    private void saveData(int level, int steps) {
        currentPlayer.getCurrentProfile().save(level, steps);
        //log.add("Saving failed", 1);
    }

    /**
     * Create Data Object for saving.
     *
     * @return data Object.
     */
    private Data createDataForSaving() {
        Data data = new Data();
        Cell[][] map = gameManager.getCurrentMap().getMap();
        data.setMap(map);
        data.setFov(FOV);
        data.setGridCellHeight(GRID_CELL_HEIGHT);
        data.setGridCellWidth(GRID_CELL_WIDTH);
        data.setMapsArray(mapsArray);
        data.setLevelNumber(levelNumber);

        return data;
    }

    /**
     * Loading game from Data object;
     *
     * @param data Data to be load;
     */
    private void loadData(Data data) {
        Cell[][] map = data.getMap();
        FOV = data.getFov();
        GRID_CELL_WIDTH = data.getGridCellWidth();
        GRID_CELL_HEIGHT = data.getGridCellHeight();
        mapsArray = data.getMapsArray();
        levelNumber = data.getLevelNumber();
        initGame(map);
        drawGame();
    }

    /**
     * Method check with season is current on map
     *
     * @param c Cell
     */
    private void setSeason(Cell c){
        if (c instanceof Wall){
            season = "summer";
        } else {
            season = "winter";
        }
    }

    /**
     * Method drawing season that is on map
     *
     * @param gc   GraphicsContext the playable game is held in
     * @param cell Cell to render
     * @param x    X location of the Cell to render
     * @param y    Y location of the Cell to render
     */
    private void drawSeason(GraphicsContext gc, Cell cell, int x, int y){
        if (season.equals("summer")) {
            drawCell(gc, new Ground(x, y), x, y);
        } else {
            drawCell(gc, new SnowGround(x, y), x, y);
        }
    }

}
