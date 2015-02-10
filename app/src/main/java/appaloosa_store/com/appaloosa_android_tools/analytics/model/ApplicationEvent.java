package appaloosa_store.com.appaloosa_android_tools.analytics.model;

import com.google.gson.JsonObject;

public class ApplicationEvent extends Event {

    private String appName;

    public ApplicationEvent(String appName) {
        super(System.currentTimeMillis(), EventCategory.APPLICATION_STARTED);
        this.appName = appName;
    }

    @Override
    public JsonObject toJson() {
        JsonObject jAppEvent = super.toJson();
        jAppEvent.addProperty("app_name", appName);
        return jAppEvent;
    }
}
