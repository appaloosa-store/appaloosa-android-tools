package appaloosa_store.com.appaloosa_android_tools.analytics.model;

import android.util.Pair;

import com.google.gson.JsonObject;

import appaloosa_store.com.appaloosa_android_tools.utils.DeviceUtils;

public class ApplicationEvent extends Event {


    public ApplicationEvent() {
        super(System.currentTimeMillis(), EventCategory.APPLICATION_STARTED);
    }

    @Override
    public JsonObject toJson() {
        JsonObject jEvent = super.toJson();

        Pair<Integer, Integer> screenHeightWidth = DeviceUtils.getScreenHeightAndWidth();
        jEvent.addProperty("screen_height", screenHeightWidth.first);
        jEvent.addProperty("screen_width", screenHeightWidth.second);
        jEvent.addProperty("screen_dpi", DeviceUtils.getScreenDpi());
        jEvent.addProperty("os_version", DeviceUtils.getOSVersion());
        jEvent.addProperty("device_model", DeviceUtils.getDeviceModel());

        return jEvent;
    }
}
