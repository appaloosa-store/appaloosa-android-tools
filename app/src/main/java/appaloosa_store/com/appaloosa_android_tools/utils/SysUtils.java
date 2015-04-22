package appaloosa_store.com.appaloosa_android_tools.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.appaloosa_store.R;

import appaloosa_store.com.appaloosa_android_tools.Appaloosa;

public class SysUtils {
    public static final String DEVICE_PLATFORM = "android";
    private static final int VERSION_CODE_PACKAGE_NOT_FOUND = -1;

    public static String getApplicationPackage() {
        return Appaloosa.getApplicationContext().getPackageName();
    }

    public static int getApplicationVersionCode() {
        Context context = Appaloosa.getApplicationContext();
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            return VERSION_CODE_PACKAGE_NOT_FOUND;
        }
    }

    public static String getSDKVersionCode() {
        return Appaloosa.getApplicationContext().getResources().getString(R.string.version_code);
    }

    public static String getSDKVersionName() {
        return Appaloosa.getApplicationContext().getResources().getString(R.string.version_name);
    }
}
