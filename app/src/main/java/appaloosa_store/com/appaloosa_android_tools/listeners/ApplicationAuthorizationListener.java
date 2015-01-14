package appaloosa_store.com.appaloosa_android_tools.listeners;

import android.content.DialogInterface;

import com.appaloosa_store.R;

import appaloosa_store.com.appaloosa_android_tools.interfaces.ApplicationAuthorizationActivity;
import appaloosa_store.com.appaloosa_android_tools.models.ApplicationAuthorization;
import appaloosa_store.com.appaloosa_android_tools.utils.AlertDialogUtils;

public class ApplicationAuthorizationListener extends ApplicationAuthorizationActivity {

    @Override
    public void isAuthorized(ApplicationAuthorization authorization) {
        AlertDialogUtils.showOkDialog(this, getString(R.string.blacklist_dialog_title), authorization.getMessage(), null);
    }

    @Override
    public void isNotAuthorized(ApplicationAuthorization authorization) {
        AlertDialogUtils.showOkDialog(this, getString(R.string.blacklist_dialog_title), authorization.getMessage(), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
    }
}
