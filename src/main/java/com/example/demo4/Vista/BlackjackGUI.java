package com.example.demo4.Vista;

import com.example.demo4.Modelo.CartaInglesa;
import com.example.demo4.Modelo.Dealer;
import com.example.demo4.Modelo.Jugador;
import com.example.demo4.Modelo.Palo;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class BlackjackGUI {

    // ---------- Carga de imágenes ----------

    private static final String CARPETA_IMAGENES = "/cartas poker/";
    private static final String RUTA_REVERSO = CARPETA_IMAGENES + "cartaAtras.jpg";

    private static final Map<Palo, String> NOMBRE_PALO_EN_ARCHIVO = new EnumMap<>(Palo.class);
    private static final Map<String, Image> CACHE_IMAGENES = new HashMap<>();

    static {
        NOMBRE_PALO_EN_ARCHIVO.put(Palo.TREBOL, "TREBOL");
        NOMBRE_PALO_EN_ARCHIVO.put(Palo.CORAZON, "CORAZON");
        NOMBRE_PALO_EN_ARCHIVO.put(Palo.PICA, "PICAS");
        NOMBRE_PALO_EN_ARCHIVO.put(Palo.DIAMANTE, "DIAMANTE");
    }

    private static Image obtenerImagen(CartaInglesa carta) {
        int numero = (carta.getValor() == 14) ? 1 : carta.getValor();
        String nombrePalo = NOMBRE_PALO_EN_ARCHIVO.get(carta.getPalo());
        String ruta = CARPETA_IMAGENES + "CARTA " + numero + " " + nombrePalo + ".jpg";
        return CACHE_IMAGENES.computeIfAbsent(ruta,
                r -> new Image(BlackjackGUI.class.getResourceAsStream(r)));
    }

    private static Image obtenerImagenReverso() {
        return CACHE_IMAGENES.computeIfAbsent(RUTA_REVERSO,
                r -> new Image(BlackjackGUI.class.getResourceAsStream(r)));
    }
    
    private final List<TextField> camposNombres = new ArrayList<>();
    private final VBox contenedorCampos = new VBox(8);

    public Scene crearPantallaInicio(Consumer<List<String>> alComenzar) {
        Label titulo = new Label("BLACKJACK");
        titulo.setStyle("-fx-text-fill: white; -fx-font-size: 28px; -fx-font-weight: bold;");

        ChoiceBox<Integer> selectorCantidad = new ChoiceBox<>();
        selectorCantidad.getItems().addAll(1, 2, 3, 4);
        selectorCantidad.setValue(1);
        selectorCantidad.setOnAction(e -> regenerarCamposNombre(selectorCantidad.getValue()));

        Label labelCantidad = new Label("Número de jugadores:");
        labelCantidad.setStyle("-fx-text-fill: white;");
        HBox filaCantidad = new HBox(10, labelCantidad, selectorCantidad);
        filaCantidad.setAlignment(Pos.CENTER);

        contenedorCampos.setAlignment(Pos.CENTER);
        regenerarCamposNombre(1);

        Button botonComenzar = new Button("Comenzar juego");
        botonComenzar.setOnAction(e -> {
            List<String> nombres = new ArrayList<>();
            for (int i = 0; i < camposNombres.size(); i++) {
                String texto = camposNombres.get(i).getText().trim();
                nombres.add(texto.isEmpty() ? ("Jugador " + (i + 1)) : texto);
            }
            alComenzar.accept(nombres);
        });

        Button botonSalir = new Button("Salir del juego");
        botonSalir.setOnAction(e -> Platform.exit());

        VBox raiz = new VBox(16, titulo, filaCantidad, contenedorCampos, botonComenzar, botonSalir);
        raiz.setAlignment(Pos.CENTER);
        raiz.setPadding(new Insets(30));
        raiz.setStyle("-fx-background-color: #1b5e20;");

        return new Scene(raiz, 500, 400);
    }

    private void regenerarCamposNombre(int cantidad) {
        camposNombres.clear();
        contenedorCampos.getChildren().clear();
        for (int i = 1; i <= cantidad; i++) {
            TextField campo = new TextField();
            campo.setPromptText("Nombre del jugador " + i);
            camposNombres.add(campo);
            contenedorCampos.getChildren().add(campo);
        }
    }

    private Scene escenaMesa;
    private VBox areaDealer;
    private FlowPane areaJugadores;
    private ImageView imagenMazo;
    private Label labelCartasRestantes;
    private Label labelMensaje;
    private Button botonPedir;
    private Button botonPlantarse;

    private List<Jugador> ultimosJugadores;
    private Dealer ultimoDealer;
    private boolean ultimoMostrarTotalDealer;
    private int ultimoIndiceTurno;
    private int ultimasCartasRestantes = -1;

    public Scene crearPantallaMesa(Runnable alPedirCarta, Runnable alPlantarse, Runnable alPausa) {
        BorderPane raiz = new BorderPane();
        raiz.setPadding(new Insets(20));
        raiz.setStyle("-fx-background-color: #2e7d32;");

        areaDealer = new VBox();
        areaDealer.setAlignment(Pos.CENTER);
        raiz.setTop(areaDealer);

        areaJugadores = new FlowPane(24, 24);
        areaJugadores.setAlignment(Pos.CENTER);
        raiz.setCenter(areaJugadores);

        imagenMazo = new ImageView();
        imagenMazo.setPreserveRatio(false);
        labelCartasRestantes = new Label();
        labelCartasRestantes.setStyle("-fx-text-fill: white;");
        VBox areaMazo = new VBox(8, imagenMazo, labelCartasRestantes);
        areaMazo.setAlignment(Pos.CENTER);
        raiz.setLeft(areaMazo);
        BorderPane.setMargin(areaMazo, new Insets(0, 25, 0, 0));

        botonPedir = new Button("Pedir carta");
        botonPedir.setOnAction(e -> alPedirCarta.run());

        botonPlantarse = new Button("Plantarse");
        botonPlantarse.setOnAction(e -> alPlantarse.run());

        Button botonPausa = new Button("Pausa");
        botonPausa.setOnAction(e -> alPausa.run());

        HBox botones = new HBox(12, botonPedir, botonPlantarse, botonPausa);
        botones.setAlignment(Pos.CENTER);

        labelMensaje = new Label();
        labelMensaje.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        labelMensaje.setWrapText(true);
        labelMensaje.setMaxWidth(600);

        VBox abajo = new VBox(10, labelMensaje, botones);
        abajo.setAlignment(Pos.CENTER);
        abajo.setPadding(new Insets(15, 0, 0, 0));
        raiz.setBottom(abajo);

        escenaMesa = new Scene(raiz, 900, 600);
        escenaMesa.widthProperty().addListener((obs, viejo, nuevo) -> dibujarMesa());
        escenaMesa.heightProperty().addListener((obs, viejo, nuevo) -> dibujarMesa());

        dibujarMesa();
        return escenaMesa;
    }

    public void actualizarMesa(List<Jugador> jugadores, Dealer dealer, boolean mostrarTotalDealer,
                               int indiceTurno, int cartasRestantes) {
        this.ultimosJugadores = jugadores;
        this.ultimoDealer = dealer;
        this.ultimoMostrarTotalDealer = mostrarTotalDealer;
        this.ultimoIndiceTurno = indiceTurno;
        this.ultimasCartasRestantes = cartasRestantes;
        dibujarMesa();
    }

    private void dibujarMesa() {
        actualizarMazoVisual();

        if (ultimosJugadores == null || ultimoDealer == null) {
            return;
        }

        areaDealer.getChildren().setAll(crearVistaDeJugador(ultimoDealer, ultimoMostrarTotalDealer, false));

        areaJugadores.getChildren().clear();
        for (int i = 0; i < ultimosJugadores.size(); i++) {
            areaJugadores.getChildren().add(
                    crearVistaDeJugador(ultimosJugadores.get(i), true, i == ultimoIndiceTurno));
        }
    }

    private void actualizarMazoVisual() {
        double ancho = calcularAnchoCarta();
        imagenMazo.setImage(obtenerImagenReverso());
        imagenMazo.setFitWidth(ancho);
        imagenMazo.setFitHeight(ancho * 1.45);
        labelCartasRestantes.setText(ultimasCartasRestantes >= 0 ? "Cartas restantes: " + ultimasCartasRestantes : "");
    }

    private double calcularAnchoCarta() {
        double anchoDisponible = (escenaMesa != null) ? escenaMesa.getWidth() : 900;
        double ancho = anchoDisponible / 20.0;
        return Math.max(35, Math.min(ancho, 70));
    }

    private VBox crearVistaDeJugador(Jugador jugador, boolean mostrarTotal, boolean resaltar) {
        Label nombre = new Label(jugador.getNombre());
        nombre.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

        FlowPane filaCartas = new FlowPane(8, 8);
        filaCartas.setAlignment(Pos.CENTER);
        for (CartaInglesa carta : jugador.getMano().getCartas()) {
            filaCartas.getChildren().add(crearVistaDeCarta(carta));
        }

        Label total = new Label(mostrarTotal ? "Total: " + jugador.getMano().calcularTotal() : "");
        total.setStyle("-fx-text-fill: white;");

        VBox contenedor = new VBox(6, nombre, filaCartas, total);
        contenedor.setAlignment(Pos.CENTER);
        contenedor.setPadding(new Insets(8));
        contenedor.setStyle(resaltar
                ? "-fx-background-color: rgba(255,255,255,0.25); -fx-background-radius: 10;"
                : "-fx-background-color: transparent;");

        return contenedor;
    }

    private StackPane crearVistaDeCarta(CartaInglesa carta) {
        double ancho = calcularAnchoCarta();

        ImageView imageView = new ImageView(carta.isFaceup() ? obtenerImagen(carta) : obtenerImagenReverso());
        imageView.setFitWidth(ancho);
        imageView.setFitHeight(ancho * 1.45);
        imageView.setPreserveRatio(false);

        return new StackPane(imageView);
    }

    public void mostrarMensaje(String mensaje) {
        labelMensaje.setText(mensaje);
    }

    public void habilitarBotonesDeTurno(boolean habilitados) {
        botonPedir.setDisable(!habilitados);
        botonPlantarse.setDisable(!habilitados);
    }

    private ButtonType mostrarDialogo(Alert.AlertType tipo, String titulo, String encabezado,
                                      String contenido, ButtonType... botones) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(encabezado);
        alerta.setContentText(contenido);
        alerta.getButtonTypes().setAll(botones);
        return alerta.showAndWait().orElse(null);
    }

    public void mostrarDialogoPausa(Runnable alReiniciar, Runnable alSalir) {
        ButtonType btnReiniciar = new ButtonType("Reiniciar partida");
        ButtonType btnSalir = new ButtonType("Salir de la partida");
        ButtonType btnContinuar = new ButtonType("Continuar jugando", ButtonBar.ButtonData.CANCEL_CLOSE);

        ButtonType respuesta = mostrarDialogo(Alert.AlertType.CONFIRMATION, "Pausa", null,
                "¿Qué quieres hacer?", btnReiniciar, btnSalir, btnContinuar);

        if (respuesta == btnReiniciar) {
            alReiniciar.run();
        } else if (respuesta == btnSalir) {
            alSalir.run();
        }
    }

    public void mostrarDialogoFinRonda(String resultado, Runnable alJugarDeNuevo, Runnable alSalirAlMenu) {
        ButtonType btnJugarDeNuevo = new ButtonType("Jugar de nuevo");
        ButtonType btnSalirAlMenu = new ButtonType("Salir al menú");

        ButtonType respuesta = mostrarDialogo(Alert.AlertType.INFORMATION, "Fin de la ronda", resultado,
                "¿Qué quieres hacer?", btnJugarDeNuevo, btnSalirAlMenu);

        if (respuesta == btnJugarDeNuevo) {
            alJugarDeNuevo.run();
        } else if (respuesta == btnSalirAlMenu) {
            alSalirAlMenu.run();
        }
    }
}