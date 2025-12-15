package Red;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

import interfaces.GameController;
import interfaces.Menu;
import niveles.Nivel1;

public class HiloServidor extends Thread {

    private DatagramSocket socket;
    private int puertoServidor = 5555;
    private boolean fin = false;
    private final int MAX_CLIENTES = 2;
    private int clientesConectados = 0;
    private int clientesFinalizadoHistoria = 0;
    private ArrayList<Cliente> clientes = new ArrayList<Cliente>();
    private GameController gameController;
    private Game juego;

    private boolean juegoIniciado = false;

    // ✅ NUEVO: Control de reinicio coordinado
    private int jugadoresListosParaReiniciar = 0;
    private boolean esperandoReinicio = false;

    // Tracking de estados para evitar envíos duplicados
    private Map<Integer, String> ultimosEstadosJugadores = new HashMap<>();
    private Map<Integer, String> ultimosEstadosEnemigos = new HashMap<>();

    public HiloServidor(Game juego) {
        this.juego = juego;

        try {
            socket = new DatagramSocket(puertoServidor);
            System.out.println("✅ Servidor iniciado en puerto " + puertoServidor);
        } catch (SocketException e) {
            System.err.println("❌ Error al iniciar servidor: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        System.out.println("🔄 HiloServidor corriendo...");

        do {
            DatagramPacket paquete = new DatagramPacket(new byte[1024], 1024);
            try {
                socket.setSoTimeout(100);
                socket.receive(paquete);
                procesarMensaje(paquete);
            } catch (java.net.SocketTimeoutException e) {
                // Normal - sin mensajes en este ciclo
            } catch (IOException e) {
                if (!socket.isClosed()) {
                    System.err.println("❌ Error al recibir paquete: " + e.getMessage());
                }
            }
        } while(!fin);
    }

    public void reiniciarEstado() {
        System.out.println("🔄 Reiniciando estado del servidor");
        this.clientesConectados = 0;
        this.clientesFinalizadoHistoria = 0;
        this.juegoIniciado = false;
        this.clientes.clear();
        this.gameController = null;
        this.ultimosEstadosJugadores.clear();
        this.ultimosEstadosEnemigos.clear();

        // ✅ Resetear estado de reinicio
        this.jugadoresListosParaReiniciar = 0;
        this.esperandoReinicio = false;

        System.out.println("✅ Estado del servidor reiniciado");
    }

    private void procesarMensaje(DatagramPacket paquete) {
        String mensaje = (new String(paquete.getData())).trim();
        String[] partes = mensaje.split(":");
        int indice = encontrarIndiceCliente(paquete);

        System.out.println("📩 [SERVIDOR] Mensaje: " + mensaje);

        // DETECTAR DESCONEXIÓN
        if(partes[0].equals("Desconectar") || partes[0].equals("VolverAlMenu")){
            if(indice != -1) {
                Cliente cliente = clientes.get(indice);
                System.out.println("👋 Cliente " + cliente.getIdCliente() + " desconectado");

                clientes.remove(indice);
                clientesConectados--;

                System.out.println("📊 Clientes: " + clientesConectados + "/" + MAX_CLIENTES);

                if(clientesConectados == 0) {
                    System.out.println("🔄 Todos los clientes desconectados - Reiniciando servidor");

                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            reiniciarEstado();
                            juego.setScreen(new Menu(juego));
                            System.out.println("✅ Servidor en menú");
                        }
                    });
                }
            }
            return;
        }

