package personajes;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import Red.HiloServidor;
import niveles.NivelBase;

public class Enemigo {

    private int vida = 50;
    private Body cuerpo;
    private float anchoHitbox = 48;
    private float altoHitbox = 48;
    private int daño = 10;
    private int cooldown = 1;
    private float tiempoTranscurrido = 0;
    private NivelBase nivel;
    private float alcanceAtaque = 0.15f;
    private boolean enCooldown = false;
    private boolean encontroJugador;
    private World mundo;
    private boolean muerto = false;
    private HiloServidor hiloServidor;
    private final int ID;

    // ✅ Referencias directas a los jugadores
    private Jugador jugador1;
    private Jugador jugador2;

    // ✅ Control de sincronización para evitar spam
    private float tiempoDesdeUltimaSincronizacion = 0f;
    private static final float INTERVALO_SINCRONIZACION = 0.1f; // 10Hz para enemigos

    public Enemigo(World mundo, float x, float y, NivelBase nivel, int id) {
        this.ID = id;
        this.nivel = nivel;
        this.anchoHitbox = 48;
        this.altoHitbox = 48;
        this.mundo = mundo;

        // ✅ OBTENER REFERENCIAS A LOS JUGADORES
        this.jugador1 = nivel.getJugador1();
        this.jugador2 = nivel.getJugador2();

        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.position.set(x * NivelBase.PIXELES_A_METROS, y * NivelBase.PIXELES_A_METROS);
        def.fixedRotation = true;

        this.cuerpo = this.mundo.createBody(def);

        PolygonShape forma = new PolygonShape();
        forma.setAsBox(
            anchoHitbox / 2 * NivelBase.PIXELES_A_METROS,
            altoHitbox / 2 * NivelBase.PIXELES_A_METROS
        );

        FixtureDef fixture = new FixtureDef();
        fixture.shape = forma;
        fixture.density = 1f;
        fixture.friction = 0.5f;
        cuerpo.createFixture(fixture);
        forma.dispose();

        cuerpo.setUserData(this);

        System.out.println("👹 Enemigo " + id + " creado con referencias a jugadores");
    }

    public void setHiloServidor(HiloServidor hiloServidor) {
        this.hiloServidor = hiloServidor;
    }

    public void act(float delta) {
        // ✅ DEBUG temporal - Verificar distancias
        if (jugador1 != null && jugador1.getCuerpo() != null &&
            jugador2 != null && jugador2.getCuerpo() != null) {

            float distJ1 = Vector2.dst(
                this.cuerpo.getPosition().x, this.cuerpo.getPosition().y,
                jugador1.getCuerpo().getPosition().x,
                jugador1.getCuerpo().getPosition().y
            );
            float distJ2 = Vector2.dst(
                this.cuerpo.getPosition().x, this.cuerpo.getPosition().y,
                jugador2.getCuerpo().getPosition().x,
                jugador2.getCuerpo().getPosition().y
            );

            if (distJ1 < 1f || distJ2 < 1f) {
               // vacio
            }
        }

        // Calcular y moverse hacia el jugador más cercano
        Jugador objetivo = calcularJugadorObjetivo();
        if (objetivo != null) {
            determinarDireccionMovimiento(objetivo);
        }

        // ✅ Sincronizar posición solo cada 100ms
        tiempoDesdeUltimaSincronizacion += delta;

        if (hiloServidor != null && tiempoDesdeUltimaSincronizacion >= INTERVALO_SINCRONIZACION) {
            hiloServidor.enviarMensajeATodos(
                String.format(java.util.Locale.US, "Enemigo:%d:ActualizarPosicion:%.2f:%.2f",
                    this.ID, getPosicionX(), getPosicionY())
            );
            tiempoDesdeUltimaSincronizacion = 0f;
        }

        this.tiempoTranscurrido += delta;

        if (this.tiempoTranscurrido >= cooldown && enCooldown) {
            enCooldown = false;
        }

        if (!enCooldown) {
            encontroJugador = false;

            Vector2 posicionEnemigo = this.cuerpo.getPosition();

            float anchoAreaAtaque = (anchoHitbox * NivelBase.PIXELES_A_METROS) + (2 * alcanceAtaque);
            float altoAreaAtaque = altoHitbox * NivelBase.PIXELES_A_METROS * 0.6f;

            float centroXAreaAtaque = posicionEnemigo.x;
            float centroYAreaAtaque = posicionEnemigo.y;

            float lowerX = centroXAreaAtaque - (anchoAreaAtaque / 2);
            float upperX = centroXAreaAtaque + (anchoAreaAtaque / 2);
            float lowerY = centroYAreaAtaque - (altoAreaAtaque / 2);
            float upperY = centroYAreaAtaque + (altoAreaAtaque / 2);

            this.mundo.QueryAABB(fixture -> {
                Object userData = fixture.getBody().getUserData();

                if (userData instanceof Jugador) {
                    Jugador jugador = (Jugador) userData;
                    Vector2 posJugador = jugador.getCuerpo().getPosition();

                    float diferenciaY = Math.abs(posJugador.y - posicionEnemigo.y);

                    if (diferenciaY < 0.3f) {
                        // ✅ Aplicar daño
                        jugador.recibirDaño(this.daño);

                        if (hiloServidor != null) {
                            int nuevaVida = jugador.getVida();
                            String mensaje = "Jugador:" + jugador.getIdJugador() + ":Dañar:" + nuevaVida;
                            hiloServidor.enviarMensajeATodos(mensaje);

                            System.out.println("💥 [SERVIDOR] Enemigo " + this.ID + " dañó a Jugador " +
                                jugador.getIdJugador() + " → Vida: " + nuevaVida + " (Mensaje: " + mensaje + ")");
                        }

                        this.encontroJugador = true;
                    }

                    return true;
                }
                return true;
            }, lowerX, lowerY, upperX, upperY);

            if (encontroJugador) {
                this.enCooldown = true;
                this.tiempoTranscurrido = 0f;

                if (hiloServidor != null) {
                    hiloServidor.enviarMensajeATodos("Enemigo:" + this.ID + ":Atacando");
                }
            }
        }
    }

