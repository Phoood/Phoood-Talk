package com.phooodstudio.phooodtalk;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FacebookAuthCredential;
import com.phooodstudio.phooodtalk.model.Account;

/**
 * Created by Chris on 12-Jul-16.
 * Application class
 */
public class PhooodTalkApp extends Application {

    private static final String TAG = "PhooodTalkApp";

    private FirebaseAnalytics mFirebaseAnalytics;
    private Account mCurrentAccount;

    @Override
    public void onCreate() {
        super.onCreate();

        //Initialize Facebook Sdk
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        //Initialize Firebase
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }

    public Account getCurrentAccount() {
        return mCurrentAccount;
    }

    public void setCurrentAccount(Account currentAccount) {
        mCurrentAccount = currentAccount;
    }
}
