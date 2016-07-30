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
@IgnoreExtraProperties
public class Message{
//
//    private Location mLocation;
    private String mSender; //id
    private String mTimeSent;
    private String mId;
    private String mContents;
//
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

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }
}
