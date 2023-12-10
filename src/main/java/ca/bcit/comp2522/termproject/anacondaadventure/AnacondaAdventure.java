package ca.bcit.comp2522.termproject.anacondaadventure;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Objects;

/**
 * The main class controlling the Anaconda Adventure game.
 * Manages the game modes, graphics, and gameplay logic.
 */
public class AnacondaAdventure extends Application {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int TILE_SIZE = 20;

    // subtracted 1 because points start from 0

    private static final int TILE_COUNT_X = (WIDTH / TILE_SIZE) - 1;
    private static final int TILE_COUNT_Y = (HEIGHT / TILE_SIZE) - 1;
    public static ProgressBar progressBar;

    private static final int INITIAL_REMAINING_TIME = 15;
    private static final int OBSTACLE_SPAWN_TIME = 10;
    private static final int NUM_FOODS = 3;
    AnimationTimer gameTimer;

    private boolean paused = false;
    private Sound sound = new Sound();

    private static String gameMode;
    private GraphicsContext gc;
    private Snake snake;
    private Food food;

    private Food [] foods = new Food[3];
    private Obstacle obstacle;

    private Stage stage = null;

    private Image backgroundImage = null;

    private Font gameFont = null;

    private String paneStyle;
    private HBox timeGameBox;
    private VBox timeInfoBox;

    public static int remainingTime = 60;
    public static Label time;

    /**
     * Retrieves the width of the game grid.
     *
     * @return The width of the game grid.
     */
    public static int getWIDTH() {
        return TILE_COUNT_X;
    }

    /**
     * Retrieves the height of the game grid.
     *
     * @return The height of the game grid.
     */
    public static int getHEIGHT() {
        return TILE_COUNT_Y;
    }

    /**
     * The entry point of the application.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Initializes and starts the game upon application launch.
     *
     * @param primaryStage The primary stage of the application.
     */
    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;

        File file = new File("src/main/resources/images/background.jpg");
        backgroundImage = new Image(file.toURI().toString());

        file = new File("src/main/resources/Fonts/Retro Gaming.ttf");
        try {
            InputStream inputStream = new FileInputStream(file);
            gameFont = Font.loadFont(inputStream, 30);
            System.out.println(gameFont.getName());
        } catch (Exception e){
            e.printStackTrace();
        }

        if(Objects.equals(getGameMode(), "Time Attack"))
            createTimeGame();
        else startClassicGame();

