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

    private static final Integer MAX_NB_OF_TRY = 10;
    private static final Long REFERENCE_TIME_BETWEEN_TRY = 30000l;

    private List<Integer> eventIds;
    private JsonObject sentData;
    private int currentBatchSendAttemptNb;

    public BatchSentCallback(List<Integer> eventIds, JsonObject data, int currentBatchSendAttemptNb) {
        super();
        this.eventIds = eventIds;
        this.sentData = data;
        this.currentBatchSendAttemptNb = currentBatchSendAttemptNb;
    }

    @Override
    public void onCompleted(Exception e, Response<JsonObject> result) {
        if (e != null || !httpStatusCodeOk(result) || !received(result)) {
            Log.v(AppaloosaAnalytics.ANALYTICS_LOG_TAG, "An error occurred when sending Appaloosa-Store analytics");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    retry(sentData, currentBatchSendAttemptNb);
                }
            }).start();
        } else {
            AnalyticsServices.sending = false;
            Log.v(AppaloosaAnalytics.ANALYTICS_LOG_TAG, "Analytics sent");
            AnalyticsServices.deleteEventsSent(eventIds);
        }
    }

    private boolean received(Response<JsonObject> result) {
        JsonObject jsonResult = result.getResult();
        return jsonResult != null &&
                !jsonResult.get("received").isJsonNull() &&
                jsonResult.get("received").getAsBoolean();
    }

    private boolean httpStatusCodeOk(Response<JsonObject> result) {
        int httpCode = result.getHeaders().code();
        return httpCode >= HttpStatus.SC_OK || httpCode < HttpStatus.SC_MULTIPLE_CHOICES;
    }


    private void retry(JsonObject data, int currentBatchSendAttemptNb) {
        if (currentBatchSendAttemptNb < MAX_NB_OF_TRY) {
            try {
                long sleepingTime = currentBatchSendAttemptNb * currentBatchSendAttemptNb * REFERENCE_TIME_BETWEEN_TRY;
                Thread.sleep(sleepingTime);
                if (!DeviceUtils.getActiveNetwork().equals(DeviceUtils.NO_ACTIVE_NETWORK)) {
                    AnalyticsServices.send(eventIds, data, ++currentBatchSendAttemptNb);
                    return;
                }
            } catch (InterruptedException ignored) {}
        }
        AnalyticsServices.deleteEventsSent(eventIds);
        AnalyticsServices.sending = false;
    }
}
