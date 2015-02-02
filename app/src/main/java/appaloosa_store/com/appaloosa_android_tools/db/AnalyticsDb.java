package appaloosa_store.com.appaloosa_android_tools.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import appaloosa_store.com.appaloosa_android_tools.AppaloosaAnalytics;
import appaloosa_store.com.appaloosa_android_tools.listeners.AnalyticsBatchingHandler;
import appaloosa_store.com.appaloosa_android_tools.models.Event;

public class AnalyticsDb extends SQLiteOpenHelper {

    private static final String TAG = AnalyticsDb.class.getSimpleName();

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "analytics";

    private static final String TABLE_EVENT = "event";
    private static final String COLUMN_ID = "id";
    private static final int COLUMN_ID_INDEX = 0;
    private static final String COLUMN_TIME = "time";
    private static final int COLUMN_TIME_INDEX = 1;
    private static final String COLUMN_TYPE = "type";
    private static final int COLUMN_TYPE_INDEX = 2;
    private static final String COLUMN_NAME = "name";
    private static final int COLUMN_NAME_INDEX = 3;
    private static final String COLUMN_CONNECTION = "connection";
    private static final int COLUMN_CONNECTION_INDEX = 4;
    private static final String[] COLUMNS = new String[] {COLUMN_ID, COLUMN_TIME, COLUMN_TYPE, COLUMN_NAME, COLUMN_CONNECTION};
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_EVENT +
            "(" + COLUMN_ID + " INTEGER PRIMARY KEY NOT NULL, " +
            COLUMN_TIME + " INTEGER, " +
            COLUMN_TYPE + " TEXT, " +
            COLUMN_NAME + " TEXT, " +
            COLUMN_CONNECTION + " TEXT);";

    private SQLiteDatabase db;
    private AnalyticsBatchingHandler batchingHandler;
    private final Object lock = new Object();
    private Integer eventCount;

    public AnalyticsDb(Context context, AnalyticsBatchingHandler batchingHandler) {
        super(context, DB_NAME, null, DB_VERSION);
        db = getWritableDatabase();
        this.batchingHandler = batchingHandler;
        eventCount = countEvents();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO Check if it is OK to do so...
        //Normally there is nothing critical so it's easier to drop and recreate.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENT + ";");
        onCreate(db);
    }

    public boolean insertEvent(Event event) {
        ContentValues value = new ContentValues();
        value.put(COLUMN_TIME, event.getTimestamp());
        value.put(COLUMN_TYPE, event.getType().toString());
        value.put(COLUMN_NAME, event.getName());
        value.put(COLUMN_CONNECTION, event.getConnection());
        synchronized (lock) {
            if (db.insert(TABLE_EVENT, null, value) != -1) {
                eventCount++;
                if (eventCount >= AppaloosaAnalytics.ANALYTICS_DB_BATCH_SIZE) {
                    batchingHandler.sendMessage(batchingHandler.obtainMessage(0, AppaloosaAnalytics.ANALYTICS_DB_BATCH_SIZE_REACHED));
                }
                return true;
            }
        }
        return false;
    }

    public int countEvents() {
        Cursor c = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_EVENT, null);
        if (c.moveToNext()) {
            return c.getInt(0);
        }
        return 0;
    }

    public List<Event> getAndRemoveOldestEvents(int batchSize) {
        List<Event> events = new ArrayList<>();
        synchronized (lock) {
            Cursor cursor = db.query(TABLE_EVENT,
                    COLUMNS,
                    null, null, null, null,
                    COLUMN_TIME + " ASC",
                    batchSize + "");
            while (cursor.moveToNext()) {
                events.add(new Event(cursor.getLong(COLUMN_TIME_INDEX),
                        Event.EventType.valueOf(cursor.getString(COLUMN_TYPE_INDEX)),
                        cursor.getString(COLUMN_NAME_INDEX),
                        cursor.getString(COLUMN_CONNECTION_INDEX)));

                db.delete(TABLE_EVENT, COLUMN_ID + " = ?", new String[]{"" + cursor.getInt(COLUMN_ID_INDEX)});

                eventCount--;
            }
            cursor.close();
        }
        return events;
    }
}













