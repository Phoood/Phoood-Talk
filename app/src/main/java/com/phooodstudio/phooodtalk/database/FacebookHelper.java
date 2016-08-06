package com.phooodstudio.phooodtalk.database;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Chris on 05-Aug-16.
 */
public class FacebookHelper {

    public static final String TAG = "FacebookHelper";

    public static Bitmap getFacebookProfilePicture(String userID) throws IOException {
        URL imageURL = new URL("https://graph.facebook.com/" + userID + "/picture?type=large");

        Log.d(TAG, "Receiving profile picture from " + userID);

        return BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
    }
}
