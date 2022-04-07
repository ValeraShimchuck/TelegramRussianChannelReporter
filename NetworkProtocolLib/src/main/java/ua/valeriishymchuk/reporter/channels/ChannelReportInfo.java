package ua.valeriishymchuk.reporter.channels;

import java.io.Serializable;
import java.util.Objects;

public class ChannelReportInfo implements Serializable {

    private final long channelId;
    private final String reportReason;

    public ChannelReportInfo(long channelId, String reportReason) {
        this.channelId = channelId;
        this.reportReason = reportReason;
    }

    public long getChannelId() {
        return channelId;
    }

    public String getReportReason() {
        return reportReason;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChannelReportInfo that = (ChannelReportInfo) o;
        return channelId == that.channelId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(channelId);
    }
}
