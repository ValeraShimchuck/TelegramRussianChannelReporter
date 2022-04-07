package ua.valeriishymchuk.reporter.network.out;

import ua.valeriishymchuk.reporter.network.Packet;
import ua.valeriishymchuk.utils.ObjectByteSerializer;

public class ErrorPacket extends Packet {

    private final Errors error;

    public ErrorPacket(Errors error) {
        this.error = error;
    }

    public Errors getError() {
        return error;
    }

    public enum Errors {
        SERVER_UNHANDLED_PACKET,
        UNKNOWN_PACKET_OBJECT,
        ADMIN_LOG_FAILED,
        ADMIN_KEY_INVALID,
        TO_MANY_LOG_ATTEMPTS,
        TO_MANY_PACKETS
    }

}
