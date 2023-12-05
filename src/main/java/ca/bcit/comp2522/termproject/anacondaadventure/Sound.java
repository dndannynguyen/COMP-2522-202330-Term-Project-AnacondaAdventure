package ca.bcit.comp2522.termproject.anacondaadventure;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class Sound {
    public static MediaPlayer foodEatPlayer;
    public static MediaPlayer gameOverPlayer;
    public static MediaPlayer bonusPlayer;
    public static MediaPlayer nerfPlayer;
    public static MediaPlayer difficultyPlayer;

    public Sound(){
        File file = new File("src/main/resources/sound/foodeat.mp3");
        Media media = new Media(file.toURI().toString());
        foodEatPlayer = new MediaPlayer(media);
        file = new File("src/main/resources/sound/gameover.mp3");
        media = new Media(file.toURI().toString());
        gameOverPlayer = new MediaPlayer(media);
        file = new File("src/main/resources/sound/gamebonus.mp3");
        media = new Media(file.toURI().toString());
        bonusPlayer = new MediaPlayer(media);
        file = new File("src/main/resources/sound/nerf.mp3");
        media = new Media(file.toURI().toString());
        nerfPlayer = new MediaPlayer(media);
        file = new File("src/main/resources/sound/difficultyincrease.mp3");
        media = new Media(file.toURI().toString());
        difficultyPlayer = new MediaPlayer(media);
    }

    public static void foodEaten(){
        foodEatPlayer.stop();
        foodEatPlayer.play();
    }
    public static void gameOver(){
        gameOverPlayer.stop();
        gameOverPlayer.play();
    }
    public static void gameBonus(){
        bonusPlayer.stop();
        bonusPlayer.play();
    }
    public static void nerf(){
        nerfPlayer.stop();
        nerfPlayer.play();
    }
    public static void difficultyIncrease(){
        difficultyPlayer.stop();
        difficultyPlayer.play();
    }
}