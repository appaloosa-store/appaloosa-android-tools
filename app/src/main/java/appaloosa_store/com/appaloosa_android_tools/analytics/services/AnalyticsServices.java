package appaloosa_store.com.appaloosa_android_tools.analytics.services;

import android.util.Log;

import com.appaloosa_store.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import appaloosa_store.com.appaloosa_android_tools.Appaloosa;
import appaloosa_store.com.appaloosa_android_tools.analytics.AnalyticsConstant;
import appaloosa_store.com.appaloosa_android_tools.analytics.AppaloosaAnalytics;
import appaloosa_store.com.appaloosa_android_tools.analytics.db.AnalyticsDb;
import appaloosa_store.com.appaloosa_android_tools.analytics.model.Event;
import appaloosa_store.com.appaloosa_android_tools.utils.DeviceUtils;
import appaloosa_store.com.appaloosa_android_tools.utils.SysUtils;

public class AnalyticsServices {

    private static final String TAG = AnalyticsServices.class.getSimpleName();

    public static void registerEvent(final Event event) {
        final AnalyticsDb db = AppaloosaAnalytics.getAnalyticsDb();
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(!db.insertEvent(event)){
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
                JsonArray eventsToSend = db.getAndRemoveOldestEvents(AnalyticsConstant.ANALYTICS_DB_BATCH_SIZE);
                JsonObject data = buildJson(eventsToSend);
                Ion.with(Appaloosa.getApplicationContext())
                        .load(AnalyticsConstant.API_SERVER_BASE_URL + "metrics/record_metrics_batch")
                        .setJsonObjectBody(data)
                        .asJsonObject()
                        .setCallback(new BatchSentCallback());
            }
        }).start();
    }

    private static JsonObject buildJson(JsonArray eventsToSend) {
        JsonObject data = new JsonObject();
        data.add("events", eventsToSend);
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
        //TODO Ajouter le type de données mobile ? 2G, 3G, 4G ?
        return connectionJson;
    }

    private static class BatchSentCallback implements FutureCallback<JsonObject> {

        @Override
        public void onCompleted(Exception e, JsonObject result) {
            //TODO À implémenter si on veut récupérer des infos après envoie.
            if (e != null) {
                Log.d(getClass().getSimpleName(), "error : " + e.getMessage());
            } else {
                Log.d(getClass().getSimpleName(), "result : " + result.toString());
            }
        }
    }
}
