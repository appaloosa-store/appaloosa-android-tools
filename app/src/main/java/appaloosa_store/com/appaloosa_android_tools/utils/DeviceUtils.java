package appaloosa_store.com.appaloosa_android_tools.utils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

import java.security.MessageDigest;
import java.util.UUID;

import appaloosa_store.com.appaloosa_android_tools.AppaloosaTools;

public class DeviceUtils{

    public static String getDeviceId() {
        Context context = AppaloosaTools.context;
        String imei = getImei(context);
        if (imei != null) return imei;
        String tid = "unkown";
        try {
            tid = getWifiMacAddress(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tid;
    }

    public static String getWifiMacAddress(Context context) throws Exception {
        WifiManager manager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = manager.getConnectionInfo();
        if (wifiInfo == null || wifiInfo.getMacAddress() == null)
            return md5(UUID.randomUUID().toString());
        else return wifiInfo.getMacAddress().replace(":", "").replace(".", "");
    }

    public static String getImei(Context context) {
        TelephonyManager m = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String imei = m != null ? m.getDeviceId() : null;
        return imei;
    }

    public static String md5(String s) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");

        md.update(s.getBytes());

        byte digest[] = md.digest();
        StringBuffer result = new StringBuffer();

        for (int i = 0; i < digest.length; i++) {
            result.append(Integer.toHexString(0xFF & digest[i]));
        }
        return (result.toString());
    }
}