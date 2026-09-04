package com.example.demo4.Vista;

import com.example.demo4.Modelo.Dealer;
import com.example.demo4.Modelo.Jugador;

import java.util.List;
import java.util.Scanner;

public class BlackjackVista {
    private Scanner scanner = new Scanner(System.in);

    public int pedirCantidadJugadores(){
        System.out.println("Ingresa la cantidad de jugadores (1-4)");
        while (!scanner.hasNextInt()){
            System.out.println("Solo se aceptan números ");
            scanner.next();
        }
        int cantidad = scanner.nextInt();
        scanner.nextLine();
        return cantidad;
    }

    public String pedirNombreJugador(int numero){
        System.out.println("Nombre del jugador " + numero + ": ");
        return scanner.nextLine();
    }

    public String pedirOpcionesJugador(String nombreDelJugador){
        System.out.println(nombreDelJugador + " Pedir carta (p) o plantarte (t)");
        return scanner.nextLine().trim().toLowerCase();
    }

    public boolean pedirJugarOtraVez(){
        System.out.println("\n Jugar otra ronda? (s/n)");
        return scanner.nextLine().trim().equalsIgnoreCase("s");
    }

    public void mostrarMesa(List<Jugador> jugadores, Dealer dealer, boolean ocultarTotalDealer){
        System.out.println();
        System.out.print(dealer.getNombre() + ": " + dealer.getMano());
        if(!ocultarTotalDealer){
            System.out.print(" (total: "+ dealer.getMano().calcularTotal() + ")");
        }
        System.out.println();

        for(Jugador jugador : jugadores){
            System.out.println(jugador.getNombre() + ": " + jugador.getMano());
        }
    }
}
