package interfaces;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import Red.HiloServidor;
import Red.ServidorManager;
import globales.EsceneManager;
import interfaces.Instrucciones;
import niveles.EscenaBase;

public class Menu extends EscenaBase {

    private Music musica;
    private boolean musicaActiva = true;
    private HiloServidor hiloServidor;

    public Menu(Game juego) {
        super(juego, "Fondo.jpeg");

        // ✅ Obtener servidor único a través del manager
        this.hiloServidor = ServidorManager.obtenerServidor(juego);
        System.out.println("✅ Servidor obtenido en Menu");

        // Fuente / Skin
        super.fuenteTextos = new Skin(Gdx.files.internal("uiskin.json"));

        // Música de fondo
        this.musica = Gdx.audio.newMusic(Gdx.files.internal("musica_fondo.mp3"));
        this.musica.setLooping(true);
        this.musica.setVolume(0.5f);
        this.musica.play();

        // Tabla principal de layout
        Table table = new Table();
        table.setFillParent(true);
        super.escena.addActor(table);

        // Botón Jugar
        TextButton botonJugar = new TextButton("Jugar", super.fuenteTextos);
        botonJugar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Menu.this.musica.stop();
                Menu.this.musica.dispose();

                // ✅ NO crear nivel aquí - El servidor lo hará automáticamente
                System.out.println("⏳ Esperando a que los clientes se conecten y completen la historia...");

                // Opcional: Cambiar a pantalla de espera
                // juego.setScreen(new PantallaEspera(juego, hiloServidor));
            }
        });

        // Botón de Música (activar/desactivar)
        TextButton botonMusicaMenu = new TextButton("Silenciar Música", super.fuenteTextos);
        botonMusicaMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Menu.this.musicaActiva = !Menu.this.musicaActiva;

                if (Menu.this.musicaActiva) {
                    Menu.this.musica.play();
                    botonMusicaMenu.setText("Silenciar Música");
                } else {
                    Menu.this.musica.pause();
                    botonMusicaMenu.setText("Activar Música");
                }
            }
        });

        // Botón con instrucciones básicas
        TextButton btnInstrucciones = new TextButton("Instrucciones", super.fuenteTextos);
        btnInstrucciones.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                EsceneManager.setEscenaActual(Menu.this);
                juego.setScreen(new Instrucciones(juego));
            }
        });

        // Agregar elementos a la tabla
        table.add(botonJugar).width(350).height(40).padBottom(15);
        table.row();

        table.add(botonMusicaMenu).width(350).height(40).padBottom(15);
        table.row();

        table.add(btnInstrucciones).width(350).height(40);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this.escena);
    }

    @Override
    public void dispose() {
        super.dispose();
        // ✅ NO terminar el servidor aquí, solo limpiar la música
        if (this.musica != null) {
            this.musica.dispose();
        }
        System.out.println("✅ Menu dispose - Música limpiada (servidor sigue activo)");
    }
}
