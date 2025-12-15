package interfaces;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

import globales.EsceneManager;
import niveles.EscenaBase;

public class Instrucciones extends EscenaBase {

    public Instrucciones(Game juego) {
        super(juego, "instrucciones.png");
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this.inputManager);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        if (this.inputManager.getIsEscPressed()) {
            // ✅ Volver a la escena anterior guardada
            Object escenaAnterior = EsceneManager.getEscenaActual();

            if (escenaAnterior != null && escenaAnterior instanceof Screen) {
                juego.setScreen((Screen) escenaAnterior);
            } else {
                // Si no hay escena anterior, ir al menú
                juego.setScreen(new Menu(juego));
            }
        }
    }
}
