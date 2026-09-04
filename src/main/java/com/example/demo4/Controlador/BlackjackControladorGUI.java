package com.example.demo4.Controlador;

import com.example.demo4.Modelo.CartaInglesa;
import com.example.demo4.Modelo.Dealer;
import com.example.demo4.Modelo.Jugador;
import com.example.demo4.Modelo.Mazo;
import com.example.demo4.Vista.BlackjackGUI;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class BlackjackControladorGUI extends Application {

    private static final int TOTAL_PARA_PLANTARSE_AUTOMATICO = 21;

    private final BlackjackGUI vista = new BlackjackGUI();
    private Stage stage;

    private Mazo mazo;
    private final List<Jugador> jugadores = new ArrayList<>();
    private Dealer dealer;
    private int indiceJugadorActual;

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        stage.setTitle("Blackjack");
        stage.setMinWidth(480);
        stage.setMinHeight(360);
        stage.setScene(vista.crearPantallaInicio(this::comenzarJuego));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void comenzarJuego(List<String> nombres) {
        jugadores.clear();
        for (String nombre : nombres) {
            jugadores.add(new Jugador(nombre));
        }
        dealer = new Dealer();

        stage.setScene(vista.crearPantallaMesa(
                this::jugadorPideCarta,
                this::jugadorSePlanta,
                this::mostrarPausa));

        nuevaRonda();
    }

    private void nuevaRonda() {
        mazo = new Mazo();
        for (Jugador jugador : jugadores) {
            jugador.nuevaMano();
        }
        dealer.nuevaMano();

        for (int vuelta = 0; vuelta < 2; vuelta++) {
            for (Jugador jugador : jugadores) {
                jugador.recibirCarta(mazo.obtenerUnaCarta());
            }
            dealer.recibirCarta(mazo.obtenerUnaCarta());
        }
        dealer.getMano().getCartas().get(0).makeFaceDown();

        indiceJugadorActual = 0;
        avanzarTurno();
    }

    private void jugadorPideCarta() {
        Jugador jugador = jugadores.get(indiceJugadorActual);
        jugador.recibirCarta(mazo.obtenerUnaCarta());
        redibujar(false);

        int total = jugador.getMano().calcularTotal();

        if (jugador.getMano().sePaso()) {
            vista.mostrarMensaje(jugador.getNombre() + " se pasó de 21.");
            indiceJugadorActual++;
            avanzarTurno();
        } else if (total == TOTAL_PARA_PLANTARSE_AUTOMATICO) {
            vista.mostrarMensaje(jugador.getNombre() + " llegó a 21, se planta automáticamente.");
            indiceJugadorActual++;
            avanzarTurno();
        }
    }

    private void jugadorSePlanta() {
        indiceJugadorActual++;
        avanzarTurno();
    }

    private void avanzarTurno() {
        if (indiceJugadorActual < jugadores.size()) {
            Jugador jugador = jugadores.get(indiceJugadorActual);

            if (jugador.getMano().calcularTotal() == TOTAL_PARA_PLANTARSE_AUTOMATICO) {
                vista.mostrarMensaje(jugador.getNombre() + " tiene 21, se planta automáticamente.");
                indiceJugadorActual++;
                avanzarTurno();
                return;
            }

            vista.mostrarMensaje("Turno de " + jugador.getNombre());
            vista.habilitarBotonesDeTurno(true);
            redibujar(false);
        } else {
            vista.habilitarBotonesDeTurno(false);
            turnoDealer();
        }
    }

    private void turnoDealer() {
        revelarDealer();

        if (todosSePasaron()) {
            redibujar(true);
            String mensaje = "Todos los jugadores se pasaron de 21.";
            vista.mostrarMensaje(mensaje);
            vista.mostrarDialogoFinRonda(mensaje, this::nuevaRonda, this::reiniciarPartida);
            return;
        }

        while (dealer.debePedirCarta()) {
            dealer.recibirCarta(mazo.obtenerUnaCarta());
        }
        redibujar(true);

        String resultado = determinarGanadores();
        vista.mostrarDialogoFinRonda(resultado, this::nuevaRonda, this::reiniciarPartida);
    }

    private boolean todosSePasaron() {
        for (Jugador jugador : jugadores) {
            if (!jugador.getMano().sePaso()) {
                return false;
            }
        }
        return true;
    }

    private void revelarDealer() {
        for (CartaInglesa carta : dealer.getMano().getCartas()) {
            carta.makeFaceUp();
        }
    }

    private String determinarGanadores() {
        int totalDealer = dealer.getMano().calcularTotal();
        StringBuilder resultado = new StringBuilder();

        for (Jugador jugador : jugadores) {
            int totalJugador = jugador.getMano().calcularTotal();
            resultado.append(jugador.getNombre()).append(": ");

            if (jugador.getMano().sePaso()) {
                resultado.append("perdió (se pasó de 21).\n");
            } else if (dealer.getMano().sePaso() || totalJugador > totalDealer) {
                resultado.append("¡ganó!\n");
            } else if (totalJugador < totalDealer) {
                resultado.append("perdió contra el dealer.\n");
            } else {
                resultado.append("empató.\n");
            }
        }

        String texto = resultado.toString();
        vista.mostrarMensaje(texto);
        return texto;
    }

    private void mostrarPausa() {
        vista.mostrarDialogoPausa(this::reiniciarPartida, this::salirDeLaAplicacion);
    }

    private void reiniciarPartida() {
        jugadores.clear();
        dealer = null;
        mazo = null;
        indiceJugadorActual = 0;
        stage.setScene(vista.crearPantallaInicio(this::comenzarJuego));
    }

    private void salirDeLaAplicacion() {
        Platform.exit();
    }

    private void redibujar(boolean mostrarTotalDealer) {
        int indiceTurno = (indiceJugadorActual < jugadores.size()) ? indiceJugadorActual : -1;
        vista.actualizarMesa(jugadores, dealer, mostrarTotalDealer, indiceTurno, mazo.getCartas().size());
    }
}