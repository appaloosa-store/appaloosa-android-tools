package appaloosa_store.com.appaloosa_android_tools.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.view.Display;
import android.view.WindowManager;

import com.appaloosa_store.R;

import org.apache.http.conn.util.InetAddressUtils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.util.Enumeration;
import java.util.UUID;

import appaloosa_store.com.appaloosa_android_tools.Appaloosa;

public class DeviceUtils{

    public static final String NO_ACTIVE_NETWORK = "none";

    public static String getDeviceID() {
        final String imei = getImei();

        if(imei != null)
            return imei;
        else
            return getWifiMacAddress();
    }

    public static String getWifiMacAddress() {
        WifiManager manager = (WifiManager) Appaloosa.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
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
        TelephonyManager telephonyManager = (TelephonyManager) Appaloosa.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
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

    public static String getActiveNetwork() {
        ConnectivityManager connectivityManager = (ConnectivityManager) Appaloosa.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo == null ? NO_ACTIVE_NETWORK : networkInfo.getTypeName().toLowerCase();
    }

    public static String getIPAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && InetAddressUtils.isIPv4Address(inetAddress.getHostAddress()) ) {
                        return inetAddress.getHostAddress();

                    }
                }
            }
        } catch (SocketException ex) {
            Log.d(DeviceUtils.class.getSimpleName(), Appaloosa.getApplicationContext().getResources().getString(R.string.ip_address_no_retrieved));
        }
        return "0.0.0.0";
    }

    public static String getMobileNetworkType() {
        TelephonyManager mTelephonyManager = (TelephonyManager)
                Appaloosa.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        int networkType = mTelephonyManager.getNetworkType();
        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return "2g";
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return "3g";
            case TelephonyManager.NETWORK_TYPE_LTE:
                return "4g";
            default:
                return "unknown";
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static Pair<Integer, Integer> getScreenHeightAndWidth() {
        WindowManager wm = (WindowManager) Appaloosa.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        Point point = new Point();
        try {
            display.getRealSize(point);
        } catch (NoSuchMethodError e) {
            display.getSize(point);
        }
        return new Pair<>(point.y, point.x);
    }

    public static int getScreenDpi() {
        DisplayMetrics metrics = Appaloosa.getApplicationContext().getResources().getDisplayMetrics();
        return metrics.densityDpi;
    }

    public static String getOSVersion() {
        int version = Build.VERSION.SDK_INT;
        return "Android API " + version;
    }

    public static String getDeviceModel() {
        return Build.MODEL;
    }
}