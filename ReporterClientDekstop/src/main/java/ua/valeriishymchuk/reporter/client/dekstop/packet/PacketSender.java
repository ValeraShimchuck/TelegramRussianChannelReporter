package ua.valeriishymchuk.reporter.client.dekstop.packet;

import ua.valeriishymchuk.common.ThreadRunner;
import ua.valeriishymchuk.reporter.network.Packet;
import ua.valeriishymchuk.reporter.network.model.ConnectionInfo;
import ua.valeriishymchuk.utils.ObjectByteSerializer;
import ua.valeriishymchuk.utils.TimerTaskUtils;
import ua.valeriishymchuk.utils.stream.model.CheckedConsumer;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Timer;
import java.util.function.Consumer;

public class PacketSender<T extends Packet> {

    private final T packet;
    private final CheckedConsumer<Packet> handler;
    private final long timeOut;

    private volatile ThreadRunner receiveChecker;

    private PacketSender(T packet, CheckedConsumer<Packet> handler, long timeOut) {
        this.packet = packet;
        this.handler = handler;
        this.timeOut = timeOut;
    }

    public void sendPacket(ConnectionInfo info) throws Throwable {
        DatagramSocket clientSocket = new DatagramSocket();
        byte[] sendData;
        byte[] receiveData = new byte[1024];
        sendData = packet.serialize();
        try {
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, info.getInetAddress(), info.getPort());
            clientSocket.send(sendPacket);
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            (receiveChecker = ThreadRunner.of(() -> {
                clientSocket.receive(receivePacket);
                Object fromReceive = ObjectByteSerializer.fromArray(receivePacket.getData());
                if(!clientSocket.isClosed())
                    clientSocket.close();
                if(fromReceive instanceof Packet packet) {
                    handler.accept(packet);
                } else {
                    throw new RuntimeException("unknown object: " + fromReceive.getClass().getName());
                }
            })).run();
            new Timer().schedule(TimerTaskUtils.task(() -> {
                receiveChecker.stop();
                if(!clientSocket.isClosed())
                    clientSocket.close();
            }), timeOut);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static <T extends Packet> PacketSender<T> of( T packet, CheckedConsumer<Packet> handler, long timeOut) {
        return new PacketSender<T>(packet, handler, timeOut);
    }

    public static <T extends Packet> PacketSender<T> of(T packet, CheckedConsumer<Packet> handler) {
        return PacketSender.of(packet, handler, 5000L);
    }

    public static <T extends Packet> PacketSender<T> of(T packet) {
        return PacketSender.of(packet, p -> {});
    }


}
