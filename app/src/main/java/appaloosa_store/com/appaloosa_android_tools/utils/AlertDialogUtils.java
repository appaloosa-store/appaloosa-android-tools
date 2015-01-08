package appaloosa_store.com.appaloosa_android_tools.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class AlertDialogUtils {
    public static void showOkDialog(final Activity context, String title, String message, DialogInterface.OnClickListener buttonListener) {
        if(buttonListener == null) {
            buttonListener = new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            };
        }
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, buttonListener)
                .show();
    }
}
