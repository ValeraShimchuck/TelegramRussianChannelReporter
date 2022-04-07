package ua.valeriishymchuk.reporter.network.model;

import java.net.InetAddress;
import java.util.Objects;

public class ConnectionInfo {

    private final InetAddress inetAddress;
    private final int port;

    public ConnectionInfo(InetAddress inetAddress, int port) {
        this.inetAddress = inetAddress;
        this.port = port;
    }

    public InetAddress getInetAddress() {
        return inetAddress;
    }

    public int getPort() {
        return port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConnectionInfo info = (ConnectionInfo) o;
        return port == info.port && Objects.equals(inetAddress, info.inetAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inetAddress, port);
    }
}
