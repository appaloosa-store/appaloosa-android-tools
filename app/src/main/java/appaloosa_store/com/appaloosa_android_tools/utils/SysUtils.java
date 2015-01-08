package appaloosa_store.com.appaloosa_android_tools.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import appaloosa_store.com.appaloosa_android_tools.AppaloosaTools;

public class SysUtils {
    public static String getApplicationPackage() {
        Context context = AppaloosaTools.context;
        return context.getPackageName();
    }

    public static int getApplicationVersionCode() {
        Context context = AppaloosaTools.context;
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            return -1;
        }
    }
}
