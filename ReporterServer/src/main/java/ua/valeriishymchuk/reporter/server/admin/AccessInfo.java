package ua.valeriishymchuk.reporter.server.admin;

import ua.valeriishymchuk.reporter.network.model.ConnectionInfo;

import java.util.Random;

public class AccessInfo {

    private boolean valid = false;
    private long tempAdminKey = 0;
    private long keyEndOfTermTime = 0;
    private final String ipAddress;

    public AccessInfo(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public AccessInfo(String ip, int port) {
        this(ip + ":" + port);
    }

    public long generateNewKey(long keyEndOfTermTime) {
        long key = new Random().nextLong();
        this.keyEndOfTermTime = keyEndOfTermTime;
        valid = true;
        this.tempAdminKey = key;
        return key;
    }

    public long getTempAdminKey() {
        return tempAdminKey;
    }

    public boolean validate(long key, String ipAddress) {
        return isValid() && key == tempAdminKey && ipAddress.equals(this.ipAddress);
    }

    public boolean validate(long key, ConnectionInfo connectionInfo) {
        return validate(key, connectionInfo.getInetAddress().getHostAddress() + ":" + connectionInfo.getPort());
    }

    public boolean isValid() {
        return valid && System.currentTimeMillis() < keyEndOfTermTime;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getIpAddress() {
        return ipAddress;
    }
}
