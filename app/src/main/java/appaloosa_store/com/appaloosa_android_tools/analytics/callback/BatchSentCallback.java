package appaloosa_store.com.appaloosa_android_tools.analytics.callback;

import android.util.Log;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Response;

import org.apache.http.HttpStatus;

import java.util.List;

import appaloosa_store.com.appaloosa_android_tools.analytics.services.AnalyticsServices;
import appaloosa_store.com.appaloosa_android_tools.utils.DeviceUtils;

public class BatchSentCallback implements FutureCallback<Response<JsonObject>> {

    private static final String TAG = BatchSentCallback.class.getSimpleName();
    private static final Integer MAX_NB_OF_TRY = 5;
    private static final Long TIME_BETWEEN_TRY = 5000l;

    private List<Integer> eventIds;
    private JsonObject sentData;
    private int tryNb;

    public BatchSentCallback(List<Integer> eventIds, JsonObject data, int tryNb) {
        super();
        this.eventIds = eventIds;
        this.sentData = data;
        this.tryNb = tryNb;
    }

    @Override
    public void onCompleted(Exception e, Response<JsonObject> result) {
        if (e != null) {
            Log.v(TAG, "Error when sending Appaloosa-Store analytics, error message : " + e.getMessage());
            retry(sentData, tryNb);
            return;
        }
        if (result != null) {
            int httpCode = result.getHeaders().code();
            if (httpCode < HttpStatus.SC_OK || httpCode >= HttpStatus.SC_MULTIPLE_CHOICES) {
                Log.v(TAG, "Request not ok, HTTP code : " + httpCode);
                retry(sentData, tryNb);
            } else if (result.getResult() != null && result.getResult().get("received") != null) {
                if (!result.getResult().get("received").getAsBoolean()){
                    retry(sentData, tryNb);
                    return;
                }

                AnalyticsServices.deleteEventsSent(eventIds);
                AnalyticsServices.setSending(false);
                Log.v(TAG, "Analytics sent");
            }
        }
    }

    private void retry(JsonObject data, int tryNb) {
        if (tryNb < MAX_NB_OF_TRY) {
            try {
                Thread.sleep(TIME_BETWEEN_TRY);
                if (!DeviceUtils.getActiveNetwork().equals(DeviceUtils.NO_ACTIVE_NETWORK)) {
                    AnalyticsServices.send(eventIds, data, ++tryNb);
                    return;
                }
            } catch (InterruptedException e1) {
                //Nothing to do.
            }
        }
        AnalyticsServices.setSending(false);
    }
}
