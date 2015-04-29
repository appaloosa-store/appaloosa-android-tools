package appaloosa_store.com.appaloosa_android_tools.tools;

import android.app.Activity;
import android.content.DialogInterface;

import com.appaloosa_store.R;

import appaloosa_store.com.appaloosa_android_tools.tools.interfaces.ApplicationAuthorizationActivity;
import appaloosa_store.com.appaloosa_android_tools.tools.models.ApplicationAuthorization;
import appaloosa_store.com.appaloosa_android_tools.tools.services.blacklist.CheckBlacklistService;
import appaloosa_store.com.appaloosa_android_tools.utils.AlertDialogUtils;

public class AppaloosaTools {
    public String developmentServerUrl;
    public Activity activity;

    private static AppaloosaTools instance = new AppaloosaTools();

    public static AppaloosaTools getInstance() {
        return instance;
    }

    public void checkBlacklist(ApplicationAuthorizationActivity listeningActivity) {
        this.activity = listeningActivity;
        CheckBlacklistService.checkBlacklist(listeningActivity);
    }

    public void checkBlacklist(Activity activity) {
        this.activity = activity;
        CheckBlacklistService.checkBlacklist(null);
    }

    public void setDevelopmentServerUrl(String url) {
        developmentServerUrl = url;
    }

    public void displayAuthorizationToast(ApplicationAuthorization applicationAuthorization) {
        if (!applicationAuthorization.isAuthorized() && activity != null) {
            AlertDialogUtils.showOkDialog(activity, activity.getString(R.string.blacklist_dialog_title), applicationAuthorization.getMessage(), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    activity.finish();
                }
            });
        }
    }
}
