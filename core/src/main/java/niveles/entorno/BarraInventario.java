package niveles.entorno;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.viewport.Viewport;

import personajes.Jugador;

/**
 * Barra de inventario visual que muestra los items del jugador.
 * Se posiciona en la parte superior de la pantalla.
 */
public class BarraInventario extends Actor {

    private Jugador jugador;
    private Texture slotVacio;
    private Texture[] iconos;
    private boolean posicionIzquierda;

    /**
     * Constructor de la barra de inventario.
     * @param jugador El jugador dueño de este inventario
     * @param posicionIzquierda true para posicionar en la izquierda, false para la derecha
     */
    public BarraInventario(Jugador jugador, boolean posicionIzquierda) {
        this.jugador = jugador;
        this.posicionIzquierda = posicionIzquierda;
        this.slotVacio = new Texture("barraInventario.png");
        this.iconos = new Texture[5];

        for (int i = 0; i < this.iconos.length; i++) {
            this.iconos[i] = null;
        }

        this.setWidth(300f);
        this.setHeight(50f);
    }

    /**
     * Obtiene el icono en la posición especificada.
     * @param i Índice del slot (0-4)
     * @return Textura del icono o null si está vacío
     */
    public Texture getIcono(int i) {
        return (i >= 0 && i < this.iconos.length) ? this.iconos[i] : null;
    }

    /**
     * Establece un icono en la posición especificada.
     * @param i Índice del slot (0-4)
     * @param textura Textura del icono a colocar
     */
    public void setIcono(int i, Texture textura) {
        if (i >= 0 && i < this.iconos.length) {
            this.iconos[i] = textura;
        }
    }

    /**
     * Limpia el icono en la posición especificada.
     * @param i Índice del slot (0-4)
     */
    public void limpiarIcono(int i) {
        if (i >= 0 && i < this.iconos.length) {
            this.iconos[i] = null;
        }
    }

    /**
     * Busca el primer slot vacío en el inventario.
     * @return Índice del primer slot libre, o -1 si está lleno
     */
    public int getPrimeraCasillaLibre() {
        for (int i = 0; i < this.iconos.length; i++) {
            if (this.iconos[i] == null) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        // ✅ Usar el Stage de este Actor, no del jugador
        if (getStage() == null) {
            return; // No dibujar si no hay Stage (servidor headless)
        }

        Viewport viewport = getStage().getViewport();
        float y = viewport.getScreenY() + viewport.getScreenHeight() - 90;

        if (posicionIzquierda) {
            // Dibujar inventario desde la izquierda
            float baseX = viewport.getScreenX() + 10;

            for (int i = 0; i < 5; i++) {
                float x = baseX + (i * 52);
                batch.draw(slotVacio, x, y, 50f, 50f);
                if (iconos[i] != null) {
                    batch.draw(iconos[i], x, y, 50f, 50f);
                }
            }
        } else {
            // Dibujar inventario desde la derecha
            float baseX = viewport.getScreenX() + viewport.getScreenWidth() - 10 - 50;

            for (int i = 0; i < 5; i++) {
                float x = baseX - (i * 52);
                batch.draw(slotVacio, x, y, 50f, 50f);
                if (iconos[i] != null) {
                    batch.draw(iconos[i], x, y, 50f, 50f);
                }
            }
        }
    }

    /**
     * Libera los recursos de texturas utilizados.
     */
    public void dispose() {
        if (slotVacio != null) {
            slotVacio.dispose();
        }

        for (Texture icono : iconos) {
            if (icono != null) {
                icono.dispose();
            }
        }
    }
}
