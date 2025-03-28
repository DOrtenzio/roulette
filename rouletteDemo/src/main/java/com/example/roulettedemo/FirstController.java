package com.example.roulettedemo;

import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class FirstController {

    @FXML
    private AnchorPane box1, box2, box3, box4, box5, root;
    @FXML
    private TextField t1, t2;
    @FXML
    private Label giocPresenti;
    @FXML
    private Button b1, b2, b3, b4, b5;

    private ArrayList<Giocatore> giocatori;

    public FirstController() {
        this.giocatori = new ArrayList<>();
    }

    @FXML
    public void initialize() {
        // Configurazione degli stili dei pulsanti per gli eventi di hover
        configuraBottoneSopra(b1,"e7e7ea");
        configuraBottoneSopra(b2,"e7e7ea");
        configuraBottoneSopra(b3,"e7e7ea");
        configuraBottoneSopra(b4,"e7e7ea");
        configuraBottoneSopra(b5,"bfc2ca");
    }

    /*
      Configura gli stili per i pulsanti al passaggio del mouse.
     */
    private void configuraBottoneSopra(Button button,String coloreBackground) {
        button.setOnMouseMoved(event -> button.setStyle("-fx-border-color: #24292f; -fx-border-radius: 16; -fx-background-radius: 16; -fx-background-color: #FFECA1; -fx-font-family: 'Goudy Stout'; -fx-font-size: 10;"));
        button.setOnMouseExited(event -> button.setStyle("-fx-border-color: #24292f; -fx-border-radius: 16; -fx-background-radius: 16; -fx-background-color:"+coloreBackground+"; -fx-font-family: 'Goudy Stout'; -fx-font-size: 10;"));
    }


    // Effettua l'animazione iniziale di entrata dei box principali.
    @FXML
    public void entrataIniziale() {
        entrataAnchor(box1, -300, 0);
        entrataAnchor(box4, 1028, 0);
    }


    // Mostra il box per aggiungere un nuovo giocatore.
    @FXML
    public void aggiungiGiocatore() {
        box2.setVisible(true);
        box2.setDisable(false);
        entrataAnchor(box2, -300, 0);
    }


    // Aggiunge un nuovo giocatore all'elenco e aggiorna l'interfaccia.
    @FXML
    public void inserimentoGiocArray() {
        try {
            if (!t1.getText().isBlank() && !t2.getText().isBlank()) {
                giocatori.add(new Giocatore(t1.getText(), Double.parseDouble(t2.getText()), null, null));
                giocPresenti.setText(giocPresenti.getText() + "\n >> " + t1.getText());
                t1.clear();
                t2.clear();

                // Mostra il box3 solo al primo inserimento ===>  box3 == inizio partita
                if (giocatori.size() == 1) {
                    box3.setVisible(true);
                    box3.setDisable(false);
                    entrataAnchor(box3, 1028, 0);
                }
            } else {
                errore();
            }
        } catch (NumberFormatException e) {
            errore();
        }
    }

     // Mostra un messaggio di errore temporaneo sul box1.
    @FXML
    private void errore() {
        box1.setStyle("-fx-background-color: #bc0000; -fx-background-radius: 0 12 12 0; -fx-border-color: #24292f; -fx-border-radius: 0 12 12 0; -fx-border-width: 2 2 2 0;");
        entrataAnchor(box1, -300, 0);

        PauseTransition pausa = new PauseTransition(Duration.seconds(3));
        pausa.setOnFinished(e -> {
            box1.setStyle("-fx-background-color: bfc2ca; -fx-background-radius: 0 12 12 0; -fx-border-color: #24292f; -fx-border-radius: 0 12 12 0; -fx-border-width: 2 2 2 0;");
            entrataAnchor(box1, -300, 0);
        });
        pausa.play();
    }


    // Mostra il box di conferma.
    @FXML
    private void isSicuro() {
        disabilitaTutto();

        box5.setVisible(true);
        box5.setDisable(false);
        entrataAnchor(box5, -300, 0);
    }


    // Nasconde il box di conferma e riattiva gli altri box.
    @FXML
    private void nonSicuro() {
        abilitaTutto();
        entrataAnchor(box5, 0, -300);
        box5.setVisible(false);
        box5.setDisable(true);
    }

    /*
     Esegue l'animazione di entrata per un AnchorPane.
     param ---> anchorPane Il pannello da animare.
     param ---> xIn Posizione iniziale.
     param ---> xFin Posizione finale.
     */
    @FXML
    private void entrataAnchor(AnchorPane anchorPane, int xIn, int xFin) {
        anchorPane.setTranslateX(xIn);

        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(1), anchorPane);
        translateTransition.setFromX(xIn);
        translateTransition.setToX(xFin);
        translateTransition.setCycleCount(1);
        translateTransition.setAutoReverse(false);
        translateTransition.play();
    }


    // Passa alla vista "hello-view2" e avvia i thread dei giocatori.
    @FXML
    private void cambiaAHelloView2(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("hello-view2.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 728, 568);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        HelloController helloController = fxmlLoader.getController();

        // Configura Roulette e giocatori
        Semaphore semaphore = new Semaphore(giocatori.size());
        helloController.setGiocatori(giocatori);
        helloController.inserimentoGiocatoriChoiceBox();

        Roulette roulette = new Roulette(50000.0, semaphore.availablePermits(), semaphore, helloController);
        avviaThreads(roulette, giocatori, semaphore);

        stage.setResizable(false);
        stage.getIcons().add(new Image(HelloApplication.class.getResource("/com/example/roulettedemo/img/logo.png").toString()));
        stage.setScene(scene);
    }

    /*
     Avvia i thread per la Roulette e per ogni giocatore.
     param ---> roulette La roulette da avviare.
     param ---> giocatori L'elenco dei giocatori.
     param ---> semaphore Il semaforo condiviso.
     */
    private void avviaThreads(Roulette roulette, ArrayList<Giocatore> giocatori, Semaphore semaphore) {
        roulette.start();
        for (Giocatore giocatore : giocatori) {
            giocatore.setRouletteAttuale(roulette);
            giocatore.setProntiAlGioco(semaphore);
            giocatore.start();
        }
    }

    // Disabilita tutti i box x conferma avvio
    private void disabilitaTutto() {
        box1.setDisable(true);
        box2.setDisable(true);
        box3.setDisable(true);
        box4.setDisable(true);
    }

    // Abilita tutti i box
    private void abilitaTutto() {
        box1.setDisable(false);
        box2.setDisable(false);
        box3.setDisable(false);
        box4.setDisable(false);
    }
}
