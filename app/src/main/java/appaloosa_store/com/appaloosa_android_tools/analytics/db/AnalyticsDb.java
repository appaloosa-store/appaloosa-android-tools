package appaloosa_store.com.appaloosa_android_tools.analytics.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Pair;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

import appaloosa_store.com.appaloosa_android_tools.analytics.callback.handler.AnalyticsBatchingHandler;
import appaloosa_store.com.appaloosa_android_tools.analytics.model.Event;

public class AnalyticsDb extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "analytics";

    private static final String TABLE_EVENT = "event";
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_EVENT +
            "(" + DBColumn.ID + " INTEGER PRIMARY KEY NOT NULL, " +
            DBColumn.EVENT + " TEXT);";
    private static final int BATCH_SIZE_LIMIT = 100;

    private SQLiteDatabase db;
    private AnalyticsBatchingHandler batchingHandler;
    private final Object lock = new Object();

    public AnalyticsDb(Context context, AnalyticsBatchingHandler batchingHandler) {
        super(context, DB_NAME, null, DB_VERSION);
        db = getWritableDatabase();
        this.batchingHandler = batchingHandler;
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
            if(db.insert(TABLE_EVENT, null, value) != -1) {
                this.shouldSendBatch();
                return true;
            }
            return false;
        }
    }

    public Pair<List<Integer>, JsonArray> getOldestEvents() {
        List<Integer> eventsIds = new ArrayList<>();
        JsonArray events = new JsonArray();
        JsonParser parser = new JsonParser();
        synchronized (lock) {
            Cursor cursor = db.query(TABLE_EVENT,
                    DBColumn.COLUMNS,
                    null, null, null, null,
                    DBColumn.ID.toString() + " ASC",
                    BATCH_SIZE_LIMIT + "");
            while (cursor.moveToNext()) {
                events.add(parser.parse(cursor.getString(DBColumn.EVENT.getIndex())));
                eventsIds.add(cursor.getInt(DBColumn.ID.getIndex()));
            }
            cursor.close();
        }
        return new Pair<>(eventsIds, events);
    }

    public void deleteEvents(List<Integer> ids) {
        String arg = TextUtils.join(",", ids.toArray());
        synchronized (lock) {
            db.execSQL(String.format("DELETE FROM " + TABLE_EVENT + " WHERE " + DBColumn.ID + " IN (%s);", arg));
            if(countEvents() > 0) {
                this.shouldSendBatch();
            }
        }
    }

    private int countEvents() {
        Cursor c = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_EVENT, null);
        int count = 0;
        if (c.moveToNext()) {
            count = c.getInt(0);
        }
        c.close();
        return count;
    }

    private void shouldSendBatch() {
        batchingHandler.sendMessage(
                batchingHandler.obtainMessage(
                        AnalyticsBatchingHandler.ANALYTICS_DB_SHOULD_SEND_BATCH));
    }
}