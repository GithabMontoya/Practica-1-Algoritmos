package com.example.demo4.Controlador;

import com.example.demo4.Modelo.CartaInglesa;
import com.example.demo4.Modelo.Dealer;
import com.example.demo4.Modelo.Jugador;
import com.example.demo4.Modelo.Mazo;
import com.example.demo4.Vista.BlackjackVista;

import java.util.ArrayList;
import java.util.List;

public class BlackjackControlador {
    private static final int MIN_JUGADORES = 1;
    private static final int MAX_JUGADORES = 4;

    private BlackjackVista vista = new BlackjackVista();
    private Mazo mazo;
    private List<Jugador> jugadores = new ArrayList<>();
    private Dealer dealer;

    public void jugar(){
        System.out.println("Blackjack");
        agregarJugadores();
        dealer = new Dealer();

        boolean seguirJugando = true;
        while(seguirJugando){
            jugarRonda();
            seguirJugando = vista.pedirJugarOtraVez();
        }

        System.out.println("Gracias, adiós.");
    }

    private void agregarJugadores(){
        int cantidad = vista.pedirCantidadJugadores();
        while(cantidad < MIN_JUGADORES || cantidad > MAX_JUGADORES){
            System.out.println("El numero de jugadores debe de ser entre " + MIN_JUGADORES + " y " + MAX_JUGADORES);
            cantidad = vista.pedirCantidadJugadores();
        }
        for(int i = 1; i <= cantidad; i++){
            String nombre = vista.pedirNombreJugador(i);
            jugadores.add(new Jugador(nombre));
        }
    }

    private void jugarRonda(){
        mazo = new Mazo();
        for(Jugador jugador : jugadores){
            jugador.nuevaMano();
        }
        dealer.nuevaMano();

        for(int vuelta = 0; vuelta < 2; vuelta++){
            for(Jugador jugador : jugadores){
                jugador.recibirCarta(mazo.obtenerUnaCarta());
            }
            dealer.recibirCarta(mazo.obtenerUnaCarta());
        }

        dealer.getMano().getCartas().get(0).makeFaceDown();

        vista.mostrarMesa(jugadores, dealer, true);

        for(Jugador jugador : jugadores){
            turnoJugador(jugador);
        }

        if(todosSePasaron()){
            revelarDealer();
            System.out.println("Todos los jugadores se pasaron de 21");
            vista.mostrarMesa(jugadores, dealer, false);
            return;
        }
        turnoDealer();
        determinarGanadores();
    }

    private boolean todosSePasaron(){
        for (Jugador jugador : jugadores){
            if(!jugador.getMano().sePaso()){
                return false;
            }
        }
       return true;
    }

    private void turnoJugador(Jugador jugador){
        boolean pidiendo = true;
        while(pidiendo && !jugador.getMano().sePaso()){
            String opcion = vista.pedirOpcionesJugador(jugador.getNombre());

            if(opcion.equals("p")){
                jugador.recibirCarta(mazo.obtenerUnaCarta());
                vista.mostrarMesa(jugadores, dealer, true);
            } else if (opcion.equals("t")){
                pidiendo = false;
            } else {
                System.out.println("Opción invalida, selecciona (p) para jugar, o (t) para plantarse");
            }
        }

        if (jugador.getMano().sePaso()){
            System.out.println(jugador.getNombre() + " se pasó de 21");
        }
    }

    private void turnoDealer(){
        revelarDealer();
        System.out.println("Turno del dealer:");
        vista.mostrarMesa(jugadores, dealer, false);

        while(dealer.debePedirCarta()){
            System.out.println("El dealer pidió una carta");
            dealer.recibirCarta(mazo.obtenerUnaCarta());
            vista.mostrarMesa(jugadores, dealer, false);
        }

        if(dealer.getMano().sePaso()) {
            System.out.println("El dealer se pasó de 21.");
        } else {
            System.out.println("El dealer se planta con " + dealer.getMano().calcularTotal() + "puntos.");
        }
    }

    private void revelarDealer(){
        for(CartaInglesa carta : dealer.getMano().getCartas()){
            carta.makeFaceUp();
        }
    }

    private void determinarGanadores(){
        int totalDealer = dealer.getMano().calcularTotal();

        System.out.println("Resultados de la ronnda: ");
        vista.mostrarMesa(jugadores, dealer, false);

        for(Jugador jugador : jugadores){
            int totalJugadores = jugador.getMano().calcularTotal();

            if(jugador.getMano().sePaso()){
                System.out.println(jugador.getNombre() + " se pasó de 21");
            } else if (dealer.getMano().sePaso() || totalJugadores > totalDealer){
                System.out.println(jugador.getNombre() + " ganó");
            } else if (totalJugadores < totalDealer) {
                System.out.println(jugador.getNombre() + " perde contra el dealer.");
            } else {
                System.out.println(jugador.getNombre() + " empató con el dealer");
            }
        }

    }

}
