package ca.bcit.comp2522.termproject.anacondaadventure;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 700);
        stage.getIcons().add(new javafx.scene.image.Image("file:src/main/resources/images/anaconda.jpg"));
        stage.setTitle("Anaconda Adventure");
        scene.getStylesheets().add(getClass().getResource("index.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}