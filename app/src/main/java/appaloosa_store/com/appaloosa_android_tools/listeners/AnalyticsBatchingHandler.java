package appaloosa_store.com.appaloosa_android_tools.listeners;

import android.os.Handler;
import android.os.Message;

import appaloosa_store.com.appaloosa_android_tools.AppaloosaAnalytics;
import appaloosa_store.com.appaloosa_android_tools.services.analytics.AnalyticsServices;

public class AnalyticsBatchingHandler extends Handler {

    @Override
    public void handleMessage(Message msg) {
        if (msg.obj.equals(AppaloosaAnalytics.ANALYTICS_DB_BATCH_SIZE_REACHED)) {
            AnalyticsServices.sendBatchToServer();
        }
    }
}