        primaryStage.show();
    }

    /**
     * Draws the game elements on the canvas.
     */
    private void draw() {
        gc.clearRect(0, 0, WIDTH, HEIGHT);
        gc.drawImage(backgroundImage,0,0, WIDTH, HEIGHT);
        snake.render(gc, TILE_SIZE);
        if(Objects.equals("Time Attack", getGameMode()))
            for(int i = 0; i < 3; i++)
                foods[i].render(gc, TILE_SIZE);
        else
            food.render(gc, TILE_SIZE);
        if(obstacle != null)
            obstacle.render(gc, TILE_SIZE);
    }

    public static void setGameMode(String mode) {
        gameMode = mode;
    }
    public static String getGameMode(){
        return gameMode;
    }

    /**
     * Creates and initializes the basic game layout.
     * Sets up the canvas, initializes game elements, starts the game loop, and centers the stage on the screen.
     *
     * @return The StackPane containing the game canvas and elements.
     */
    public StackPane createBasicGame(){
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        gc = canvas.getGraphicsContext2D();

        StackPane root = new StackPane();
        root.getChildren().add(canvas);

        snake = new Snake();
        snake.init();

        gameTimer = new AnimationTimer() {
            long lastUpdate = 0;

            public void handle(long now) {
                if (now - lastUpdate >= 66_666_666) {
                    boolean successfulMove = snake.update(obstacle); // Update the snake
                    if (!successfulMove) {
                        stopGame(); // Stop the game if the snake's movement was unsuccessful
                    }
                    checkForFoodEaten(); // Check for food eaten
                    draw(); // Redraw the game
                    lastUpdate = now; // Update last update time
                }
            }
        };
        gameTimer.start();

        // Get the visual bounds of the primary screen
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        // Calculate the center coordinates
        double centerX = screenBounds.getMinX() + (screenBounds.getWidth() - stage.getWidth()) / 2;
        double centerY = screenBounds.getMinY() + (screenBounds.getHeight() - stage.getHeight()) / 2;

        // Set the stage coordinates
        stage.setX(centerX);
        stage.setY(centerY);
        stage.show();

        return root;
    }

    /**
     * Starts the Classic game mode by initializing the initial food, setting up the scene,
     * and defining key event handlers for controlling the snake's movement.
     */
    public void startClassicGame(){
        food = Food.generateRandomFood(new Point(0,0), new Point(getWIDTH(), getHEIGHT()), obstacle, snake, getGameMode());
        stage.setScene(new Scene(createBasicGame()));

        Scene scene = stage.getScene();

        scene.setOnKeyPressed(event -> {
            KeyCode keyCode = event.getCode();
            if (keyCode == KeyCode.UP || keyCode == KeyCode.W) {
                snake.changeDirection(Direction.UP);
            } else if (keyCode == KeyCode.DOWN || keyCode == KeyCode.S) {
                snake.changeDirection(Direction.DOWN);
            } else if (keyCode == KeyCode.LEFT || keyCode == KeyCode.A) {
                snake.changeDirection(Direction.LEFT);
            } else if (keyCode == KeyCode.RIGHT || keyCode == KeyCode.D) {
                snake.changeDirection(Direction.RIGHT);
            }
            else if(keyCode == KeyCode.ESCAPE){
                if(paused)
                    gameTimer.start();
                else
                    gameTimer.stop();
                paused = !paused;

            }
        });

    }

    /**
     * Creates the Time Attack game mode by initializing the game with a time limit,
     * setting up the scene, handling time-related functionalities such as obstacle spawning,
     * and defining key event handlers for controlling the snake's movement and game pausing.
     */
    public void createTimeGame() {
        remainingTime = INITIAL_REMAINING_TIME;
        StackPane root = createBasicGame();

        Label timeLabel = new Label("Time Remaining: " + remainingTime);
        timeLabel.setFont(new Font(25));

        ProgressBar timerProgressBar = new ProgressBar();
        timerProgressBar.setProgress(1.0);
        timerProgressBar.setPrefWidth(200);

        VBox timeInfoBox = new VBox(10);
        timeInfoBox.setAlignment(Pos.CENTER);
        timeInfoBox.getChildren().addAll(timeLabel, timerProgressBar);

        obstacle = new Obstacle();
        for (int i = 0; i < NUM_FOODS; i++)
            foods[i] = Food.generateRandomFood(new Point(0, 0), new Point(getWIDTH(), getHEIGHT()), obstacle, snake, getGameMode());

        AnimationTimer gameTimer = new AnimationTimer() {
            private long lastUpdate = 0;
            private double obstacleTime = 0;
            private double elapsedTime = 0;

            @Override
            public void handle(long now) {
                if (lastUpdate > 0) {
                    long elapsedNanos = now - lastUpdate;
                    double elapsedSeconds = elapsedNanos / 1_000_000_000.0;

                    elapsedTime += elapsedSeconds;
                    obstacleTime += elapsedSeconds;

                    if (elapsedTime >= 1) {
                        remainingTime--;
                        elapsedTime = 0;
                        timerProgressBar.setProgress((double) remainingTime / INITIAL_REMAINING_TIME);
                    }

                    if (remainingTime <= 0) {
                        this.stop();
                        stopGame();
                    }

                    if (obstacleTime >= OBSTACLE_SPAWN_TIME) {
                        Sound.difficultyIncrease();
                        timeInfoBox.getChildren().add(new Label("Adding Obstacles!"));
                        spawnObstacles();
                        obstacleTime = 0;
                    }

                    timeLabel.setText("Time Remaining: " + remainingTime);
                }
                lastUpdate = now;
            }
        };

        gameTimer.start();

        HBox timeGameBox = new HBox(10);
        timeGameBox.setAlignment(Pos.CENTER);
        timeGameBox.getChildren().addAll(timeInfoBox, root);

        Scene scene = new Scene(timeGameBox);
        scene.setOnKeyPressed(event -> {
            KeyCode keyCode = event.getCode();
            switch (keyCode) {
                case UP:
                case W:
                    snake.changeDirection(Direction.UP);
                    break;
                case DOWN:
                case S:
                    snake.changeDirection(Direction.DOWN);
                    break;
                case LEFT:
                case A:
                    snake.changeDirection(Direction.LEFT);
                    break;
                case RIGHT:
                case D:
                    snake.changeDirection(Direction.RIGHT);
                    break;
            }
        });

        stage.setScene(scene);
    }
    /**
     * Spawns a set number of obstacles within the game area.
     * The number of obstacles is determined by the constant NUM_FOODS.
     * Each obstacle is spawned at a random location within the game area,
     * which is defined by the width and height of the game.
     */
    private void spawnObstacles() {
        for (int i = 0; i < NUM_FOODS; i++)
            obstacle.spawn(new Point(0, 0), new Point(getWIDTH(), getHEIGHT()));
    }






    /**
     * Checks if the snake has eaten any food, updates the snake's size accordingly,
     * and generates new food items based on the game mode (Time Attack or Classic).
     * Stops the game if the snake encounters an obstacle or reaches its maximum size.
     */
    public void checkForFoodEaten() {
        if (Objects.equals(getGameMode(), "Time Attack")) {
            for (int i = 0; i < 3; i++) {
                if (snake.getHead().getX() == foods[i].getPoint().getX() && snake.getHead().getY() == foods[i].getPoint().getY()) {
                    if (!snake.incrementSize(foods[i].getType())) {
                        stopGame();
                    }
                    foods[i] = Food.generateRandomFood(new Point(0, 0), new Point(getWIDTH(), getHEIGHT()), obstacle, snake, getGameMode());
                }
            }
        } else {
            if (snake.getHead().getX() == food.getPoint().getX() && snake.getHead().getY() == food.getPoint().getY()) {
                if (!snake.incrementSize(food.getType())) {
                    stopGame();
                }
                food = Food.generateRandomFood(new Point(0, 0), new Point(getWIDTH(), getHEIGHT()), obstacle, snake, getGameMode());
            }
        }
    }


    /**
     * Creates a pane for the game-over screen, displaying the final score and providing options to play again or exit to the menu.
     *
     * @return A Pane object representing the game-over screen layout.
     */
    public Pane gameOverPane() {
        Pane pane = new Pane();
        pane.setPrefSize(400, 400);
        pane.setBackground(new Background(new BackgroundFill(Color.GRAY, null, null)));

        Label scoreLabel = new Label("SCORE: " + (snake.getSize()-9)*10);
        scoreLabel.setAlignment(Pos.CENTER);
        scoreLabel.setFont(gameFont);
        scoreLabel.setPrefSize(300, 50);
        scoreLabel.setLayoutX(50);
        scoreLabel.setLayoutY(75);

        String[] buttonLabels = {"Play Again", "Exit to Menu"};
        Button[] buttons = new Button[buttonLabels.length];

        VBox buttonBox = new VBox();
        buttonBox.setSpacing(50);
        buttonBox.setLayoutX(25);
        buttonBox.setLayoutY(175);

        for (int i = 0; i < buttonLabels.length; i++) {
            buttons[i] = new Button(buttonLabels[i]);
            buttons[i].setFont(gameFont);
            buttons[i].setPrefSize(350, 50);
            buttons[i].setCursor(Cursor.HAND);
            buttonBox.getChildren().add(buttons[i]);
        }

        buttons[0].setOnMouseClicked(event -> {
            if ("Time Attack".equals(getGameMode())) {
                createTimeGame();
            } else {
                startClassicGame();
            }
        });

        buttons[1].setOnMouseClicked(event -> new StartMenu().start(stage));

        pane.getChildren().addAll(scoreLabel, buttonBox);

        return pane;
    }


    /**
     * Stops the game by triggering a game-over scenario.
     * Stops the game timer, plays a game-over sound, and transitions the scene to the game-over screen.
     */
    public void stopGame(){
        Sound.gameOver();
        gameTimer.stop();
        stage.setScene(new Scene(gameOverPane()));
    }
}
