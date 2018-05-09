package com.example.ghd_t.myapplication;

/**
 * Created by ghd-t on 2018-05-10.
 */

public class Messages {
    private String userId;
    private String contents;

    public Messages(String userId, String contents) {
        this.userId = userId;
        this.contents = contents;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

}
