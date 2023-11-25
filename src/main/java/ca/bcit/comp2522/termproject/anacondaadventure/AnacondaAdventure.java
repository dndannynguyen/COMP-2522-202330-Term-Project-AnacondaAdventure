package ca.bcit.comp2522.termproject.anacondaadventure;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class AnacondaAdventure extends Application {
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

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Anaconda Adventure");
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        gc = canvas.getGraphicsContext2D();

        StackPane root = new StackPane();
        root.getChildren().add(canvas);
        primaryStage.setScene(new Scene(root));

        snake = new Snake();
        snake.init();

        food = new Food(new Point(0, 0));
        obstacle = new Obstacle();

        Scene scene = primaryStage.getScene();

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

        primaryStage.show();
    }

    private void draw() {
        gc.clearRect(0, 0, WIDTH, HEIGHT);
        snake.render(gc, TILE_SIZE);
        food.render(gc, TILE_SIZE);
        obstacle.render(gc);
    }

    public static void setGameMode(String mode) {
        gameMode = mode;
    }
}
