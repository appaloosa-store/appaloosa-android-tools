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

    private static AnalyticsDb analyticsDb;
    private static AnalyticsBatchingHandler batchingHandler;

    public static void start() {
        analyticsDb = new AnalyticsDb(Appaloosa.getApplicationContext());
        batchingHandler = new AnalyticsBatchingHandler(analyticsDb);

        AnalyticsServices.registerEvent(new ApplicationEvent());

        Appaloosa.getApplication().registerActivityLifecycleCallbacks(new ActivityLifeCycleHandler());

        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        Appaloosa.getApplicationContext().registerReceiver(broadcastReceiver, filter);
    }

    public static AnalyticsDb getAnalyticsDb() {
        return analyticsDb;
    }

    private static BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            batchingHandler.shouldRun(!DeviceUtils.getActiveNetwork().equals(DeviceUtils.NO_ACTIVE_NETWORK));
        }
    };

}
