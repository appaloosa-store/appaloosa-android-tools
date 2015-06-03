package appaloosa_store.com.appaloosa_android_tools.tools.services.blacklist;

import android.content.Context;
import android.util.Log;

import com.appaloosa_store.R;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import appaloosa_store.com.appaloosa_android_tools.Appaloosa;
import appaloosa_store.com.appaloosa_android_tools.tools.interfaces.ApplicationAuthorizationInterface;
import appaloosa_store.com.appaloosa_android_tools.tools.models.ApplicationAuthorization;

public class CheckBlacklistService {
    private static final String LOG_TAG = "APPALOOSA_TOOLS";
    private static final String BLACKLIST_FILENAME = "BLACKLIST_STATUS";

    public static void checkBlacklist(final ApplicationAuthorizationInterface authorizationInterface) {
        if(Appaloosa.getStoreId() == null || Appaloosa.getStoreToken() == null) {
            informActivityItIsNotAllowed(authorizationInterface);
        } else {
            Ion.with(Appaloosa.getApplicationContext())
                .load(BlacklistUrlUtils.buildURL())
                .as(new TypeToken<ApplicationAuthorization>() {})
                .setCallback(new ApplicationAuthorizationCallback(authorizationInterface));
        }
    }

    /*
        AUTHORIZATION ------------------------------------------------------------------------------
     */
    private static class ApplicationAuthorizationCallback implements FutureCallback<ApplicationAuthorization> {
        private final ApplicationAuthorizationInterface authorizationInterface;

        public ApplicationAuthorizationCallback(ApplicationAuthorizationInterface authorizationInterface) {
            this.authorizationInterface = authorizationInterface;
        }

        @Override
        public void onCompleted(Exception e, ApplicationAuthorization applicationAuthorization) {
            if (e == null && applicationAuthorization != null) {
                informActivityOfAuthorization(authorizationInterface, applicationAuthorization);
                Appaloosa.setAnalyticsEndpoint(applicationAuthorization.getAnalyticsEndpoint());
                setBlacklistStatusToFile(applicationAuthorization);
            } else {
                ApplicationAuthorization savedApplicationAuthorization = getBlacklistStatusFromFile();
                informActivityOfAuthorization(authorizationInterface, savedApplicationAuthorization);
                Appaloosa.setAnalyticsEndpoint(savedApplicationAuthorization.getAnalyticsEndpoint());
            }
        }
    }

    private static void informActivityItIsNotAllowed(ApplicationAuthorizationInterface authorizationInterface) {
        ApplicationAuthorization authorization = new ApplicationAuthorization();
        authorization.setStatus(ApplicationAuthorization.Status.REQUEST_ERROR.toString());
        authorization.setMessage(Appaloosa.getApplicationContext().getString(R.string.missing_store_params));

        authorizationInterface.isNotAuthorized(authorization);
    }

    private static void informActivityOfAuthorization(ApplicationAuthorizationInterface authorizationInterface,
                                                      ApplicationAuthorization applicationAuthorization) {
        if (applicationAuthorization.isAuthorized()) {
            authorizationInterface.isAuthorized(applicationAuthorization);
            Log.d(LOG_TAG, "device is authorized to launch this app");
        } else {
            authorizationInterface.isNotAuthorized(applicationAuthorization);
            Log.d(LOG_TAG, "device is NOT authorized to launch this app");
        }
    }

    /*
        BLACKLIST FILE MGMT ------------------------------------------------------------------------
     */
    private static void setBlacklistStatusToFile(ApplicationAuthorization applicationAuthorization) {
        String statusString = applicationAuthorization.getStatus();
        String analyticsEndpoint = applicationAuthorization.getAnalyticsEndpoint();
        try {
            FileOutputStream fOS = Appaloosa.getApplicationContext().openFileOutput(BLACKLIST_FILENAME, Context.MODE_PRIVATE);
            BufferedWriter bW = new BufferedWriter(new OutputStreamWriter(fOS));
            bW.write(statusString);
            bW.newLine();
            bW.write(analyticsEndpoint == null ? "null" : analyticsEndpoint);
            bW.close();
            fOS.close();
        } catch (IOException ignored) { }
    }

    private static ApplicationAuthorization getBlacklistStatusFromFile() {
        Log.d(LOG_TAG, "reading blacklist status from file");
        ApplicationAuthorization applicationAuthorization;
        try {
            FileInputStream fIS = Appaloosa.getApplicationContext().openFileInput(BLACKLIST_FILENAME);
            InputStreamReader iSR = new InputStreamReader(fIS);
            BufferedReader bR = new BufferedReader(iSR);
            ApplicationAuthorization.Status status = ApplicationAuthorization.Status.valueOf(bR.readLine());
            String analyticsEndpoint = bR.readLine();
            bR.close();
            iSR.close();
            fIS.close();
            applicationAuthorization = ApplicationAuthorization.getApplicationAuthorizationForStatus(status, analyticsEndpoint);
        } catch (IOException e) {
            applicationAuthorization = ApplicationAuthorization.getApplicationAuthorizationForStatus(ApplicationAuthorization.Status.AUTHORIZED, null);
        }
        return applicationAuthorization;
    }
}
