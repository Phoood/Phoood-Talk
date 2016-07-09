package com.phooodstudio.phooodtalk.model;

import android.graphics.Bitmap;
import android.location.Location;

import java.sql.Time;

/**
 * Created by Christopher Cabreros on 27-Jun-16.
 * Defines a singular message to be sent
 */
public class Message {

    private Location mLocation;
    private Account mSender;
    private Time mTimeSent;

    private Object mContents;

    public Location getLocation() {
        return mLocation;
    }

    public void setLocation(Location location) {
        mLocation = location;
    }

    public Account getSender() {
        return mSender;
    }

    public void setSender(Account sender) {
        mSender = sender;
    }

    public Time getTimeSent() {
        return mTimeSent;
    }

    public void setTimeSent(Time timeSent) {
        mTimeSent = timeSent;
    }

    public Object getContents() {
        return mContents;
    }

    public void setContents(Object contents) {
        mContents = contents;
    }
}
