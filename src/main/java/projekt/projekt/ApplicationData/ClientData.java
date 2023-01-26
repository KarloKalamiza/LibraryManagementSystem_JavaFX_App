package projekt.projekt.ApplicationData;

import java.io.Serializable;

public class ClientData implements Serializable {
    private Integer port;
    private String ipAddress;
    private String username;
    private String password;

    public ClientData(Integer port, String ipAddress, String username, String password) {
        this.port = port;
        this.ipAddress = ipAddress;
        this.username = username;
        this.password = password;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
