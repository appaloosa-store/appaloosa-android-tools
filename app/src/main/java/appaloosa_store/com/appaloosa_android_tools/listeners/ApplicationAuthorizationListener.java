package appaloosa_store.com.appaloosa_android_tools.listeners;

import android.app.Activity;
import android.content.DialogInterface;

import com.appaloosa_store.R;

import appaloosa_store.com.appaloosa_android_tools.interfaces.ApplicationAuthorizationInterface;
import appaloosa_store.com.appaloosa_android_tools.models.ApplicationAuthorization;
import appaloosa_store.com.appaloosa_android_tools.utils.AlertDialogUtils;

public class ApplicationAuthorizationListener implements ApplicationAuthorizationInterface {
    private final Activity activity;

    public ApplicationAuthorizationListener(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void isAllowed(ApplicationAuthorization authorization) {
        AlertDialogUtils.showOkDialog(activity, activity.getString(R.string.blacklist_dialog_title), authorization.getMessage(), null);
    }

    @Override
    public void isNotAllowed(ApplicationAuthorization authorization) {
        AlertDialogUtils.showOkDialog(activity, activity.getString(R.string.blacklist_dialog_title), authorization.getMessage(), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                activity.finish();
            }
        });
    }
}
