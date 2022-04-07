package ua.valeriishymchuk.reporter.server.packet;

import ua.valeriishymchuk.reporter.network.Packet;
import ua.valeriishymchuk.reporter.network.model.ConnectionInfo;

import java.util.concurrent.CompletableFuture;

public class PacketResult {

    private final Packet packet;
    private final CompletableFuture<Packet> result;
    private final ConnectionInfo connectionInfo;


    public PacketResult(Packet packet, ConnectionInfo connectionInfo) {
        this.packet = packet;
        this.connectionInfo = connectionInfo;
        result = new CompletableFuture<>();
        result.thenRun(() -> System.out.println("end"));

    }

    public CompletableFuture<Packet> getResult() {
        return result;
    }

    public void tryComplete(PacketHandler<? extends Packet> handler, ConnectionInfo info) {
        if(handler.isAssignable(packet.getClass())) {
            result.completeAsync(() -> handler.executeRaw(packet, info));
        }
    }

    public ConnectionInfo getSenderInfo() {
        return connectionInfo;
    }

    public void completeError() {
        result.completeExceptionally(new Exception());
    }

    public Class<? extends Packet> getPacketClass() {
        return packet.getClass();
    }

}
