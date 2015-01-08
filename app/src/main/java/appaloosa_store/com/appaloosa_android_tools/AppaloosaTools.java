package appaloosa_store.com.appaloosa_android_tools;

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

    public void checkBlacklist(Integer storeID, String storeToken, ApplicationAuthorizationInterface activity) {
        CheckBlacklistService.checkBlacklist(storeID, storeToken, activity);
    }
}
