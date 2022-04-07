package ua.valeriishymchuk.reporter.network.in;

import ua.valeriishymchuk.reporter.network.Packet;

public class AdminLoginPacket extends Packet {

    private final String name;
    private final String password;

    public AdminLoginPacket(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
