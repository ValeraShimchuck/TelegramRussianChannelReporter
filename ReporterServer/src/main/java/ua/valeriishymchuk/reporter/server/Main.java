package ua.valeriishymchuk.reporter.server;

import ua.valeriishymchuk.reporter.network.Packet;
import ua.valeriishymchuk.reporter.network.in.AdminAddChannelPacket;
import ua.valeriishymchuk.reporter.network.in.AdminBannedChannelPacket;
import ua.valeriishymchuk.reporter.network.in.AdminLoginPacket;
import ua.valeriishymchuk.reporter.network.in.ClientRequestPacket;
import ua.valeriishymchuk.reporter.network.out.*;
import ua.valeriishymchuk.reporter.server.admin.AccessInfo;
import ua.valeriishymchuk.reporter.server.admin.AccountInfo;
import ua.valeriishymchuk.reporter.server.channels.ChannelsInfoContainer;
import ua.valeriishymchuk.reporter.network.model.ConnectionInfo;
import ua.valeriishymchuk.reporter.server.thread.PacketHandlerManager;
import ua.valeriishymchuk.reporter.server.thread.SocketThread;
import ua.valeriishymchuk.utils.TimerTaskUtils;

import java.util.*;
import java.util.function.BiFunction;

public class Main {

    private static ChannelsInfoContainer channelsInfoContainer = new ChannelsInfoContainer();
    private static PacketHandlerManager packetHandlerManager;
    private static SocketThread socketThread;
    private static String version;
    private static List<AccountInfo> accountInfoList = new ArrayList<>();


    private static final Properties properties = new Properties();
    private static long keyAliveTime;
    private static int maxLogAttempts;
    private static long clearAttemptsDelay;
    private static final Map<ConnectionInfo, Integer> logAttempts = new HashMap<>();

    public static void main(String[] args) throws Exception {
        String host = args[0];
        int port = Integer.parseInt(args[1]);
        properties.load(Main.class.getClassLoader().getResourceAsStream("project.properties"));
        version = properties.getProperty("version");
        keyAliveTime = Long.parseLong(properties.getProperty("key_alive_time"));
        maxLogAttempts = Integer.parseInt(properties.getProperty("log_attempts"));
        clearAttemptsDelay = Long.parseLong(properties.getProperty("log_attempts_clear_delay"));
        packetHandlerManager = new PacketHandlerManager();
        registerPacketHandlers();
        socketThread = new SocketThread(packetHandlerManager, host, port);
        socketThread.start();
        System.out.println("start server: " + version);
        runAutoLogAttemptsClear();
        // temporary
        accountInfoList.add(new AccountInfo("admin", "admin"));
    }

    private static void registerPacketHandlers() {
        registerHandler(ClientRequestPacket.class, (packet, senderInfo) -> {
            ClientRequestPacket.Request requestType = packet.getRequest();
            if(requestType == ClientRequestPacket.Request.SERVER_INFO) return new ServerInfoPacket(version);
            if(requestType == ClientRequestPacket.Request.REPORT_DATA)
                return new ReportDataPacket(channelsInfoContainer.getToReport(), channelsInfoContainer.getBanned());
            return new ErrorPacket(ErrorPacket.Errors.SERVER_UNHANDLED_PACKET);
        });
        registerHandler(AdminLoginPacket.class, (packet, senderInfo) -> {
            AccountInfo account = getAccount(packet.getName());
            int attempt = logAttempts.getOrDefault(senderInfo, 0) + 1;
            if(maxLogAttempts > 3) return new ErrorPacket(ErrorPacket.Errors.TO_MANY_LOG_ATTEMPTS);
            if(account == null || !account.validate(packet.getPassword())) {
                if(logAttempts.containsKey(senderInfo)) logAttempts.put(senderInfo, attempt);
                else logAttempts.replace(senderInfo, attempt);
                return new ErrorPacket(ErrorPacket.Errors.ADMIN_LOG_FAILED);
            }
            return new AdminLogSuccessful(account.activateAccessor(senderInfo.getInetAddress().getHostAddress(),
                    senderInfo.getPort(),
                    System.currentTimeMillis() + keyAliveTime)
                    .getTempAdminKey());
        });
        registerHandler(AdminAddChannelPacket.class, (packet, senderInfo) -> {
            AccountInfo accountInfo = getAccount(packet.getAdminTempKey());
            if(accountInfo == null) return new ErrorPacket(ErrorPacket.Errors.ADMIN_KEY_INVALID);
            AccessInfo accessInfo = accountInfo.getAccessInfoByKey(packet.getAdminTempKey());
            if(!accessInfo.validate(packet.getAdminTempKey(), senderInfo)) return new ErrorPacket(ErrorPacket.Errors.ADMIN_KEY_INVALID);
            channelsInfoContainer.addToReport(packet.getChannel());
            return new SuccessfulPacket();
        });
        registerHandler(AdminBannedChannelPacket.class, (packet, senderInfo) -> {
            AccountInfo accountInfo = getAccount(packet.getAdminTempKey());
            if(accountInfo == null) return new ErrorPacket(ErrorPacket.Errors.ADMIN_KEY_INVALID);
            AccessInfo accessInfo = accountInfo.getAccessInfoByKey(packet.getAdminTempKey());
            if(!accessInfo.validate(packet.getAdminTempKey(), senderInfo)) return new ErrorPacket(ErrorPacket.Errors.ADMIN_KEY_INVALID);
            channelsInfoContainer.addToBannedFromReport(packet.getChannelId());
            return new SuccessfulPacket();
        });
    }

    private static void runAutoLogAttemptsClear() {
        new Timer(true).schedule(TimerTaskUtils.task(logAttempts::clear), clearAttemptsDelay);
    }


    private static <I extends Packet> void registerHandler(Class<I> iClass, BiFunction<I, ConnectionInfo, ? extends Packet> function) {
        packetHandlerManager.addHandler(new ua.valeriishymchuk.reporter.server.packet.PacketHandler(iClass, function));
    }

    private static AccountInfo getAccount(String login) {
        return accountInfoList.stream()
                .filter(accountInfo -> accountInfo.getLogin().equals(login))
                .findFirst().orElse(null);
    }

    private static AccountInfo getAccount(long tempKey) {
        return accountInfoList.stream().filter(accountInfo -> accountInfo.containsTempKey(tempKey)).findFirst().orElse(null);
    }



}
