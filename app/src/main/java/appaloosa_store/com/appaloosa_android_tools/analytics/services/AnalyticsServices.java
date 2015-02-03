package appaloosa_store.com.appaloosa_android_tools.analytics.services;

import android.util.Log;

import com.appaloosa_store.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.List;

import appaloosa_store.com.appaloosa_android_tools.Appaloosa;
import appaloosa_store.com.appaloosa_android_tools.analytics.AnalyticsConstant;
import appaloosa_store.com.appaloosa_android_tools.analytics.AppaloosaAnalytics;
import appaloosa_store.com.appaloosa_android_tools.analytics.db.AnalyticsDb;
import appaloosa_store.com.appaloosa_android_tools.analytics.model.Event;
import appaloosa_store.com.appaloosa_android_tools.utils.DeviceUtils;

public class AnalyticsServices {

    private static final String TAG = AnalyticsServices.class.getSimpleName();

    public static void registerEvent(final Event.EventCategory type, final String eventName) {
        final AnalyticsDb db = AppaloosaAnalytics.getAnalyticsDb();
        final Event e = new Event(System.currentTimeMillis(), type, eventName, DeviceUtils.getActiveNetwork());
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(!db.insertEvent(e)){
                    Log.d(TAG, Appaloosa.getApplicationContext().getResources().getString(R.string.event_not_recorded));
                }
            }
        }).start();
    }

    public static void sendBatchToServer() {
        final AnalyticsDb db = AppaloosaAnalytics.getAnalyticsDb();
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Event> eventsToSend = db.getAndRemoveOldestEvents(AnalyticsConstant.ANALYTICS_DB_BATCH_SIZE);
                JsonObject data = buildEventsJson(eventsToSend);
                Ion.with(Appaloosa.getApplicationContext())
                        .load(AnalyticsConstant.API_SERVER_BASE_URL + "analytics/record_events.json")
                        .setJsonObjectBody(data)
                        .asJsonObject()
                        .setCallback(new BatchSentCallback());
            }
        }).start();
    }

    private static JsonObject buildEventsJson(List<Event> eventsToSend) {
        JsonArray jsonArray = new JsonArray();
        for (Event e : eventsToSend) {
            JsonObject jEvent = new JsonObject();
            jEvent.addProperty("time", e.getTimestamp());
            jEvent.addProperty("type", e.getCategory().toString());
            jEvent.addProperty("name", e.getName());
            jEvent.addProperty("connection", e.getConnection());
            jsonArray.add(jEvent);
        }
        JsonObject data = new JsonObject();
        data.addProperty("deviceID", DeviceUtils.getDeviceID());
        data.add("events", jsonArray);
        return data;
    }

    private static class BatchSentCallback implements FutureCallback<JsonObject> {

        @Override
        public void onCompleted(Exception e, JsonObject result) {
            //TODO À implémenter correctement.
            if (e != null) {
                Log.d(getClass().getSimpleName(), "error : " + e.getMessage());
            } else {
                Log.d(getClass().getSimpleName(), "result : " + result.toString());
            }
        }
    }
}
