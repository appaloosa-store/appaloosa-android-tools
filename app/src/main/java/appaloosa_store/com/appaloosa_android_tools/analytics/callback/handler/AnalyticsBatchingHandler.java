package appaloosa_store.com.appaloosa_android_tools.analytics.callback.handler;

import android.os.Handler;
import android.os.Message;

import appaloosa_store.com.appaloosa_android_tools.analytics.AnalyticsConstant;
import appaloosa_store.com.appaloosa_android_tools.analytics.db.AnalyticsDb;
import appaloosa_store.com.appaloosa_android_tools.analytics.services.AnalyticsServices;

public class AnalyticsBatchingHandler extends Handler {

    private static final Integer ANALYTICS_DB_CHECK_BATCH_SIZE_MESSAGE = 10;
    private static final Long DELAY_BETWEEN_CHECKS = 10000l;

    private AnalyticsDb analyticsDb;

    public AnalyticsBatchingHandler(AnalyticsDb analyticsDb) {
        this.analyticsDb = analyticsDb;
    }

    @Override
    public void handleMessage(Message msg) {
        if (AnalyticsServices.isSending()) {
            this.sleep();
            return;
        }

        if (msg.what == ANALYTICS_DB_CHECK_BATCH_SIZE_MESSAGE) {
            int eventsCount = analyticsDb.countEvents();
            if (eventsCount > AnalyticsConstant.ANALYTICS_DB_BATCH_SIZE) {
                AnalyticsServices.sendBatchToServer();
            }
            this.sleep();
        }
    }

    public void shouldRun(boolean run) {
        if (run) {
            this.sleep();
        } else {
            this.removeMessages(ANALYTICS_DB_CHECK_BATCH_SIZE_MESSAGE);
        }
    }

    private void sleep() {
        this.removeMessages(ANALYTICS_DB_CHECK_BATCH_SIZE_MESSAGE);
        sendMessageDelayed(this.obtainMessage(ANALYTICS_DB_CHECK_BATCH_SIZE_MESSAGE), DELAY_BETWEEN_CHECKS);
    }
}
