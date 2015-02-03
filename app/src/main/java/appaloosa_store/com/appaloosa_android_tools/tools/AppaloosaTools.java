package appaloosa_store.com.appaloosa_android_tools.tools;

import android.app.Activity;

import appaloosa_store.com.appaloosa_android_tools.tools.interfaces.ApplicationAuthorizationActivity;
import appaloosa_store.com.appaloosa_android_tools.tools.services.blacklist.CheckBlacklistService;

public class AppaloosaTools {
    public String developmentServerUrl;
    public Activity activity;

    private static AppaloosaTools instance = new AppaloosaTools();

    public static AppaloosaTools getInstance() {
        return instance;
    }

    public void checkBlacklist(Integer storeID, String storeToken, ApplicationAuthorizationActivity listeningActivity) {
        this.activity = listeningActivity;
        CheckBlacklistService.checkBlacklist(storeID, storeToken, listeningActivity);
    }

    public void checkBlacklist(Integer storeID, String storeToken, Activity activity) {
        this.activity = activity;
        CheckBlacklistService.checkBlacklist(storeID, storeToken);
    }

    public void setDevelopmentServerUrl(String url) {
        developmentServerUrl = url;
    }
}
