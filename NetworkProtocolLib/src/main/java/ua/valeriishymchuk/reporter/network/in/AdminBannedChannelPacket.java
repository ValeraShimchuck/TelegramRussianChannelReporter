package ua.valeriishymchuk.reporter.network.in;

import ua.valeriishymchuk.reporter.channels.ChannelReportInfo;
import ua.valeriishymchuk.reporter.network.Packet;

public class AdminBannedChannelPacket extends Packet {

    private final long channelId;
    private final long adminTempKey;

    public AdminBannedChannelPacket(long channel, long adminTempKey) {
        this.channelId = channel;
        this.adminTempKey = adminTempKey;
    }

    public long getChannelId() {
        return channelId;
    }

    public long getAdminTempKey() {
        return adminTempKey;
    }
}
