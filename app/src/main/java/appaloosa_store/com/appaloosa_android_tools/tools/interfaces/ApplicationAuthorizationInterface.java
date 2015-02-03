package appaloosa_store.com.appaloosa_android_tools.tools.interfaces;

import appaloosa_store.com.appaloosa_android_tools.tools.models.ApplicationAuthorization;

public interface ApplicationAuthorizationInterface {
    public void isAuthorized(ApplicationAuthorization authorization);
    public void isNotAuthorized(ApplicationAuthorization authorization);
}
