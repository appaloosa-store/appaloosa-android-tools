package appaloosa_store.com.appaloosa_android_tools.tools.services.autoupdate;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.appaloosa_store.R;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.File;

import appaloosa_store.com.appaloosa_android_tools.Appaloosa;
import appaloosa_store.com.appaloosa_android_tools.tools.callbacks.CheckLastUpdateCallback;
import appaloosa_store.com.appaloosa_android_tools.tools.callbacks.DownloadProgressCallback;
import appaloosa_store.com.appaloosa_android_tools.tools.callbacks.GetDownloadURLCallback;
import appaloosa_store.com.appaloosa_android_tools.tools.models.MobileApplicationUpdate;
import appaloosa_store.com.appaloosa_android_tools.utils.SysUtils;

public class AutoUpdateService {

    private static AutoUpdateService instance;

    private AutoUpdateUrlUtils autoUpdateUrlUtils;
    private DownloadProgressCallback downloadProgressCallback;

    public static AutoUpdateService getInstance() {
        if(instance == null) {
            instance = new AutoUpdateService();
        }
        return instance;
    }

    private AutoUpdateService() {
        autoUpdateUrlUtils = new AutoUpdateUrlUtils();
    }

    public void checkLastUpdate(CheckLastUpdateCallback callback) {
        String url = autoUpdateUrlUtils.buildApplicationInfoURL();
        Ion.with(Appaloosa.getApplicationContext())
                .load(url)
                .as(new TypeToken<MobileApplicationUpdate>() {})
                .setCallback(callback);
    }

    public void downloadAndInstall(MobileApplicationUpdate mobileApplicationUpdate, DownloadProgressCallback progressCallback) {
        this.downloadProgressCallback = progressCallback;
        getDownloadURL(mobileApplicationUpdate);
    }

    private void getDownloadURL(MobileApplicationUpdate mobileApplicationUpdate) {
        String url = autoUpdateUrlUtils.buildGetDownloadURL(mobileApplicationUpdate.getMobileApplicationUpdateId());
        Ion.with(Appaloosa.getApplicationContext())
                .load(url)
                .asJsonObject()
                .withResponse()
                .setCallback(new GetDownloadURLCallback(this, mobileApplicationUpdate));
    }

    public void downloadAPK(final MobileApplicationUpdate mobileApplicationUpdate, String downloadURL) {
        final File APKFile = SysUtils.getAPKFile(Appaloosa.getStoreId() + "_" + (mobileApplicationUpdate.getMobileApplicationUpdateId() + 1));
        Ion.with(Appaloosa.getApplicationContext())
                .load(downloadURL)
                .progressHandler(downloadProgressCallback)
                .write(APKFile)
                .setCallback(new FutureCallback<File>() {
                    @Override
                    public void onCompleted(Exception e, File file) {
                        if (e != null || file == null || file.length() != mobileApplicationUpdate.getBinarySize()) {
                            Log.v(Appaloosa.APPALOOSA_LOG_TAG, Appaloosa.getApplicationContext().getResources().getString(R.string.download_apk_error));
                            Context context = Appaloosa.getApplicationContext();
                            Toast.makeText(context, context.getResources().getString(R.string.download_failed), Toast.LENGTH_LONG).show();
                        } else {
                            SysUtils.installAPK(file);
                        }
                        downloadProgressCallback.dismissDialog();
                    }
                });
    }
}