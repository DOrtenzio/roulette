package com.example.roulettedemo;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

import java.util.ArrayList;

public class HelloController{
    @FXML
    private ChoiceBox <String> p1,p2,p3,p4,d1,d2,d3,d4;
    @FXML
    private ChoiceBox [] denaroChoice;
    @FXML
    private Label number;

    private ArrayList<Giocatore> giocatori;

    public void setGiocatori(ArrayList<Giocatore> giocatori){
        this.giocatori= giocatori;
    }

    @FXML
    protected void puntoSuDiTe1(){
        giocatori.get(0).premiPulsante(Double.parseDouble(d1.getValue()),p1.getValue());
        d1.setDisable(true);
        p1.setDisable(true);
    }
    @FXML
    protected void puntoSuDiTe2(){
        giocatori.get(1).premiPulsante(Double.parseDouble(d2.getValue()),p2.getValue());
        d2.setDisable(true);
        p2.setDisable(true);
    }
    @FXML
    protected void puntoSuDiTe3(){
        giocatori.get(2).premiPulsante(Double.parseDouble(d3.getValue()),p3.getValue());
        d3.setDisable(true);
        p3.setDisable(true);
    }
    @FXML
    protected void puntoSuDiTe4(){
        giocatori.get(3).premiPulsante(Double.parseDouble(d4.getValue()),p4.getValue());
        d4.setDisable(true);
        p4.setDisable(true);
    }

    @FXML
    public void reableAll(){
        System.out.println("Reabilitazione...");
        d1.setDisable(false);
        p1.setDisable(false);
        d2.setDisable(false);
        p2.setDisable(false);
        d3.setDisable(false);
        p3.setDisable(false);
        d4.setDisable(false);
        p4.setDisable(false);
    }
    @FXML
    protected void cambioNum(int numero){
        System.out.println("Cambio numero...");
        number.setText(String.valueOf(numero));
    }

    @FXML
    protected void initializeChoiceBox(ChoiceBox <String> choiceBox){
        choiceBox.getItems().clear();
        for (int i=0;i<37;i++){
            choiceBox.getItems().add(String.valueOf(i));
        }
        choiceBox.getItems().addAll("Verde","Rosso","Nero","ro","r1","r2","r3","q0","q1","q2","q3","m0","m1","m2","p","d");
    }
    @FXML
    protected void initializeChoiceBoxDenaro(){
        for (int i=0; i< giocatori.size();i++){
            ChoiceBox <String> choiceBox=denaroChoice[i];
            choiceBox.getItems().clear();
            for (int p = 10; p<= giocatori.get(i).getCassaPersonale(); p+=50){
                choiceBox.getItems().add(String.valueOf(p));
            }
        }
    }

    @FXML
    public void faseIniziale(){
        System.out.println("Fase iniziale...");
        denaroChoice = new ChoiceBox[]{d1, d2, d3, d4};
        initializeChoiceBox(p1);
        initializeChoiceBox(p2);
        initializeChoiceBox(p3);
        initializeChoiceBox(p4);
        initializeChoiceBoxDenaro();
    }
}