package appaloosa_store.com.appaloosa_android_tools;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.appaloosa_store.R;

import appaloosa_store.com.appaloosa_android_tools.analytics.AppaloosaAnalytics;
import appaloosa_store.com.appaloosa_android_tools.tools.AppaloosaAutoUpdate;
import appaloosa_store.com.appaloosa_android_tools.tools.AppaloosaBlacklist;
import appaloosa_store.com.appaloosa_android_tools.tools.interfaces.ApplicationAuthorizationInterface;
import appaloosa_store.com.appaloosa_android_tools.tools.interfaces.CloseAndCleanActivity;

public class Appaloosa {

    public static final String APPALOOSA_LOG_TAG = "APPALOOSA";

    public static String developmentServerUrl;

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

    /**
     * This method initializes the recording of various stats on the device and the application usage.
     * This features requires you to implement the checkBlacklist method. The recording will effectively
     * start only after the checkBlacklist call is done. No statistics are recorded when the user is
     * blacklisted.
     */
    public static void startAnalytics() {
        AppaloosaAnalytics.initialize();
    }

    /**
     * This method checks if the device is blacklisted or not.
     * With the two methods of the ApplicationAuthorizationInterface you can manage what
     * information should be displayed to the user. It is your responsibility to kill your
     * application if the application is not authorized and we strongly recommend you to do so.
     * You may use the method closeApplication(Activity activity) to finish your app.
     * @param activity It should be an Object extending an Activity object and implementing the
     *                 ApplicationAuthorizationInterface.
     */
    public static void checkBlacklist(ApplicationAuthorizationInterface activity) {
        AppaloosaBlacklist.getInstance().checkBlacklist(activity);
    }

    /**
     * This method allows you to automatically check for possible updates on Appaloosa.
     * If an update is available, this method does not give the choice of download to the user. The
     * update is downloaded right away. However, once downloaded the user still has the choice to
     * install it or not.
     * @param activity The activity that will be used as background for the upload.
     */
    public static void autoUpdate(Activity activity) {
        AppaloosaAutoUpdate.getInstance().autoUpdate(activity, false, null, null);
    }

    /**
     * This method allows you to automatically check for possible updates on Appaloosa.
     * If an update is available, this method displays an Dialog to the user with the title and
     * message provided. The user can cancel or approve. If he clicks on "OK" the download starts.
     * Once downloaded the user still has the choice to install it or not.
     * @param activity The activity that will be used as background for the upload.
     * @param title The title of the update dialog.
     * @param message The message displayed in the update dialog.
     */
    public static void autoUpdateWithMessage(Activity activity, String title, String message) {
        AppaloosaAutoUpdate.getInstance().autoUpdate(activity, true, title, message);
    }

    /**
     * This method closes your application.
     * @param activity The current activity.
     */
    public static void closeApplication(Activity activity) {
        if (activity != null && !activity.isFinishing()) {
            Intent intent = new Intent(activity, CloseAndCleanActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            activity.startActivity(intent);
        }
    }

    /**
     * For development purpose only.
     * @param developmentServerUrl String
     */
    public static void setDevelopmentServerUrl(String developmentServerUrl) {
        Appaloosa.developmentServerUrl = developmentServerUrl;
    }

    /**
     * For internal purpose only.
     * @param analyticsEndpoint String
     */
    public static void setAnalyticsEndpoint(String analyticsEndpoint) {
        AppaloosaAnalytics.setAnalyticsEndpoint(analyticsEndpoint);
    }
}
