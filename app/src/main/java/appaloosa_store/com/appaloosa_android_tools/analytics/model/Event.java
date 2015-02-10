package appaloosa_store.com.appaloosa_android_tools.analytics.model;


import com.google.gson.JsonObject;

public abstract class Event {

    public static enum EventCategory {
        APPLICATION_STARTED,
        ACTIVITY_PAUSED,
    }

    private long eventTimestamp;
    private EventCategory category;

    public Event(long eventTimestamp, EventCategory category) {
        this.eventTimestamp = eventTimestamp;
        this.category = category;
    }

    public long getEventTimestamp() {
        return eventTimestamp;
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
        jEvent.addProperty("event_timestamp", this.getEventTimestamp());
        jEvent.addProperty("category", this.getCategory().toString());
        return jEvent;
    }

    @Override
    public String toString() {
        return "Event : [eventTimestamp : " + eventTimestamp +
                ", category : " + category.toString() + "]";
    }
}
