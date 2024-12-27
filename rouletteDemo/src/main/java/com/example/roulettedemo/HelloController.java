package com.example.roulettedemo;

import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.util.ArrayList;

public class HelloController {
    @FXML
    private ChoiceBox<String> selectGiocatore;
    @FXML
    private AnchorPane rootDinamica, root;
    @FXML
    private StackPane wheelContainer;

    private static final int[] ROSSI = {1, 3, 5, 7, 9, 12, 14, 16, 18, 19, 21, 23, 25, 27, 30, 32, 34, 36};
    private ArrayList<Giocatore> giocatori;
    private String puntata;

    //Get e set
    public void setGiocatori(ArrayList<Giocatore> giocatori) {
        this.giocatori = giocatori;
    }

    //Metodi puntata
    @FXML
    private void puntoSuDiTe(Giocatore giocatore, String puntata, ChoiceBox<String> denaroBox) {
        giocatore.premiPulsante(Double.parseDouble(denaroBox.getValue()), puntata);
    }

    //Metodi gestionali
    @FXML
    public void caricaCampoGiocatore(Giocatore giocatore) {
        rootDinamica.getChildren().clear();

        VBox tabellone=creaTabellone();
        ChoiceBox<String> d = new ChoiceBox<String>();
        Label l3 = new Label("Inserire denaro: ");
        Button b = new Button("Punta");
        b.setStyle("-fx-border-color: #24292f; -fx-border-radius: 16; -fx-background-radius: 16; -fx-background-color: e7e7ea;");

        rootDinamica.getChildren().addAll(tabellone,l3, d, b);

        l3.setLayoutX(286);
        l3.setLayoutY(222);
        d.setLayoutX(52);
        d.setLayoutY(120);
        b.setLayoutX(286);
        b.setLayoutY(353);

        System.out.println("Inserimento valori x giocatore: "+giocatore.getIdentificativo());
        fasePreScommessa(d,giocatore);

        tabellone.setDisable(true);
        d.setDisable(true);
        b.setDisable(true);

        if (giocatore.getPuntataCorrente() == null){
            tabellone.setDisable(false);
            d.setDisable(false);
            b.setDisable(false);
        }

        b.setOnMouseClicked(e -> {
            puntoSuDiTe(giocatore, this.puntata, d);
            rootDinamica.getChildren().clear();
        });
    }

    //Metodi di modifica ed strutturazione delle choicebox
    //TODO: TEMPORANEI
    @FXML
    private void inserisciOpzioniDenaro(ChoiceBox<String> boxDenaro, Giocatore giocatore) {
        boxDenaro.getItems().clear();
        for (int p = 10; p <= giocatore.getCassaPersonale(); p += 50) {
            boxDenaro.getItems().add(String.valueOf(p));
        }
    }

