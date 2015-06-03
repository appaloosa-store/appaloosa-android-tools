package appaloosa_store.com.appaloosa_android_tools.tools.interfaces;


import android.app.Activity;
import android.os.Bundle;

public class CloseAndCleanActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        finish();
    }
}
