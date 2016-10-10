package appaloosa_store.com.appaloosa_android_tools.utils;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.ShadowTelephonyManager;

import appaloosa_store.com.appaloosa_android_tools.Appaloosa;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 21)
public class DeviceUtilsTest {
    private static final String ANDROID_ID = "android_id";
    private static final String IMEI = "imei";

    private Context mContext;
    private ShadowApplication mShadowApplication;
    private ShadowTelephonyManager mShadowTelephonyManager;

    @Before
    public void setUp() {
        Application application = RuntimeEnvironment.application;
        mContext = application;
        Appaloosa.init(RuntimeEnvironment.application, 1, "");
        mShadowApplication = Shadows.shadowOf(application);
        mShadowTelephonyManager = Shadows.shadowOf(
                (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE)
        );
    }

    @Test
    public void getDeviceId_deviceNotPhone() {
        mShadowTelephonyManager.setPhoneType(TelephonyManager.PHONE_TYPE_NONE);
        Settings.Secure.putString(mContext.getContentResolver(),
                Settings.Secure.ANDROID_ID, ANDROID_ID);

        assertEquals(ANDROID_ID, DeviceUtils.getDeviceID());
    }

    @Test
    public void getDeviceId_devicePhone_permissionDenied() {
        mShadowTelephonyManager.setPhoneType(TelephonyManager.PHONE_TYPE_GSM);
        mShadowApplication.denyPermissions(Manifest.permission.READ_PHONE_STATE);

        assertNull(DeviceUtils.getDeviceID());
    }

    @Test
    public void getDeviceId_devicePhone_permissionAuthorized() {
        mShadowTelephonyManager.setPhoneType(TelephonyManager.PHONE_TYPE_GSM);
        mShadowTelephonyManager.setDeviceId(IMEI);
        mShadowApplication.grantPermissions(Manifest.permission.READ_PHONE_STATE);

        assertEquals(IMEI, DeviceUtils.getDeviceID());
    }
}