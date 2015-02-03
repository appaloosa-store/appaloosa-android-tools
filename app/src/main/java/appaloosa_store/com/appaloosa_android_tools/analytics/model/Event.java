package appaloosa_store.com.appaloosa_android_tools.analytics.model;


public class Event {

    public static enum EventCategory {
        APPLICATION_STARTED,
        ACTIVITY_STARTED,
        ACTIVITY_STOPPED
    }

    private long timestamp;
    private EventCategory category;
    private String name;
    private String connection;

    public Event(long timestamp, EventCategory category, String name, String connection) {
        this.timestamp = timestamp;
        this.category = category;
        this.name = name;
        this.connection = connection;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public EventCategory getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public String getConnection() {
        return connection;
    }

    @Override
    public String toString() {
        return "Event : [timestamp : " + timestamp +
                ", category : " + category.toString() +
                ", name : " + name +
                ", connection : " + connection;
    }
}
