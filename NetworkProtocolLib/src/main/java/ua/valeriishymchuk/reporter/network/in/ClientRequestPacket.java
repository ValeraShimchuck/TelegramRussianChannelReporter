package ua.valeriishymchuk.reporter.network.in;

import ua.valeriishymchuk.reporter.network.Packet;

public class ClientRequestPacket extends Packet {

    private final Request request;

    public ClientRequestPacket(Request request) {
        this.request = request;
    }

    public Request getRequest() {
        return request;
    }

    public enum Request {
        SERVER_INFO,
        REPORT_DATA
    }


}
