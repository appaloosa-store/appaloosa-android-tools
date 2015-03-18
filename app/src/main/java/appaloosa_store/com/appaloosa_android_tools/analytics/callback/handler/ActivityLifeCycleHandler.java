package appaloosa_store.com.appaloosa_android_tools.analytics.callback.handler;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import appaloosa_store.com.appaloosa_android_tools.analytics.model.ActivityEvent;
import appaloosa_store.com.appaloosa_android_tools.analytics.services.AnalyticsServices;

public class ActivityLifeCycleHandler implements Application.ActivityLifecycleCallbacks {

    private long resumeTime = System.currentTimeMillis();

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {}

    @Override
    public void onActivityStarted(Activity activity) {}

    @Override
    public void onActivityResumed(Activity activity) {
        resumeTime = System.currentTimeMillis();
    }

    @Override
    public void onActivityPaused(Activity activity) {
        AnalyticsServices.registerEvent(new ActivityEvent(activity.getClass().getSimpleName(), System.currentTimeMillis() - resumeTime));
    }

    @Override
    public void onActivityStopped(Activity activity) {}

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {}

    @Override
    public void onActivityDestroyed(Activity activity) {}
}
