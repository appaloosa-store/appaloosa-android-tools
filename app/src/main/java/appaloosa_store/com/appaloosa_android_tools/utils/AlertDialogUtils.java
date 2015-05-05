package appaloosa_store.com.appaloosa_android_tools.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import appaloosa_store.com.appaloosa_android_tools.utils.exception.NullActivityException;

public class AlertDialogUtils {
    public static void showConfirmDialog(final Activity activity, String title, String message, DialogInterface.OnClickListener positiveButtonListener) throws NullActivityException {
        if(positiveButtonListener == null) {
            positiveButtonListener = new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            };
        }
        if (activity==null) {
            throw new NullActivityException();
        }

        new AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, positiveButtonListener)
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
}
