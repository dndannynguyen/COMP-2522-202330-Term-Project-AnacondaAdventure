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
    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 50;
    private static final int FONT_SIZE = 18;
    private static final String FONT_FAMILY = "Retro Gaming";
    private static final String BUTTON_STYLE = "-fx-font-family: '" + FONT_FAMILY + "'; -fx-font-size: " + FONT_SIZE + "; -fx-background-color: #E5E8E8; -fx-text-fill: black; -fx-border-color: black; -fx-border-width: 2px;";
    private static final String BUTTON_HOVER_STYLE = "-fx-font-family: '" + FONT_FAMILY + "'; -fx-font-size: " + FONT_SIZE + "; -fx-background-color: #d9ead3; -fx-text-fill: black; -fx-border-color: black; -fx-border-width: 2px;";

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

        String[][] buttons = {{"Time Attack", "Time Attack"}, {"Endless", "Endless"}};

        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().add(logoView);
        layout.getChildren().add(title);

        for (String[] button : buttons) {
            Button b = createStyledButton(button[0], customFont);
            b.setOnAction(event -> startGame(primaryStage, button[1]));
            layout.getChildren().add(b);
        }

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
        button.setMinSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        button.setMaxSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        button.setStyle(BUTTON_STYLE);
        button.setOnMouseEntered(e -> button.setStyle(BUTTON_HOVER_STYLE));
        button.setOnMouseExited(e -> button.setStyle(BUTTON_STYLE));
        return button;
    }

    private void startGame(Stage primaryStage, String mode) {
        switch (mode) {
            case "Time Attack":
                AnacondaAdventure.setGameMode("Time Attack");
                break;
            case "Endless":
                AnacondaAdventure.setGameMode("Endless");
                break;
            default:
                throw new IllegalArgumentException("Invalid game mode: " + mode);
        }
        AnacondaAdventure game = new AnacondaAdventure();
        game.start(primaryStage);
    }
}
