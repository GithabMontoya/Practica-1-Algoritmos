package com.example.demo4.Modelo;

public class Dealer extends Jugador {
    public static final int LIMITE_PARA_PARAR = 18;

    public Dealer(){
        super("Dealer");
    }

    public boolean debePedirCarta(){
        return mano.calcularTotal() < LIMITE_PARA_PARAR;
    }

}
