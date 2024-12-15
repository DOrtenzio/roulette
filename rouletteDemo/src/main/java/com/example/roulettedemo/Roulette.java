package com.example.roulettedemo;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Roulette extends Thread{
    private ArrayList<Puntata> puntate=new ArrayList<Puntata>();
    private int numGioc;
    private Semaphore prontiAlGioco;

    //RELATIVO ALLA ROULETTE
    private double cassa;
    private ArrayList<Integer> riga1 = new ArrayList<Integer>();
    private ArrayList<Integer> riga2 = new ArrayList<Integer>();
    private ArrayList<Integer> riga3 = new ArrayList<Integer>();
    private final int[] rossi = {1, 3, 5, 7, 912, 14, 16, 18, 19, 21, 23, 25, 27, 30, 32, 34, 36};

    //Costruttore e inizializzazione sequenze colori
    public Roulette(int cassa, int numGioc, Semaphore prontiAlGioco) {
        this.cassa = cassa;
        this.numGioc=numGioc;
        this.prontiAlGioco=prontiAlGioco;
    }

    private void initializeArrays() {
        for (int i = 1; i <= 34; i += 3) {
            riga1.add(i);
        }
        for (int i = 2; i <= 35; i += 3) {
            riga2.add(i);
        }
        for (int i = 3; i <= 36; i += 3) {
            riga3.add(i);
        }
    }

    //GESTIONE PUNTATE
    public void addPuntata(Puntata puntata){
        puntate.add(puntata);
    }



    //METODI PRIVATI CALCOLO COMBINAZIONI E GESTIONE CASSE
    private int estraiNumero() { return ((int) (Math.random() * 37)); }

    private String getColoreEstratto(int numEstratto) {
        if (numEstratto == 0)
            return "Verde";
        else {
            for (int j : rossi) {
                if (j == numEstratto)
                    return "Rosso";
            }
            return "Nero";
        }
    }

    private String getRiga(int numEstratto) {
        if (numEstratto == 0)
            return "ro";
        else if (riga1.contains(numEstratto)) {
            return "r1";
        } else if (riga2.contains(numEstratto)) {
            return "r2";
        } else {
            return "r3";
        }
    }

    private String getQuadrante(int numEstratto) {
        if (numEstratto == 0)
            return "q0";
        else if (numEstratto <= 12) {
            return "q1";
        } else if (numEstratto <= 24) {
            return "q2";
        } else {
            return "q3";
        }
    }

    private String getMeta(int numEstratto) {
        if (numEstratto >= 1 && numEstratto <= 18) {
            return "m1";
        } else if (numEstratto >= 19 && numEstratto <= 36) {
            return "m2";
        } else {
            return "m0";
        }
    }

    private String getSimmetria(int numEstratto) {
        if (numEstratto % 2 == 0)
            return "p";
        else
            return "d";
    }

    private double restiuisciORicavaDenaro(Puntata puntata, int estratto) {
        double percentualeDecrementoOIncremento, denaroRestituire;
        if (puntata.equals(Integer.toString(estratto))) {
            percentualeDecrementoOIncremento=-1;
            denaroRestituire=puntata.getDenaro() * 2;
        } else if (puntata.equals(getRiga(estratto))) {
            percentualeDecrementoOIncremento=-0.1;
            denaroRestituire=puntata.getDenaro() * 1.5;
        }else if (puntata.equals(getColoreEstratto(estratto))) {
            percentualeDecrementoOIncremento=-0.1;
            denaroRestituire=puntata.getDenaro() * 1.1;
        } else if (puntata.equals(getQuadrante(estratto)) && getQuadrante(estratto).equals("q0")) {
            percentualeDecrementoOIncremento=-0.5;
            denaroRestituire=puntata.getDenaro() * 1.5;
        } else if (puntata.equals(puntata.equals(getQuadrante(estratto)))) {
            percentualeDecrementoOIncremento=-0.2;
            denaroRestituire=puntata.getDenaro() * 1.2;
        } else if (puntata.equals(getMeta(estratto)) && getMeta(estratto).equals("m0")) {
            percentualeDecrementoOIncremento=-0.2;
            denaroRestituire=puntata.getDenaro() * 1.2;
        } else if (puntata.equals(getMeta(estratto))) {
            percentualeDecrementoOIncremento=-0.5;
            denaroRestituire=puntata.getDenaro() * 1.5;
        } else if (puntata.equals(getSimmetria(estratto))){
            percentualeDecrementoOIncremento=-0.1;
            denaroRestituire=puntata.getDenaro() * 1.1;
        }else{
            percentualeDecrementoOIncremento=1;
            denaroRestituire=puntata.getDenaro() * 0;
        }
        this.cassa+=puntata.getDenaro()*percentualeDecrementoOIncremento;
        return denaroRestituire;
    }

    //Run
    @Override
    public void run() {
        while (this.cassa>0) {
            try {
                TimeUnit.SECONDS.sleep(5);

                for (int i=0;i<this.numGioc;i++){
                    this.prontiAlGioco.acquire();
                }

                System.out.println("Tutti i giocatori hanno piazzato la loro puntata.");

                int estratto = estraiNumero();
                for (Puntata puntata : puntate) {
                    Giocatore giocatorePuntata = puntata.getGiocatorePuntante();
                    giocatorePuntata.setCassaPersonale( giocatorePuntata.getCassaPersonale() + restiuisciORicavaDenaro(puntata, estratto) );
                }

                puntate.clear();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
