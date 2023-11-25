package ca.bcit.comp2522.termproject.anacondaadventure;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.InputStream;

public class StartMenu extends Application {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Anaconda Adventure - Start Menu");

        Image logo = new Image(getClass().getResourceAsStream("/images/anaconda.jpg"));
        ImageView logoView = new ImageView(logo);
        logoView.setFitWidth(300);
        logoView.setPreserveRatio(true);

        Font customFont = loadCustomFont("/fonts/Retro Gaming.ttf", 36);

        Text title = new Text("Anaconda Adventure");
        title.setFont(customFont);
        title.setFill(Color.DARKGREEN);

        Button timeAttackButton = createStyledButton("Time Attack", customFont);
        timeAttackButton.setOnAction(event -> startGame(primaryStage, "Time Attack"));

        Button endlessButton = createStyledButton("Endless", customFont);
        endlessButton.setOnAction(event -> startGame(primaryStage, "Endless"));

        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(logoView, title, timeAttackButton, endlessButton);
        layout.setStyle("-fx-background-color: #d9ead3; -fx-padding: 50px;");

        Scene scene = new Scene(layout, WIDTH, HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Font loadCustomFont(String path, double size) {
        InputStream fontStream = getClass().getResourceAsStream(path);
        return Font.loadFont(fontStream, size);
    }

    private Button createStyledButton(String text, Font font) {
        Button button = new Button(text);
        button.setMinSize(200, 50);
        button.setMaxSize(200, 50);
        button.setStyle("-fx-font-family: 'Retro Gaming'; -fx-font-size: 18;");
        return button;
    }

    private void startGame(Stage primaryStage, String mode) {
        AnacondaAdventure.setGameMode(mode);
        AnacondaAdventure game = new AnacondaAdventure();
        game.start(primaryStage);
    }
}
