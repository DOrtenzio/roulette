package com.example.roulettedemo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("first-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 728, 568);

        FirstController firstController=fxmlLoader.getController();

        stage.setTitle("DRAWN ROULETTE");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.getIcons().add(new Image(HelloApplication.class.getResource("/com/example/roulettedemo/img/logo.png").toString()));
        stage.show();

        firstController.entrataIniziale(); // Inserimento grafico
    }

    public static void main(String[] args) {
        launch();
    }
}
