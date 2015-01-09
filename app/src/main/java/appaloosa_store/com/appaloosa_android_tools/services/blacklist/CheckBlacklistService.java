package appaloosa_store.com.appaloosa_android_tools.services.blacklist;

import android.content.Context;
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
import appaloosa_store.com.appaloosa_android_tools.interfaces.ApplicationAuthorisationActivity;
import appaloosa_store.com.appaloosa_android_tools.interfaces.ApplicationAuthorizationInterface;
import appaloosa_store.com.appaloosa_android_tools.listeners.ApplicationAuthorizationListener;
import appaloosa_store.com.appaloosa_android_tools.models.ApplicationAuthorization;

public class CheckBlacklistService {
    private static final String LOG_TAG = "APPALOOSA_TOOLS";
    private static final String BLACKLIST_FILENAME = "BLACKLIST_STATUS";

    public static void checkBlacklist(Integer storeID, String storeToken, final ApplicationAuthorisationActivity listeningActivity) {
        if(storeID == null || storeToken == null) {
            informActivityItIsNotAllowed(listeningActivity);
        } else {
            Ion.with(listeningActivity)
                .load(BlacklistUrlUtils.buildURL(storeID, storeToken))
                .as(new TypeToken<ApplicationAuthorization>() {
                })
                .setCallback(new ApplicationAuthorizationCallback(listeningActivity));
        }
    }

    public static void checkBlacklist(Integer storeID, String storeToken) {
        checkBlacklist(storeID, storeToken, new ApplicationAuthorizationListener());
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

    private static void informActivityItIsNotAllowed(ApplicationAuthorizationInterface listeningActivity) {
        ApplicationAuthorization authorization = new ApplicationAuthorization();
        authorization.setStatus(ApplicationAuthorization.Status.REQUEST_ERROR.toString());
        authorization.setMessage(AppaloosaTools.getInstance().activity.getString(R.string.missing_store_params));
        listeningActivity.isNotAllowed(authorization);
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
        String statusString = applicationAuthorization.getStatus();
        try {
            FileOutputStream fOS = AppaloosaTools.getInstance().activity.openFileOutput(BLACKLIST_FILENAME, Context.MODE_PRIVATE);
            fOS.write(statusString.getBytes());
            fOS.close();
        } catch (IOException e) { }
    }

    private static ApplicationAuthorization getBlacklistStatusFromFile() {
        Log.d(LOG_TAG, "readinging blacklist status from file");
        ApplicationAuthorization applicationAuthorization;
        try {
            FileInputStream fIS = AppaloosaTools.getInstance().activity.openFileInput(BLACKLIST_FILENAME);
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
}
