package appaloosa_store.com.appaloosa_android_tools.analytics;

import appaloosa_store.com.appaloosa_android_tools.Appaloosa;
import appaloosa_store.com.appaloosa_android_tools.analytics.db.AnalyticsDb;
import appaloosa_store.com.appaloosa_android_tools.analytics.handler.ActivityLifeCycleHandler;
import appaloosa_store.com.appaloosa_android_tools.analytics.handler.AnalyticsBatchingHandler;
import appaloosa_store.com.appaloosa_android_tools.analytics.model.ApplicationEvent;
import appaloosa_store.com.appaloosa_android_tools.analytics.services.AnalyticsServices;

public class AppaloosaAnalytics {

    private static AnalyticsDb analyticsDb;

    public static void start() {
        analyticsDb = new AnalyticsDb(Appaloosa.getApplicationContext(), new AnalyticsBatchingHandler());

        AnalyticsServices.registerEvent(new ApplicationEvent());

        Appaloosa.getApplication().registerActivityLifecycleCallbacks(new ActivityLifeCycleHandler());
    }

    public static AnalyticsDb getAnalyticsDb() {
        return analyticsDb;
    }

}
