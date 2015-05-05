package appaloosa_store.com.appaloosa_android_tools.tools.callbacks;

import android.app.ProgressDialog;

import com.appaloosa_store.R;
import com.koushikdutta.ion.ProgressCallback;

import appaloosa_store.com.appaloosa_android_tools.Appaloosa;

public class DownloadProgressCallback implements ProgressCallback {

    private ProgressDialog dialog;

    public DownloadProgressCallback(ProgressDialog dialog) {
        this.dialog = dialog;
    }

    @Override
    public void onProgress(long downloaded, long total) {
        String dialogMessage = Appaloosa.getApplicationContext().getResources().getString(R.string.downloading_progress_message);
        long percent = downloaded / total;
        dialog.setMessage(dialogMessage + " : " + percent + "%");
    }

    public void dismissDialog() {
        dialog.dismiss();
    }
}
