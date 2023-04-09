package com.osamaalek.sharemyapps;

import android.content.Context;
import android.net.wifi.WifiManager;

import java.lang.reflect.InvocationTargetException;

public class WifiAPUtil {

    public static boolean isWifiAPEnable(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        try {
            final int apState = (Integer) wifiManager.getClass().getMethod("getWifiApState").invoke(wifiManager);
            return apState == 13;
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
            return false;
        }
    }
}
