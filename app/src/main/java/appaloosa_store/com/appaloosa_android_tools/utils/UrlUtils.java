package appaloosa_store.com.appaloosa_android_tools.utils;

import com.appaloosa_store.R;

import appaloosa_store.com.appaloosa_android_tools.Appaloosa;

public class UrlUtils {

    public static String getServerBaseUrl() {
        String developmentServerUrl = Appaloosa.developmentServerUrl;
        return developmentServerUrl != null ? developmentServerUrl : Appaloosa.getApplicationContext().getString(R.string.server_url);
    }
}
