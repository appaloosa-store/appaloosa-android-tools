package appaloosa_store.com.appaloosa_android_tools.analytics.handler;

import android.os.Handler;
import android.os.Message;

import appaloosa_store.com.appaloosa_android_tools.analytics.AnalyticsConstant;
import appaloosa_store.com.appaloosa_android_tools.analytics.services.AnalyticsServices;

public class AnalyticsBatchingHandler extends Handler {

    @Override
    public void handleMessage(Message msg) {
        if (msg.obj.equals(AnalyticsConstant.ANALYTICS_DB_BATCH_SIZE_REACHED)) {
            AnalyticsServices.sendBatchToServer();
        }
    }
}
