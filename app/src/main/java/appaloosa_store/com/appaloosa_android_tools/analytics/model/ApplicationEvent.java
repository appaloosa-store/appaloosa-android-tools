package appaloosa_store.com.appaloosa_android_tools.analytics.model;

import com.google.gson.JsonObject;

public class ApplicationEvent extends Event {


    public ApplicationEvent() {
        super(System.currentTimeMillis(), EventCategory.APPLICATION_STARTED);
    }

    @Override
    public JsonObject toJson() {
        return super.toJson();
    }
}
