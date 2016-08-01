package com.phooodstudio.phooodtalk.model;

import android.graphics.Bitmap;
import android.location.Location;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.sql.Time;

/**
 * Created by Christopher Cabreros on 27-Jun-16.
 * Defines a singular message to be sent
 */
public class Message {

    // number of types
    public static final int TYPES = 3;
    public static final int TYPE_STRING = 0;
    public static final int TYPE_IMAGE = 1;
    public static final int TYPE_RATING = 2;

//    private Location mLocation;
    private String mSender; //id
    private String mTimeSent;
    private String mContents;

    private int mType;

//    public Location getLocation() {
//        return mLocation;
//    }
//
//    public void setLocation(Location location) {
//        mLocation = location;
//    }

    public String getSender() {
        return mSender;
    }

    public void setSender(String sender) {
        mSender = sender;
    }

    public String getTimeSent() {
        return mTimeSent;
    }

    public void setTimeSent(String timeSent) {
        mTimeSent = timeSent;
    }

    public String getContents() {
        return mContents;
    }

    public void setContents(String contents) {
        mContents = contents;
    }

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        mType = type;
    }
}
