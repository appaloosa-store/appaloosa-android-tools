package appaloosa_store.com.appaloosa_android_tools.models;


public class Event {

    public static enum EventType {
        APPLICATION_STARTED,
        ACTIVITY_STARTED,
        ACTIVITY_STOPPED
    }

    private long timestamp;
    private EventType type;
    private String name;
    private String connection;

    public Event(long timestamp, EventType type, String name, String connection) {
        this.timestamp = timestamp;
        this.type = type;
        this.name = name;
        this.connection = connection;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public EventType getType() {
        return type;
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
                ", type : " + type.toString() +
                ", name : " + name +
                ", connection : " + connection;
    }
}
