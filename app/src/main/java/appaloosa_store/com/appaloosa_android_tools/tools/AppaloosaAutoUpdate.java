package appaloosa_store.com.appaloosa_android_tools.tools;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;

import com.appaloosa_store.R;

import appaloosa_store.com.appaloosa_android_tools.Appaloosa;
import appaloosa_store.com.appaloosa_android_tools.utils.exception.NullActivityException;
import appaloosa_store.com.appaloosa_android_tools.tools.callbacks.CheckLastUpdateCallback;
import appaloosa_store.com.appaloosa_android_tools.tools.callbacks.DownloadProgressCallback;
import appaloosa_store.com.appaloosa_android_tools.tools.models.MobileApplicationUpdate;
import appaloosa_store.com.appaloosa_android_tools.tools.services.autoupdate.AutoUpdateService;
import appaloosa_store.com.appaloosa_android_tools.utils.AlertDialogUtils;
import appaloosa_store.com.appaloosa_android_tools.utils.SysUtils;

public class AppaloosaAutoUpdate {

    private Activity autoUpdateActivity;
    private AutoUpdateService autoUpdateService;
    private boolean autoUpdateWithMessage;
    private String autoUpdateTitle;
    private String autoUpdateMessage;

    private static AppaloosaAutoUpdate instance;

    public static AppaloosaAutoUpdate getInstance() {
        if(instance == null) {
            instance = new AppaloosaAutoUpdate();
        }
        return instance;
    }

    private AppaloosaAutoUpdate() {
        autoUpdateService = AutoUpdateService.getInstance();
    }

    public void autoUpdate(Activity activity, boolean withMessage, String title, String message) {
        this.autoUpdateActivity = activity;
        this.autoUpdateWithMessage = withMessage;
        this.autoUpdateTitle = title;
        this.autoUpdateMessage = message;
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
        ProgressDialog downloadDialog = createAndShowDownloadDialog();
        autoUpdateService.downloadAndInstall(mobileApplicationUpdate, new DownloadProgressCallback(downloadDialog));
    }

    private ProgressDialog createAndShowDownloadDialog() {
        ProgressDialog downloadProgressDialog = new ProgressDialog(autoUpdateActivity);
        String dialogMessage = Appaloosa.getApplicationContext().getResources().getString(R.string.downloading_progress_message) + "...";
        downloadProgressDialog.setMessage(dialogMessage);
        downloadProgressDialog.show();
        return downloadProgressDialog;
    }
}
