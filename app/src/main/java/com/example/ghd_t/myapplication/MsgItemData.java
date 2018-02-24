package com.example.ghd_t.myapplication;

import android.graphics.drawable.Drawable;

/**
 * Created by ghd-t on 2018-02-25.
 */

public class MsgItemData {
    private Drawable msg_photo;
    private String msg_name;
    private String msg_content;

    public MsgItemData(Drawable msg_photo, String msg_name, String msg_content) {
        this.msg_photo = msg_photo;
        this.msg_name = msg_name;
        this.msg_content = msg_content;
    }

    public Drawable getMsg_photo() {
        return msg_photo;
    }

    public void setMsg_photo(Drawable msg_photo) {
        this.msg_photo = msg_photo;
    }

    public String getMsg_name() {
        return msg_name;
    }

    public void setMsg_name(String msg_name) {
        this.msg_name = msg_name;
    }

    public String getMsg_content() {
        return msg_content;
    }

    public void setMsg_content(String msg_content) {
        this.msg_content = msg_content;
    }


}
