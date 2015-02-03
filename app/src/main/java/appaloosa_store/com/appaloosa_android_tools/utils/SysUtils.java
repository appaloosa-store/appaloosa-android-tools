package appaloosa_store.com.appaloosa_android_tools.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import appaloosa_store.com.appaloosa_android_tools.tools.AppaloosaTools;

public class SysUtils {
    private static final int VERSION_CODE_PACKAGE_NOT_FOUND = -1;

    public static String getApplicationPackage() {
        return AppaloosaTools.getInstance().activity.getPackageName();
    }

    public static int getApplicationVersionCode() {
        Context context = AppaloosaTools.getInstance().activity.getBaseContext();
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            return VERSION_CODE_PACKAGE_NOT_FOUND;
        }
    }
}
