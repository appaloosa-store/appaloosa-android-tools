package appaloosa_store.com.appaloosa_android_tools.tools;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.util.Log;

import com.appaloosa_store.R;

import appaloosa_store.com.appaloosa_android_tools.Appaloosa;
import appaloosa_store.com.appaloosa_android_tools.exception.NullActivityException;
import appaloosa_store.com.appaloosa_android_tools.tools.callbacks.CheckLastUpdateCallback;
import appaloosa_store.com.appaloosa_android_tools.tools.callbacks.DownloadProgressCallback;
import appaloosa_store.com.appaloosa_android_tools.tools.interfaces.ApplicationAuthorizationInterface;
import appaloosa_store.com.appaloosa_android_tools.tools.models.MobileApplicationUpdate;
import appaloosa_store.com.appaloosa_android_tools.tools.services.autoupdate.AutoUpdateService;
import appaloosa_store.com.appaloosa_android_tools.tools.services.blacklist.CheckBlacklistService;
import appaloosa_store.com.appaloosa_android_tools.utils.AlertDialogUtils;
import appaloosa_store.com.appaloosa_android_tools.utils.SysUtils;

public class AppaloosaTools {
    public String developmentServerUrl;
    private Activity blacklistActivity;

    //AutoUpdate
    private Activity autoUpdateActivity;
    private AutoUpdateService autoUpdateService;
    private boolean autoUpdateWithMessage;
    private String autoUpdateTitle;
    private String autoUpdateMessage;
    private ProgressDialog autoUpdateProgressDialog;

    private static AppaloosaTools instance;

    public static AppaloosaTools getInstance() {
        if(instance == null) {
            instance = new AppaloosaTools();
        }
        return instance;
    }

    public void checkBlacklist(ApplicationAuthorizationInterface listeningActivity) {
        if (!(listeningActivity instanceof Activity)) {
            Log.e(Appaloosa.APPALOOSA_LOG_TAG, Appaloosa.getApplicationContext().getString(R.string.not_an_activity));
            return;
        }
        this.blacklistActivity = (Activity) listeningActivity;
        CheckBlacklistService.checkBlacklist(listeningActivity);
    }

    public void finishActivity() {
        if(this.blacklistActivity != null) {
            this.blacklistActivity.finish();
        }
    }

    public void setDevelopmentServerUrl(String url) {
        developmentServerUrl = url;
    }

    public void autoUpdate(Activity activity, boolean withMessage, String title, String message) {
        this.autoUpdateActivity = activity;
        this.autoUpdateWithMessage = withMessage;
        this.autoUpdateTitle = title;
        this.autoUpdateMessage = message;
        autoUpdateService = AutoUpdateService.getInstance();
        autoUpdateService.checkLastUpdate(new CheckLastUpdateCallback());
    }

    public void lastUpdateVersionCode(final MobileApplicationUpdate mobileApplicationUpdate) {
        int currentVersionCode = SysUtils.getApplicationVersionCode();
        if (currentVersionCode < mobileApplicationUpdate.getVersionCode()) {
            if (autoUpdateWithMessage) {
                showConfirmInstallDialog(new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        downloadAndInstall(mobileApplicationUpdate);
                    }
                });
            } else {
                this.downloadAndInstall(mobileApplicationUpdate);
            }
        }
    }

    private void showConfirmInstallDialog(DialogInterface.OnClickListener positiveClickListener) {
        try {
            AlertDialogUtils.showConfirmDialog(autoUpdateActivity,
                    this.autoUpdateTitle,
                    this.autoUpdateMessage,
                    positiveClickListener);
        } catch (NullActivityException ignored) { }

    }

    private void downloadAndInstall(MobileApplicationUpdate mobileApplicationUpdate) {
        autoUpdateProgressDialog = new ProgressDialog(autoUpdateActivity);
        autoUpdateProgressDialog.setMessage("Download in progress");
        autoUpdateProgressDialog.show();

        autoUpdateService.downloadAndInstall(mobileApplicationUpdate, new DownloadProgressCallback(autoUpdateProgressDialog));
    }
}
