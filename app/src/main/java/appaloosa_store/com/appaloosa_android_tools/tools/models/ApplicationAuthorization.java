package appaloosa_store.com.appaloosa_android_tools.tools.models;

import com.appaloosa_store.R;

import appaloosa_store.com.appaloosa_android_tools.tools.AppaloosaTools;

public class ApplicationAuthorization {
    public static enum Status {
        UNKNOWN_APPLICATION,
        AUTHORIZED,
        UNREGISTERED_DEVICE,
        UNKNOWN_DEVICE,
        NOT_AUTHORIZED,
        DEVICE_ID_FORMAT_ERROR,
        NO_NETWORK,
        REQUEST_ERROR,
        UNKNOWN
    }

    private String status;
    private String message;
    private String analyticsEndpoint;

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getAnalyticsEndpoint() {
        return analyticsEndpoint;
    }

    public boolean isAuthorized() {
        try {
            return ApplicationAuthorization.Status.valueOf(status).equals(ApplicationAuthorization.Status.AUTHORIZED);
        } catch(NullPointerException e) {
            didNotRetreiveStatusFromAppaloosa();
            return false;
        }
    }

    private void didNotRetreiveStatusFromAppaloosa() {
        status = "UNKNOWN";
        message = AppaloosaTools.getInstance().activity.getResources().getString(R.string.unknown_status_message);
    }

    public static ApplicationAuthorization getApplicationAuthorizationForStatus(Status status) {
        ApplicationAuthorization applicationAuthorization = new ApplicationAuthorization();
        applicationAuthorization.setStatus(status.toString());
        if(status == Status.NOT_AUTHORIZED) {
            applicationAuthorization.setMessage(AppaloosaTools.getInstance().activity.getString(R.string.not_authorized_message));
        }
        return applicationAuthorization;
    }
}
