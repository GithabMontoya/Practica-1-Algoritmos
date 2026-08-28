package com.example.demo4.BlackJack;

import com.example.demo4.DeckOfCards.Carta;
import com.example.demo4.DeckOfCards.Mazo;

public class Mano {
    private Carta carta;
    private Mazo mazo;

    public Mano(){
        for(int i = 1 ; i <= 2; i++){
            recibirCarta();
        }
    }

    private void recibirCarta(){
        mazo.obtenerUnaCarta();
    }

    @Override
    public String toString() {

        return super.toString();
    }
}
