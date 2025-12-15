package Red;

import com.badlogic.gdx.Game;

public class ServidorManager {

    private static HiloServidor instanciaServidor = null;
    private static boolean servidorIniciado = false;

    /**
     * Obtiene o crea la única instancia del servidor
     */
    public static HiloServidor obtenerServidor(Game juego) {
        if (instanciaServidor == null) {
            System.out.println("🔧 Creando nueva instancia de HiloServidor");
            instanciaServidor = new HiloServidor(juego);
            instanciaServidor.start();
            servidorIniciado = true;
        } else {
            System.out.println("✅ Reutilizando instancia existente de HiloServidor");
        }
        return instanciaServidor;
    }

    /**
     * Reinicia el servidor (para volver a jugar)
     */
    public static void reiniciarServidor(Game juego) {
        if (instanciaServidor != null) {
            System.out.println("🔄 Reiniciando servidor...");
            // No terminamos el servidor, solo reseteamos su estado
            instanciaServidor.desconectarClientes();
        }
    }

    /**
     * Termina completamente el servidor (solo al cerrar la aplicación)
     */
    public static void terminarServidor() {
        if (instanciaServidor != null) {
            System.out.println("🛑 Terminando servidor completamente");
            instanciaServidor.terminar();
            instanciaServidor = null;
            servidorIniciado = false;
        }
    }

    public static boolean estaIniciado() {
        return servidorIniciado && instanciaServidor != null;
    }
}
