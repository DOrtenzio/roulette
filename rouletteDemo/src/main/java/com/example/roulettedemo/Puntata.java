package com.example.roulettedemo;

public class Puntata {
    private double denaro;
    private String oggetto; //Stringa indicante cosa si è puntato ad esempio 12,riga 1, pari, ...
    private Giocatore giocatorePuntante;

    public Puntata(double denaro, String oggetto, Giocatore giocatorePuntante) {
        this.denaro = denaro;
        this.oggetto = oggetto;
        this.giocatorePuntante=giocatorePuntante;
    }

    //Get e set Vari
    public double getDenaro() { return denaro; }
    public void setDenaro(double denaro) { this.denaro = denaro; }

    public String getOggetto() { return oggetto; }
    public void setOggetto(String oggetto) { this.oggetto = oggetto; }

    public Giocatore getGiocatorePuntante() { return giocatorePuntante; }
    public void setGiocatorePuntante(Giocatore giocatorePuntante) { this.giocatorePuntante = giocatorePuntante; }

    //Metodo che compara la puntata con l'uscita
    public boolean equals(String s){
        return this.oggetto.equals(s);
    }

    @Override
    public String toString(){ return "Puntata di: "+this.giocatorePuntante.toString()+" Su: "+this.oggetto+" di: "+this.denaro+" €;"; }
}
