package appaloosa_store.com.appaloosa_android_tools.tools;

import android.app.Activity;
import android.util.Log;

import com.appaloosa_store.R;

import appaloosa_store.com.appaloosa_android_tools.Appaloosa;
import appaloosa_store.com.appaloosa_android_tools.tools.interfaces.ApplicationAuthorizationInterface;
import appaloosa_store.com.appaloosa_android_tools.tools.services.blacklist.CheckBlacklistService;

public class AppaloosaBlacklist {

    private static AppaloosaBlacklist instance;

    public static synchronized AppaloosaBlacklist getInstance() {
        if(instance == null) {
            instance = new AppaloosaBlacklist();
        }
        return instance;
    }

    public void checkBlacklist(ApplicationAuthorizationInterface listeningActivity) {
        if (!(listeningActivity instanceof Activity)) {
            Log.e(Appaloosa.APPALOOSA_LOG_TAG, Appaloosa.getApplicationContext().getString(R.string.not_an_activity));
            return;
        }
        CheckBlacklistService.checkBlacklist(listeningActivity);
    }
}
