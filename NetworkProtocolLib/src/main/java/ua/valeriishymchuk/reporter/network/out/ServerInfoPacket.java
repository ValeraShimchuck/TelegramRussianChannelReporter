package ua.valeriishymchuk.reporter.network.out;

import ua.valeriishymchuk.reporter.network.Packet;

public class ServerInfoPacket extends Packet {

    private final String serverVersion;

    public ServerInfoPacket(String serverVersion) {
        this.serverVersion = serverVersion;
    }

    public String getServerVersion() {
        return serverVersion;
    }

}
