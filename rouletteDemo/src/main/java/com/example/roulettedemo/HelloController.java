package com.example.roulettedemo;

import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.util.ArrayList;

public class HelloController {
    @FXML
    private ChoiceBox<String> selectGiocatore;
    @FXML
    private AnchorPane rootDinamica, root, rootFish, controlRoot,boxRisPuntata,boxDenPuntata,ball;
    @FXML
    private StackPane wheelContainer;
    @FXML
    private Label labelCredito,labelIniziale,labelPuntato,labelSoldiPuntato;
    @FXML
    private Button auto;

    private static final int[] ROSSI = {1, 3, 5, 7, 9, 12, 14, 16, 18, 19, 21, 23, 25, 27, 30, 32, 34, 36};
    private ArrayList<Giocatore> giocatori;
    private String puntata,denaroPuntato;
    private int posizionePallina;

    /*GET E SET*/
    public void setGiocatori(ArrayList<Giocatore> giocatori) {
        this.giocatori = giocatori;
        this.posizionePallina=0;
    }

    /*METODI PER LA GESTIONE DEL TURNO*/
    @FXML
    public void caricaFunzioniDelGiocatore(Giocatore giocatore) {
        setCassa(giocatore.getCassaPersonale());
        labelIniziale.setVisible(false);
        rootDinamica.getChildren().clear();
        Label labelBenvenuto=new Label("PRONTO/A ?");
        labelBenvenuto.setStyle("-fx-font-family: 'Goudy Stout'; -fx-font-size: 11;");
        labelBenvenuto.setLayoutY(14);
        labelBenvenuto.setLayoutX(18);
        labelBenvenuto.setPrefWidth(244);
        labelBenvenuto.setPrefHeight(30);

        VBox tabellone=creaTabellone();
        Button b = new Button("Punta");
        b.setStyle("-fx-border-color: #24292f; -fx-border-radius: 16; -fx-background-radius: 16; -fx-background-color: bfc2ca; -fx-font-family: 'Goudy Stout'; -fx-font-size: 10;");
        b.setPrefWidth(143.0);
        b.setPrefHeight(38.0);
        b.setLayoutX(290);
        b.setLayoutY(7);

        //Metodo per cambio colore quando mouse sovrappone
        b.setOnMouseMoved(event -> {
            b.setStyle("-fx-border-color: #24292f; -fx-border-radius: 16; -fx-background-radius: 16; -fx-background-color: #FFECA1; -fx-font-family: 'Goudy Stout'; -fx-font-size: 10;");
        });
        b.setOnMouseExited(event -> {
            b.setStyle("-fx-border-color: #24292f; -fx-border-radius: 16; -fx-background-radius: 16; -fx-background-color: bfc2ca; -fx-font-family: 'Goudy Stout'; -fx-font-size: 10;");
        });

        Button b1 = new Button("Ritirati");
        b1.setStyle("-fx-border-color: #24292f; -fx-border-radius: 16; -fx-background-radius: 16; -fx-background-color: e1e2e6; -fx-font-family: 'Goudy Stout'; -fx-font-size: 10;");
        b1.setPrefWidth(143.0);
        b1.setPrefHeight(38.0);
        b1.setLayoutX(137.0);
        b1.setLayoutY(7);

        //Metodo per cambio colore quando mouse sovrappone
        b1.setOnMouseMoved(event -> {
            b1.setStyle("-fx-border-color: #24292f; -fx-border-radius: 16; -fx-background-radius: 16; -fx-background-color: #FFECA1; -fx-font-family: 'Goudy Stout'; -fx-font-size: 10;");
        });
        b1.setOnMouseExited(event -> {
            b1.setStyle("-fx-border-color: #24292f; -fx-border-radius: 16; -fx-background-radius: 16; -fx-background-color: e1e2e6; -fx-font-family: 'Goudy Stout'; -fx-font-size: 10;");
        });

        rootDinamica.getChildren().addAll(labelBenvenuto,tabellone,b,b1);

        creaFiches(giocatore);

        tabellone.setDisable(true);
        rootFish.setDisable(true);
        b1.setDisable(true);
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
            b1.setDisable(false);
            b.setDisable(false);
        }

