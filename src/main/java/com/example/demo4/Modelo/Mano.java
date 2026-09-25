package com.example.demo4.Modelo;

import java.util.ArrayList;

public class Mano {
    private Pila<CartaInglesa> cartas = new Pila<>();

    public void agregarCarta(CartaInglesa carta){
        cartas.push(carta);
    }

    public CartaInglesa devolverUltimaCarta(){
        return cartas.pop();
    }

    public ArrayList<CartaInglesa> getCartas(){
        ArrayList<CartaInglesa> resultado = new ArrayList<>();
        Pila<CartaInglesa> pilaTemporal = new Pila<>();
        while(!cartas.vacia()){
            resultado.add(0, cartas.peek());
            pilaTemporal.push(cartas.pop());
        }
        while (!pilaTemporal.vacia()){
            cartas.push(pilaTemporal.pop());
        }
        return resultado;
    }

    public int calcularTotal(){
        int total = 0;
        int ases = 0;

        for(CartaInglesa carta : getCartas()){
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
        return getCartas().toString();
    }
}
