package niveles.entorno;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import io.github.timoria.Principal;

public abstract class ElementoEntorno {

    protected World mundo;
    protected float x;
    protected float y;
    protected Texture textura;
    protected Body cuerpo;
    protected float ancho;
    protected float alto;
    protected BodyDef.BodyType tipoCuerpo = BodyDef.BodyType.StaticBody;
    protected FixtureDef fixtureDef = new FixtureDef();
    protected final int ID;

    protected ElementoEntorno(World mundo, float x, float y, float ancho, float alto, int id) {
        this.ID = id;
        this.mundo = mundo;
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
    }

    protected ElementoEntorno(World mundo, float x, float y, int id) {
        this.ID = id;
        this.mundo = mundo;
        this.x = x;
        this.y = y;
    }

    protected void crearYPosicionarCuerpo() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(
            (x + ancho / 2) / Principal.PPM,
            (y + alto / 2) / Principal.PPM
        );
        bodyDef.type = tipoCuerpo;

        cuerpo = mundo.createBody(bodyDef);
        cuerpo.setUserData(this);

        PolygonShape forma = new PolygonShape();
        forma.setAsBox(
            ancho / 2 / Principal.PPM,
            (alto / 2 - 2) / Principal.PPM
        );

        fixtureDef.shape = forma;
        fixtureDef.friction = 0.5f;
        cuerpo.createFixture(fixtureDef);
        forma.dispose();

        cuerpo.setUserData(this);
    }

    public Body getCuerpo() {
        return cuerpo;
    }

    public int getID() {
        return ID;
    }

    protected void setTipoCuerpo(BodyDef.BodyType tipo) {
        this.tipoCuerpo = tipo;
    }

    protected void setFixtureDef(FixtureDef fixtureDef) {
        this.fixtureDef = fixtureDef;
    }

    public void dispose() {
        if (textura != null) {
            textura.dispose();
        }
    }
}
