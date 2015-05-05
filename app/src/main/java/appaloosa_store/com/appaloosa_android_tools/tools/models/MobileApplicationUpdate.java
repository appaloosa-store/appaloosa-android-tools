package appaloosa_store.com.appaloosa_android_tools.tools.models;

import com.google.gson.annotations.SerializedName;

public class MobileApplicationUpdate {

    @SerializedName("id")
    private int mobileApplicationUpdateId;

    @SerializedName("version")
    private int versionCode;

    @SerializedName("binary_size")
    private int binarySize;

    public int getMobileApplicationUpdateId() {
        return mobileApplicationUpdateId;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public int getBinarySize() {
        return binarySize;
    }
}
