package interfaces;

import java.util.Random;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import Red.HiloServidor;
import niveles.EscenaBase;
import niveles.Nivel1;
import niveles.NivelBase;
import personajes.Jugador;
import personajes.accesorios.MejoraTemporal;

public class PantallaDeMuerte extends EscenaBase {

    private Music musicaMuerte;
    private boolean musicaMuerteActiva = true;

    private final String[] frases = {
        "Las sombras te han tomado. ¿Renacerás?",
        "El ciclo se repite, mientras tu alma vaga sin paz.",
        "El velo entre mundos se cierra… vuelve a intentarlo.",
        "Caíste, pero el vacío aún te llama.",
        "Tu viaje ha terminado, pero tu alma aún busca redención.",
        "Has cruzado el umbral… pero no estabas listo.",
        "Un susurro apagó tu llama."
    };

    private String fraseElegida;
    private Jugador jugador;
    private int idJugadorMuerto;
    private MejoraTemporal mejorasJugador1;
    private MejoraTemporal mejorasJugador2;
    private HiloServidor hiloServidor;

    public PantallaDeMuerte(Game principal, Jugador jugadorMuerto, NivelBase nivelAnterior, HiloServidor hiloServidor) {
        super(principal, "PantallaDeMuerte.png");

        this.jugador = jugadorMuerto;
        this.idJugadorMuerto = jugadorMuerto.getIdJugador();
        this.hiloServidor = hiloServidor;

        // ✅ Guardar mejoras actuales (con las nuevas mejoras aplicadas)
        if (nivelAnterior.getJugador1() != null) {
            this.mejorasJugador1 = copiarMejoras(nivelAnterior.getJugador1().getMejoras());
        }
        if (nivelAnterior.getJugador2() != null) {
            this.mejorasJugador2 = copiarMejoras(nivelAnterior.getJugador2().getMejoras());
        }

        super.fuenteTextos = new Skin(Gdx.files.internal("uiskin.json"));
        this.fraseElegida = frases[new Random().nextInt(frases.length)];

        Label frase = new Label(fraseElegida, super.fuenteTextos);
        frase.setAlignment(Align.center);
        frase.setFontScale(1.2f);
        frase.setWrap(true);
        frase.setWidth(600);

        this.musicaMuerte = Gdx.audio.newMusic(Gdx.files.internal("Muerte.mp3"));
        this.musicaMuerte.setLooping(true);
        if (this.musicaMuerteActiva) this.musicaMuerte.play();

        Label tituloJugador = new Label("=== JUGADOR " + idJugadorMuerto + " - ELIGE UNA MEJORA ===", super.fuenteTextos);
        tituloJugador.setAlignment(Align.center);
        tituloJugador.setColor(Color.YELLOW);
        tituloJugador.setFontScale(1.1f);

        // Botones de mejoras
        TextButton btnMejorarVida = crearBotonMejora("Mejorar Vida (+20)",
            jugador.getMejoras().getMejorasVida(), jugador.getMejoras().getMaxMejoras());

        TextButton btnMejorarVelocidad = crearBotonMejora("Mejorar Velocidad (+1)",
            jugador.getMejoras().getMejorasVelocidad(), jugador.getMejoras().getMaxMejoras());

        TextButton btnMejorarSalto = crearBotonMejora("Mejorar Salto (+1.5)",
            jugador.getMejoras().getMejorasSalto(), jugador.getMejoras().getMaxMejoras());

        TextButton btnMejorarDaño = crearBotonMejora("Mejorar Daño (+10)",
            jugador.getMejoras().getMejorasDaño(), jugador.getMejoras().getMaxMejoras());

        // Listeners de mejoras
        btnMejorarVida.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!btnMejorarVida.isDisabled() && jugador.getMejoras().mejorarVida()) {
                    jugador.actualizarVidaConMejoras();
                    actualizarMejorasGuardadas();
                    actualizarBotonMejora(btnMejorarVida, "Mejorar Vida (+20)",
                        jugador.getMejoras().getMejorasVida(), jugador.getMejoras().getMaxMejoras());
                    if (hiloServidor != null) {
                        hiloServidor.enviarMensajeATodos("Jugador:" + idJugadorMuerto + ":MejoraAplicada:Vida");
                    }
                }
            }
        });

        btnMejorarVelocidad.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!btnMejorarVelocidad.isDisabled() && jugador.getMejoras().mejorarVelocidad()) {
                    actualizarMejorasGuardadas();
                    actualizarBotonMejora(btnMejorarVelocidad, "Mejorar Velocidad (+1)",
                        jugador.getMejoras().getMejorasVelocidad(), jugador.getMejoras().getMaxMejoras());
                    if (hiloServidor != null) {
                        hiloServidor.enviarMensajeATodos("Jugador:" + idJugadorMuerto + ":MejoraAplicada:Velocidad");
                    }
                }
            }
        });

        btnMejorarSalto.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!btnMejorarSalto.isDisabled() && jugador.getMejoras().mejorarSalto()) {
                    actualizarMejorasGuardadas();
                    actualizarBotonMejora(btnMejorarSalto, "Mejorar Salto (+1.5)",
                        jugador.getMejoras().getMejorasSalto(), jugador.getMejoras().getMaxMejoras());
                    if (hiloServidor != null) {
                        hiloServidor.enviarMensajeATodos("Jugador:" + idJugadorMuerto + ":MejoraAplicada:Salto");
                    }
                }
            }
        });

        btnMejorarDaño.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!btnMejorarDaño.isDisabled() && jugador.getMejoras().mejorarDaño()) {
                    actualizarMejorasGuardadas();
                    actualizarBotonMejora(btnMejorarDaño, "Mejorar Daño (+10)",
                        jugador.getMejoras().getMejorasDaño(), jugador.getMejoras().getMaxMejoras());
                    if (hiloServidor != null) {
                        hiloServidor.enviarMensajeATodos("Jugador:" + idJugadorMuerto + ":MejoraAplicada:Daño");
                    }
                }
            }
        });

        TextButton botonMenu = new TextButton("Menú principal", super.fuenteTextos);
        botonMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                PantallaDeMuerte.this.musicaMuerte.pause();

                // ✅ Reiniciar el estado del servidor sin cerrarlo
                if (hiloServidor != null) {
                    hiloServidor.reiniciarEstado();
                }

                juego.setScreen(new Menu(juego));
            }
        });

        // ✅ BOTÓN VOLVER A JUGAR - Crear NUEVO nivel con mejoras guardadas
        TextButton botonVolver = new TextButton("Volver a jugar", super.fuenteTextos);
        botonVolver.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                PantallaDeMuerte.this.musicaMuerte.pause();

                // ✅ Actualizar mejoras estáticas ANTES de crear el nuevo nivel
                if (idJugadorMuerto == 1) {
                    NivelBase.getMejorasJugador1().reset();
                    copiarMejorasA(mejorasJugador1, NivelBase.getMejorasJugador1());
                } else {
                    NivelBase.getMejorasJugador2().reset();
                    copiarMejorasA(mejorasJugador2, NivelBase.getMejorasJugador2());
                }

                // ✅ Crear NUEVO nivel con mejoras actualizadas
                Nivel1 nuevoNivel = new Nivel1(juego, hiloServidor, mejorasJugador1, mejorasJugador2);

                // ✅ Notificar a los clientes que deben reiniciar
                if (hiloServidor != null) {
                    hiloServidor.enviarMensajeATodos("ReiniciarNivel");
                }

                juego.setScreen(nuevoNivel);
                System.out.println("🔄 Nivel reiniciado con mejoras aplicadas");
            }
        });

        TextButton botonMusica = new TextButton("Silenciar música", super.fuenteTextos);
        botonMusica.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                PantallaDeMuerte.this.musicaMuerteActiva = !PantallaDeMuerte.this.musicaMuerteActiva;
                if (PantallaDeMuerte.this.musicaMuerteActiva) {
                    PantallaDeMuerte.this.musicaMuerte.play();
                    botonMusica.setText("Silenciar música");
                } else {
                    PantallaDeMuerte.this.musicaMuerte.pause();
                    botonMusica.setText("Reactivar música");
                }
            }
        });

        // Layout
        Table table = new Table();
        table.setFillParent(true);
        table.center();
        table.padTop(30);

        table.add(tituloJugador).padBottom(15).row();
        table.add(btnMejorarVida).width(250).padBottom(8).row();
        table.add(btnMejorarVelocidad).width(250).padBottom(8).row();
        table.add(btnMejorarSalto).width(250).padBottom(8).row();
        table.add(btnMejorarDaño).width(250).padBottom(20).row();
        table.add(frase).width(600).padBottom(25).row();
        table.add(botonVolver).width(200).padBottom(10).row();
        table.add(botonMusica).width(200).padBottom(10).row();
        table.add(botonMenu).width(200);

        super.escena.addActor(table);
    }

    private void actualizarMejorasGuardadas() {
        if (idJugadorMuerto == 1) {
            mejorasJugador1 = copiarMejoras(jugador.getMejoras());
        } else if (idJugadorMuerto == 2) {
            mejorasJugador2 = copiarMejoras(jugador.getMejoras());
        }
    }

    private MejoraTemporal copiarMejoras(MejoraTemporal origen) {
        MejoraTemporal copia = new MejoraTemporal();
        copiarMejorasA(origen, copia);
        return copia;
    }

    // ✅ Método auxiliar para copiar mejoras
    private void copiarMejorasA(MejoraTemporal origen, MejoraTemporal destino) {
        destino.reset();

        for (int i = 0; i < origen.getMejorasVida(); i++) {
            destino.mejorarVida();
        }
        for (int i = 0; i < origen.getMejorasVelocidad(); i++) {
            destino.mejorarVelocidad();
        }
        for (int i = 0; i < origen.getMejorasSalto(); i++) {
            destino.mejorarSalto();
        }
        for (int i = 0; i < origen.getMejorasDaño(); i++) {
            destino.mejorarDaño();
        }
    }

    private TextButton crearBotonMejora(String nombre, int nivel, int max) {
        String texto = String.format("%s [%d/%d]", nombre, nivel, max);
        TextButton boton = new TextButton(texto, super.fuenteTextos);

        if (nivel >= max) {
            boton.setDisabled(true);
            boton.setColor(Color.GRAY);
        }

        return boton;
    }

    private void actualizarBotonMejora(TextButton boton, String nombre, int nivel, int max) {
        boton.setText(String.format("%s [%d/%d]", nombre, nivel, max));
        if (nivel >= max) {
            boton.setDisabled(true);
            boton.setColor(Color.GRAY);
        }
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(escena);
    }

    @Override
    public void dispose() {
        super.dispose();
        if (musicaMuerte != null) {
            musicaMuerte.dispose();
        }
    }
}
