package ua.valeriishymchuk.reporter.server.thread;

import ua.valeriishymchuk.reporter.network.Packet;
import ua.valeriishymchuk.reporter.server.packet.PacketHandler;
import ua.valeriishymchuk.reporter.server.packet.PacketResult;
import ua.valeriishymchuk.reporter.network.model.ConnectionInfo;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class PacketHandlerManager {

    private final ArrayDeque<PacketResult> resultQueue;
    private final List<PacketHandler<?>> handlers;


    public PacketHandlerManager() {
        this(new ArrayDeque<>());
    }

    public PacketHandlerManager(ArrayDeque<PacketResult> resultQueue) {
        this(resultQueue, new ArrayList<>());
    }

    public PacketHandlerManager(ArrayDeque<PacketResult> resultQueue, List<PacketHandler<?>> handlers) {
        this.resultQueue = resultQueue;
        this.handlers = handlers;
    }

    public <T extends Packet> CompletableFuture<Packet> processPacket(T packet, ConnectionInfo info) {
        PacketResult packetResult = new PacketResult(packet, info);
        PacketHandler<?> handler = getHandler(packetResult.getPacketClass());
        packetResult.tryComplete(handler, info);
        return packetResult.getResult();
    }

    public void addHandler(ua.valeriishymchuk.reporter.server.packet.PacketHandler<?> handler) {
        handlers.add(handler);
    }

    private PacketHandler<?> getHandler(Class<?> clazz) {
        return handlers.stream().filter(packetHandler -> packetHandler.isAssignable(clazz)).findFirst().orElse(null);
    }

}
