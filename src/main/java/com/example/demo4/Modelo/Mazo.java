package com.example.demo4.Modelo;
/**
 * Write a description of class Mazo here.
 *
 * @author (Cecilia Curlango Rosas)
 * @version (2025-2)
 */
import com.fasterxml.jackson.databind.type.PlaceholderForType;

import java.util.ArrayList;
import java.util.Collections;

public class Mazo {
    private Pila<CartaInglesa> cartas = new Pila<>();

    public Mazo() {
        llenar(); // crea todas las cartas, excluyendo Jokers
        mezclar();
    }

    /**
     * Obtiene todas las cartas del mazo.
     */
    public ArrayList<CartaInglesa> getCartas() {
        ArrayList<CartaInglesa> resultado = new ArrayList<>();
        Pila<CartaInglesa> temporal = new Pila<>();

        while (!cartas.vacia()){
            CartaInglesa carta = cartas.pop();
            resultado.add(carta);
            temporal.push(carta);
        } while (!temporal.vacia()){
            cartas.push(temporal.pop());
        }
        return resultado;
    }

    public void devolverCarta(CartaInglesa carta){
        if(carta == null){
            return;
        } else {
            cartas.push(carta);
            mezclar();
        }
    }

    public CartaInglesa obtenerUnaCarta() {
        if (!cartas.vacia()) {
            return cartas.pop();
        }
        return null;
    }

    private void mezclar() {
        ArrayList<CartaInglesa> temp = new ArrayList<>();
        while (!cartas.vacia()){
            temp.add(cartas.pop());
        }
        Collections.shuffle(temp);
        for (CartaInglesa c : temp){
            cartas.push(c);
        }
    }

    private void llenar() {
        for (int i = 2; i <=14 ; i++) {
            for (Palo palo : Palo.values()) {
                CartaInglesa c = new CartaInglesa(i,palo, palo.getColor());
                cartas.push(c);
            }
        }
    }

    @Override
    public String toString() {
        return cartas.toString();
    }
}
