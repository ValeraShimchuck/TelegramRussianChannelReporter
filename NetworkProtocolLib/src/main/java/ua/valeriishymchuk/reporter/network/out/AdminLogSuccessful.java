package ua.valeriishymchuk.reporter.network.out;

import ua.valeriishymchuk.reporter.network.Packet;

public class AdminLogSuccessful extends Packet {

    private final long adminTempKey;

    public AdminLogSuccessful(long adminTempKey) {
        this.adminTempKey = adminTempKey;
    }

    public long getAdminTempKey() {
        return adminTempKey;
    }
}
