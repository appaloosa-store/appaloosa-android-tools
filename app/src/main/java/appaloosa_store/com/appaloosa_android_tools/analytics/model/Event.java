package appaloosa_store.com.appaloosa_android_tools.analytics.model;


import com.google.gson.JsonObject;

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
        jEvent.addProperty("event_time", this.getEventTime());
        jEvent.addProperty("category", this.getCategory().toString());
        return jEvent;
    }

    @Override
    public String toString() {
        return "Event : [eventTime : " + eventTime +
                ", category : " + category.toString() + "]";
    }
}
