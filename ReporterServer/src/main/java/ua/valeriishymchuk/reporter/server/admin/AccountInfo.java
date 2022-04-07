package ua.valeriishymchuk.reporter.server.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AccountInfo {

    private final List<AccessInfo> accessors = new ArrayList<>();

    private final String login;
    private String password;


    public AccountInfo(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<AccessInfo> getAccessors() {
        return accessors;
    }

    public boolean validate(String password) {
        return password.equals(this.password);
    }

    public AccessInfo activateAccessor(String host, int port, long workTime) {
        String ipAddress = host + ":" + port;
        AccessInfo accessInfo = accessors.stream()
                .filter(adminAccessInfo -> adminAccessInfo.getIpAddress().equals(ipAddress))
                .findFirst().orElse(null);
        if(accessInfo == null) {
            accessInfo = new AccessInfo(host, port);
            accessors.add(accessInfo);
        }
        accessInfo.generateNewKey(workTime);
        return accessInfo;
    }


    public boolean containsTempKey(long key) {
        return accessors.stream().anyMatch(accessInfo -> accessInfo.getTempAdminKey() == key);
    }

    public AccessInfo getAccessInfoByKey(long key) {
        return accessors.stream().filter(accessInfo -> accessInfo.getTempAdminKey() == key).findFirst().orElse(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountInfo that = (AccountInfo) o;
        return Objects.equals(login, that.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login);
    }
}
