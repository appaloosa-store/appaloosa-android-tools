package appaloosa_store.com.appaloosa_android_tools.tools.callbacks;

import android.util.Log;

import com.appaloosa_store.R;
import com.koushikdutta.async.future.FutureCallback;

import appaloosa_store.com.appaloosa_android_tools.Appaloosa;
import appaloosa_store.com.appaloosa_android_tools.tools.AppaloosaAutoUpdate;
import appaloosa_store.com.appaloosa_android_tools.tools.models.MobileApplicationUpdate;

public class CheckLastUpdateCallback implements FutureCallback<MobileApplicationUpdate> {

    @Override
    public void onCompleted(Exception e, MobileApplicationUpdate result) {
        if (e != null) {
            Log.v(Appaloosa.APPALOOSA_LOG_TAG, Appaloosa.getApplicationContext().getResources().getString(R.string.check_last_update_error));
        } else if (result != null) {
            AppaloosaAutoUpdate.getInstance().lastUpdateVersionCode(result);
        }
    }
}
