# Appaloosa Android Tools

Add authorization, analytics and auto-update features to your apps hosted on Appaloosa Store https://www.appaloosa-store.com

## Prerequisites

> - Android 4.0.3+ (API lvl 15)
> - An Enterprise account on [Appaloosa Store](https://www.appaloosa-store.com)
> - A native store must have been created for your account
> - At least one login on the native store

## Installation

### Gradle
Just add at your app level **build.gradle**
```
compile('com.appaloosa-store:appaloosa-android-tools:VERSION_NUMBER@aar') {
        transitive=true
}
```
The latest stable release is 0.3.3.

### Maven
Soon

## Usage

### Initialization
In order to work, this library requires your app to have the following permission in the **AndroidManifest.xml**

```
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.READ_PHONE_STATE" />
```

Then, add the following line at the ***start of your application***. In your subclass of application for instance or the onCreate method of your first subclass of Activity. ***This method has to be called before any other one in the SDK.***
```
Appaloosa.init(INSTANCE_OF_YOUR_APPLICATION, YOUR_APPALOOSA_STORE_ID, YOUR_APPALOOSA_STORE_TOKEN);
```
You can retrieve the instance of your application in your activity with this method :
```
this.getApplication();
```
To retrieve your Appaloosa store id and token, go to the Appaloosa web admin, then click on your store's name in the menu on the top right corner. Then, click on "settings" in the dropdown menu. At the bottom of the page, you'll find your store id and token.


### Permissions
Since Android M, it is required for the developer to ask for sensitive permissions at runtime.
Appaloosa uses the device's IMEI as an identifier (if available) ; so, if your app targets Android Marshmallow (API 23) or above, you have to ensure that the permission `android.permission.READ_PHONE_STATE` is allowed _before_ using the SDK blacklisting, analytics or auto-update capabilities. If the permssion is denied we advise to forbid access to the app.

For UI flow tips and reference on runtime permissions, see [Google's Android developper training article](https://developer.android.com/training/permissions/requesting.html), or [Codepath's tutorial](http://guides.codepath.com/android/Understanding-App-Permissions).


### Authorization
This library provides an app authorization mechanism. Via our web admin on https://www.appaloosa-store.com, you can manage a per device access. It works by sending device information to the Appaloosa servers. In case of an offline access to your app, the status is read from a protected file on the device.

The next thing to do is to initialize the check in one of your app activities (in most cases the first one).

```
Appaloosa.checkBlacklist(INSTANCE_OF_THE_CURRENT_ACTIVITY);
```

The INSTANCE_OF_THE_CURRENT_ACTIVITY should be an Object extending Activity and implementing the ApplicationAuthorizationInterface we provide. The two methods to implement allow you to customize the behaviour as explained below.
Now launch your app. The check is done asynchronously and no UI is displayed to the user. ***If the device is not authorized, it is the developer's responsibility to kill the app in the ```isNotAuthorized``` method.*** We provide a method ```closeApplication(Activity)``` to close your application.

#### Customizing the behaviour
In case you want to customize the authorization behaviour, you only need to override the isAuthorized(ApplicationAuthorization) and isNotAuthorized(ApplicationAuthorization) methods.

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

### Analytics

This library allows you to gather intel on the way people use your app. Various user events will be recorded and sent asynchronously to our servers. The graphs will be displayed in the admin view. Note that statistics may appear with delay.

To record analytics on your app usage, simply add the following line at the start of your application.
```
Appaloosa.startAnalytics();
```
*A prerequisite for the recording of analytics is to check if the user is blacklisted (see Authorization paragraph above). The recording of events starts as soon as the checkBlacklist is done.*

### Auto-Update
This library allows you to encourage updates by forcing the download of the new update when the application starts. Simply add the following line to your code :
```
Appaloosa.autoUpdate(INSTANCE_OF_THE_CURRENT_ACTIVITY);
```

The INSTANCE_OF_THE_CURRENT_ACTIVITY should be an object extending Activity. With this method, the SDK checks for an update and, if available, starts downloading it immediately. If the user is still on the Activity provided, a dialog appears showing the loading progress.
Once downloaded, the SDK launches the Android installation process but the user can still cancel it.

#### Customization
If your prefer to leave the choice to the user to download or not the update, the following method will suit your needs :
```
Appaloosa.autoUpdateWithMessage(INSTANCE_OF_THE_CURRENT_ACTIVITY, TITLE_OF_DIALOG, MESSAGE_OF_DIALOG);
```

This method will display a confirm dialog before download with the title and message provided. The user will have the choice to cancel or approve. The rest of the process is the same as the first method.

## License

  The MIT License (MIT)

  Copyright (c) 2015 Appaloosa Technology

  Permission is hereby granted, free of charge, to any person obtaining a copy
  of this software and associated documentation files (the "Software"), to deal
  in the Software without restriction, including without limitation the rights
  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  copies of the Software, and to permit persons to whom the Software is
  furnished to do so, subject to the following conditions:

  The above copyright notice and this permission notice shall be included in all
  copies or substantial portions of the Software.

  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
  SOFTWARE.
