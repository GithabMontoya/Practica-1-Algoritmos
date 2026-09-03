package com.example.demo4.BlackJack;

import com.example.demo4.DeckOfCards.CartaInglesa;

public class Jugador {
    protected String nombre;
    protected Mano mano;

    public Jugador(String nombre){
        this.nombre = nombre;
        this.mano = new Mano();
    }

    public String getNombre(){
        return nombre;
    }

    public void recibirCarta(CartaInglesa carta){
        carta.makeFaceUp();
        mano.agregarCarta(carta);
    }

    public void nuevaMano(){
        mano = new Mano();
    }
}
