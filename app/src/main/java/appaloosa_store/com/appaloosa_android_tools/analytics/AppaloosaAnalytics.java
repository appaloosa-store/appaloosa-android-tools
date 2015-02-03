package appaloosa_store.com.appaloosa_android_tools.analytics;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import appaloosa_store.com.appaloosa_android_tools.analytics.db.AnalyticsDb;
import appaloosa_store.com.appaloosa_android_tools.analytics.handler.ActivityLifeCycleHandler;
import appaloosa_store.com.appaloosa_android_tools.analytics.handler.AnalyticsBatchingHandler;
import appaloosa_store.com.appaloosa_android_tools.analytics.model.Event;
import appaloosa_store.com.appaloosa_android_tools.analytics.services.AnalyticsServices;
import appaloosa_store.com.appaloosa_android_tools.tools.AppaloosaTools;

public class AppaloosaAnalytics {

    private static final String TAG = AppaloosaAnalytics.class.getSimpleName();

    private static Context context;
    private static AnalyticsDb analyticsDb;

    /**
     * Starts recording the metrics on the app for the analytics tool.
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

        AnalyticsServices.registerEvent(Event.EventCategory.APPLICATION_STARTED, context.getResources().getString(application.getApplicationInfo().labelRes));

        application.registerActivityLifecycleCallbacks(new ActivityLifeCycleHandler());
    }

    public static Context getContext() {
        return context;
    }

    public static AnalyticsDb getAnalyticsDb() {
        return analyticsDb;
    }

}
