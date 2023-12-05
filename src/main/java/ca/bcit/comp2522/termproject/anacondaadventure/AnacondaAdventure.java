package ca.bcit.comp2522.termproject.anacondaadventure;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class AnacondaAdventure extends Application {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int TILE_SIZE = 20;

    // subtracted 1 because points start from 0
    private static final int TILE_COUNT_X = (WIDTH / TILE_SIZE) - 1;
    private static final int TILE_COUNT_Y = (HEIGHT / TILE_SIZE) - 1;

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
        this.stage = primaryStage;

        File file = new File("src/main/resources/images/anaconda.jpg");
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
                if (now - lastUpdate >= 100_000_000) {
                    if(!snake.update(obstacle)) {
                        stopGame();
                    }
                    checkForFoodEaten();
                    draw();
                    lastUpdate = now;
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

    public void createTimeGame(){
        remainingTime = 20;
        StackPane root = createBasicGame();

        time = new Label("" + remainingTime);
        time.setFont(new Font(25));

        obstacle = new Obstacle();
        Timer timer2 = new Timer();
        for(int i = 0; i < 3; i++)
            foods[i] = Food.generateRandomFood(new Point(0,0), new Point(getWIDTH(), getHEIGHT()), obstacle, snake, getGameMode());
        timer2.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Sound.difficultyIncrease();
                Platform.runLater(()->{
                    timeInfoBox.getChildren().add(new Label("Adding Obstacles!"));
                });
                for(int i = 0; i < 3; i++)
                    obstacle.spawn(new Point(0,0), new Point(getWIDTH(), getHEIGHT()));
            }
        }, 10000, 15000);

        Timer timer1 = new Timer(true);
        timer1.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                remainingTime--;
                Platform.runLater(()->{
                    if(remainingTime <= 0) {
                        timer1.cancel();
                        timer2.cancel();
                        stopGame();
                    }
                    time.setText("" + remainingTime);
                });
            }
        }, 1000, 1000);

        timeInfoBox = new VBox(time);
        timeGameBox = new HBox(timeInfoBox, root);
        stage.setScene(new Scene(timeGameBox));


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

    public void checkForFoodEaten(){
        if(Objects.equals(getGameMode(), "Time Attack")){
            for(int i = 0; i < 3; i++ ){
                if(snake.getHead().getX() == foods[i].getPoint().getX() && snake.getHead().getY() == foods[i].getPoint().getY()){
                    if(!snake.incrementSize(foods[i].getType()))
                        stopGame();
                    foods[i] = Food.generateRandomFood(new Point(0, 0), new Point(getWIDTH(), getHEIGHT()), obstacle, snake, getGameMode());
                }
            }
        }
        else{
            if(snake.getHead().getX() == food.getPoint().getX() && snake.getHead().getY() == food.getPoint().getY()) {
                if(!snake.incrementSize(food.getType()))
                    stopGame();
                food = Food.generateRandomFood(new Point(0, 0), new Point(getWIDTH(), getHEIGHT()), obstacle, snake, getGameMode());
            }
        }
    }

    public Pane gameOverPane(){
        Pane pane = new Pane();
        pane.setPrefSize(400, 400);
        pane.setBackground(new Background(new BackgroundFill(Color.GRAY, null, null)));

        Label score = new Label("SCORE: " + (snake.getSize()-9)*10);
        score.setAlignment(Pos.CENTER);
        score.setFont(gameFont);
        score.setPrefSize(300, 50);
        score.setLayoutX(50);
        score.setLayoutY(75);
        pane.getChildren().add(score);

        Button [] options = new Button[2];
        String [] opt = new String[]{
                "Play Again",
                "Exit to Menu"
        };

        for(int i = 0, y = 175; i < 2; i++, y += 100){
            options[i] = new Button(opt[i]);
            options[i].setFont(gameFont);
            options[i].setPrefSize(350, 50);
            options[i].setLayoutX(25);
            options[i].setLayoutY(y);
            options[i].setCursor(Cursor.HAND);
            pane.getChildren().add(options[i]);
        }
        options[0].setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(Objects.equals("Time Attack", getGameMode()))
                    createTimeGame();
                else startClassicGame();
            }
        });
        options[1].setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                new StartMenu().start(stage);
            }
        });

        return pane;
    }

    public void stopGame(){
        Sound.gameOver();
        gameTimer.stop();
        stage.setScene(new Scene(gameOverPane()));
    }
}
