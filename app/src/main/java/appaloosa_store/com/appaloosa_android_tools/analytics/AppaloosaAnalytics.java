package appaloosa_store.com.appaloosa_android_tools.analytics;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import appaloosa_store.com.appaloosa_android_tools.Appaloosa;
import appaloosa_store.com.appaloosa_android_tools.analytics.callback.handler.ActivityLifeCycleHandler;
import appaloosa_store.com.appaloosa_android_tools.analytics.callback.handler.AnalyticsBatchingHandler;
import appaloosa_store.com.appaloosa_android_tools.analytics.db.AnalyticsDb;
import appaloosa_store.com.appaloosa_android_tools.analytics.model.ApplicationEvent;
import appaloosa_store.com.appaloosa_android_tools.analytics.services.AnalyticsServices;
import appaloosa_store.com.appaloosa_android_tools.utils.DeviceUtils;

public class AppaloosaAnalytics {

    public static final String ANALYTICS_LOG_TAG = "APPALOOSA_ANALYTICS";
    public static final int ANALYTICS_DB_BATCH_SIZE = 25;

    private static AnalyticsDb analyticsDb;
    private static AnalyticsBatchingHandler batchingHandler;
    private static boolean authorizedToLaunch;
    private static String analyticsEndpoint;
    private static BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            batchingHandler.shouldRun(
                    !DeviceUtils.getActiveNetwork().equals(DeviceUtils.NO_ACTIVE_NETWORK)
            );
        }
    };
    private static boolean alreadyStarted;

    public static void initialize() {
        analyticsDb = new AnalyticsDb(Appaloosa.getApplicationContext());
        batchingHandler = new AnalyticsBatchingHandler(analyticsDb);
        authorizedToLaunch = true;
        start();
    }

    private static void start() {
        if(authorizedToLaunch && batchingHandler != null && analyticsEndpoint != null && !alreadyStarted) {
            alreadyStarted = true;
            registerConnectivityStatusListener();
            AnalyticsServices.registerEvent(new ApplicationEvent());
            Appaloosa.getApplication().registerActivityLifecycleCallbacks(new ActivityLifeCycleHandler());
        }
    }

    public static AnalyticsDb getAnalyticsDb() {
        return analyticsDb;
    }

    public static String getAnalyticsEndpoint() {
        return analyticsEndpoint;
    }

    public static void setAnalyticsEndpoint(String analyticsEndpoint) {
        AppaloosaAnalytics.analyticsEndpoint = analyticsEndpoint;
        start();
    }

    private static void registerConnectivityStatusListener() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        Appaloosa.getApplicationContext().registerReceiver(broadcastReceiver, filter);
    }

}