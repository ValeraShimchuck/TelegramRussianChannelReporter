package ua.valeriishymchuk.reporter.server.channels;

import ua.valeriishymchuk.reporter.channels.ChannelReportInfo;

import java.util.ArrayList;
import java.util.List;

public class ChannelsInfoContainer {

    private final List<ChannelReportInfo> toReport;
    private final List<Long> banned;


    public ChannelsInfoContainer(List<ChannelReportInfo> toReport, List<Long> banned) {
        this.toReport = toReport;
        this.banned = banned;
    }

    public ChannelsInfoContainer() {
        this(new ArrayList<>(), new ArrayList<>());
    }

    public void addToReport(ChannelReportInfo reportInfo) {
        toReport.remove(reportInfo);
        toReport.add(reportInfo);
    }

    public void addToBannedFromReport(long chatId) {
        toReport.removeIf(channelReportInfo -> channelReportInfo.getChannelId() == chatId);
        banned.add(chatId);
    }

    public List<Long> getBanned() {
        return banned;
    }

    public List<ChannelReportInfo> getToReport() {
        return toReport;
    }
}
