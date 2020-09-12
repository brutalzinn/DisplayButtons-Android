package net.robertocpaes.displaybuttons.utils.networkscan;

/**
 * Created by NickAc on 30/12/2017.
 * This project is licensed with the MIT license.
 * Please see the project root to find the LICENSE file.
 */
public class NetworkDevice {
    private String ip, deviceName = "";

    public NetworkDevice(String ip, String deviceName) {
        this.ip = ip;
        this.deviceName = deviceName;
    }

    public NetworkDevice(String ip) {
        this.ip = ip;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
}
