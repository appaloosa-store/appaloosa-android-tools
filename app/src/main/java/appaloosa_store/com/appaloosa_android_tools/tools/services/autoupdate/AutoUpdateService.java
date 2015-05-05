package appaloosa_store.com.appaloosa_android_tools.tools.services.autoupdate;

import android.util.Log;

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
import appaloosa_store.com.appaloosa_android_tools.utils.DeviceUtils;
import appaloosa_store.com.appaloosa_android_tools.utils.SysUtils;
import appaloosa_store.com.appaloosa_android_tools.utils.UrlUtils;

public class AutoUpdateService {

    private static final String APPLICATION_INFO_PATH = "%1$d/mobile_applications/%2$s.json?token=%3$s&imei=%4$s";
    private static final String APPLICATION_DOWNLOAD_URL_PATH = "%1$d/mobile_applications/%2$d/install?token=%3$s&imei=%4$s";

    private static AutoUpdateService instance;

    private String deviceIMEI;
    private DownloadProgressCallback downloadProgressCallback;

    public static AutoUpdateService getInstance() {
        if(instance == null) {
            instance = new AutoUpdateService();
        }
        return instance;
    }

    private AutoUpdateService() {
        deviceIMEI = DeviceUtils.getImei();
    }

    public void checkLastUpdate(CheckLastUpdateCallback callback) {
        String url = this.buildApplicationInfoURL();
        Log.w(Appaloosa.APPALOOSA_LOG_TAG, "url : " + url);
        Ion.with(Appaloosa.getApplicationContext())
                .load(url)
                .as(new TypeToken<MobileApplicationUpdate>() {
                })
                .setCallback(callback);
    }

    public void downloadAndInstall(MobileApplicationUpdate mobileApplicationUpdate, DownloadProgressCallback progressCallback) {
        this.downloadProgressCallback = progressCallback;
        getDownloadURL(mobileApplicationUpdate);
    }

    private void getDownloadURL(MobileApplicationUpdate mobileApplicationUpdate) {
        String url = this.buildGetDownloadURL(mobileApplicationUpdate.getMobileApplicationUpdateId());
        Log.w(Appaloosa.APPALOOSA_LOG_TAG, "url : " + url);
        Ion.with(Appaloosa.getApplicationContext())
                .load(url)
                .asJsonObject()
                .withResponse()
                .setCallback(new GetDownloadURLCallback(this, mobileApplicationUpdate));
    }

    public void downloadAPK(final MobileApplicationUpdate mobileApplicationUpdate, String url) {
        url = "http://admin.araokat.com/static/delivrables/app-debug.apk";
        Log.w(Appaloosa.APPALOOSA_LOG_TAG, "url : " + url);
        final File APKFile = SysUtils.getAPKFile(Appaloosa.getStoreId() + "_" + (mobileApplicationUpdate.getMobileApplicationUpdateId() +1));
        Ion.with(Appaloosa.getApplicationContext())
                .load(url)
                .progressHandler(downloadProgressCallback)
                .write(APKFile)
                .setCallback(new FutureCallback<File>() {
                    @Override
                    public void onCompleted(Exception e, File file) {
                        if (e != null) {
                            Log.w(Appaloosa.APPALOOSA_LOG_TAG, Appaloosa.getApplicationContext().getResources().getString(R.string.download_apk_error));
                        } else if (file != null && file.length() == mobileApplicationUpdate.getBinarySize()){
                            SysUtils.installAPK(file);
                        } else {
                            Log.w(Appaloosa.APPALOOSA_LOG_TAG, "bad file length : " + file.length() + " instead of : " + mobileApplicationUpdate.getBinarySize());
                            SysUtils.installAPK(file);
                        }
                        downloadProgressCallback.dismissDialog();
                    }
                });
    }

    //URL Builders

    private String buildApplicationInfoURL() {
        String formattedPackageName = SysUtils.getApplicationPackage().replaceAll("\\.", "%2E");
        String path = String.format(APPLICATION_INFO_PATH, Appaloosa.getStoreId(), formattedPackageName, Appaloosa.getStoreToken(), deviceIMEI);
        return UrlUtils.getServerBaseUrl() + path;
    }

    private String buildGetDownloadURL(int mau_id) {
        String path = String.format(APPLICATION_DOWNLOAD_URL_PATH, Appaloosa.getStoreId(), mau_id, Appaloosa.getStoreToken(), deviceIMEI);
        return UrlUtils.getServerBaseUrl() + path;
    }
}
