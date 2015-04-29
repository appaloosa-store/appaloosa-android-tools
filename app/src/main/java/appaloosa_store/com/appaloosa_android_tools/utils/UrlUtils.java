package appaloosa_store.com.appaloosa_android_tools.utils;

import com.appaloosa_store.R;

import appaloosa_store.com.appaloosa_android_tools.Appaloosa;
import appaloosa_store.com.appaloosa_android_tools.tools.AppaloosaTools;

public class UrlUtils {

    public static String getServerBaseUrl() {
        final AppaloosaTools appaloosaTools = AppaloosaTools.getInstance();
        String developmentServerUrl = appaloosaTools.developmentServerUrl;
        return developmentServerUrl != null ? developmentServerUrl : Appaloosa.getApplicationContext().getString(R.string.server_url);
    }
}
