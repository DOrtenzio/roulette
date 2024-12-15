package com.example.roulettedemo;

import java.util.concurrent.Semaphore;

public class TestMain {
    public  static  void main(String [] args) {
        Semaphore semaphore = new Semaphore(6);
        Roulette roulette = new Roulette(5000, semaphore.availablePermits(), semaphore);
        Giocatore g1 = new Giocatore("a", 340.00, roulette, semaphore);
        Giocatore g2 = new Giocatore("b", 340.00, roulette, semaphore);
        Giocatore g3 = new Giocatore("c", 340.00, roulette, semaphore);
        Giocatore g4 = new Giocatore("d", 340.00, roulette, semaphore);

        roulette.start();
        g1.start();
        g2.start();
        g3.start();
        g4.start();
    }
}
