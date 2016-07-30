package com.phooodstudio.phooodtalk.model;

import java.io.Serializable;

/**
 * Created by Christopher Cabreros on 27-Jun-16.
 */
public class Account implements Serializable{
//TODO: upon full implementation, implement as parcelable

    private String mId;
    private long mLastLogin;
    private String mName;
    private String mEmail;
    private String mGender;
    private String mBirthday;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public long getLastLogin() {
        return mLastLogin;
    }

    public void setLastLogin(long lastLogin) {
        mLastLogin = lastLogin;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getGender() {
        return mGender;
    }

    public void setGender(String gender) {
        mGender = gender;
    }

    public String getBirthday() {
        return mBirthday;
    }

    public void setBirthday(String birthday) {
        mBirthday = birthday;
    }
}
