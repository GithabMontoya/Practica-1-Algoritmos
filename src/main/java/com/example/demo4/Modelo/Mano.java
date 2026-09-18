package com.example.demo4.Modelo;

import java.util.ArrayList;

public class Mano {
    private Pila<CartaInglesa> cartasPila = new Pila<>();
    private ArrayList<CartaInglesa> cartas = new ArrayList<>();
    private Carta carta;
    private Mazo mazo;

    public void agregarCarta(CartaInglesa carta){
        cartas.add(carta);
        cartasPila.push(Pila);
    }

    public ArrayList<CartaInglesa> getCartas(){
        return cartas;
    }

    public void limpiar(){
        cartas.clear();
    }

    public int calcularTotal(){
        int total = 0;
        int ases = 0;

        for(CartaInglesa carta : cartas){
            int valor = carta.getValor();
            if (valor == 14){
                total += 11;
                ases++;
            } else if(valor >= 11 && valor <= 13){
                total += 10;
            } else {
                total += valor;
            }
        } while (total > 21 && ases > 0){
            total -= 10;
            ases--;
        }
        return total;
    }

    public boolean sePaso(){
        return calcularTotal() > 21;
    }

    public boolean esBlackjack(){
        return calcularTotal() == 21;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(CartaInglesa carta:cartas){
            sb.append(carta.toString()).append(" ");
        }
        return sb.toString().trim();
    }
}
