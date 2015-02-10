package appaloosa_store.com.appaloosa_android_tools.analytics.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import appaloosa_store.com.appaloosa_android_tools.analytics.AnalyticsConstant;
import appaloosa_store.com.appaloosa_android_tools.analytics.handler.AnalyticsBatchingHandler;
import appaloosa_store.com.appaloosa_android_tools.analytics.model.Event;

public class AnalyticsDb extends SQLiteOpenHelper {

    private static final String TAG = AnalyticsDb.class.getSimpleName();

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "analytics";

    private static final String TABLE_EVENT = "event";
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_EVENT +
            "(" + DBColumn.ID + " INTEGER PRIMARY KEY NOT NULL, " +
            DBColumn.EVENT + " TEXT);";

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
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENT + ";");
        onCreate(db);
    }

    public boolean insertEvent(Event event) {
        ContentValues value = new ContentValues();
        value.put(DBColumn.EVENT.toString(), event.toJson().toString());
        synchronized (lock) {
            if (db.insert(TABLE_EVENT, null, value) != -1) {
                eventCount++;
                if (eventCount >= AnalyticsConstant.ANALYTICS_DB_BATCH_SIZE) {
                    batchingHandler.sendMessage(batchingHandler.obtainMessage(0, AnalyticsConstant.ANALYTICS_DB_BATCH_SIZE_REACHED));
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

    public JsonArray getAndRemoveOldestEvents(int batchSize) {
        JsonArray events = new JsonArray();
        JsonParser parser = new JsonParser();
        synchronized (lock) {
            Cursor cursor = db.query(TABLE_EVENT,
                    DBColumn.COLUMNS,
                    null, null, null, null,
                    DBColumn.ID.toString() + " ASC",
                    batchSize + "");
            while (cursor.moveToNext()) {
                events.add(parser.parse(cursor.getString(DBColumn.EVENT.getIndex())));

                db.delete(TABLE_EVENT, DBColumn.ID.toString() + " = ?", new String[]{"" + cursor.getInt(DBColumn.ID.getIndex())});

                eventCount--;
            }
            cursor.close();
        }
        return events;
    }
}