        b.setOnMouseClicked(e -> {
            confermaAzionePuntata(giocatore,b1,tabellone);
        });

        b1.setOnMouseClicked(e -> {
            inserimentoCorretto(giocatore);
            giocatore.ritirati();
            selectNoGiocatore();
        });
    }

    @FXML
    public void confermaAzionePuntata(Giocatore giocatore, Button b, VBox tabellone) {
        tabellone.setDisable(true);
        rootFish.setDisable(true);
        b.setDisable(true);

        // Creazione dell'AnchorPane
        AnchorPane box5 = new AnchorPane();
        box5.setId("box5");
        box5.setDisable(true);
        box5.setLayoutX(219.0);
        box5.setLayoutY(214.0);
        box5.setPrefHeight(142.0);
        box5.setPrefWidth(283.0);
        box5.setStyle("-fx-background-color: e7e7ea; " +
                "-fx-background-radius: 12; " +
                "-fx-border-color: #24292f; " +
                "-fx-border-radius: 12; " +
                "-fx-border-width: 2;");
        box5.setVisible(false);

        // Creazione del primo pulsante (No)
        Button buttonNo = new Button("No");
        buttonNo.setLayoutX(42.0);
        buttonNo.setLayoutY(83.0);
        buttonNo.setMnemonicParsing(false);
        buttonNo.setPrefHeight(42.0);
        buttonNo.setPrefWidth(99.0);
        buttonNo.setStyle("-fx-border-color: #24292f; " +
                "-fx-border-radius: 16; " +
                "-fx-background-radius: 16; " +
                "-fx-background-color: #e1e2e6; " +
                "-fx-font-family: 'Goudy Stout'; " +
                "-fx-font-size: 10;"
        );
        buttonNo.setWrapText(true);
        buttonNo.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        buttonNo.setOnAction(event ->{
            box5.setVisible(false);
            box5.setDisable(true);

            tabellone.setDisable(false);
            rootFish.setDisable(false);
            b.setDisable(false);
        });
        //Metodo per cambio colore quando mouse sovrappone
        buttonNo.setOnMouseMoved(event -> {
            buttonNo.setStyle("-fx-border-color: #24292f; " +
                    "-fx-border-radius: 16; " +
                    "-fx-background-radius: 16; " +
                    "-fx-background-color: #FFECA1; " +
                    "-fx-font-family: 'Goudy Stout'; " +
                    "-fx-font-size: 10;");
        });
        buttonNo.setOnMouseExited(event -> {
            buttonNo.setStyle("-fx-border-color: #24292f; " +
                    "-fx-border-radius: 16; " +
                    "-fx-background-radius: 16; " +
                    "-fx-background-color: #e1e2e6; " +
                    "-fx-font-family: 'Goudy Stout'; " +
                    "-fx-font-size: 10;");
        });

        // Creazione del secondo pulsante (Si)
        Button buttonSi = new Button("Si");
        buttonSi.setLayoutX(154.0);
        buttonSi.setLayoutY(82.0);
        buttonSi.setMnemonicParsing(false);
        buttonSi.setPrefHeight(42.0);
        buttonSi.setPrefWidth(99.0);
        buttonSi.setStyle("-fx-border-color: #24292f; " +
                "-fx-border-radius: 16; " +
                "-fx-background-radius: 16; " +
                "-fx-background-color: bfc2ca; " +
                "-fx-font-family: 'Goudy Stout'; " +
                "-fx-font-size: 10;");
        buttonSi.setWrapText(true);
        buttonSi.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        buttonSi.setOnAction(event ->{
            try {
                if (denaroPuntato.equalsIgnoreCase("all"))
                    giocatore.premiPulsante(giocatore.getCassaPersonale(), this.puntata); //Passo cosa ho puntato e quanto
                else
                    giocatore.premiPulsante(Double.parseDouble(this.denaroPuntato), this.puntata);

                inserimentoCorretto(giocatore); //Avviso visivo all'utente
                rootDinamica.getChildren().clear();
                rootFish.getChildren().clear();

                box5.setDisable(true);
                box5.setVisible(false);
                boxDenPuntata.setVisible(false);
                boxRisPuntata.setVisible(false);
            } catch (Exception error) {
                System.out.println(error);
                errore();

                box5.setVisible(false);
                box5.setDisable(true);

                tabellone.setDisable(false);
                rootFish.setDisable(false);
                b.setDisable(false);
            }
        });
        //Metodo per cambio colore quando mouse sovrappone
        buttonSi.setOnMouseMoved(event -> {
            buttonSi.setStyle("-fx-border-color: #24292f; " +
                    "-fx-border-radius: 16; " +
                    "-fx-background-radius: 16; " +
                    "-fx-background-color: #FFECA1; " +
                    "-fx-font-family: 'Goudy Stout'; " +
                    "-fx-font-size: 10;");
        });
        buttonSi.setOnMouseExited(event -> {
            buttonSi.setStyle("-fx-border-color: #24292f; " +
                    "-fx-border-radius: 16; " +
                    "-fx-background-radius: 16; " +
                    "-fx-background-color: bfc2ca; " +
                    "-fx-font-family: 'Goudy Stout'; " +
                    "-fx-font-size: 10;");
        });

        // Creazione dell'etichetta
        Label label = new Label("Sei sicuro?");
        label.setAlignment(javafx.geometry.Pos.CENTER);
        label.setLayoutX(29.0);
        label.setLayoutY(14.0);
        label.setPrefHeight(60.0);
        label.setPrefWidth(231.0);
        label.setStyle("-fx-font-family: 'Goudy Stout'; " +
                "-fx-font-size: 18;");

        // Aggiunta dei figli all'AnchorPane
        box5.getChildren().addAll(buttonNo, buttonSi, label);

        root.getChildren().add(box5);

        box5.setDisable(false);
        box5.setVisible(true);
        entrataAnchor(box5);
    }
    public void selectNoGiocatore(){ //Metodo per uscire in automatico della selezione del singolo
        selectGiocatore.setValue(null);
        rootDinamica.getChildren().clear();
        rootFish.getChildren().clear();
        labelCredito.setText("Seleziona un giocatore");

        // Creiamo un oggetto ImageView
        ImageView immIniziale = new ImageView();

        // Impostiamo l'immagine da caricare
        Image image = new Image(getClass().getResourceAsStream("/com/example/roulettedemo/img/omP.png"));
        immIniziale.setImage(image);

        // Impostiamo le proprietà
        immIniziale.setFitHeight(334.0);
        immIniziale.setFitWidth(367.0);
        immIniziale.setLayoutX(33.0);
        immIniziale.setLayoutY(28.0);
        immIniziale.setNodeOrientation(javafx.geometry.NodeOrientation.INHERIT);
        immIniziale.setPreserveRatio(true);
        immIniziale.setTranslateX(-1.0);
        immIniziale.setTranslateY(1.0);

        rootDinamica.getChildren().add(immIniziale);
    }
    public void inserimentoAutomatico(){
        auto.setDisable(true);
        labelIniziale.setVisible(false);
        rootDinamica.getChildren().clear();
        Label labelBenvenuto=new Label("AZIONI AUTOMATICHE");
        labelBenvenuto.setStyle("-fx-font-family: 'Goudy Stout'; -fx-font-size: 11;");
        labelBenvenuto.setLayoutY(14);
        labelBenvenuto.setLayoutX(18);
        labelBenvenuto.setPrefWidth(244);
        labelBenvenuto.setPrefHeight(30);
        wheelContainer.setVisible(true);
        selectNoGiocatore();
        rootDinamica.getChildren().add(labelBenvenuto);
        for (Giocatore giocatore: giocatori){
            if(giocatore.getPuntataCorrente()==null) {
                double denaroPuntatoCasuale = (giocatore.getCassaPersonale() / 100) * ((int) ((Math.random() * 100) + 1));
                giocatore.premiPulsante(denaroPuntatoCasuale, String.valueOf((int) (Math.random() * 36)));
            }
        }
    }

    /*METODI GESTIONE DEL TABELLONE RELATIVI AL TURNO*/
    @FXML
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
            Button bottoneRow = creaBottoneConBordo("Row", 56, 65, "#e7e7ea", 2, "BLACK");
            bottoneRow.setFont(Font.font(11));
            bottoneRow.setId("r"+riga);
            bottoneRow.setOnAction(this::selezionaPuntata); //Assegno al bottone su cui si richiama il set il metodo

            hboxRiga.getChildren().add(bottoneRow);

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
        ultimi18.setId("m2");
        ultimi18.setOnAction(this::selezionaPuntata); //Assegno al bottone su cui si richiama il set il metodo

        hbox3.getChildren().addAll(spazio2, primi18, pari, rossi, neri, dispari, ultimi18);
        tabellonePrincipale.getChildren().add(hbox3);
        return tabellonePrincipale;
    }
    private Button creaBottone(String text, double width, double height, String color, double radius, String textColor) {
        Button b = new Button(text);
        b.setPrefSize(width, height);
        b.setStyle("-fx-background-color: " + color + "; -fx-background-radius: " + radius + "; -fx-text-fill: " + textColor + "; -fx-font-family: 'Bodoni MT'; -fx-font-size: 10;");
        //Metodo per cambio colore quando mouse sovrappone
        b.setOnMouseMoved(event -> {
            b.setStyle("-fx-background-color: #FFECA1; -fx-background-radius: " + radius + "; -fx-text-fill: " + textColor + "; -fx-font-family: 'Bodoni MT'; -fx-font-size: 10;");
        });
        b.setOnMouseExited(event -> {
            b.setStyle("-fx-background-color: " + color + "; -fx-background-radius: " + radius + "; -fx-text-fill: " + textColor + "; -fx-font-family: 'Bodoni MT'; -fx-font-size: 10;");
        });
        return b;
    }
    private Button creaBottoneConBordo(String text, double width, double height, String color, double radius, String textColor) {
        Button b = new Button(text);
        b.setPrefSize(width, height);
        b.setStyle("-fx-background-color: " + color + "; -fx-background-radius: " + radius + "; -fx-text-fill: " + textColor + "; -fx-border-radius: 2; -fx-border-width: 1; -fx-border-color:  #24292f; -fx-font-family: 'Bodoni MT'; -fx-font-size: 10;");
        //Metodo per cambio colore quando mouse sovrappone
        b.setOnMouseMoved(event -> {
            b.setStyle("-fx-background-color: #FFECA1; -fx-background-radius: " + radius + "; -fx-text-fill: " + textColor + "; -fx-border-radius: 2; -fx-border-width: 1; -fx-border-color:  #24292f; -fx-font-family: 'Bodoni MT'; -fx-font-size: 10;");
        });
        b.setOnMouseExited(event -> {
            b.setStyle("-fx-background-color: " + color + "; -fx-background-radius: " + radius + "; -fx-text-fill: " + textColor + "; -fx-border-radius: 2; -fx-border-width: 1; -fx-border-color:  #24292f; -fx-font-family: 'Bodoni MT'; -fx-font-size: 10;");
        });
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
        labelPuntato.setText("Hai scelto : " + puntata);

    }
    private void selezionaPuntataDenaro(ActionEvent event) {
        Button clickedButton = (Button) event.getSource(); // Recupera il pulsante cliccato in questo caso fiches
        denaroPuntato=clickedButton.getText();
        System.out.println("Hai puntato su: " + denaroPuntato);
        labelSoldiPuntato.setText("con : "+denaroPuntato);
    }
    @FXML
    public void setCassa(double cassa){
        labelCredito.setText("Credito attuale: "+cassa+" €");
    }

    /* METODI INIZIO PARTITA */
    @FXML
    public void initialize() {
        /* Aggiunge un listener alla lista degli elementi della ChoiceBox
         Un listener è un componente che osserva i cambiamenti di uno specifico oggetto o proprietà.
         Quando si verifica un cambiamento, il listener esegue un'azione definita nel suo corpo.*/
        selectGiocatore.getItems().addListener((javafx.collections.ListChangeListener.Change<? extends String> change) -> {
            //ListChangeListener.Change<? extends String> è un tipo generico che indica che il listener
            // reagirà ai cambiamenti in una lista contenente oggetti di tipo String (o un suo sottotipo).

            // Cicla attraverso i cambiamenti nella lista
            while (change.next()) {
                // Controlla se la lista non è vuota
                if (!selectGiocatore.getItems().isEmpty()) {
                    // Quando gli elementi nella ChoiceBox sono disponibili,
                    // aggiunge un listener per monitorare la selezione dell'utente
                    selectGiocatore.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
                        // Se il nuovo elemento selezionato non è nullo anche se non può esserlo
                        if (newValue != null) {
                            // Ottiene l'indice dell'elemento selezionato nella ChoiceBox
                            int selectedIndex = selectGiocatore.getSelectionModel().getSelectedIndex();
                            // Carica le informazioni relative al giocatore selezionato
                            caricaFunzioniDelGiocatore(giocatori.get(selectedIndex));
                        }
                    });
                }
            }
        });
        auto.setOnMouseMoved(e -> {
            auto.setStyle("-fx-border-color: #24292f; -fx-border-radius: 16; -fx-background-radius: 16; -fx-background-color: FFECA1; -fx-font-family: 'Goudy Stout'; -fx-font-size: 10;");
        });
        auto.setOnMouseExited(e -> {
            auto.setStyle("-fx-border-color: #24292f; -fx-border-radius: 16; -fx-background-radius: 16; -fx-background-color: bfc2ca; -fx-font-family: 'Goudy Stout'; -fx-font-size: 10;");
        });

    }
    @FXML
    public void inserimentoGiocatoriChoiceBox() {
        System.out.println(">  Inseriti identificativi nel menù di scelta.");
        selectGiocatore.getItems().clear();
        for (int i = 0; i < giocatori.size(); i++) {
            selectGiocatore.getItems().add(giocatori.get(i).getIdentificativo());
        }
    }

    /*METODI AVVISI GRAFICI X L'UTENTE*/
    private void errore(){
        controlRoot.setStyle("-fx-background-color:  #bc0000; -fx-background-radius: 0 12 12 0; -fx-border-color: #24292f; -fx-border-radius: 0 12 12 0; -fx-border-width: 2 2 2 0; -fx-font-family: 'Goudy Stout'; -fx-font-size: 9;");
        String s= labelCredito.getText();
        labelCredito.setText("Errore ");
        selectGiocatore.setDisable(true);
        auto.setDisable(true);
        entrataAnchor(controlRoot);
        PauseTransition pausa = new PauseTransition(Duration.seconds(3));
        pausa.setOnFinished(e -> {
            controlRoot.setStyle("-fx-background-color: bfc2ca; -fx-background-radius: 0 12 12 0; -fx-border-color: #24292f; -fx-border-radius: 0 12 12 0; -fx-border-width: 2 2 2 0; -fx-font-family: 'Goudy Stout'; -fx-font-size: 9;");
            labelCredito.setText(s);
            entrataAnchor(controlRoot);
            selectGiocatore.setDisable(false);
            auto.setDisable(false);
        });
        pausa.play();
    }
    @FXML
    private void inserimentoCorretto(Giocatore giocatore){
        controlRoot.setStyle("-fx-background-color:  #1e860e; -fx-background-radius: 0 12 12 0; -fx-border-color: #24292f; -fx-border-radius: 0 12 12 0; -fx-border-width: 2 2 2 0; -fx-font-family: 'Goudy Stout'; -fx-font-size: 9;");
        labelCredito.setText("Ottimo :)");
        selectGiocatore.setDisable(true);
        auto.setDisable(true);
        entrataAnchor(controlRoot);
        PauseTransition pausa = new PauseTransition(Duration.seconds(1.2));
        pausa.setOnFinished(e -> {
            controlRoot.setStyle("-fx-background-color: bfc2ca; -fx-background-radius: 0 12 12 0; -fx-border-color: #24292f; -fx-border-radius: 0 12 12 0; -fx-border-width: 2 2 2 0; -fx-font-family: 'Goudy Stout'; -fx-font-size: 9;");
            labelCredito.setText("CREDITO ATTUALE: "+giocatore.getCassaPersonale()+" €");
            entrataAnchor(controlRoot);
            selectGiocatore.setDisable(false);
            auto.setDisable(false);
        });
        pausa.play();
    }
    @FXML
    public void sovrastaWheel(int estratto) {
        String colore;
        if (isRosso(estratto))
            colore="#bc0000";
        else if (estratto==0)
            colore="#00FF00";
        else
            colore="#24292f";

        // Creazione del quadrante
        Rectangle quadrante = new Rectangle(170, 170);
        quadrante.setArcWidth(16);
        quadrante.setArcHeight(16);
        quadrante.setFill(Color.web(colore, 0.9)); // Colore di sfondo verde trasparente
        quadrante.setLayoutX(270); // Posizione centrata rispetto alla ruota
        quadrante.setLayoutY(209);

        // Creazione del messaggio
        Label messaggioConferma = new Label("Estratto: " + estratto);
        messaggioConferma.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-family: 'Goudy Stout'; -fx-font-size: 8;");
        messaggioConferma.setAlignment(Pos.CENTER);
        messaggioConferma.setPrefWidth(170); // Larghezza uguale al quadrante
        messaggioConferma.setPrefHeight(170); // Altezza uguale al quadrante
        messaggioConferma.setLayoutX(270);
        messaggioConferma.setLayoutY(209);

        selectGiocatore.setDisable(true);
        // Aggiunta del quadrante e del messaggio al contenitore
        root.getChildren().addAll(quadrante, messaggioConferma);

        // Timer per rimuovere gli elementi dopo 5 secondi
        PauseTransition pausa = new PauseTransition(Duration.seconds(3));
        pausa.setOnFinished(e -> {
            root.getChildren().removeAll(quadrante, messaggioConferma);
            selectGiocatore.setDisable(false);
        });
        pausa.play();

    }
    @FXML
    public void finePartita() {
        // Creazione del quadrante
        Rectangle quadrante = new Rectangle(170, 170);
        quadrante.setArcWidth(16);
        quadrante.setArcHeight(16);
        quadrante.setFill(Color.web("#00FF00", 0.9)); // Colore di sfondo verde trasparente
        quadrante.setLayoutX(270); // Posizione centrata rispetto alla ruota
        quadrante.setLayoutY(209);

        // Creazione del messaggio
        Label messaggioConferma = new Label("PARTITA TERMINATA");
        messaggioConferma.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-family: 'Goudy Stout'; -fx-font-size: 8;");
        messaggioConferma.setAlignment(Pos.CENTER);
        messaggioConferma.setPrefWidth(170); // Larghezza uguale al quadrante
        messaggioConferma.setPrefHeight(170); // Altezza uguale al quadrante
        messaggioConferma.setLayoutX(270);
        messaggioConferma.setLayoutY(209);


        // Aggiunta del quadrante e del messaggio al contenitore
        root.getChildren().addAll(quadrante, messaggioConferma);

    }

    /* METODI PER GESTIONE ELEMENTI GRAFICI  */
    @FXML
    public void rimuoviGiocatore(Giocatore giocatore){ //Modifica il menù a tendina rimuovendo il giocatore
        giocatori.remove(giocatore);
        inserimentoGiocatoriChoiceBox();
    }

    //Creazione e modifica delle fish
    @FXML
    private void valoreFiches(Giocatore giocatore, Button fish1, Button fish2, Button fish3, Button fish4) {
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
    private void creaFiches(Giocatore giocatore) {
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
            button.setStyle("-fx-background-radius: 35; -fx-background-color: #E4ECF6; -fx-border-color: " + borderColor + "; -fx-border-radius: 35; -fx-border-width: 10; -fx-font-family: 'Goudy Stout'; -fx-font-size: 9;");
            button.setOnAction(this::selezionaPuntataDenaro); //Assegno al bottone su cui si richiama il set il metodo
            //Metodo per cambio colore quando mouse sovrappone
            button.setOnMouseMoved(event -> {
                button.setStyle("-fx-background-radius: 35; -fx-background-color: #FFECA1; -fx-border-color: " + borderColor + "; -fx-border-radius: 35; -fx-border-width: 10; -fx-font-family: 'Goudy Stout'; -fx-font-size: 9;");
            });
            button.setOnMouseExited(event -> {
                button.setStyle("-fx-background-radius: 35; -fx-background-color: #E4ECF6; -fx-border-color: " + borderColor + "; -fx-border-radius: 35; -fx-border-width: 10; -fx-font-family: 'Goudy Stout'; -fx-font-size: 9;");
            });

            fiches[inserite++]=button;
            rootFish.getChildren().add(button);
        }
        valoreFiches(giocatore,fiches[0],fiches[1],fiches[2],fiches[3]);
    }

    //TRANSIZIONI
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
    public void rotate(int extractedNumber) {
        auto.setDisable(true);
        selectGiocatore.setDisable(true);

        // Array che rappresenta i numeri sulla ruota da destra a sinistra
        int[] wheel = {
                0, 32, 15, 19, 4, 21, 2, 25, 17, 34, 6, 27, 13, 36, 11, 30, 8, 23, 10, 5, 24, 16, 33, 1,
                20, 14, 31, 9, 22, 18, 29, 7, 28, 12, 35, 3, 26
        };

        // Trova l'indice del numero estratto
        int indiceNumero = -1;
        for (int i = 0; i < wheel.length; i++) {
            if (wheel[i] == extractedNumber) {
                indiceNumero = i;
                break;
            }
        }

        // Calcolo dell'angolo necessario per fermare la ruota
        double angoloNumeroEstratto = indiceNumero*(360.0 / wheel.length);
        double angoloGiroPallina = -720 + ((-(this.posizionePallina * (360.0 / wheel.length))) - (360-angoloNumeroEstratto));// Due giri completi + l'offset

        // Animazione: la ruota ruota con la pallina
        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(6), wheelContainer); // Ruota
        rotateTransition.setByAngle(720); // Ruota di 720 gradi (2 giri completi)
        rotateTransition.setCycleCount(1);
        rotateTransition.setInterpolator(Interpolator.EASE_OUT);

        RotateTransition ballRotateTransition = new RotateTransition(Duration.seconds(6), ball); // Pallina
        ballRotateTransition.setByAngle(angoloGiroPallina); // Ruota in senso opposto alla ruota

        // Al termine della rotazione principale, pallina si ferma sul numero estratto
        rotateTransition.setOnFinished(event -> {
                selectGiocatore.setDisable(false);
                auto.setDisable(false);
                System.out.println("La ruota si è fermata sul numero: " + extractedNumber);
        });

        rotateTransition.play();
        ballRotateTransition.play();

        this.posizionePallina=indiceNumero;
    }
}
