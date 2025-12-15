package Red;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class ClientePrueba {

    private DatagramSocket socket;
    private InetAddress ipServidor;
    private int puertoServidor = 5555;
    private final int tamañoMensaje = 1024;
    private boolean conectado = false;
    private int numeroJugador = -1;

    public ClientePrueba() {
        try {
            // 1. Inicializa el socket del cliente (se asigna un puerto local aleatorio)
            socket = new DatagramSocket();
            // 2. Define la dirección del servidor (asume que está en la misma máquina)
            ipServidor = InetAddress.getByName("localhost"); 
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }
    }

    /**
     * Envía un mensaje de texto al servidor.
     */
    public void enviarMensaje(String mensaje) {
        if (socket == null || ipServidor == null) return;

        byte[] buffer = mensaje.getBytes();
        DatagramPacket paquete = new DatagramPacket(buffer, buffer.length, ipServidor, puertoServidor);
        try {
            socket.send(paquete);
            System.out.println("-> Enviado: " + mensaje);
        } catch (IOException e) {
            System.err.println("Error al enviar el paquete: " + e.getMessage());
        }
    }

    /**
     * Espera y recibe una respuesta del servidor.
     */
    public String recibirMensaje() {
        if (socket == null) return null;

        byte[] buffer = new byte[tamañoMensaje];
        DatagramPacket paquete = new DatagramPacket(buffer, buffer.length);
        try {
            // El socket se bloquea aquí hasta que recibe un paquete
            socket.receive(paquete); 
            String mensajeRecibido = (new String(paquete.getData(), 0, paquete.getLength())).trim();
            System.out.println("<- Recibido: " + mensajeRecibido);
            return mensajeRecibido;
        } catch (IOException e) {
            if (!socket.isClosed()) {
                System.err.println("Error al recibir el paquete: " + e.getMessage());
            }
            return null;
        }
    }

    /**
     * Procesa la respuesta del servidor (especialmente el mensaje 'Connected').
     */
    public void procesarRespuestaServidor(String mensaje) {
        if (mensaje == null) return;

        String[] partes = mensaje.split(":");
        String comando = partes[0];

        switch (comando) {
            case "Conectado":
                // Ejemplo: "Connected:1"
                this.numeroJugador = Integer.parseInt(partes[1]);
                this.conectado = true;
                System.out.println("Conectado, tu número de jugador es: " + numeroJugador);
                break;
            case "YaConectado":
                System.out.println("Ya estás conectado.");
                break;
            case "Lleno":
                System.out.println("El servidor está lleno");
                break;
            case "Empezar":
                System.out.println("Arrancó el juego");
                break;
            default:
                // Manejar otros mensajes como "Move" o "Disconnect"
                break;
        }
    }
    
    public void close() {
        if (socket != null && !socket.isClosed()) {
            socket.close();
            System.out.println("Socket de cliente cerrado.");
        }
    }
    
    public boolean getConnected() {
    	return this.conectado;
    }
}