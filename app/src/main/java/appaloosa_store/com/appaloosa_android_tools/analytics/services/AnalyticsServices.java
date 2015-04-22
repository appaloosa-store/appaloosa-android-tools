package appaloosa_store.com.appaloosa_android_tools.analytics.services;

import android.util.Log;
import android.util.Pair;

import com.appaloosa_store.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.ion.Ion;

import java.util.List;

import appaloosa_store.com.appaloosa_android_tools.Appaloosa;
import appaloosa_store.com.appaloosa_android_tools.analytics.AppaloosaAnalytics;
import appaloosa_store.com.appaloosa_android_tools.analytics.callback.BatchSentCallback;
import appaloosa_store.com.appaloosa_android_tools.analytics.db.AnalyticsDb;
import appaloosa_store.com.appaloosa_android_tools.analytics.model.Event;
import appaloosa_store.com.appaloosa_android_tools.utils.SysUtils;

public class AnalyticsServices {

    public static boolean sending;

    public static void registerEvent(final Event event) {
        final AnalyticsDb db = AppaloosaAnalytics.getAnalyticsDb();
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(!db.insertEvent(event)){
                    Log.v(AppaloosaAnalytics.ANALYTICS_LOG_TAG, Appaloosa.getApplicationContext().getResources().getString(R.string.event_not_recorded));
                }
            }
        }).start();
    }

    public static void sendBatchToServer() {
        sending = true;
        final AnalyticsDb db = AppaloosaAnalytics.getAnalyticsDb();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Pair<List<Integer>, JsonArray> idsAndOldestEvents = db.getOldestEvents();
                if(idsAndOldestEvents.first.size() > 0) {
                    JsonObject data = buildJson(idsAndOldestEvents.second);
                    send(idsAndOldestEvents.first, data, 1);
                }
            }
        }).start();
    }

    public static void send(List<Integer> eventIds, JsonObject data, int tryNb) {
        Log.v(AppaloosaAnalytics.ANALYTICS_LOG_TAG, "Sending analytics batch, try n°" + tryNb);
        Ion.with(Appaloosa.getApplicationContext())
                .load(AppaloosaAnalytics.getAnalyticsEndpoint() + "metrics/record_metrics_batch")
                .setJsonObjectBody(data)
                .asJsonObject()
                .withResponse()
                .setCallback(new BatchSentCallback(eventIds, data, tryNb));
    }

    public static void deleteEventsSent(final List<Integer> eventIds) {
        final AnalyticsDb db = AppaloosaAnalytics.getAnalyticsDb();
        new Thread(new Runnable() {
            @Override
            public void run() {
                db.deleteEvents(eventIds);
            }
        }).start();
    }

    private static JsonObject buildJson(JsonArray eventsToSend) {
        JsonObject data = new JsonObject();
        data.addProperty("device_platform", SysUtils.DEVICE_PLATFORM);
        data.addProperty("sdk_version_code", SysUtils.getSDKVersionCode());
        data.addProperty("sdk_version_name", SysUtils.getSDKVersionName());
        data.add("events", eventsToSend);
        data.addProperty("events_batch_size", eventsToSend != null ? eventsToSend.size() : 0);
        return data;
    }
}
