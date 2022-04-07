package ua.valeriishymchuk.reporter.network.in;

import ua.valeriishymchuk.reporter.channels.ChannelReportInfo;
import ua.valeriishymchuk.reporter.network.Packet;

public class AdminAddChannelPacket extends Packet {

    private final ChannelReportInfo channel;
    private final long adminTempKey;

    public AdminAddChannelPacket(ChannelReportInfo channel, long adminTempKey) {
        this.channel = channel;
        this.adminTempKey = adminTempKey;
    }

    public ChannelReportInfo getChannel() {
        return channel;
    }

    public long getAdminTempKey() {
        return adminTempKey;
    }
}
