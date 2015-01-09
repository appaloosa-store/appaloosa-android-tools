package appaloosa_store.com.appaloosa_android_tools.utils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

import java.security.MessageDigest;
import java.util.UUID;

import appaloosa_store.com.appaloosa_android_tools.AppaloosaTools;

public class DeviceUtils{

    public static String getDeviceID() {
        final String imei = getImei();

        if(imei != null)
            return imei;
        else
            return getWifiMacAddress();
    }

    public static String getWifiMacAddress() {
        WifiManager manager = (WifiManager) AppaloosaTools.getInstance().activity.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = manager.getConnectionInfo();
        if(wifiInfo == null || wifiInfo.getMacAddress() == null) {
            return getRandomUUID();
        } else {
            return wifiInfo.getMacAddress().replace(":", "").replace(".", "");
        }
    }

    private static String getRandomUUID() {
        try {
            return md5(UUID.randomUUID().toString());
        } catch (Exception e) {
            return "unknown";
        }
    }

    public static String getImei() {
        TelephonyManager telephonyManager = (TelephonyManager) AppaloosaTools.getInstance().activity.getSystemService(Context.TELEPHONY_SERVICE);
        String imei = null;
        if(telephonyManager != null) {
            imei = telephonyManager.getDeviceId();
        }
        return imei;
    }

    public static String md5(String s) throws Exception {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");

        messageDigest.update(s.getBytes());
        byte digest[] = messageDigest.digest();
        StringBuffer result = new StringBuffer();

        for(int i = 0; i < digest.length; i++) {
            result.append(Integer.toHexString(0xFF & digest[i]));
        }
        return result.toString();
    }
}