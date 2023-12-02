package ca.bcit.comp2522.termproject.anacondaadventure;

import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;


public class SnakeGame extends StackPane {
    private static final int WIDTH = 1200;
    private static final int HEIGHT = 700;
    private static final int TILE_SIZE = 20;
    private static final int TILE_COUNT_X = WIDTH / TILE_SIZE;
    private static final int TILE_COUNT_Y = HEIGHT / TILE_SIZE;

    private static String gameMode;
    private GraphicsContext gc;
    private Snake snake;
    private Food food;
    private Obstacle obstacle;

    public static int getWIDTH() {
        return TILE_COUNT_X;
    }

    public static int getHEIGHT() {
        return TILE_COUNT_Y;
    }

    public SnakeGame() {
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        gc = canvas.getGraphicsContext2D();
        getChildren().add(canvas);

        snake = new Snake();
        snake.init();


        food = new Food(new Point(0, 0), FoodType.NORMAL);
        obstacle = new Obstacle();

        setOnKeyPressed(event -> {
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
        });

        new AnimationTimer() {
            long lastUpdate = 0;

            public void handle(long now) {
                if (now - lastUpdate >= 100_000_000) {
                    snake.update();
                    draw();
                    lastUpdate = now;
                }
            }
        }.start();



        setFocusTraversable(true);
        requestFocus();

        setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
    }

    private void draw() {
        gc.setFill(Color.BLACK);
        gc.clearRect(0, 0, WIDTH, HEIGHT);
        snake.render(gc, TILE_SIZE);
        food.render(gc, TILE_SIZE);
        obstacle.render(gc);
    }

    public static void setGameMode(String mode) {
        gameMode = mode;
    }

    public void init() {

    }




}
