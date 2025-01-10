package com.example.roulettedemo;

import java.util.concurrent.Semaphore;

public class Giocatore extends Thread {
    private double cassaPersonale; //Cassa personale del giocatore
    private Roulette rouletteAttuale;
    private Puntata puntataCorrente;
    private Semaphore prontiAlGioco;

    private final String identificativo;
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
    public void setProntiAlGioco(Semaphore prontiAlGioco) { this.prontiAlGioco = prontiAlGioco; }

    public void premiPulsante(double denaro, String oggetto) { //Metodo utilizzato per comunicare con la GUI
        this.puntataCorrente = new Puntata(denaro, oggetto, this);
        attesaPulsante.release();
    }

    public void ritirati(){
        rouletteAttuale.ritirati(this);
        System.out.println(">> IL GIOCATORE "+this.identificativo+" SI E' RITIRATO");
        prontiAlGioco.release();
        Thread.currentThread().interrupt();
    }

    @Override
    public String toString(){ return "Giocatore: Id = "+this.identificativo+" Quantitativo Cassa: "+this.cassaPersonale+" €,\t"; }

    @Override
    public boolean equals(Object obj){
        if (obj instanceof Giocatore){
            Giocatore r=(Giocatore) obj;
            return r.getCassaPersonale() == this.cassaPersonale && r.getIdentificativo().equalsIgnoreCase(this.identificativo);
        }
        return  false;
    }

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
        rouletteAttuale.ritirati(this);
        System.out.println(">> IL GIOCATORE "+this.identificativo+" SI E' RITIRATO");
    }
}
