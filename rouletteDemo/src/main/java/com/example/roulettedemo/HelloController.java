package com.example.roulettedemo;

import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
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
    private AnchorPane rootDinamica, root, rootRuota, rootFish, controlRoot,boxRisPuntata,boxDenPuntata;
    @FXML
    private StackPane wheelContainer;
    @FXML
    private Label labelCredito,labelIniziale;

    private static final int[] ROSSI = {1, 3, 5, 7, 9, 12, 14, 16, 18, 19, 21, 23, 25, 27, 30, 32, 34, 36};
    private ArrayList<Giocatore> giocatori;
    private String puntata,denaroPuntato;

    //Get e set
    public void setGiocatori(ArrayList<Giocatore> giocatori) {
        this.giocatori = giocatori;
    }

    //Metodi gestionali
    @FXML
    public void caricaCampoGiocatore(Giocatore giocatore) {
        setCassa(giocatore.getCassaPersonale());
        labelIniziale.setVisible(false);
        rootDinamica.getChildren().clear();

        VBox tabellone=creaTabellone();
        Button b = new Button("Punta");
        b.setStyle("-fx-border-color: #24292f; -fx-border-radius: 16; -fx-background-radius: 16; -fx-background-color: e7e7ea;");
        b.setPrefWidth(143.0);
        b.setPrefHeight(38.0);

        rootDinamica.getChildren().addAll(tabellone,b);

        b.setLayoutX(290);
        b.setLayoutY(7);

        fasePreScommessa(giocatore);

        tabellone.setDisable(true);
        rootFish.setDisable(true);
        b.setDisable(true);

        boxDenPuntata.setVisible(true);
        boxRisPuntata.setVisible(true);
        wheelContainer.setVisible(true);
        controlRoot.setVisible(true);

        entrataAnchor(boxDenPuntata);
        entrataAnchor(boxRisPuntata);
        entrataAnchor(controlRoot);

        if (giocatore.getPuntataCorrente() == null){
            tabellone.setDisable(false);
            rootFish.setDisable(false);
            b.setDisable(false);
        }

        b.setOnMouseClicked(e -> {
            try {
                giocatore.premiPulsante(Double.parseDouble(this.denaroPuntato), this.puntata);
                inserimentoCorretto(); //Avviso visivo all'utente
                rootDinamica.getChildren().clear();
                rootFish.getChildren().clear();

                boxDenPuntata.setVisible(false);
                boxRisPuntata.setVisible(false);
            } catch (Exception error) {
                System.out.println(error);
                errore();
            }
        });
    }

    public void setCassa(double cassa){
        labelCredito.setText("Credito attuale: "+cassa);
    }
    private void errore(){
        controlRoot.setStyle("-fx-background-color:  #bc0000; -fx-background-radius: 0 12 12 0; -fx-border-color: #24292f; -fx-border-radius: 0 12 12 0; -fx-border-width: 2 2 2 0;");
        String s= labelCredito.getText();
        labelCredito.setText("Errore nell'inserimento");
        entrataAnchor(controlRoot);
        PauseTransition pausa = new PauseTransition(Duration.seconds(3));
        pausa.setOnFinished(e -> {
            controlRoot.setStyle("-fx-background-color: bfc2ca; -fx-background-radius: 0 12 12 0; -fx-border-color: #24292f; -fx-border-radius: 0 12 12 0; -fx-border-width: 2 2 2 0;");
            labelCredito.setText(s);
            entrataAnchor(controlRoot);
        });
        pausa.play();
    }
    private void inserimentoCorretto(){
        controlRoot.setStyle("-fx-background-color:  #1e860e; -fx-background-radius: 0 12 12 0; -fx-border-color: #24292f; -fx-border-radius: 0 12 12 0; -fx-border-width: 2 2 2 0;");
        String s= labelCredito.getText();
        labelCredito.setText("Ottimo :)");
        entrataAnchor(controlRoot);
        PauseTransition pausa = new PauseTransition(Duration.seconds(3));
        pausa.setOnFinished(e -> {
            controlRoot.setStyle("-fx-background-color: bfc2ca; -fx-background-radius: 0 12 12 0; -fx-border-color: #24292f; -fx-border-radius: 0 12 12 0; -fx-border-width: 2 2 2 0;");
            labelCredito.setText(s);
            entrataAnchor(controlRoot);
        });
        pausa.play();
    }

    //Metodi di modifica ed strutturazione delle fiches
    @FXML
    private void inserisciOpzioniDenaro(Giocatore giocatore, Button fish1,Button fish2,Button fish3,Button fish4) {
        if (giocatore.getCassaPersonale() >= 50.0 ){
            fish1.setText("10.0");
            fish2.setText("20.0");
            fish3.setText("50.0");
            fish4.setText("All");
        }else{
            fish1.setText(String.valueOf((int)(giocatore.getCassaPersonale()/4)));
            fish2.setText(String.valueOf((int)(giocatore.getCassaPersonale()/3)));
            fish3.setText(String.valueOf((int)(giocatore.getCassaPersonale()/2)));
            fish4.setText(String.valueOf((int)(giocatore.getCassaPersonale())));
        }
    }

    @FXML
    private void fasePreScommessa(Giocatore giocatore) {
        //CREAZIONE FICHES
        String[][] buttonData = {
                {"fish1", "23.6", "14.0", "#B62696"},
                {"fish2", "117.2", "14.0", "#EFC3CA"},
                {"fish3", "210.8", "14.0", "#FE9900"},
                {"fish4", "304.4", "14.0", "#000000"}
        };
        Button [] fiches=new Button[buttonData.length];
        int inserite=0;

        for (String[] data : buttonData) {
            String id = data[0];
            double layoutX = Double.parseDouble(data[1]);
            double layoutY = Double.parseDouble(data[2]);
            String borderColor = data[3];

            // Creazione del pulsante
            Button button = new Button();
            button.setId(id);
            button.setLayoutX(layoutX);
            button.setLayoutY(layoutY);
            button.setPrefHeight(70.0);
            button.setPrefWidth(70.0);
            button.setStyle("-fx-background-radius: 35; -fx-background-color: #E4ECF6; -fx-border-color: " + borderColor + "; -fx-border-radius: 35; -fx-border-width: 10;");
            button.setOnAction(this::selezionaPuntataDenaro); //Assegno al bottone su cui si richiama il set il metodo

            fiches[inserite++]=button;
            rootFish.getChildren().add(button);
        }
        inserisciOpzioniDenaro(giocatore,fiches[0],fiches[1],fiches[2],fiches[3]);
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
        /* Aggiunge un listener alla lista degli elementi della ChoiceBox
         Un listener è un componente che osserva i cambiamenti di uno specifico oggetto o proprietà.
         Quando si verifica un cambiamento, il listener esegue un'azione definita nel suo corpo.*/
        selectGiocatore.getItems().addListener((javafx.collections.ListChangeListener.Change<? extends String> change) -> {
            // Cicla attraverso i cambiamenti nella lista
            while (change.next()) {
                // Controlla se la lista non è vuota
                if (!selectGiocatore.getItems().isEmpty()) {
                    // Quando gli elementi nella ChoiceBox sono disponibili,
                    // aggiunge un listener per monitorare la selezione dell'utente
                    selectGiocatore.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
                        // Se il nuovo elemento selezionato non è nullo
                        if (newValue != null) {
                            // Ottiene l'indice dell'elemento selezionato nella ChoiceBox
                            int selectedIndex = selectGiocatore.getSelectionModel().getSelectedIndex();
                            // Carica le informazioni relative al giocatore selezionato
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
    private void selezionaPuntataDenaro(ActionEvent event) {
        Button clickedButton = (Button) event.getSource(); // Recupera il pulsante cliccato in questo caso fiches
        denaroPuntato=clickedButton.getText();
        System.out.println("Hai puntato su: " + denaroPuntato);
    }


    //RUOTA E TRANSIZIONI
    @FXML
    private void entrataAnchor(AnchorPane anchorPane){
        // Posizione iniziale fuori dallo schermo (fuori dalla vista verso sinistra)
        anchorPane.setTranslateX(-300);

        // Creazione del TranslateTransition
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(1), anchorPane);
        translateTransition.setFromX(-300); // Valore iniziale
        translateTransition.setToX(0);     // Valore finale
        translateTransition.setCycleCount(1); // Nessun ciclo ripetuto
        translateTransition.setAutoReverse(false); // Non invertire l'animazione

        // Avvia l'animazione
        translateTransition.play();
    }

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
