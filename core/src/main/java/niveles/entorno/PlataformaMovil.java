package niveles.entorno;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

import Red.HiloServidor;
import io.github.timoria.Principal;

public class PlataformaMovil extends ElementoEntorno {

    private int direccionMovimiento;
    private int distanciaMovimiento;
    private Palanca palancaActivadora;
    private float velocidadMovimiento = 1.5f;
    private float posicionInicial;
    private float dx = 0;
    private float dy = 0;
    private HiloServidor hiloServidor;

    // ✅ Control de sincronización
    private float tiempoDesdeUltimaSincronizacion = 0f;
    private static final float INTERVALO_SINCRONIZACION = 0.05f; // 20Hz

    // Constructor con palanca
    public PlataformaMovil(World mundo, float x, float y, int direccionMovimiento,
                           int distanciaMovimiento, Palanca palanca, int id) {
        super(mundo, x, y, 150, 20, id);
        this.direccionMovimiento = direccionMovimiento;
        this.distanciaMovimiento = (int) (distanciaMovimiento / Principal.PPM);
        this.palancaActivadora = palanca;

        inicializarMovimiento();
    }

    // Constructor sin palanca
    public PlataformaMovil(World mundo, float x, float y, int direccionMovimiento,
                           int distanciaMovimiento, int id) {
        super(mundo, x, y, 150, 20, id);
        this.direccionMovimiento = direccionMovimiento;
        this.distanciaMovimiento = (int) (distanciaMovimiento / Principal.PPM);

        inicializarMovimiento();
    }

    public void setHiloServidor(HiloServidor hiloServidor) {
        this.hiloServidor = hiloServidor;
    }

    private void inicializarMovimiento() {
        switch (this.direccionMovimiento) {
            case 1: dy = 1; break; // Vertical
            case 2: dx = 1; break; // Horizontal
        }

        setTipoCuerpo(BodyDef.BodyType.KinematicBody);
        super.crearYPosicionarCuerpo();

        this.posicionInicial = (dx != 0) ? cuerpo.getPosition().x : cuerpo.getPosition().y;
    }

    public void act(float delta) {
        tiempoDesdeUltimaSincronizacion += delta;

        // Si hay palanca y no está activada, detener movimiento
        if (palancaActivadora != null && !palancaActivadora.getActivada()) {
            cuerpo.setLinearVelocity(0, 0);
            return;
        }

        float posicion = (dx != 0) ? cuerpo.getPosition().x : cuerpo.getPosition().y;

        if (velocidadMovimiento > 0) {
            if (posicion < posicionInicial + distanciaMovimiento) {
                cuerpo.setLinearVelocity(velocidadMovimiento * dx, velocidadMovimiento * dy);
            }
            cambiarSentidoMovimiento(posicion, posicionInicial, distanciaMovimiento);
        } else {
            if (posicion > posicionInicial) {
                cuerpo.setLinearVelocity(velocidadMovimiento * dx, velocidadMovimiento * dy);
            }
            cambiarSentidoMovimiento(posicion, posicionInicial, distanciaMovimiento);
        }

        // ✅ Sincronizar posición con clientes
        if (hiloServidor != null && tiempoDesdeUltimaSincronizacion >= INTERVALO_SINCRONIZACION) {
            float posX = this.cuerpo.getPosition().x;
            float posY = this.cuerpo.getPosition().y;

            String mensaje = String.format(java.util.Locale.US, "PlataformaMovil:%d:%.2f:%.2f",
                this.ID, posX, posY);

            hiloServidor.enviarMensajeATodos(mensaje);

            // ✅ DEBUG: Ver qué se envía
            System.out.println("📤 [SERVIDOR] " + mensaje);

            tiempoDesdeUltimaSincronizacion = 0f;
        }
    }

    private void cambiarSentidoMovimiento(float posicion, float posicionInicial, int distanciaMovimiento) {
        if (posicion >= posicionInicial + distanciaMovimiento || posicion <= posicionInicial) {
            this.velocidadMovimiento = -this.velocidadMovimiento;
        }
    }
}