        // ✅ NUEVO: Manejar solicitud de reinicio
        if(partes[0].equals("SolicitarReinicio")) {
            jugadoresListosParaReiniciar++;
            System.out.println("🔄 Jugador listo para reiniciar (" + jugadoresListosParaReiniciar + "/" + MAX_CLIENTES + ")");

            if (jugadoresListosParaReiniciar >= MAX_CLIENTES) {
                System.out.println("✅ Todos los jugadores listos - Reiniciando nivel");

                // Resetear contador
                jugadoresListosParaReiniciar = 0;
                esperandoReinicio = false;

                // Notificar a todos los clientes
                enviarMensajeATodos("ReiniciarNivel");

                // Reiniciar nivel en el servidor
                final HiloServidor servidorRef = this;
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Nivel1 nivel = new Nivel1(juego, servidorRef);
                            juego.setScreen(nivel);
                            System.out.println("✅ Nivel reiniciado en servidor");
                        } catch (Exception e) {
                            System.err.println("❌ Error al reiniciar nivel: " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                });
            } else {
                esperandoReinicio = true;
            }
            return;
        }

        // Manejo de historia
        if(clientesConectados == MAX_CLIENTES && partes[0].equals("FinHistoria")){
            this.clientesFinalizadoHistoria++;
            System.out.println("📖 Historia: " + clientesFinalizadoHistoria + "/" + MAX_CLIENTES);
        }

        if(this.clientesFinalizadoHistoria == MAX_CLIENTES && !juegoIniciado) {
            System.out.println("🎮 Iniciando juego");
            enviarMensajeATodos("EmpezarJuego");

            clientesFinalizadoHistoria++;
            juegoIniciado = true;

            final HiloServidor servidorRef = this;

            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    try {
                        Nivel1 nivel = new Nivel1(juego, servidorRef);
                        juego.setScreen(nivel);
                        System.out.println("✅ Nivel creado (headless)");
                    } catch (Exception e) {
                        System.err.println("❌ Error: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            });
        }

        // Manejo de conexión
        if(partes[0].equals("Conectar")){
            if(indice != -1) {
                this.enviarMensaje("YaConectado", paquete.getAddress(), paquete.getPort());
                return;
            }

            if(clientesConectados < MAX_CLIENTES) {
                clientesConectados++;
                Cliente nuevoCliente = new Cliente(clientesConectados, paquete.getAddress(), paquete.getPort());
                clientes.add(nuevoCliente);
                enviarMensaje("Conectado:"+clientesConectados, paquete.getAddress(), paquete.getPort());

                System.out.println("✅ Cliente " + clientesConectados + " conectado");

                if(clientesConectados == MAX_CLIENTES) {
                    this.enviarMensajeATodos("EmpezarHistoria");
                }
            } else {
                enviarMensaje("Lleno", paquete.getAddress(), paquete.getPort());
            }
            return;
        }

        if(indice == -1){
            this.enviarMensaje("NoConectado", paquete.getAddress(), paquete.getPort());
            return;
        }

        // Procesar inputs - ENVIAR SOLO CAMBIOS
        if (gameController != null && partes.length >= 2) {
            try {
                switch(partes[0]){
                    case "Input":
                        if (partes.length >= 3) {
                            int idJugador = Integer.parseInt(partes[1]);
                            String accion = partes[2];

                            final int idFinal = idJugador;
                            final String accionFinal = accion;

                            Gdx.app.postRunnable(new Runnable() {
                                @Override
                                public void run() {
                                    if (gameController == null) return;

                                    switch(accionFinal) {
                                        case "MoverDerecha":
                                            gameController.moverJugador(idFinal, true);

                                            break;
                                        case "MoverIzquierda":
                                            gameController.moverJugador(idFinal, false);

                                            break;
                                        case "Saltar":
                                            gameController.saltar(idFinal);

                                            break;
                                        case "Atacar":
                                            gameController.atacar(idFinal);
                                            enviarMensajeATodos("Jugador:" + idFinal + ":Atacar");
                                            break;
                                        case "Detener":
                                            gameController.detener(idFinal);
                                            break;
                                    }
                                }
                            });
                        }
                        break;

                    case "Llave":
                        if (partes.length >= 4 && partes[2].equals("Recoger")) {
                            System.out.println("🔑 [SERVIDOR] Procesando recogida de llave");

                            int idLlave = Integer.parseInt(partes[1]);
                            int idJugador = Integer.parseInt(partes[3]);

                            System.out.println("🔑 [SERVIDOR] Llave: " + idLlave + ", Jugador: " + idJugador);

                            // ✅ Procesar en el servidor
                            Gdx.app.postRunnable(new Runnable() {
                                @Override
                                public void run() {
                                    if (gameController != null) {
                                        gameController.recogerItem(idLlave, idJugador);

                                        // ✅ Enviar a TODOS los clientes
                                        String mensaje = "Llave:" + idLlave + ":Recoger:" + idJugador;
                                        enviarMensajeATodos(mensaje);
                                        System.out.println("📤 [SERVIDOR] Mensaje enviado a todos: " + mensaje);
                                    }
                                }
                            });
                        }
                        break;

                    case "Jugador":
                        if (partes.length >= 4) {
                            int idJugador = Integer.parseInt(partes[1]);

                            // ✅ Jugador golpea a otro jugador
                            if (partes[2].equals("GolpearJugador")) {
                                int idVictima = Integer.parseInt(partes[3]);

                                System.out.println("⚔️ [SERVIDOR] J" + idJugador + " golpeó a J" + idVictima);

                                Gdx.app.postRunnable(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (gameController != null) {
                                            gameController.jugadorGolpeaJugador(idJugador, idVictima);
                                        }
                                    }
                                });
                            }
                            // ✅ Jugador golpea a enemigo
                            else if (partes[2].equals("Atacar") && partes.length >= 4) {
                                int idEnemigo = Integer.parseInt(partes[3]);

                                System.out.println("⚔️ [SERVIDOR] J" + idJugador + " atacó enemigo " + idEnemigo);

                                Gdx.app.postRunnable(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (gameController != null) {
                                            gameController.jugadorAtacaEnemigo(idJugador, idEnemigo);
                                        }
                                    }
                                });
                            }
                            else if (partes[2].equals("MejorarEstadistica") && partes.length >= 4) {
                                String tipoMejora = partes[3];
                                gameController.procesarMejora(idJugador, tipoMejora);
                                enviarMensajeATodos("Jugador:" + idJugador + ":MejoraAplicada:" + tipoMejora);
                            }
                        }
                        break;

                    case "Palanca":
                        if (partes.length >= 3 && partes[2].equals("Activar")) {
                            int idPalanca = Integer.parseInt(partes[1]);
                            gameController.sincronizarActivacionPalanca(idPalanca);
                            enviarMensajeATodos("Palanca:" + idPalanca + ":Activar");
                        }
                        break;
                }
            } catch (Exception e) {
                System.err.println("❌ Error: " + e.getMessage());
            }
        }
    }


    private int encontrarIndiceCliente(DatagramPacket paquete) {
        int i = 0;
        int indiceCliente = -1;
        while(i < clientes.size() && indiceCliente == -1) {
            Cliente cliente = clientes.get(i);
            String identificador = paquete.getAddress().toString()+":"+paquete.getPort();
            if(identificador.equals(cliente.getId())){
                indiceCliente = i;
            }
            i++;
        }
        return indiceCliente;
    }

    public void enviarMensaje(String mensaje, InetAddress ipCliente, int puertoCliente) {
        byte[] bytesMensaje = mensaje.getBytes();
        DatagramPacket paquete = new DatagramPacket(bytesMensaje, bytesMensaje.length, ipCliente, puertoCliente);
        try {
            socket.send(paquete);
        } catch (IOException e) {
            System.err.println("❌ Error envío: " + e.getMessage());
        }
    }

    public void terminar(){
        System.out.println("🛑 Terminando servidor");

        enviarMensajeATodos("Desconectar:ServidorCerrado");

        this.fin = true;
        this.juegoIniciado = false;

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (socket != null && !socket.isClosed()) {
            socket.close();
        }

        this.interrupt();
    }

    public void enviarMensajeATodos(String mensaje) {
        for (Cliente cliente : clientes) {
            enviarMensaje(mensaje, cliente.getIp(), cliente.getPuerto());
        }
    }

    public void desconectarClientes() {
        System.out.println("🔌 Desconectando clientes");
        for (Cliente cliente : clientes) {
            enviarMensaje("Desconectar", cliente.getIp(), cliente.getPuerto());
        }
        this.clientes.clear();
        this.clientesConectados = 0;
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
        System.out.println("✅ GameController asignado");
    }
}
