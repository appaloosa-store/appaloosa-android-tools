package appaloosa_store.com.appaloosa_android_tools.utils;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import appaloosa_store.com.appaloosa_android_tools.models.Event;
import appaloosa_store.com.appaloosa_android_tools.services.analytics.AnalyticsServices;

public class ActivityLifeCycleHandler implements Application.ActivityLifecycleCallbacks {

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {}

    @Override
    public void onActivityStarted(Activity activity) {
        AnalyticsServices.registerEvent(Event.EventType.ACTIVITY_STARTED, activity.getClass().getSimpleName());
    }

    @Override
    public void onActivityResumed(Activity activity) {}

    @Override
    public void onActivityPaused(Activity activity) {}

    @Override
    public void onActivityStopped(Activity activity) {
        AnalyticsServices.registerEvent(Event.EventType.ACTIVITY_STOPPED, activity.getClass().getSimpleName());
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {}

    @Override
    public void onActivityDestroyed(Activity activity) {}
}
