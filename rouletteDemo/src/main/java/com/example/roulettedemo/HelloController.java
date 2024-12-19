package com.example.roulettedemo;

import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.ArrayList;

public class HelloController {
    @FXML
    private ChoiceBox<String> selectGiocatore;
    @FXML
    private AnchorPane rootDinamica, root;
    @FXML
    private StackPane wheelContainer;

    private ArrayList<Giocatore> giocatori;

    //Get e set
    public void setGiocatori(ArrayList<Giocatore> giocatori) {
        this.giocatori = giocatori;
    }

    //Metodi puntata
    @FXML
    private void puntoSuDiTe(Giocatore giocatore, ChoiceBox<String> puntataBox, ChoiceBox<String> denaroBox) {
        giocatore.premiPulsante(Double.parseDouble(denaroBox.getValue()), puntataBox.getValue());
    }

    //Metodi gestionali
    @FXML
    public void caricaCampoGiocatore(Giocatore giocatore) {
        rootDinamica.getChildren().clear();

        ChoiceBox<String> p = new ChoiceBox<String>();
        ChoiceBox<String> d = new ChoiceBox<String>();
        Label l1 = new Label("TEMP: "+giocatore.getIdentificativo());
        Label l2 = new Label("Inserire su cosa puntare:");
        Label l3 = new Label("Inserire denaro: ");
        Button b = new Button("Punta");
        b.setStyle("-fx-border-color: #24292f; -fx-border-radius: 16; -fx-background-radius: 16; -fx-background-color: e7e7ea;");

        rootDinamica.getChildren().addAll(l1, l2, p, l3, d, b);

        l1.setLayoutX(14);
        l1.setLayoutY(14);
        l2.setLayoutX(52);
        l2.setLayoutY(40);
        p.setLayoutX(52);
        p.setLayoutY(58);
        l3.setLayoutX(52);
        l3.setLayoutY(103);
        d.setLayoutX(52);
        d.setLayoutY(120);
        b.setLayoutX(286);
        b.setLayoutY(353);

        System.out.println("Inserimento valori x giocatore: "+giocatore.getIdentificativo());
        fasePreScommessa(p,d,giocatore);

        p.setDisable(true);
        d.setDisable(true);
        b.setDisable(true);

        if (giocatore.getPuntataCorrente() == null){
            p.setDisable(false);
            d.setDisable(false);
            b.setDisable(false);
        }

        b.setOnMouseClicked(e -> {
            puntoSuDiTe(giocatore, p, d);
            rootDinamica.getChildren().clear();
        });
    }

    //Metodi di modifica ed strutturazione delle choicebox
    @FXML
    private void inserisciOpzioniPuntata(ChoiceBox<String> choiceBox, int maxNumber) {
        choiceBox.getItems().clear();
        for (int i = 0; i <= maxNumber; i++) {
            choiceBox.getItems().add(String.valueOf(i));
        }
        choiceBox.getItems().addAll("Verde", "Rosso", "Nero", "ro", "r1", "r2", "r3", "q0", "q1", "q2", "q3", "m0", "m1", "m2", "p", "d");
    }

    @FXML
    private void inserisciOpzioniDenaro(ChoiceBox<String> boxDenaro, Giocatore giocatore) {
        boxDenaro.getItems().clear();
        for (int p = 10; p <= giocatore.getCassaPersonale(); p += 50) {
            boxDenaro.getItems().add(String.valueOf(p));
        }
    }

    @FXML
    private void fasePreScommessa(ChoiceBox<String> boxPuntata, ChoiceBox<String> boxDenaro, Giocatore giocatore) {
        inserisciOpzioniPuntata(boxPuntata, 36);
        inserisciOpzioniDenaro(boxDenaro, giocatore);
    }



    //Inizializzazioni

    @FXML
    public void initializeGiocatoriBox() {
        System.out.println(">  Inseriti identificativi nel menù di scelta.");
        selectGiocatore.getItems().clear();
        for (int i = 0; i < giocatori.size(); i++) {
            selectGiocatore.getItems().add(giocatori.get(i).getIdentificativo());
        }
    }

    @FXML
    public void initialize() {
        // Aggiungi un listener alla lista degli elementi della ChoiceBox
        selectGiocatore.getItems().addListener((javafx.collections.ListChangeListener.Change<? extends String> change) -> {
            while (change.next()) {
                if (!selectGiocatore.getItems().isEmpty()) {
                    // Quando gli elementi sono disponibili, aggiungi il listener alla selezione
                    selectGiocatore.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
                        if (newValue != null) {
                            int selectedIndex = selectGiocatore.getSelectionModel().getSelectedIndex();
                            caricaCampoGiocatore(giocatori.get(selectedIndex));
                        }
                    });
                }
            }
        });
    }



    //RUOTA
    @FXML
    public void rotate() {
        // Animazione di rotazione applicata solo alla ruota
        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(6), wheelContainer);
        rotateTransition.setByAngle(720 + (int) (Math.random() * 360));
        rotateTransition.setCycleCount(1);

        rotateTransition.play();
    }

    @FXML
    public void sovrastaWheel(int estratto) {
        // Creazione del quadrante
        Rectangle quadrante = new Rectangle(170, 170);
        quadrante.setArcWidth(16);
        quadrante.setArcHeight(16);
        quadrante.setFill(Color.web("#00FF00", 0.9)); // Colore di sfondo verde trasparente
        quadrante.setLayoutX(270); // Posizione centrata rispetto alla ruota
        quadrante.setLayoutY(209);

        // Creazione del messaggio
        Label messaggioConferma = new Label("Estratto: " + estratto);
        messaggioConferma.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");
        messaggioConferma.setAlignment(Pos.CENTER);
        messaggioConferma.setPrefWidth(170); // Larghezza uguale al quadrante
        messaggioConferma.setPrefHeight(170); // Altezza uguale al quadrante
        messaggioConferma.setLayoutX(270);
        messaggioConferma.setLayoutY(209);


        // Aggiunta del quadrante e del messaggio al contenitore
        root.getChildren().addAll(quadrante, messaggioConferma);

        // Timer per rimuovere gli elementi dopo 5 secondi
        PauseTransition pausa = new PauseTransition(Duration.seconds(3));
        pausa.setOnFinished(e -> {
            root.getChildren().removeAll(quadrante, messaggioConferma);
        });
        pausa.play();

    }
}
