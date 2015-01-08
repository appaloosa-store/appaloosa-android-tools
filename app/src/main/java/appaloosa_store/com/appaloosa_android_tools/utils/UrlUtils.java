package appaloosa_store.com.appaloosa_android_tools.utils;

import android.content.Context;

import com.appaloosa_store.R;

import appaloosa_store.com.appaloosa_android_tools.AppaloosaTools;

public class UrlUtils {

    public static String getServerBaseUrl() {
        Context context = AppaloosaTools.context;
        return context.getString(R.string.server_url);
    }
}
