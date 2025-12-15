package niveles;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import globales.InputManager;
import interfaces.Menu;

/**
 * Clase base para todas las escenas del juego.
 * Implementa Screen de libGDX y proporciona funcionalidad común.
 */
public abstract class EscenaBase implements Screen {

    protected Game juego;
    protected Stage escena;
    protected Texture fondo;
    protected Skin fuenteTextos;
    protected InputManager inputManager;
    protected Image imagenFondo;

    /**
     * Constructor de la escena base.
     * @param juego Instancia del juego principal
     * @param fondo Ruta del archivo de textura para el fondo
     */
    public EscenaBase(Game juego, String fondo) {
        this.juego = juego;
        this.escena = new Stage(new ScreenViewport());
        this.fondo = new Texture(fondo);
        this.fuenteTextos = new Skin(Gdx.files.internal("uiskin.json"));
        this.inputManager = new InputManager();
        this.imagenFondo = new Image(this.fondo);
        this.imagenFondo.setPosition(0, 0);
        this.imagenFondo.setSize(escena.getViewport().getWorldWidth(), escena.getViewport().getWorldHeight());
        escena.addActor(imagenFondo);
    }

    /**
     * Cambia la escena actual a una nueva escena.
     * @param nuevaEscena La nueva escena a mostrar
     */
    protected void cambiarEscena(Menu nuevaEscena) {
        this.juego.setScreen(nuevaEscena);
    }

    @Override
    public void render(float delta) {
        // Limpiar frame anterior, rellenar con negro
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        escena.act(delta);
        escena.draw();
    }

    @Override
    public void resize(int width, int height) {
        this.escena.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        this.escena.dispose();
        if (this.fuenteTextos != null) {
            this.fuenteTextos.dispose();
        }
    }

    @Override
    public void show() { }

    @Override
    public void resume() { }

    @Override
    public void pause() { }

    @Override
    public void hide() { }
}
