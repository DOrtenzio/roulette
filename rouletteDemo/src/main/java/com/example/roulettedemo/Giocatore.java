package com.example.roulettedemo;

import java.util.concurrent.Semaphore;

public class Giocatore extends Thread {
    private double cassaPersonale; //Cassa personale del giocatore
    private Roulette rouletteAttuale;
    private Puntata puntataCorrente;

    private final String identificativo;
    private final Semaphore prontiAlGioco;
    private final Semaphore attesaPulsante = new Semaphore(0);

    public Giocatore(String identificativo, double cassa, Roulette rouletteAttuale, Semaphore prontiAlGioco) {
        this.identificativo = identificativo;
        this.cassaPersonale = cassa;
        this.rouletteAttuale = rouletteAttuale;
        this.prontiAlGioco = prontiAlGioco;
    }

    //Get e set
    public String getIdentificativo() { return identificativo; }
    public double getCassaPersonale() { return cassaPersonale; }
    public void setCassaPersonale(double cassaPersonale) { this.cassaPersonale = cassaPersonale; }
    public void setRouletteAttuale(Roulette rouletteAttuale){ this.rouletteAttuale = rouletteAttuale; }
    public Puntata getPuntataCorrente() { return puntataCorrente; }

    public void premiPulsante(double denaro, String oggetto) { //Metodo utilizzato per comunicare con la GUI
        this.puntataCorrente = new Puntata(denaro, oggetto, this);
        attesaPulsante.release();
    }

    @Override
    public String toString(){ return "Giocatore: Id = "+this.identificativo+" Quantitativo Cassa: "+this.cassaPersonale+" €,\t"; }

    @Override
    public void run() {
        while (cassaPersonale > 0) {
            try {
                prontiAlGioco.acquire(); //Vedo se la roulette è pronta alle puntate
                attesaPulsante.acquire(); //Attendo la puntata dalla GUI

                rouletteAttuale.addPuntata(puntataCorrente);
                setCassaPersonale(getCassaPersonale()-puntataCorrente.getDenaro());
                System.out.println("Puntata inviata: " + puntataCorrente.toString());

                prontiAlGioco.release();//Dico alla roulette di essere pronto

                synchronized (rouletteAttuale) { //Aspetto l'estrazione
                    rouletteAttuale.wait();
                }

                puntataCorrente=null;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Thread interrotto: " + e.getMessage());
            }
        }
    }
}
