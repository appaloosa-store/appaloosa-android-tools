package appaloosa_store.com.appaloosa_android_tools.analytics.callback.handler;

import android.os.Handler;
import android.os.Message;

import appaloosa_store.com.appaloosa_android_tools.analytics.services.AnalyticsServices;

public class AnalyticsBatchingHandler extends Handler {

    public static final Integer ANALYTICS_DB_SHOULD_SEND_BATCH = 10;
    private static boolean hasNetwork;

    @Override
    public void handleMessage(Message msg) {
        if (AnalyticsServices.sending || !hasNetwork) {
            return;
        }

        if (msg.what == ANALYTICS_DB_SHOULD_SEND_BATCH) {
            AnalyticsServices.sendBatchToServer();
        }
    }

    public void hasNetwork(boolean run) {
        hasNetwork = run;
        if(hasNetwork) {
            this.sendMessage(this.obtainMessage(ANALYTICS_DB_SHOULD_SEND_BATCH));
        }
    }
}
