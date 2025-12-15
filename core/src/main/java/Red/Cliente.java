package Red;

import java.net.InetAddress;

public class Cliente {
    private int idCliente;
    private InetAddress ip;
    private int puerto;
    private long ultimaActividad;

    public Cliente(int idCliente, InetAddress ip, int puerto) {
        this.idCliente = idCliente;
        this.ip = ip;
        this.puerto = puerto;
        this.ultimaActividad = System.currentTimeMillis();
    }

    public String getId() {
        return ip.toString() + ":" + puerto;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public InetAddress getIp() {
        return ip;
    }

    public int getPuerto() {
        return puerto;
    }

    public long getUltimaActividad() {
        return ultimaActividad;
    }

    public void actualizarActividad() {
        this.ultimaActividad = System.currentTimeMillis();
    }

    // Verificar si el cliente está inactivo (más de 10 segundos sin mensajes)
    public boolean estaInactivo() {
        return (System.currentTimeMillis() - ultimaActividad) > 10000;
    }
}
