package com.example.roulettedemo;

import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class FirstController {
    @FXML
    private AnchorPane box1,box2,box3,box4,box5,root;
    @FXML
    private TextField t1,t2;
    @FXML
    private Label giocPresenti;

    private ArrayList<Giocatore> giocatori;

    //Costruttore
    public FirstController(){
        this.giocatori=new ArrayList<Giocatore>();
    }

    //Metodi
    @FXML
    public void entrataIniziale(){
        //Entrata box principali
        entrataAnchor(box1,-300, 0);
        entrataAnchor(box4,1028, 0);
    }

    @FXML
    public void aggiungiGiocatore(){
        box2.setVisible(true);
        box2.setDisable(false);
        entrataAnchor(box2,-300, 0);
    }

    @FXML
    public void inserimentoGiocArray(){
        try {
            if (!t1.getText().isBlank() && !t2.getText().isBlank()) {
                giocatori.add(new Giocatore(t1.getText(), Double.parseDouble(t2.getText()), null, null));
                giocPresenti.setText(giocPresenti.getText() + "\n >> " + t1.getText());
                t1.clear();
                t2.clear();
            } else
                errore();
        } catch (Exception e) {
            errore();
        }
        if ((giocatori.size()-1) == 0){ //Solo per primo inserito
            box3.setVisible(true);
            box3.setDisable(false);
            entrataAnchor(box3,1028, 0);
        }
    }
    @FXML
    private void errore(){
        box1.setStyle("-fx-background-color:  #bc0000; -fx-background-radius: 0 12 12 0; -fx-border-color: #24292f; -fx-border-radius: 0 12 12 0; -fx-border-width: 2 2 2 0; -fx-font-family: 'Goudy Stout'; -fx-font-size: 9;");
        entrataAnchor(box1,-300, 0);
        PauseTransition pausa = new PauseTransition(Duration.seconds(3));
        pausa.setOnFinished(e -> {
            box1.setStyle("-fx-background-color: bfc2ca; -fx-background-radius: 0 12 12 0; -fx-border-color: #24292f; -fx-border-radius: 0 12 12 0; -fx-border-width: 2 2 2 0;");
            entrataAnchor(box1,-300 ,0);
        });
        pausa.play();
    }
    @FXML
    private void isSicuro(){
        box1.setDisable(true);
        box2.setDisable(true);
        box3.setDisable(true);
        box4.setDisable(true);

        box5.setVisible(true);
        box5.setDisable(false);
        entrataAnchor(box5,-300,0);
    }

    @FXML
    private void nonSicuro(){
        box1.setDisable(false);
        box2.setDisable(false);
        box3.setDisable(false);
        box4.setDisable(false);

        entrataAnchor(box5,0,-300);
        box5.setVisible(false);
        box5.setDisable(true);
    }

    @FXML
    private void entrataAnchor(AnchorPane anchorPane, int xIn,int xFin){
        // Posizione iniziale fuori dallo schermo
        anchorPane.setTranslateX(xIn);

        // Creazione del TranslateTransition
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(1), anchorPane);
        translateTransition.setFromX(xIn); // Valore iniziale
        translateTransition.setToX(xFin);     // Valore finale
        translateTransition.setCycleCount(1); // Nessun ciclo ripetuto
        translateTransition.setAutoReverse(false); // Non invertire l'animazione

        // Avvia l'animazione
        translateTransition.play();
    }

    @FXML
    private void switchToHelloView2(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("hello-view2.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 728, 568);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        HelloController helloController = fxmlLoader.getController(); //In JavaFX, i controller gestiti tramite file FXML vengono automaticamente creati e inizializzati da FXMLLoader

        // Configura attributi della Roulette e dei giocatori
        Semaphore semaphore = new Semaphore(this.giocatori.size());
        helloController.setGiocatori(this.giocatori);
        helloController.initializeGiocatoriBox();
        Roulette roulette = new Roulette(50000.0, semaphore.availablePermits(), semaphore, helloController);
        //Struttura Costruttore: cassa, numero giocatori, semaforo condiviso per la gestione della rotazione, controller per modifiche grafiche, array di giocatori

        avviaThreads(roulette, this.giocatori,semaphore);

        stage.setResizable(false);
        stage.getIcons().add(new Image(HelloApplication.class.getResource("/com/example/roulettedemo/img/logo.png").toString()));
        stage.setScene(scene);
    }

    //Gestionali thread

    private void avviaThreads(Roulette roulette, ArrayList<Giocatore> giocatori, Semaphore semaphore) {
        roulette.start();
        for (Giocatore giocatore : giocatori) {
            giocatore.setRouletteAttuale(roulette); // Imposta la Roulette per ogni giocatore
            giocatore.setProntiAlGioco(semaphore); //Imposto il semaforo condiviso

            giocatore.start();
        }
    }
}
