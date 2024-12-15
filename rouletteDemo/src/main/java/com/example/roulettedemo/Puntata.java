package com.example.roulettedemo;

public class Puntata {
    private double denaro;
    private String oggetto;
    private Giocatore giocatorePuntante;

    public Puntata(double denaro, String oggetto, Giocatore giocatorePuntante) {
        this.denaro = denaro;
        this.oggetto = oggetto;
        this.giocatorePuntante=giocatorePuntante;
    }

    public double getDenaro() { return denaro; }
    public void setDenaro(double denaro) { this.denaro = denaro; }

    public String getOggetto() { return oggetto; }
    public void setOggetto(String oggetto) { this.oggetto = oggetto; }

    public Giocatore getGiocatorePuntante() { return giocatorePuntante; }
    public void setGiocatorePuntante(Giocatore giocatorePuntante) { this.giocatorePuntante = giocatorePuntante; }

    public boolean equals(String s){
        return this.oggetto.equals(s);
    }
}
