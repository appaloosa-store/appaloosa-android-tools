package appaloosa_store.com.appaloosa_android_tools.analytics;

import android.content.Context;

import appaloosa_store.com.appaloosa_android_tools.Appaloosa;
import appaloosa_store.com.appaloosa_android_tools.analytics.db.AnalyticsDb;
import appaloosa_store.com.appaloosa_android_tools.analytics.handler.ActivityLifeCycleHandler;
import appaloosa_store.com.appaloosa_android_tools.analytics.handler.AnalyticsBatchingHandler;
import appaloosa_store.com.appaloosa_android_tools.analytics.model.Event;
import appaloosa_store.com.appaloosa_android_tools.analytics.services.AnalyticsServices;

public class AppaloosaAnalytics {

    private static AnalyticsDb analyticsDb;

    public static void start() {
        analyticsDb = new AnalyticsDb(Appaloosa.getApplicationContext(), new AnalyticsBatchingHandler());

        Context context = Appaloosa.getApplicationContext();
        AnalyticsServices.registerEvent(Event.EventCategory.APPLICATION_STARTED, context.getResources().getString(context.getApplicationInfo().labelRes));

        Appaloosa.getApplication().registerActivityLifecycleCallbacks(new ActivityLifeCycleHandler());
    }

    public static AnalyticsDb getAnalyticsDb() {
        return analyticsDb;
    }

}
