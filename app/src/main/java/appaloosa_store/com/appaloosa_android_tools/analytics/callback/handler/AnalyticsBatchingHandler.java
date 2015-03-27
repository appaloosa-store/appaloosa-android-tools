package appaloosa_store.com.appaloosa_android_tools.analytics.callback.handler;

import android.os.Handler;
import android.os.Message;

import appaloosa_store.com.appaloosa_android_tools.analytics.services.AnalyticsServices;

public class AnalyticsBatchingHandler extends Handler {

    public static final Integer ANALYTICS_DB_NOT_EMPTY_MESSAGE = 10;
    private static boolean hasNetwork;

    @Override
    public void handleMessage(Message msg) {
        if (AnalyticsServices.sending || !hasNetwork) {
            return;
        }

        if (msg.what == ANALYTICS_DB_NOT_EMPTY_MESSAGE) {
            AnalyticsServices.sendBatchToServer();
        }
    }

    public void updateNetworkStatus(boolean hasNetwork) {
        AnalyticsBatchingHandler.hasNetwork = hasNetwork;
        if(AnalyticsBatchingHandler.hasNetwork) {
            this.sendEmptyMessage(ANALYTICS_DB_NOT_EMPTY_MESSAGE);
        }
    }
}
