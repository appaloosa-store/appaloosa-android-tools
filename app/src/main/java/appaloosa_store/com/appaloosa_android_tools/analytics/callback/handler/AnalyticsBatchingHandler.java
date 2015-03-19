package appaloosa_store.com.appaloosa_android_tools.analytics.callback.handler;

import android.os.Handler;
import android.os.Message;

import appaloosa_store.com.appaloosa_android_tools.analytics.AppaloosaAnalytics;
import appaloosa_store.com.appaloosa_android_tools.analytics.db.AnalyticsDb;
import appaloosa_store.com.appaloosa_android_tools.analytics.services.AnalyticsServices;

public class AnalyticsBatchingHandler extends Handler {

    private static final Integer ANALYTICS_DB_CHECK_BATCH_SIZE_MESSAGE = 10;
    private static final Long DELAY_BETWEEN_CHECKS = 5 * 60 * 1000l;

    private AnalyticsDb analyticsDb;

    public AnalyticsBatchingHandler(AnalyticsDb analyticsDb) {
        this.analyticsDb = analyticsDb;
    }

    @Override
    public void handleMessage(Message msg) {
        if (AnalyticsServices.sending) {
            this.sleep();
            return;
        }

        if (msg.what == ANALYTICS_DB_CHECK_BATCH_SIZE_MESSAGE) {
            checkBatchSize();
        }
    }

    public void shouldRun(boolean run) {
        this.removeMessages(ANALYTICS_DB_CHECK_BATCH_SIZE_MESSAGE);
        if (run) {
            sendMessage(this.obtainMessage(ANALYTICS_DB_CHECK_BATCH_SIZE_MESSAGE));
        }
    }

    private void checkBatchSize() {
        int eventsCount = analyticsDb.countEvents();
        if (eventsCount > AppaloosaAnalytics.ANALYTICS_MIN_BATCH_SIZE) {
            AnalyticsServices.sendBatchToServer();
        }
        this.sleep();
    }

    private void sleep() {
        this.removeMessages(ANALYTICS_DB_CHECK_BATCH_SIZE_MESSAGE);
        sendMessageDelayed(this.obtainMessage(ANALYTICS_DB_CHECK_BATCH_SIZE_MESSAGE), DELAY_BETWEEN_CHECKS);
    }
}
