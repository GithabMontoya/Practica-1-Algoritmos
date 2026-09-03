package com.example.demo4.BlackJack;

import com.example.demo4.DeckOfCards.Carta;
import com.example.demo4.DeckOfCards.CartaInglesa;
import com.example.demo4.DeckOfCards.Mazo;

import java.util.ArrayList;

public class Mano {
    private ArrayList<CartaInglesa> cartas = new ArrayList<>();
    private Carta carta;
    private Mazo mazo;

    public void agregarCarta(CartaInglesa carta){
        cartas.add(carta);
    }

    public ArrayList<CartaInglesa> getCartas(){
        return cartas;
    }

    public void limpiar(){
        cartas.clear();
    }


}
