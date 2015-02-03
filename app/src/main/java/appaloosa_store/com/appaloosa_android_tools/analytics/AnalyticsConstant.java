package appaloosa_store.com.appaloosa_android_tools.analytics;

public class AnalyticsConstant {

    private final static boolean PRODUCTION = false;
    public static final String API_SERVER_BASE_URL;
    public static final int ANALYTICS_DB_BATCH_SIZE;
    static {
        if (PRODUCTION) {
            API_SERVER_BASE_URL = "https://appaloosa-int.herokuapp.com/api/v1/";
            ANALYTICS_DB_BATCH_SIZE = 100;
        } else {
            API_SERVER_BASE_URL = "http://10.42.13.95:3000/";
            ANALYTICS_DB_BATCH_SIZE = 5;
        }
    }

    public static String ANALYTICS_DB_BATCH_SIZE_REACHED = "analytics_db_batch_size_reached";

}
