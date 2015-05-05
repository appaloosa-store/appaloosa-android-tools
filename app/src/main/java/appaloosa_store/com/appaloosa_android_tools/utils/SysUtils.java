package appaloosa_store.com.appaloosa_android_tools.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;

import com.appaloosa_store.R;

import java.io.File;

import appaloosa_store.com.appaloosa_android_tools.Appaloosa;

public class SysUtils {
    public static final String DEVICE_PLATFORM = "android";
    private static final int VERSION_CODE_PACKAGE_NOT_FOUND = -1;
    private static final String LOCAL_APKS_PATH = "/appaloosa/apks/";
    private static final String APK_FILE_NAME = "binary_%1$s.apk";

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

    public static File getAPKFile(String apkId) {
        String APKFolder = Environment.getExternalStorageDirectory() + LOCAL_APKS_PATH;
        File APKFolderFile = new File(APKFolder);
        if(!APKFolderFile.mkdirs()) {
            deleteExistingFiles(APKFolderFile);
        }
        return new File(APKFolderFile, String.format(APK_FILE_NAME, apkId));
    }

    public static void deleteExistingFiles(File Folder) {
        for(File f : Folder.listFiles()) {
            f.delete();
        }
    }

    public static void installAPK(File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Appaloosa.getApplicationContext().startActivity(intent);
    }
}
