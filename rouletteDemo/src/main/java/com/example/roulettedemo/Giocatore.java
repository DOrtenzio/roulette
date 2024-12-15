package com.example.roulettedemo;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Giocatore extends Thread{
    private String identificativo;
    private double cassaPersonale;

    private Roulette roulette;
    private Semaphore prontiAlGioco;

    public Giocatore(String identificativo, double cassa, Roulette roulette, Semaphore prontiAlGioco) {
        this.identificativo = identificativo;
        this.cassaPersonale = cassa;
        this.roulette=roulette;
        this.prontiAlGioco=prontiAlGioco;
    }

    public Puntata creaPuntata(String s, double denaro){
        return new Puntata(denaro,s,this);
    }

    //GET E SET
    public String getIdentificativo() { return this.identificativo; }
    public void setIdentificativo(String identificativo) { this.identificativo = identificativo; }
    public double getCassaPersonale() { return cassaPersonale; }
    public void setCassaPersonale(double cassaPersonale) { this.cassaPersonale = cassaPersonale; }

    @Override
    public void run() {
        while (this.cassaPersonale > 0) {
            try {
                prontiAlGioco.acquire();

                // Crea e registra la puntata
                Puntata nuovaPuntata = creaPuntata("r1", 20.0);
                roulette.addPuntata(nuovaPuntata);

                TimeUnit.SECONDS.sleep(5);

                prontiAlGioco.release();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }



}
