package com.example.ghd_t.myapplication;

import android.graphics.drawable.Drawable;

/**
 * Created by ghd-t on 2018-04-23.
 */

public class UserData {
    private String userName;
    private String nickName;
    private Drawable profile;

    public UserData(String userName, String nickName) {
        this.userName = userName;
        this.nickName = nickName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Drawable getProfile() {
        return profile;
    }

    public void setProfile(Drawable profile) {
        this.profile = profile;
    }
}
