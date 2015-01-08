package appaloosa_store.com.appaloosa_android_tools.services;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.appaloosa_store.R;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import appaloosa_store.com.appaloosa_android_tools.AppaloosaTools;
import appaloosa_store.com.appaloosa_android_tools.interfaces.ApplicationAuthorizationInterface;
import appaloosa_store.com.appaloosa_android_tools.models.ApplicationAuthorization;
import appaloosa_store.com.appaloosa_android_tools.utils.DeviceUtils;
import appaloosa_store.com.appaloosa_android_tools.utils.SysUtils;
import appaloosa_store.com.appaloosa_android_tools.utils.UrlUtils;

public class CheckBlacklistService {
    private static final String LOG_TAG = "APPALOOSA_TOOLS";
    private static final String BLACKLIST_FILENAME = "BLACKLIST_STATUS";
    private static final String CHECK_BLACKLIST_URL = "%d/mobile_application_updates/is_authorized?token=%s&application_id=%s&device_id=%s&version=%d&locale=%s";

    public static void checkBlacklist(Integer storeID, String storeToken, final ApplicationAuthorizationInterface activity) {
        final Context context = AppaloosaTools.context;

        if(storeID == null || storeToken == null) {
            ApplicationAuthorization authorization = new ApplicationAuthorization();
            authorization.setStatus(ApplicationAuthorization.Status.REQUEST_ERROR.toString());
            authorization.setMessage(context.getString(R.string.missing_store_params));
            activity.isNotAllowed(authorization);
            return;
        }

        Ion.with(context)
                .load(buildURL(storeID, storeToken))
                .as(new TypeToken<ApplicationAuthorization>() { })
                .setCallback(new ApplicationAuthorizationCallback(activity));
    }

    /*
        AUTHORIZATION ------------------------------------------------------------------------------
     */
    private static class ApplicationAuthorizationCallback implements FutureCallback<ApplicationAuthorization> {
        private final ApplicationAuthorizationInterface activity;

        public ApplicationAuthorizationCallback(ApplicationAuthorizationInterface activity) {
            this.activity = activity;
        }

        @Override
        public void onCompleted(Exception e, ApplicationAuthorization applicationAuthorization) {
            if (e == null && applicationAuthorization != null) {
                informActivityOfAuthorization(activity, applicationAuthorization);
                setBlacklistStatusToFile(applicationAuthorization);
            } else {
                informActivityOfAuthorization(activity, getBlacklistStatusFromFile());
            }
        }
    }

    private static void informActivityOfAuthorization(ApplicationAuthorizationInterface activity, ApplicationAuthorization applicationAuthorization) {
        if (applicationAuthorization.isAuthorized()) {
            activity.isAllowed(applicationAuthorization);
            Log.d(LOG_TAG, "device is authorized to launch this app");
        } else {
            activity.isNotAllowed(applicationAuthorization);
            Log.d(LOG_TAG, "device is NOT authorized to launch this app");
        }
    }

    /*
        BLACKLIST FILE MGMT ------------------------------------------------------------------------
     */
    private static void setBlacklistStatusToFile(ApplicationAuthorization applicationAuthorization) {
        final Context context = AppaloosaTools.context;
        String statusString = applicationAuthorization.getStatus();
        try {
            FileOutputStream fOS = context.openFileOutput(BLACKLIST_FILENAME, Context.MODE_PRIVATE);
            fOS.write(statusString.getBytes());
            fOS.close();
        } catch (IOException e) { }
    }

    private static ApplicationAuthorization getBlacklistStatusFromFile() {
        Log.d(LOG_TAG, "readinging blacklist status from file");
        final Context context = AppaloosaTools.context;
        ApplicationAuthorization applicationAuthorization;
        try {
            FileInputStream fIS = context.openFileInput(BLACKLIST_FILENAME);
            InputStreamReader iSR = new InputStreamReader(fIS);
            BufferedReader bR = new BufferedReader(iSR);
            ApplicationAuthorization.Status status = ApplicationAuthorization.Status.valueOf(bR.readLine());
            bR.close();
            iSR.close();
            fIS.close();
            applicationAuthorization = ApplicationAuthorization.getApplicationAuthorizationForStatus(status);
        } catch (IOException e) {
            applicationAuthorization = ApplicationAuthorization.getApplicationAuthorizationForStatus(ApplicationAuthorization.Status.AUTHORIZED);
        }
        return applicationAuthorization;
    }

    /*
        URL SETUP ----------------------------------------------------------------------------------
     */
    private static String buildURL(Integer storeID, String storeToken) {
        String baseUrl = UrlUtils.getServerBaseUrl();
        return baseUrl + buildParams(storeID, storeToken);
    }

    private static String buildParams(Integer storeID, String storeToken) {
        Context context = AppaloosaTools.context;
        String imei = DeviceUtils.getDeviceId();
        String encodedImei = Base64.encodeToString(imei.getBytes(), Base64.DEFAULT).trim();
        String locale = context.getResources().getConfiguration().locale.getLanguage();
        String packageName = SysUtils.getApplicationPackage();
        int versionCode = SysUtils.getApplicationVersionCode();

        return String.format(CHECK_BLACKLIST_URL, storeID, storeToken, packageName, encodedImei, versionCode, locale);
    }
}
