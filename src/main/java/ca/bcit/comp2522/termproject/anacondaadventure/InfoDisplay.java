package ca.bcit.comp2522.termproject.anacondaadventure;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class InfoDisplay extends StackPane {
    private Text textLength;
    private final Text textCountdown;
    private static int countdownSeconds = 120;

    public InfoDisplay() {
        textLength = new Text("Score: ");
        textCountdown = new Text("Countdown: " + formatTime(countdownSeconds));

        textLength.setFill(Color.BLACK);
        textCountdown.setFill(Color.BLACK);
        textLength.setFont(Font.font(null, FontWeight.BOLD, 15));
        textCountdown.setFont(Font.font(null, FontWeight.BOLD, 15));

        textCountdown.setTranslateY(20);



        getChildren().addAll(textLength, textCountdown);

        setBackground(new Background(new BackgroundFill(Color.GOLD, CornerRadii.EMPTY, Insets.EMPTY)));

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> updateTimer())
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        // Align texts at the top
        setAlignment(textLength, javafx.geometry.Pos.TOP_LEFT);
        setAlignment(textCountdown, javafx.geometry.Pos.TOP_LEFT);
    }

    private void updateTimer() {
        countdownSeconds--;
        textCountdown.setText("Countdown: " + formatTime(countdownSeconds));
        if (countdownSeconds <= 0) {

            textCountdown.setText("Game Over!");
        }
    }

    private String formatTime(int seconds) {
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        return String.format("%02d:%02d", minutes, remainingSeconds);
    }

    public void updateLength(int length) {
        textLength.setText("Length: " + length);
    }
}
