package appaloosa_store.com.appaloosa_android_tools.models;

import com.appaloosa_store.R;

import appaloosa_store.com.appaloosa_android_tools.AppaloosaTools;

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

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public boolean isAuthorized() {
        return ApplicationAuthorization.Status.valueOf(status).equals(ApplicationAuthorization.Status.AUTHORIZED);
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
