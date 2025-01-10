package com.example.roulettedemo;

import javafx.application.Platform;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Roulette extends Thread {
    private final ArrayList<Puntata> puntate = new ArrayList<>();
    private final int numGioc;
    private int numRitirati;
    private final Semaphore prontiAlGioco;
    private final HelloController helloController;
    private double cassa;

    private final ArrayList<Integer> riga1 = new ArrayList<>();
    private final ArrayList<Integer> riga2 = new ArrayList<>();
    private final ArrayList<Integer> riga3 = new ArrayList<>();
    private static final int[] ROSSI = {1, 3, 5, 7, 9, 12, 14, 16, 18, 19, 21, 23, 25, 27, 30, 32, 34, 36};

    public Roulette(double cassa, int numGioc, Semaphore prontiAlGioco, HelloController helloController) {
        this.cassa = cassa;
        this.numGioc = numGioc;
        this.prontiAlGioco = prontiAlGioco;
        this.helloController = helloController;
        this.numRitirati=0;
        initializeArrays();
    }

    /*METODI ESTRAZIONE E GESTIONE ROULETTE*/
    private void initializeArrays() { //Inizializzo array con rossi ecc.
        for (int i = 1; i <= 34; i += 3) riga1.add(i);
        for (int i = 2; i <= 35; i += 3) riga2.add(i);
        for (int i = 3; i <= 36; i += 3) riga3.add(i);
        //Metodo che può risultare utili nel caso ci si sposti dalla roulette europea ad altre
    }

    private String getColoreEstratto(int numero) {
        if (numero == 0) return "Verde";
        for (int rosso : ROSSI) {
            if (rosso == numero) return "Rosso";
        }
        return "Nero";
    }

    private String getRiga(int numero) {
        if (riga1.contains(numero)) return "r1";
        if (riga2.contains(numero)) return "r2";
        if (riga3.contains(numero)) return "r3";
        return "ro"; // Default per 0
    }

    private String getQuadrante(int numero) {
        if (numero == 0) return "q0";
        if (numero <= 12) return "q1";
        if (numero <= 24) return "q2";
        return "q3";
    }

    private String getMeta(int numero) {
        if (numero >= 1 && numero <= 18) return "m1";
        if (numero >= 19 && numero <= 36) return "m2";
        return "m0";
    }

    private String getSimmetria(int numero) {
        return (numero % 2 == 0) ? "p" : "d";
    }

    private double restiuisciORicavaDenaro(Puntata puntata, int estratto) {
        double percentualeDecrementoOIncremento, denaroPerGiocatore;
        if (puntata.equals(Integer.toString(estratto))) {
            percentualeDecrementoOIncremento = -1;
            denaroPerGiocatore = puntata.getDenaro() * 2;
        } else if (puntata.equals(getRiga(estratto))) {
            percentualeDecrementoOIncremento = -0.1;
            denaroPerGiocatore = puntata.getDenaro() * 1.5;
        } else if (puntata.equals(getColoreEstratto(estratto))) {
            percentualeDecrementoOIncremento = -0.1;
            denaroPerGiocatore = puntata.getDenaro() * 1.1;
        } else if (puntata.equals(getQuadrante(estratto)) && getQuadrante(estratto).equals("q0")) {
            percentualeDecrementoOIncremento = -0.5;
            denaroPerGiocatore = puntata.getDenaro() * 1.5;
        } else if (puntata.equals(getQuadrante(estratto))) {
            percentualeDecrementoOIncremento = -0.2;
            denaroPerGiocatore = puntata.getDenaro() * 1.2;
        } else if (puntata.equals(getMeta(estratto)) && getMeta(estratto).equals("m0")) {
            percentualeDecrementoOIncremento = -0.2;
            denaroPerGiocatore = puntata.getDenaro() * 1.2;
        } else if (puntata.equals(getMeta(estratto))) {
            percentualeDecrementoOIncremento = -0.5;
            denaroPerGiocatore = puntata.getDenaro() * 1.5;
        } else if (puntata.equals(getSimmetria(estratto))) {
            percentualeDecrementoOIncremento = -0.1;
            denaroPerGiocatore = puntata.getDenaro() * 1.1;
        } else {
            percentualeDecrementoOIncremento = 1;
            denaroPerGiocatore = puntata.getDenaro() * 0;
        }

        this.cassa += puntata.getDenaro() * percentualeDecrementoOIncremento;
        return denaroPerGiocatore;
    }

    private int estraiNumero() {
        return ((int) (Math.random() * 37));
    }

    /*METODI X ESTERNI*/
    public void addPuntata(Puntata puntata) {
        puntate.add(puntata);
    }

    public void ritirati(Giocatore giocatore){
        Platform.runLater(() -> helloController.rimuoviGiocatore(giocatore));
        this.numRitirati++;
    }

    /*METODI PER INTERFACCIA GRAFICA*/
    /*

    Platform.runLater(); --> È un metodo di JavaFX che permette di eseguire un'operazione sul thread principale dell'interfaccia grafica (UI thread).
                             È necessario perché solo questo thread può aggiornare o interagire con l'interfaccia utente.

    () -> { ... }; --> È una lambda expression, una sintassi breve per definire un'implementazione di un'interfaccia funzionale (qui un oggetto Runnable).
                       Sostituibile con helloController::fasePreScomessa

    */

    private void aggiornaNumeroEstratto(int numeroEstratto) {
        synchronized (helloController) {
            Platform.runLater(() -> helloController.rotate(numeroEstratto));
            try {
                TimeUnit.SECONDS.sleep(7);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            Platform.runLater(() -> helloController.sovrastaWheel(numeroEstratto));
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            Platform.runLater(() -> helloController.selectNoGiocatore());
        }
    }
    private void finePartita(){ Platform.runLater(() -> helloController.finePartita()); }

    /*METODI DI SINCRONIZZAZIONE*/

    private void attendiGiocatori(){
        for (int i = 0; i < this.numGioc; i++) {
            try {
                prontiAlGioco.acquire();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void abilitaAlGioco() {
        for (int i = 0; i < this.numGioc; i++) {
            prontiAlGioco.release();
        }
    }

    private void risvegliaGiocatori(){
        synchronized (this) {
            this.notifyAll();
        }
    }


    @Override
    public void run() {
        while (this.cassa > 0 && numRitirati!=numGioc) {
            try {
                //FASE INIZIALE ---> ATTESA
                TimeUnit.SECONDS.sleep(5);
                attendiGiocatori();

                //SECONDA FASE ---> ESTRAZIONE
                System.out.println("Tutti i giocatori hanno piazzato la loro puntata.");
                int numeroEstratto = estraiNumero();
                for (Puntata puntata : puntate) {
                    Giocatore giocatore = puntata.getGiocatorePuntante();
                    double vincita = restiuisciORicavaDenaro(puntata, numeroEstratto);
                    giocatore.setCassaPersonale(giocatore.getCassaPersonale() + vincita);
                }
                aggiornaNumeroEstratto(numeroEstratto);

                //FASE FINALE ---> RIABILITAZIONE GIOCATORI ED AGGIORNAMENTI GUI
                puntate.clear();
                abilitaAlGioco();
                risvegliaGiocatori();

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        }
        finePartita();
        System.out.println("PARTITA CONCLUSA");
    }
}
