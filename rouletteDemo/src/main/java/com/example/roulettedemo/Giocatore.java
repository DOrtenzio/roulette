package com.example.roulettedemo;

import java.util.concurrent.Semaphore;

public class Giocatore extends Thread{
    private String identificativo;
    private double cassaPersonale;

    private Roulette roulette;
    private Semaphore prontiAlGioco;

    private Semaphore attesaPulsante=new Semaphore(0); // Aggiunto per sincronizzare con il pulsante dell'interfaccia grafica
    private Puntata puntataCorrente;

    public Giocatore(String identificativo, double cassa, Roulette roulette, Semaphore prontiAlGioco) {
        this.identificativo = identificativo;
        this.cassaPersonale = cassa;
        this.roulette=roulette;
        this.prontiAlGioco=prontiAlGioco;
        this.puntataCorrente=null;
    }

    public Puntata creaPuntata(String s, double denaro){
        return new Puntata(denaro,s,this);
    }

    // Metodo per sbloccare l'attesa (da chiamare quando il pulsante d'inserimento viene premuto)
    public void premiPulsante(double denaro,String oggetto) {
        this.puntataCorrente=new Puntata(denaro,oggetto,this);
        attesaPulsante.release(); // Sblocca l'attesa del thread
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
                prontiAlGioco.acquire(); //Blocco in caso roulette stia girando

                attesaPulsante.acquire(); // Blocco finché il pulsante non sblocca ovvero passa i valori
                // Crea e registra la puntata
                roulette.addPuntata(this.puntataCorrente);
                System.out.println("INVIATA");

                prontiAlGioco.release();

                System.out.println("Permessi disponibili: " + prontiAlGioco.availablePermits());

                synchronized (roulette){
                    roulette.wait();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }



}
