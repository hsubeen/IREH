package com.example.ghd_t.myapplication;

/**
 * Created by ghd-t on 2018-05-10.
 */

public class ChatData {
    private String contents;
    private String userId;

    public ChatData(String contents, String userId) {
        this.contents = contents;
        this.userId = userId;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
