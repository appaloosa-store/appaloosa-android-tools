package appaloosa_store.com.appaloosa_android_tools.tools.interfaces;

import appaloosa_store.com.appaloosa_android_tools.tools.models.ApplicationAuthorization;

public interface ApplicationAuthorizationInterface {
    void isAuthorized(ApplicationAuthorization authorization);
    void isNotAuthorized(ApplicationAuthorization authorization);
}
