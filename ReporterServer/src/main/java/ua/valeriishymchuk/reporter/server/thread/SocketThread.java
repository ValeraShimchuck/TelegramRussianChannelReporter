package ua.valeriishymchuk.reporter.server.thread;

import ua.valeriishymchuk.common.Pair;
import ua.valeriishymchuk.reporter.network.Packet;
import ua.valeriishymchuk.reporter.network.out.ErrorPacket;
import ua.valeriishymchuk.reporter.network.model.ConnectionInfo;
import ua.valeriishymchuk.utils.ObjectByteSerializer;
import ua.valeriishymchuk.utils.stream.StreamUtils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

public class SocketThread extends Thread{

    private final long packetSendDelay;
    private final PacketHandlerManager handler;
    private final DatagramSocket serverSocket;
    private final Map<ConnectionInfo, Pair<Long, Boolean>> senderPacketDelayCheck = new HashMap<>();
    private boolean running = true;

    public SocketThread(long packetSendDelay, PacketHandlerManager handler, DatagramSocket serverSocket) {
        this.packetSendDelay = packetSendDelay;
        this.handler = handler;
        this.serverSocket = serverSocket;
    }

    public SocketThread(PacketHandlerManager handler, String host, int port) throws Exception {
        this(100, handler, new DatagramSocket(port, InetAddress.getByName(host)));
    }

    @Override
    public void run() {
        byte[] receiveData = new byte[1024];
        while (true) {
            try {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);
                Object receivedObject = ObjectByteSerializer.fromArray(receiveData);
                InetAddress IPAddressReceiver = receivePacket.getAddress();
                ConnectionInfo info = new ConnectionInfo(IPAddressReceiver, receivePacket.getPort());
                if(!senderPacketDelayCheck.containsKey(info)) senderPacketDelayCheck.put(info, Pair.of(System.currentTimeMillis(), false));
                else {
                    long lastPacketSend = senderPacketDelayCheck.get(info).getFirst();
                    if(System.currentTimeMillis() - lastPacketSend <= packetSendDelay) {
                        if(senderPacketDelayCheck.get(info).getSecond()) continue;
                        else {
                            senderPacketDelayCheck.replace(info, Pair.of(System.currentTimeMillis(), true));
                            sendPacket(new ErrorPacket(ErrorPacket.Errors.TO_MANY_PACKETS), info);
                        }
                    } else senderPacketDelayCheck.replace(info, Pair.of(System.currentTimeMillis(), false));
                }
                if(receivedObject instanceof Packet packet) {
                    handler.processPacket(packet, info)
                            .exceptionally(throwable -> null)
                            .thenAccept(StreamUtils.checked(toSend -> {
                                sendPacket(toSend == null? new ErrorPacket(ErrorPacket.Errors.SERVER_UNHANDLED_PACKET) : toSend, info);
                            }));
                } else sendPacket(new ErrorPacket(ErrorPacket.Errors.UNKNOWN_PACKET_OBJECT), info);
                if(!running) break;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void sendPacket(byte[] sendData, ConnectionInfo info) throws IOException {
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, info.getInetAddress(), info.getPort());
        serverSocket.send(sendPacket);
    }

    private void sendPacket(Packet packet, ConnectionInfo info) throws IOException {
        sendPacket(packet.serialize(), info);
    }

    public void stopSocketReceive() {
        running = false;
    }
}
