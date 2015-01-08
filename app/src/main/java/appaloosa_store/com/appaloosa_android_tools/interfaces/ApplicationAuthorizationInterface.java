package appaloosa_store.com.appaloosa_android_tools.interfaces;

import appaloosa_store.com.appaloosa_android_tools.models.ApplicationAuthorization;

public interface ApplicationAuthorizationInterface {
    public void isAllowed(ApplicationAuthorization authorization);
    public void isNotAllowed(ApplicationAuthorization authorization);
}
