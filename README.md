# Appaloosa Android Tools

Add authorization and auto-update features to your apps hosted on Appaloosa Store https://www.appaloosa-store.com

## Prerequisites

> - Android 4.0.3+ (API lvl 15)
> - An account on https://www.appaloosa-store.com
> - A native store must have been created for your account

## Installation

### Gradle
Just add at your app level **build.gradle**
```
compile 'com.appaloosa-store:appaloosa-android-tools:+@aar'
```

### Maven
Soon

## Usage

### Authorization
This library provides an app authorization mechanism. Via our web admin on https://www.appaloosa-store.com, you can manage a per device access. It works by sending device information to the Appaloosa servers. In case of an offline access to your app, the status is read from a protected file on the device.

In order to work, this library requires your app to have the following permission in the **AndroidManifest.xml**

```
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.READ_PHONE_STATE" />
```

The next thing to do is to initialize the check in one of your app activities (in most cases the first one).

```
AppaloosaTools.getInstance().checkBlacklist(YOUR_APPALOOSA_STORE_ID, YOUR_APPALOOSA_STORE_TOKEN, INSTANCE_OF_THE_CURRENT_ACTIVITY);
```

To retrieve your Appaloosa store id and token, go to the Appaloosa web admin, then click on your store's name in the menu on the top right corner. Then, click on "settings" in the dropdown menu. At the bottom of the page, you'll find your store id and token.

Now launch your app. The check is done asynchronously and no UI is displayed to the user. In case the device is not authorized, a dialog is shown. When the user touches "OK", the app is terminated.

#### Customizing the behaviour
In case you want to customize the authorization behaviour, you have to do two things:

> - Make the activity where the check is done extend ApplicationAuthorizationActivity
> - Override isAuthorized(ApplicationAuthorization) and isNotAuthorized(ApplicationAuthorization)

ApplicationAuthorization has the following statuses enum corresponding to all the authorization cases: 
```
public static enum Status {
        UNKNOWN_APPLICATION,
        AUTHORIZED,
        UNREGISTERED_DEVICE,
        UNKNOWN_DEVICE,
        NOT_AUTHORIZED,
        DEVICE_ID_FORMAT_ERROR,
        NO_NETWORK,
        REQUEST_ERROR,
        UNKNOWN
}    
```
