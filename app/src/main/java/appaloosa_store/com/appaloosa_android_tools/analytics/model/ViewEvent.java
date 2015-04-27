package appaloosa_store.com.appaloosa_android_tools.analytics.model;

import com.google.gson.JsonObject;

public class ViewEvent extends Event {

    private long timeSpent;
    private String activityName;

    public ViewEvent(String activityName, long timeSpent) {
        super(System.currentTimeMillis(), EventCategory.VIEW_PAUSED);
        this.activityName = activityName;
        this.timeSpent = timeSpent;
    }

    @Override
    public JsonObject toJson() {
        JsonObject jActivityEvent = super.toJson();
        jActivityEvent.addProperty("view_name", activityName);
        jActivityEvent.addProperty("time_spent", timeSpent);
        return jActivityEvent;
    }
}
