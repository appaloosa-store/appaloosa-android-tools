package appaloosa_store.com.appaloosa_android_tools.analytics.model;


import com.google.gson.JsonObject;

import appaloosa_store.com.appaloosa_android_tools.Appaloosa;
import appaloosa_store.com.appaloosa_android_tools.utils.DeviceUtils;
import appaloosa_store.com.appaloosa_android_tools.utils.SysUtils;

public abstract class Event {

    public static enum EventCategory {
        APPLICATION_STARTED,
        ACTIVITY_PAUSED,
    }

    private long eventTime;
    private EventCategory category;

    public Event(long eventTime, EventCategory category) {
        this.eventTime = eventTime;
        this.category = category;
    }

    public long getEventTime() {
        return eventTime;
    }

    public EventCategory getCategory() {
        return category;
    }

    /**
     * This method has to be overridden in the extending classes.
     * The super method has to be called to fetch the first part of the JsonObject
     * that has to be completed with the new properties of the implemented Object.
     *
     * @return A JsonObject with the properties of the Event class.
     */
    public JsonObject toJson() {
        JsonObject jEvent = new JsonObject();
        jEvent.addProperty("measured_time", getEventTime());
        jEvent.addProperty("category", this.getCategory().toString());

        jEvent.addProperty("sdk_version", SysUtils.getSDKVersionCode());
        jEvent.addProperty("phone_identifier", DeviceUtils.getDeviceID());
        jEvent.addProperty("bundle_id", SysUtils.getApplicationPackage());
        jEvent.addProperty("version_id", SysUtils.getApplicationVersionCode());
        jEvent.addProperty("store_id", Appaloosa.getStoreId());
        jEvent.addProperty("store_token", Appaloosa.getStoreToken());
        jEvent.addProperty("device_platform", SysUtils.DEVICE_PLATFORM);

        jEvent.addProperty("network_type", DeviceUtils.getActiveNetwork());
        jEvent.addProperty("ip_address", DeviceUtils.getIPAddress());
        jEvent.addProperty("mobile_network_type", DeviceUtils.getMobileNetworkType());

        return jEvent;
    }

    @Override
    public String toString() {
        return "Event : [eventTime : " + eventTime +
                ", category : " + category.toString() + "]";
    }
}
