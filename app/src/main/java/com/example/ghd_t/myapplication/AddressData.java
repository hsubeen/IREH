package com.example.ghd_t.myapplication;

import android.app.Application;

/**
 * Created by ghd-t on 2018-02-26.
 */

public class AddressData {
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    private static AddressData instance = null;

    public static synchronized AddressData getInstance(){
        if(null==instance){
            instance = new AddressData();
        }
        return instance;
        }

}
