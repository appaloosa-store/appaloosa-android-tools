package appaloosa_store.com.appaloosa_android_tools.analytics.services;

import android.util.Log;
import android.util.Pair;

import com.appaloosa_store.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.ion.Ion;

import java.util.List;

import appaloosa_store.com.appaloosa_android_tools.Appaloosa;
import appaloosa_store.com.appaloosa_android_tools.analytics.AppaloosaAnalytics;
import appaloosa_store.com.appaloosa_android_tools.analytics.callback.BatchSentCallback;
import appaloosa_store.com.appaloosa_android_tools.analytics.db.AnalyticsDb;
import appaloosa_store.com.appaloosa_android_tools.analytics.model.Event;
import appaloosa_store.com.appaloosa_android_tools.utils.DeviceUtils;
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
        final AnalyticsDb db = AppaloosaAnalytics.getAnalyticsDb();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Pair<List<Integer>, JsonArray> idsAndOldestEvents = db.getOldestEvents(AppaloosaAnalytics.ANALYTICS_DB_BATCH_SIZE);
                JsonObject data = buildJson(idsAndOldestEvents.second);
                send(idsAndOldestEvents.first, data, 1);
            }
        }).start();
    }

    public static void send(List<Integer> eventIds, JsonObject data, int tryNb) {
        Log.v(AppaloosaAnalytics.ANALYTICS_LOG_TAG, "Sending analytics batch, try n°" + tryNb);
        sending = true;
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
        data.add("events", eventsToSend);
        data.addProperty("events_batch_size", eventsToSend != null ? eventsToSend.size() : 0);
        data.add("connection", buildConnectionJson());
        data.add("device", buildDeviceJson());
        data.add("info", buildInfoJson());
        return data;
    }

    private static JsonElement buildInfoJson() {
        JsonObject idJson = new JsonObject();
        idJson.addProperty("measured_time", System.currentTimeMillis());
        idJson.addProperty("device_id", DeviceUtils.getDeviceID());
        idJson.addProperty("bundle_id", SysUtils.getApplicationPackage());
        idJson.addProperty("version_id", SysUtils.getApplicationVersionCode());
        idJson.addProperty("store_id", Appaloosa.getStoreId());
        idJson.addProperty("store_token", Appaloosa.getStoreToken());
        return idJson;
    }

    private static JsonElement buildDeviceJson() {
        JsonObject deviceJson = new JsonObject();
        deviceJson.addProperty("screen_resolution", DeviceUtils.getScreenResolution());
        deviceJson.addProperty("os_version", DeviceUtils.getOSVersion());
        deviceJson.addProperty("device_model", DeviceUtils.getDeviceModel());
        return deviceJson;
    }

    private static JsonElement buildConnectionJson() {
        JsonObject connectionJson = new JsonObject();
        connectionJson.addProperty("network_type", DeviceUtils.getActiveNetwork());
        connectionJson.addProperty("ip_address", DeviceUtils.getIPAddress());
        return connectionJson;
    }
}
