package com.phooodstudio.phooodtalk;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

/**
 * Created by Chris on 12-Jul-16.
 * Application class
 */
public class PhooodTalkApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //Initialize Facebook Sdk
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }


}
