package com.example.roulettedemo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view2.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 728, 568);

        HelloController helloController = fxmlLoader.getController(); //In JavaFX, i controller gestiti tramite file FXML vengono automaticamente creati e inizializzati da FXMLLoader

        // Configura attributi della Roulette e dei giocatori
        Semaphore semaphore = new Semaphore(4);
        ArrayList<Giocatore> giocatori = creaGiocatori(semaphore);
        helloController.setGiocatori(giocatori);
        helloController.initializeGiocatoriBox();
        Roulette roulette = new Roulette(5000, semaphore.availablePermits(), semaphore, helloController);
        //Struttura Costruttore: cassa, numero giocatori, semaforo condiviso per la gestione della rotazione, controller per modifiche grafiche, array di giocatori

        avviaThreads(roulette, giocatori);

        stage.setTitle("DRAWN ROULETTE");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.getIcons().add(new Image(HelloApplication.class.getResource("/com/example/roulettedemo/img/logo.png").toString()));
        stage.show();
    }

    private ArrayList<Giocatore> creaGiocatori(Semaphore semaphore) {
        ArrayList<Giocatore> giocatori = new ArrayList<>();
        String[] nomi = {"a", "b", "c", "d"};
        double saldoIniziale = 340.00;

        for (String nome : nomi) {
            giocatori.add(new Giocatore(nome, saldoIniziale, null, semaphore));
        }
        return giocatori;
    }

    private void avviaThreads(Roulette roulette, List<Giocatore> giocatori) {
        roulette.start();
        for (Giocatore giocatore : giocatori) {
            giocatore.setRouletteAttuale(roulette); // Imposta la Roulette per ogni giocatore
            giocatore.start();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
