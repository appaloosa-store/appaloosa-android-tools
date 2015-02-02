package appaloosa_store.com.appaloosa_android_tools;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import appaloosa_store.com.appaloosa_android_tools.db.AnalyticsDb;
import appaloosa_store.com.appaloosa_android_tools.listeners.AnalyticsBatchingHandler;
import appaloosa_store.com.appaloosa_android_tools.models.Event;
import appaloosa_store.com.appaloosa_android_tools.services.analytics.AnalyticsServices;
import appaloosa_store.com.appaloosa_android_tools.utils.ActivityLifeCycleHandler;

public class AppaloosaAnalytics {

    private static final String TAG = AppaloosaAnalytics.class.getSimpleName();
    private final static boolean PRODUCTION = false;
    public static String API_SERVER_BASE_URL = "";
    static {
        if (PRODUCTION) {
            API_SERVER_BASE_URL = "https://appaloosa-int.herokuapp.com/api/v1/";
        } else {
            API_SERVER_BASE_URL = "http://10.42.13.95:3000/";
        }
    }
    public static final int ANALYTICS_DB_BATCH_SIZE = 5;
    public static final String ANALYTICS_DB_BATCH_SIZE_REACHED = "analytics_db_batch_size_reached";

    private static Context context;
    private static AnalyticsDb analyticsDb;

    /**
     * Starts recording the metrics on the app for the analytics tool.
     * The stop() method has to be called to stop the job.
     * @param application The Application object in which the SDK is imported.
     *                    It can be obtained in an activity with the this.getApplication()
     *                    method.
     */
    public static void start(Application application, Activity activity) {
        Log.d(TAG, "SDK start");
        AppaloosaAnalytics.context = application.getApplicationContext();
        //TODO Create general context for the SDK to avoid the following line
        AppaloosaTools.getInstance().activity = activity;
        analyticsDb = new AnalyticsDb(AppaloosaAnalytics.context, new AnalyticsBatchingHandler());
        AnalyticsServices.registerEvent(Event.EventType.APPLICATION_STARTED, context.getResources().getString(application.getApplicationInfo().labelRes));
        application.registerActivityLifecycleCallbacks(new ActivityLifeCycleHandler());
    }

    public static Context getContext() {
        return context;
    }

    public static AnalyticsDb getAnalyticsDb() {
        return analyticsDb;
    }

}
