package appaloosa_store.com.appaloosa_android_tools;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.appaloosa_store.R;

import appaloosa_store.com.appaloosa_android_tools.analytics.AppaloosaAnalytics;
import appaloosa_store.com.appaloosa_android_tools.tools.AppaloosaTools;
import appaloosa_store.com.appaloosa_android_tools.tools.interfaces.ApplicationAuthorizationInterface;

public class Appaloosa {

    public static final String APPALOOSA_LOG_TAG = "APPALOOSA";

    private static Application application;
    private static Context applicationContext;

    private static Integer storeId;
    private static String storeToken;

    /**
     * Starts the Appaloosa SDK.
     * You can access our SDK tools (Blacklist and Auto-update features) through the AppaloosaTools class
     * You can access our SDK analytics features through the AppaloosaAnalytics class.
     * @param application The Application object in which the SDK is imported.
     *                    It can be obtained in an activity with the this.getApplication()
     *                    method.
     * @param storeId The id of your store. It can be found on your account on appaloosa-store.com.
     * @param storeToken The token of your store. It can be found on your account on appaloosa-store.com.
     */
    public static void init(Application application, Integer storeId, String storeToken) {
        Appaloosa.application = application;
        applicationContext = application.getApplicationContext();
        Log.d(APPALOOSA_LOG_TAG, applicationContext.getResources().getString(R.string.starting_sdk));
        Appaloosa.storeId = storeId;
        Appaloosa.storeToken = storeToken;
    }

    public static Application getApplication() {
        return application;
    }

    public static Context getApplicationContext() {
        return applicationContext;
    }

    public static Integer getStoreId() {
        return storeId;
    }

    public static String getStoreToken() {
        return storeToken;
    }

    public static void startAnalytics() {
        AppaloosaAnalytics.initialize();
    }

    /**
     * This method checks if the device is blacklisted or not.
     * With the two methods of the ApplicationAuthorizationInterface you can manage what
     * information should be displayed to the user. In case of unauthorized use, the activity is
     * finished after the call to isNotAuthorized(ApplicationAuthorization authorization);
     * @param activity It should be an Object extending an Activity object and implementing the
     *                 ApplicationAuthorizationInterface.
     */
    public static void checkBlacklist(ApplicationAuthorizationInterface activity) {
        AppaloosaTools.getInstance().checkBlacklist(activity);
    }

    public static void setDevelopmentServerUrl(String developmentServerUrl) {
        AppaloosaTools.getInstance().setDevelopmentServerUrl(developmentServerUrl);
    }

    public static void setAnalyticsEndpoint(String analyticsEndpoint) {
        AppaloosaAnalytics.setAnalyticsEndpoint(analyticsEndpoint);
    }
}
