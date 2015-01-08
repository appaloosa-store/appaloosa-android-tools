package appaloosa_store.com.appaloosa_android_tools;

import android.app.Activity;
import android.content.Context;

import appaloosa_store.com.appaloosa_android_tools.interfaces.ApplicationAuthorizationInterface;
import appaloosa_store.com.appaloosa_android_tools.services.CheckBlacklistService;

public class AppaloosaTools {
    public static AppaloosaTools instance;
    public static Context context;

    public AppaloosaTools(Context context) {
        this.context = context;
        instance = this;
    }

    public void checkBlacklist(Integer storeID, String storeToken, Activity contextActivity, ApplicationAuthorizationInterface listeningActivity) {
        CheckBlacklistService.checkBlacklist(storeID, storeToken, contextActivity, listeningActivity);
    }

    public void checkBlacklist(Integer storeID, String storeToken, Activity activity) {
        CheckBlacklistService.checkBlacklist(storeID, storeToken, activity);
    }
}
