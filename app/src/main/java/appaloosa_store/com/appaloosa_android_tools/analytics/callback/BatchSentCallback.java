package appaloosa_store.com.appaloosa_android_tools.analytics.callback;

import android.util.Log;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Response;

import org.apache.http.HttpStatus;

import java.util.List;

import appaloosa_store.com.appaloosa_android_tools.analytics.AppaloosaAnalytics;
import appaloosa_store.com.appaloosa_android_tools.analytics.services.AnalyticsServices;
import appaloosa_store.com.appaloosa_android_tools.utils.DeviceUtils;

public class BatchSentCallback implements FutureCallback<Response<JsonObject>> {

    private static final Integer MAX_NB_OF_TRY = 5;
    private static final Long TIME_BETWEEN_TRY = 30000l;

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
        if (e != null || !httpStatusCodeOk(result) || !received(result)) {
            Log.v(AppaloosaAnalytics.ANALYTICS_LOG_TAG, "An error occurred when sending Appaloosa-Store analytics");
            retry(sentData, tryNb);
        } else {
            deleteEventsSent();
        }
    }

    private void deleteEventsSent() {
        AnalyticsServices.deleteEventsSent(eventIds);
        AnalyticsServices.sending = false;
        Log.v(AppaloosaAnalytics.ANALYTICS_LOG_TAG, "Analytics sent");
    }

    private boolean received(Response<JsonObject> result) {
        JsonObject jsonResult = result.getResult();
        return jsonResult != null &&
                jsonResult.get("received") != null &&
                jsonResult.get("received").getAsBoolean();
    }

    private boolean httpStatusCodeOk(Response<JsonObject> result) {
        int httpCode = result.getHeaders().code();
        return httpCode >= HttpStatus.SC_OK || httpCode < HttpStatus.SC_MULTIPLE_CHOICES;
    }


    private void retry(JsonObject data, int tryNb) {
        if (tryNb < MAX_NB_OF_TRY) {
            try {
                Thread.sleep(TIME_BETWEEN_TRY);
                if (!DeviceUtils.getActiveNetwork().equals(DeviceUtils.NO_ACTIVE_NETWORK)) {
                    AnalyticsServices.send(eventIds, data, ++tryNb);
                    return;
                }
            } catch (InterruptedException ignored) {}
        }
        AnalyticsServices.sending = false;
    }
}
