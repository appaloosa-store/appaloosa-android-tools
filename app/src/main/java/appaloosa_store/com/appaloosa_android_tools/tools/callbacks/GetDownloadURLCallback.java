package appaloosa_store.com.appaloosa_android_tools.tools.callbacks;

import android.util.Log;

import com.appaloosa_store.R;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Response;

import org.apache.http.HttpStatus;

import appaloosa_store.com.appaloosa_android_tools.Appaloosa;
import appaloosa_store.com.appaloosa_android_tools.tools.models.MobileApplicationUpdate;
import appaloosa_store.com.appaloosa_android_tools.tools.services.autoupdate.AutoUpdateService;

public class GetDownloadURLCallback implements FutureCallback<Response<JsonObject>> {

    private AutoUpdateService autoUpdateService;
    private MobileApplicationUpdate mobileApplicationUpdate;

    public GetDownloadURLCallback(AutoUpdateService autoUpdateService, MobileApplicationUpdate mobileApplicationUpdate) {
        this.autoUpdateService = autoUpdateService;
        this.mobileApplicationUpdate = mobileApplicationUpdate;
    }

    @Override
    public void onCompleted(Exception e, Response<JsonObject> result) {
        if (e != null) {
            Log.w(Appaloosa.APPALOOSA_LOG_TAG, Appaloosa.getApplicationContext().getResources().getString(R.string.get_download_url_error));
        } else if (result != null && httpStatusCodeOk(result) && received(result)) {
            String downloadUrl = result.getResult().get("download_url").getAsString();
            autoUpdateService.downloadAPK(this.mobileApplicationUpdate, downloadUrl);
        }
    }

    private boolean received(Response<JsonObject> result) {
        JsonObject jsonResult = result.getResult();
        return jsonResult != null &&
                !jsonResult.get("download_url").isJsonNull();
    }

    private boolean httpStatusCodeOk(Response<JsonObject> result) {
        int httpCode = result.getHeaders().code();
        return httpCode >= HttpStatus.SC_OK || httpCode < HttpStatus.SC_MULTIPLE_CHOICES;
    }
}
