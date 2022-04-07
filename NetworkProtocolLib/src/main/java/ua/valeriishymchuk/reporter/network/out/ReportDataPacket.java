package ua.valeriishymchuk.reporter.network.out;

import ua.valeriishymchuk.reporter.channels.ChannelReportInfo;
import ua.valeriishymchuk.reporter.network.Packet;

import java.util.Collection;

public class ReportDataPacket extends Packet {

    private final Collection<ChannelReportInfo> toReport;
    private final Collection<Long> banned;

    public ReportDataPacket(Collection<ChannelReportInfo> toReport, Collection<Long> banned) {
        this.toReport = toReport;
        this.banned = banned;
    }

    public Collection<ChannelReportInfo> getToReport() {
        return toReport;
    }

    public Collection<Long> getBanned() {
        return banned;
    }
}
