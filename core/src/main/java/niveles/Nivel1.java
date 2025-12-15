package niveles;

import com.badlogic.gdx.Game;

import Red.HiloServidor;
import niveles.entorno.LlaveActivadora;
import niveles.entorno.Palanca;
import niveles.entorno.Plataforma;
import niveles.entorno.PlataformaMovil;
import niveles.entorno.PuertaLlegada;
import personajes.Enemigo;
import personajes.Jugador;
import personajes.accesorios.MejoraTemporal;

public class Nivel1 extends NivelBase {

    // Constructor simple (sin mejoras)
    public Nivel1(Game juego, HiloServidor hiloServidor) {
        this(juego, hiloServidor, null, null);
    }

    // Constructor completo (con mejoras opcionales)
    public Nivel1(Game juego, HiloServidor hiloServidor, MejoraTemporal mejorasJ1, MejoraTemporal mejorasJ2) {
        super(juego);

        this.hiloServidor = hiloServidor;
        System.out.println("✅ HiloServidor asignado a Nivel1: " + (hiloServidor != null));

        this.setFriendlyFire(true);


        Plataforma muroIzquierdo = new Plataforma(mundo, -10, 0, 10, 1000, 100);
        Plataforma muroDerecho = new Plataforma(mundo, 1000, 0, 10, 1000, 101);

        agregarEntidad(muroIzquierdo, 100);
        agregarEntidad(muroDerecho, 101);

        System.out.println("✅ Muros límite creados");

        // Crear jugadores
        super.jugador1 = new Jugador(mundo, "Jugador1", 100, 85, 1);
        super.jugador2 = new Jugador(mundo, "Jugador2", 120, 85, 2);

        super.jugador1.setHiloServidor(hiloServidor);
        super.jugador2.setHiloServidor(hiloServidor);

        // Aplicar mejoras
        if (mejorasJ1 != null) {
            copiarMejoras(mejorasJ1, super.jugador1.getMejoras());
            super.jugador1.actualizarVidaConMejoras();
        } else {
            copiarMejoras(NivelBase.getMejorasJugador1(), super.jugador1.getMejoras());
            super.jugador1.actualizarVidaConMejoras();
        }

        if (mejorasJ2 != null) {
            copiarMejoras(mejorasJ2, super.jugador2.getMejoras());
            super.jugador2.actualizarVidaConMejoras();
        } else {
            copiarMejoras(NivelBase.getMejorasJugador2(), super.jugador2.getMejoras());
            super.jugador2.actualizarVidaConMejoras();
        }

        // ✅ Crear entidades CON IDs FIJOS (deben coincidir con cliente)
        int idPiso = 1;
        int idPlat2 = 2;
        int idPlat3 = 3;
        int idPalanca = 4;
        int idPlatMovil = 5;
        int idPuerta = 6;
        int idLlave = 7;
        int idEnemigo = 8;

        Plataforma piso = new Plataforma(mundo, 0, 10, 1000, 50, idPiso);
        Plataforma plataforma2 = new Plataforma(mundo, 495, 200, 75, 20, idPlat2);
        Plataforma plataforma3 = new Plataforma(mundo, 80, 250, 95, 20, idPlat3);

        Palanca palanca = new Palanca(mundo, 170, 120, idPalanca);
        PlataformaMovil plataformaMovil = new PlataformaMovil(mundo, 200, 130, 2, 200, palanca, idPlatMovil);

        PuertaLlegada puerta = new PuertaLlegada(mundo, 590, 50, idPuerta);
        LlaveActivadora llave = new LlaveActivadora(mundo, 532, 210, puerta, idLlave);

        Enemigo enemigo = new Enemigo(mundo, 400, 150, this, idEnemigo);


        palanca.setHiloServidor(hiloServidor);
        plataformaMovil.setHiloServidor(hiloServidor);
        puerta.setHiloServidor(hiloServidor);
        llave.setHiloServidor(hiloServidor);
        enemigo.setHiloServidor(hiloServidor);

        // ✅ Agregar al mapa de entidades (no al escenario en servidor headless)
        agregarEntidad(super.jugador1, super.jugador1.getIdJugador());
        agregarEntidad(super.jugador2, super.jugador2.getIdJugador());
        agregarEntidad(piso, idPiso);
        agregarEntidad(plataforma2, idPlat2);
        agregarEntidad(plataforma3, idPlat3);
        agregarEntidad(palanca, idPalanca);
        agregarEntidad(plataformaMovil, idPlatMovil);
        agregarEntidad(puerta, idPuerta);
        agregarEntidad(llave, idLlave);
        agregarEntidad(enemigo, idEnemigo);

        System.out.println("✅ Nivel1 inicializado con " + entidades.size() + " entidades");
    }

    private void copiarMejoras(MejoraTemporal origen, MejoraTemporal destino) {
        destino.reset();

        for (int i = 0; i < origen.getMejorasVida(); i++) {
            destino.mejorarVida();
        }

        for (int i = 0; i < origen.getMejorasVelocidad(); i++) {
            destino.mejorarVelocidad();
        }

        for (int i = 0; i < origen.getMejorasSalto(); i++) {
            destino.mejorarSalto();
        }

        for (int i = 0; i < origen.getMejorasDaño(); i++) {
            destino.mejorarDaño();
        }
    }

    @Override
    public void jugadorAtacaEnemigo(int idJugador, int idEnemigo) {

    }

    @Override
    public void recogerItem(int idLlave, int idJugador) {

    }

    @Override
    public void jugadorGolpeaJugador(int idJugador, int idVictima) {

    }
}
