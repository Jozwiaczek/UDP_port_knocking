package server;

import java.net.InetAddress;
import java.util.Objects;

public class ClientConnection {

    InetAddress IP;
    int port;

    public ClientConnection (InetAddress IP, int port) {
        this.IP = IP;
        this.port = port;
    }

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientConnection that = (ClientConnection) o;
        return port == that.port && IP.equals(that.IP);
    }

    @Override
    public int hashCode ( ) {
        return Objects.hash(IP, port);
    }
}
