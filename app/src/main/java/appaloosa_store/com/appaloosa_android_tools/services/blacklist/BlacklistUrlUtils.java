package appaloosa_store.com.appaloosa_android_tools.services.blacklist;

import android.util.Base64;

import appaloosa_store.com.appaloosa_android_tools.AppaloosaTools;
import appaloosa_store.com.appaloosa_android_tools.utils.DeviceUtils;
import appaloosa_store.com.appaloosa_android_tools.utils.SysUtils;
import appaloosa_store.com.appaloosa_android_tools.utils.UrlUtils;

public class BlacklistUrlUtils {
    private static final String CHECK_BLACKLIST_URL = "%d/mobile_application_updates/is_authorized?token=%s&application_id=%s&device_id=%s&version=%d&locale=%s";

    public static String buildURL(Integer storeID, String storeToken) {
        String baseUrl = UrlUtils.getServerBaseUrl();
        return baseUrl + buildParams(storeID, storeToken);
    }

    private static String buildParams(Integer storeID, String storeToken) {
        String imei = DeviceUtils.getDeviceID();
        String encodedImei = Base64.encodeToString(imei.getBytes(), Base64.DEFAULT).trim();
        String locale = AppaloosaTools.getInstance().activity.getResources().getConfiguration().locale.getLanguage();
        String packageName = SysUtils.getApplicationPackage();
        int versionCode = SysUtils.getApplicationVersionCode();

        return String.format(CHECK_BLACKLIST_URL, storeID, storeToken, packageName, encodedImei, versionCode, locale);
    }
}
