package com.example.demo4.Modelo;

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

    public Mano getMano(){
        return mano;
    }

    public void recibirCarta(CartaInglesa carta){
        carta.makeFaceUp();
        mano.agregarCarta(carta);
    }

    public void nuevaMano(){
        mano = new Mano();
    }
}
