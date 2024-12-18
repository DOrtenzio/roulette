package com.example.roulettedemo;

import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
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
    private ChoiceBox<String> p1, p2, p3, p4, d1, d2, d3, d4; //Pn = Puntata  Dn = Denaro
    @FXML
    private AnchorPane root;
    @FXML
    private StackPane wheelContainer;

    private ArrayList<Giocatore> giocatori;
    private ChoiceBox<String>[] denaroChoice;

    //Get e set
    public void setGiocatori(ArrayList<Giocatore> giocatori) {
        this.giocatori = giocatori;
    }

    //Metodi puntata
    @FXML
    protected void puntoSuDiTe(int index, ChoiceBox<String> puntataBox, ChoiceBox<String> denaroBox) {
        Giocatore giocatore = giocatori.get(index);
        giocatore.premiPulsante(Double.parseDouble(denaroBox.getValue()), puntataBox.getValue());
        denaroBox.setDisable(true);
        puntataBox.setDisable(true);
    }

    @FXML
    protected void puntoSuDiTe1() {
        puntoSuDiTe(0, p1, d1);
    }

    @FXML
    protected void puntoSuDiTe2() {
        puntoSuDiTe(1, p2, d2);
    }

    @FXML
    protected void puntoSuDiTe3() {
        puntoSuDiTe(2, p3, d3);
    }

    @FXML
    protected void puntoSuDiTe4() {
        puntoSuDiTe(3, p4, d4);
    }


    //Metodi gestionali
    @FXML
    public void reableAll() {
        System.out.println("Reabilitazione...");
        for (ChoiceBox<String> box : denaroChoice) {
            box.setDisable(false);
        }
        p1.setDisable(false);
        p2.setDisable(false);
        p3.setDisable(false);
        p4.setDisable(false);
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
    private void inserisciOpzioniDenaro() {
        for (int i = 0; i < giocatori.size(); i++) {
            ChoiceBox<String> choiceBox = denaroChoice[i];
            choiceBox.getItems().clear();
            for (int p = 10; p <= giocatori.get(i).getCassaPersonale(); p += 50) {
                choiceBox.getItems().add(String.valueOf(p));
            }
        }
    }

    @FXML
    public void fasePreScommessa() {
        System.out.println("Fase pre-scommesse");
        denaroChoice = new ChoiceBox[]{d1, d2, d3, d4};
        inserisciOpzioniPuntata(p1, 36);
        inserisciOpzioniPuntata(p2, 36);
        inserisciOpzioniPuntata(p3, 36);
        inserisciOpzioniPuntata(p4, 36);
        inserisciOpzioniDenaro();
    }

    @FXML
    public void rotate(){
        // Animazione di rotazione applicata solo alla ruota
        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(6), wheelContainer);
        rotateTransition.setByAngle(720+(int)(Math.random()*360));
        rotateTransition.setCycleCount(1);

        rotateTransition.play();
    }

    @FXML
    public void sovrastaWheel(int estratto){
        // Creazione del quadrante
        Rectangle quadrante = new Rectangle(170, 170);
        quadrante.setArcWidth(16);
        quadrante.setArcHeight(16);
        quadrante.setFill(Color.web("#00FF00", 0.9)); // Colore di sfondo verde trasparente
        quadrante.setLayoutX(270); // Posizione centrata rispetto alla ruota
        quadrante.setLayoutY(209);

        // Creazione del messaggio
        Label messaggioConferma = new Label("Estratto: "+estratto);
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
