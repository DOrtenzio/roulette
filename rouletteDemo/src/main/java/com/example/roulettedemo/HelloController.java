package com.example.roulettedemo;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

import java.util.ArrayList;

public class HelloController {
    @FXML
    private ChoiceBox<String> p1, p2, p3, p4, d1, d2, d3, d4; //Pn = Puntata  Dn = Denaro
    @FXML
    private Label number; //Numero estratto

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

    @FXML
    protected void cambioNum(int numero) {
        System.out.println("Cambio numero...");
        number.setText(String.valueOf(numero));
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
}
