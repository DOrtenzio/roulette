package com.example.roulettedemo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(6);
        ArrayList<Giocatore> giocatori=new ArrayList<Giocatore>();
        HelloController helloController=new HelloController();
        Roulette roulette = new Roulette(5000, semaphore.availablePermits(), semaphore,helloController,giocatori);
        Giocatore g1 = new Giocatore("a", 340.00, roulette, semaphore);
        Giocatore g2 = new Giocatore("b", 340.00, roulette, semaphore);
        Giocatore g3 = new Giocatore("c", 340.00, roulette, semaphore);
        Giocatore g4 = new Giocatore("d", 340.00, roulette, semaphore);

        giocatori.add(g1);
        giocatori.add(g2);
        giocatori.add(g3);
        giocatori.add(g4);


        roulette.start();
        g1.start();
        g2.start();
        g3.start();
        g4.start();
        launch();
    }
}