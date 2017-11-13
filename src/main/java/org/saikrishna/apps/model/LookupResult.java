package org.saikrishna.apps.model;

public class LookupResult {

    private String hostName;
    private String ipAddress;


    public LookupResult() {
    }

    public LookupResult(String hostName, String ipAddress) {
        this.hostName = hostName;
        this.ipAddress = ipAddress;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    @Override
    public String toString() {
        return "LookupResult{" +
                "hostName='" + hostName + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                '}';
    }
}
