package appaloosa_store.com.appaloosa_android_tools.tools;

import android.app.Activity;
import android.util.Log;

import com.appaloosa_store.R;

import appaloosa_store.com.appaloosa_android_tools.Appaloosa;
import appaloosa_store.com.appaloosa_android_tools.tools.interfaces.ApplicationAuthorizationInterface;
import appaloosa_store.com.appaloosa_android_tools.tools.services.blacklist.CheckBlacklistService;

public class AppaloosaTools {
    public String developmentServerUrl;
    private Activity activity;

    private static AppaloosaTools instance;

    public static AppaloosaTools getInstance() {
        if(instance == null) {
            instance = new AppaloosaTools();
        }
        return instance;
    }

    public void checkBlacklist(ApplicationAuthorizationInterface listeningActivity) {
        if (!(listeningActivity instanceof Activity)) {
            Log.w(Appaloosa.APPALOOSA_LOG_TAG, Appaloosa.getApplicationContext().getString(R.string.not_an_activity));
            return;
        }
        this.activity = (Activity) listeningActivity;
        CheckBlacklistService.checkBlacklist(listeningActivity);
    }

    public void setDevelopmentServerUrl(String url) {
        developmentServerUrl = url;
    }

    public void finishActivity() {
        if(this.activity != null) {
            this.activity.finish();
        }
    }
}