    public void recibirDaño(int dañoAtaque) {
        if (dañoAtaque <= 0 || vida <= 0) return;

        if (vida - dañoAtaque < 0) {
            this.vida = 0;
        } else {
            this.vida -= dañoAtaque;
        }

        if (vida == 0) {
            muerto = true;
            System.out.println("💀 Enemigo " + this.ID + " murió");
        }
    }

    public void eliminar() {
        if (this.cuerpo != null && this.mundo != null) {
            this.mundo.destroyBody(this.cuerpo);
            this.cuerpo = null;
        }
    }

    public boolean getMuerto() {
        return this.muerto;
    }

    public int getID() {
        return this.ID;
    }

    public float getPosicionX() {
        return this.cuerpo.getPosition().x;
    }

    public float getPosicionY() {
        return this.cuerpo.getPosition().y;
    }

    public void dispose() {
        // No hay textura que limpiar en servidor
    }

    private void determinarDireccionMovimiento(Jugador objetivo) {
        if (objetivo == null || objetivo.getCuerpo() == null) return;

        Vector2 posicionJugador = objetivo.getCuerpo().getPosition();
        Vector2 posicionEnemigo = cuerpo.getPosition();

        Vector2 direccion = posicionJugador.cpy().sub(posicionEnemigo).nor().scl(1.5f);
        cuerpo.setLinearVelocity(direccion.x, cuerpo.getLinearVelocity().y);
    }

    private Jugador calcularJugadorObjetivo() {
        // ✅ Verificar que los jugadores no sean null
        if (jugador1 == null || jugador1.getCuerpo() == null) {
            return jugador2;
        }
        if (jugador2 == null || jugador2.getCuerpo() == null) {
            return jugador1;
        }

        float posicionAbsolutaJugador1 = Math.abs(jugador1.getCuerpo().getPosition().x);
        float posicionAbsolutaJugador2 = Math.abs(jugador2.getCuerpo().getPosition().x);
        float posicionAbsolutaEnemigo = Math.abs(this.cuerpo.getPosition().x);

        // Perseguir al jugador más cercano
        if (Math.abs(posicionAbsolutaJugador1 - posicionAbsolutaEnemigo) >
            Math.abs(posicionAbsolutaJugador2 - posicionAbsolutaEnemigo)) {
            return jugador2;
        } else {
            return jugador1;
        }
    }
}
