package appaloosa_store.com.appaloosa_android_tools.tools.services.autoupdate;

import appaloosa_store.com.appaloosa_android_tools.Appaloosa;
import appaloosa_store.com.appaloosa_android_tools.utils.DeviceUtils;
import appaloosa_store.com.appaloosa_android_tools.utils.SysUtils;
import appaloosa_store.com.appaloosa_android_tools.utils.UrlUtils;

public class AutoUpdateUrlUtils {

    private static final String APPLICATION_INFO_PATH = "%1$d/mobile_applications/%2$s.json?token=%3$s&imei=%4$s";
    private static final String APPLICATION_DOWNLOAD_URL_PATH = "%1$d/mobile_applications/%2$d/install?token=%3$s&imei=%4$s";

    private String deviceIMEI;

    public AutoUpdateUrlUtils() {
        deviceIMEI = DeviceUtils.getDeviceID();
    }

    public String buildApplicationInfoURL() {
        String formattedPackageName = SysUtils.getApplicationPackage().replaceAll("\\.", "%2E");
        String path = String.format(APPLICATION_INFO_PATH, Appaloosa.getStoreId(), formattedPackageName, Appaloosa.getStoreToken(), deviceIMEI);
        return UrlUtils.getServerBaseUrl() + path;
    }

    public String buildGetDownloadURL(int mau_id) {
        String path = String.format(APPLICATION_DOWNLOAD_URL_PATH, Appaloosa.getStoreId(), mau_id, Appaloosa.getStoreToken(), deviceIMEI);
        return UrlUtils.getServerBaseUrl() + path;
    }
}
