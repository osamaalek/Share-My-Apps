package com.osamaalek.sharemyapps;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class NetworkUtil {

    public static String getDeviceIpAddress() {
        String deviceIpAddress;
        try {
            for (Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces(); enumeration.hasMoreElements(); ) {
                NetworkInterface networkInterface = enumeration.nextElement();
                for (Enumeration<InetAddress> enumerationIpAddress = networkInterface.getInetAddresses(); enumerationIpAddress.hasMoreElements(); ) {
                    InetAddress inetAddress = enumerationIpAddress.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress.getAddress().length == 4) {
                        deviceIpAddress = inetAddress.getHostAddress() + ":" + Constants.PORT;
                        return deviceIpAddress;
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }
}
