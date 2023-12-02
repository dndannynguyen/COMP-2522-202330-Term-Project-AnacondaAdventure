package ca.bcit.comp2522.termproject.anacondaadventure;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class AnacondaAdventure extends Application {

    private static String gameMode;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Anaconda Adventure");

        InfoDisplay infoDisplay = new InfoDisplay();

        SnakeGame snakeGame = new SnakeGame();
        snakeGame.init();

        GridPane layout = new GridPane();

        ColumnConstraints infoDisplayColumn = new ColumnConstraints();
        infoDisplayColumn.setPercentWidth(10);
        ColumnConstraints snakeGameColumn = new ColumnConstraints();
        snakeGameColumn.setPercentWidth(90);

        layout.getColumnConstraints().addAll(infoDisplayColumn, snakeGameColumn);

        layout.add(infoDisplay, 0, 0);
        layout.add(snakeGame, 1, 0);

        primaryStage.setScene(new Scene(layout));
        primaryStage.show();
    }

    public static void setGameMode(String mode) {
        gameMode = mode;
    }
}
