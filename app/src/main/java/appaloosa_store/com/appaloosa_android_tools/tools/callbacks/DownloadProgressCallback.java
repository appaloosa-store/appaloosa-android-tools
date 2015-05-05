package appaloosa_store.com.appaloosa_android_tools.tools.callbacks;

import android.app.ProgressDialog;

import com.koushikdutta.ion.ProgressCallback;

public class DownloadProgressCallback implements ProgressCallback {

    private ProgressDialog dialog;

    public DownloadProgressCallback(ProgressDialog dialog) {
        this.dialog = dialog;
    }

    @Override
    public void onProgress(long downloaded, long total) {
        long percent = downloaded / total;
        dialog.setMessage(percent + "%");
    }

    public void dismissDialog() {
        dialog.dismiss();
    }
}
