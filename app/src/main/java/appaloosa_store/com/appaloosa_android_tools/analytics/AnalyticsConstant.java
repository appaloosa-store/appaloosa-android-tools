package appaloosa_store.com.appaloosa_android_tools.analytics;

public class AnalyticsConstant {

    private final static boolean PRODUCTION = false;
    public static final String API_SERVER_BASE_URL;
    public static final int ANALYTICS_DB_BATCH_SIZE;
    static {
        if (PRODUCTION) {
            //TODO Change endpoint once server application is online.
            API_SERVER_BASE_URL = "https://appaloosa-int.herokuapp.com/api/v1/";
            ANALYTICS_DB_BATCH_SIZE = 100;
        } else {
            API_SERVER_BASE_URL = "http://10.42.13.99:3000/";
            ANALYTICS_DB_BATCH_SIZE = 10;
        }
    }
}
