package appaloosa_store.com.appaloosa_android_tools.services.analytics;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.List;

import appaloosa_store.com.appaloosa_android_tools.AppaloosaAnalytics;
import appaloosa_store.com.appaloosa_android_tools.db.AnalyticsDb;
import appaloosa_store.com.appaloosa_android_tools.models.Event;
import appaloosa_store.com.appaloosa_android_tools.utils.DeviceUtils;

public class AnalyticsServices {

    private static final String TAG = AnalyticsServices.class.getSimpleName();

    private static final AnalyticsDb db = AppaloosaAnalytics.getAnalyticsDb();

    public static void registerEvent(final Event.EventType type, final String eventName) {
        final AnalyticsDb db = AppaloosaAnalytics.getAnalyticsDb();
        final Event e = new Event(System.currentTimeMillis(), type, eventName, DeviceUtils.getActiveNetwork(AppaloosaAnalytics.getContext()));
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(!db.insertEvent(e)){
                    Log.d(TAG, "Event was not recorded");
                }
            }
        }).start();
    }

    public static void sendBatchToServer() {
        List<Event> eventsToSend = db.getAndRemoveOldestEvents(AppaloosaAnalytics.ANALYTICS_DB_BATCH_SIZE);
        JsonArray jsonArray = new JsonArray();
        for (Event e : eventsToSend) {
            JsonObject jEvent = new JsonObject();
            jEvent.addProperty("time", e.getTimestamp());
            jEvent.addProperty("type", e.getType().toString());
            jEvent.addProperty("name", e.getName());
            jEvent.addProperty("connection", e.getConnection());
            jsonArray.add(jEvent);
        }
        JsonObject data = new JsonObject();
        data.addProperty("deviceID", DeviceUtils.getDeviceID());
        data.add("events", jsonArray);
        Ion.with(AppaloosaAnalytics.getContext())
                .load(AppaloosaAnalytics.API_SERVER_BASE_URL + "analytics/record_events.json")
                .setJsonObjectBody(data)
                .asJsonObject()
                //TODO À implémenter correctement.
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (e != null) {
                            Log.d(getClass().getSimpleName(), "error : " + e.getMessage());
                        } else {
                            Log.d(getClass().getSimpleName(), "result : " + result.toString());
                        }
                    }
                });
    }
}
