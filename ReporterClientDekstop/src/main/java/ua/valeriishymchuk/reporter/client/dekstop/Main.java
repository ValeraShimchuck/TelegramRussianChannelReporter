package ua.valeriishymchuk.reporter.client.dekstop;


import ua.valeriishymchuk.reporter.client.dekstop.packet.PacketSender;
import ua.valeriishymchuk.reporter.network.Packet;
import ua.valeriishymchuk.reporter.network.in.ClientRequestPacket;
import ua.valeriishymchuk.reporter.network.model.ConnectionInfo;
import ua.valeriishymchuk.reporter.network.out.ErrorPacket;
import ua.valeriishymchuk.reporter.network.out.ServerInfoPacket;
import ua.valeriishymchuk.utils.ObjectByteSerializer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.ref.Reference;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Main {

    private static ConnectionInfo info;

    public static void main(String[] args) throws Throwable{
        int port = Integer.parseInt(args[1]);
        InetAddress serverIP = InetAddress.getByName(args[0]);
        info = new ConnectionInfo(serverIP, port);
        PacketSender.of(new ClientRequestPacket(ClientRequestPacket.Request.SERVER_INFO), packet -> {
            if(packet instanceof ErrorPacket errorPacket) System.out.println(errorPacket.getError());
            else if(packet instanceof ServerInfoPacket serverInfoPacket) {
                System.out.println("successful received:");
                System.out.println(serverInfoPacket.getServerVersion());
            } else {
                System.out.println("unhandled packet: " + packet.getClass().getName());
            }
        }).sendPacket(info);
    }
}