    @FXML
    private void fasePreScommessa(ChoiceBox<String> boxDenaro, Giocatore giocatore) {
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


    //METODI TABELLONE GIOCATORE
    public VBox creaTabellone(){
        VBox tabellonePrincipale = new VBox(); // layoutX="24.0" layoutY="54.0" prefHeight="297.0" prefWidth="411.0"
            tabellonePrincipale.setLayoutX(24);
            tabellonePrincipale.setLayoutY(54);
            tabellonePrincipale.setPrefSize(411, 297);

        // ParteNumeri
        HBox hbox1 = new HBox();
            hbox1.setPrefSize(412, 244);
            hbox1.setStyle("-fx-background-color: #1e860e; -fx-background-radius: 16; -fx-border-color: #24292f; -fx-border-radius: 8; -fx-border-width: 1;");

        //BOTTONE ZERO
        Button p0 = creaBottone("0", 23, 188, "#1e860e", 8, "WHITE"); //Zero
        p0.setId("p0");
        p0.setOnAction(this::selezionaPuntata); //Assegno al bottone su cui si richiama il set il metodo

        //PARTE SCOMMESSE CON SINGOLI NUMERI
        VBox numDa1A36 = new VBox();
            numDa1A36.setPrefSize(393, 181);

            for (int riga = 0; riga < 3; riga++) {

                HBox hboxRiga = new HBox();
                hboxRiga.setPrefSize(364, 65);

                for (int colonne = 0; colonne < 12; colonne++) {
                    int num = colonne * 3 + riga + 1; //Calcolo numero in base alla colonna

                    String color; //Ricavo il colore rosso o nero
                    if (isRosso(num))
                        color = "#bc0000";
                    else
                        color="#24292f";

                    Button bottoneDelNumero = creaBottone(String.valueOf(num), 28, 65, color, 8, "WHITE");
                    bottoneDelNumero.setFont(Font.font(11));
                    bottoneDelNumero.setId("p"+num);
                    bottoneDelNumero.setOnAction(this::selezionaPuntata); //Assegno al bottone su cui si richiama il set il metodo
                    hboxRiga.getChildren().add(bottoneDelNumero);
                }

                //BOTTONE LATERALE -->
                Button bottone2to1 = creaBottoneConBordo("2 to 1", 56, 65, "#e7e7ea", 2, "BLACK");
                bottone2to1.setFont(Font.font(11));
                bottone2to1.setId("r"+riga);
                bottone2to1.setOnAction(this::selezionaPuntata); //Assegno al bottone su cui si richiama il set il metodo

                hboxRiga.getChildren().add(bottone2to1);

                numDa1A36.getChildren().add(hboxRiga); //Riga completa
            }
            hbox1.setStyle("-fx-background-color: #1e860e; -fx-background-radius: 16; -fx-border-color: #24292f; -fx-border-radius: 8; -fx-border-width: 1;");

            hbox1.getChildren().addAll(p0, numDa1A36);
            tabellonePrincipale.getChildren().add(hbox1);

        //PARTE SCOMMESSE SETTORIALI
        HBox hbox2 = new HBox();
            hbox2.setPrefSize(411, 138);

        Pane spazio = new Pane();
            spazio.setPrefSize(22, 80);

        Button primi12 = creaBottoneConBordo("1st 12", 109, 80, "#e7e7ea", 2, "BLACK");
        primi12.setId("q0");
        primi12.setOnAction(this::selezionaPuntata); //Assegno al bottone su cui si richiama il set il metodo
        Button secondi12 = creaBottoneConBordo("2nd 12", 112, 80, "#e7e7ea", 2, "BLACK");
        secondi12.setId("q1");
        secondi12.setOnAction(this::selezionaPuntata); //Assegno al bottone su cui si richiama il set il metodo
        Button terzi12 = creaBottoneConBordo("3rd 12", 112, 80, "#e7e7ea", 2, "BLACK");
        terzi12.setId("q2");
        terzi12.setOnAction(this::selezionaPuntata); //Assegno al bottone su cui si richiama il set il metodo

        hbox2.getChildren().addAll(spazio, primi12, secondi12, terzi12);
        tabellonePrincipale.getChildren().add(hbox2);

        // PARTE SCOMMESSE DIVISIONI
        HBox hbox3 = new HBox();
            hbox3.setPrefSize(200, 100);

        Pane spazio2 = new Pane();
            spazio2.setPrefSize(22, 80);

            Button primi18 = creaBottoneConBordo("1 to 18", 55, 53, "#e7e7ea", 2, "BLACK");
            primi18.setId("m1");
            primi18.setOnAction(this::selezionaPuntata); //Assegno al bottone su cui si richiama il set il metodo
            Button pari = creaBottoneConBordo("Even", 55, 47, "#e7e7ea", 2, "BLACK");
            pari.setId("p");
            pari.setOnAction(this::selezionaPuntata); //Assegno al bottone su cui si richiama il set il metodo
            Button rossi = creaBottoneConBordo("", 55, 45, "#bc0000", 2, "RED");
            rossi.setId("Rosso");
            rossi.setOnAction(this::selezionaPuntata); //Assegno al bottone su cui si richiama il set il metodo
            Button neri = creaBottoneConBordo("", 54.5, 102, "#24292f", 2, "BLACK");
            neri.setId("Nero");
            neri.setOnAction(this::selezionaPuntata); //Assegno al bottone su cui si richiama il set il metodo
            Button dispari = creaBottoneConBordo("Odd", 54.5, 103, "#e7e7ea", 2, "BLACK");
            dispari.setId("d");
            dispari.setOnAction(this::selezionaPuntata); //Assegno al bottone su cui si richiama il set il metodo
            Button ultimi18 = creaBottoneConBordo("19 to 36", 58, 46, "#e7e7ea", 2, "BLACK");
            primi18.setId("m2");
            primi18.setOnAction(this::selezionaPuntata); //Assegno al bottone su cui si richiama il set il metodo

            hbox3.getChildren().addAll(spazio2, primi18, pari, rossi, neri, dispari, ultimi18);
            tabellonePrincipale.getChildren().add(hbox3);
        return tabellonePrincipale;
    }

    private Button creaBottone(String text, double width, double height, String color, double radius, String textColor) {
        Button b = new Button(text);
        b.setPrefSize(width, height);
        b.setStyle("-fx-background-color: " + color + "; -fx-background-radius: " + radius + "; -fx-text-fill: " + textColor + ";");
        return b;
    }
    private Button creaBottoneConBordo(String text, double width, double height, String color, double radius, String textColor) {
        Button b = new Button(text);
        b.setPrefSize(width, height);
        b.setStyle("-fx-background-color: " + color + "; -fx-background-radius: " + radius + "; -fx-text-fill: " + textColor + "; -fx-border-radius: 2; -fx-border-width: 1; -fx-border-color:  #24292f");
        return b;
    }
    private boolean isRosso(int numero){
        for (int n : ROSSI) {
            if (n == numero) {
                return true;
            }
        }
        return false;
    }
    private void selezionaPuntata(ActionEvent event) {
        Button clickedButton = (Button) event.getSource(); // Recupera il pulsante cliccato
        String bottSelez=clickedButton.getId();
        if (bottSelez.charAt(0)=='p')
            puntata = bottSelez.substring(1);
        else
            puntata=bottSelez;
        System.out.println("Hai cliccato sul pulsante con ID: " + puntata);
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
