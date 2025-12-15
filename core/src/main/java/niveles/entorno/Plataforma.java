package niveles.entorno;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;

// Plataforma estática en la que los jugadores pueden pararse
public class Plataforma extends ElementoEntorno {

    public Plataforma(World mundo, float x, float y, int ancho, int alto, int id) {
        super(mundo, x, y, ancho, alto, id);
        textura = new Texture(Gdx.files.internal("plataforma.jpg"));
        super.crearYPosicionarCuerpo();
    }
}
