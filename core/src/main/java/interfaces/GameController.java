package interfaces;

import java.util.ArrayList;

public interface GameController {
    void moverJugador(int idJugador, boolean derecha);
    void saltar(int idJugador);
    void atacar(int idJugador);
    void detener(int idJugador);
    void empezar();

    String obtenerEstadoJugador(int idJugador);
    ArrayList<String> obtenerEstadosEnemigos();

    // NUEVOS MÉTODOS PARA SINCRONIZACIÓN
    void procesarMejora(int idJugador, String tipoMejora);
    void sincronizarActivacionPalanca(int idPalanca);

    void jugadorAtacaEnemigo(int idJugador, int idEnemigo);

    void recogerItem(int idLlave, int idJugador);

    void jugadorGolpeaJugador(int idJugador, int idVictima);
}
