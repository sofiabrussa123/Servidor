package niveles.entorno;

import Red.HiloServidor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public abstract class ElementoActivador extends ElementoEntorno {

    protected boolean activado = false;
    protected Texture textura;
    private HiloServidor hiloServidor;

    protected ElementoActivador(World mundo, float x, float y, int id) {
        super(mundo, x, y, id);
    }

    protected void crearYPosicionarCuerpo(float ancho, float alto) {
        super.ancho = ancho;
        super.alto = alto;

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.isSensor = true; // No colisiona físicamente

        super.setFixtureDef(fixtureDef);
        super.crearYPosicionarCuerpo();
    }

    public void setHiloServidor(HiloServidor hiloServidor) {
        this.hiloServidor = hiloServidor;
    }

}
