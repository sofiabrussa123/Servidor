package personajes.accesorios;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

// Enum que representa los diferentes estados de animación del jugador
public enum Estado {
    QUIETO("animaciones/idle.png", 0.1f, 128, 128), 
    CORRIENDO("animaciones/run.png", 0.1f, 128, 128),
    ATACANDO("animaciones/Punch_1.png", 0.1f, 128, 128);
    // SALTANDO("animaciones/jump.png", 0.1f, 128, 128); // Deshabilitado temporalmente

    private final String rutaHoja;
    private final float duracionFrame;
    private final int anchoFrame;
    private final int altoFrame;

    private Estado(String rutaHoja, float duracionFrame, int anchoFrame, int altoFrame) {
        this.rutaHoja = rutaHoja;
        this.duracionFrame = duracionFrame;
        this.anchoFrame = anchoFrame;
        this.altoFrame = altoFrame;
    }
    
    // Crea una animación a partir de una hoja de sprites
    public Animation<TextureRegion> crearAnimacion() {
        Texture hoja = new Texture(rutaHoja);
        
        TextureRegion[][] temporal = TextureRegion.split(hoja, anchoFrame, altoFrame);
        
        Array<TextureRegion> cuadros = new Array<>();
        
        // Aplanar la matriz de frames en un array lineal
        for (TextureRegion[] fila : temporal) {
            for (TextureRegion cuadro : fila) {
                cuadros.add(cuadro);
            }
        }

        if (cuadros.size == 0) {
            throw new IllegalArgumentException(
                "La hoja de sprites no contiene cuadros o no se ha cargado correctamente: " + rutaHoja
            );
        }
        
        return new Animation<TextureRegion>(duracionFrame, cuadros);
    }
}
