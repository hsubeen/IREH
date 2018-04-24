package com.example.ghd_t.myapplication;

/**
 * Created by ghd-t on 2018-04-24.
 */

public class BrandAuthData {
    String brandname;
    String weburl;
    String phone;
    String field;
    String address;
    public BrandAuthData(String brandname, String weburl, String phone, String field, String address) {
        this.brandname = brandname;
        this.weburl = weburl;
        this.phone = phone;
        this.field = field;
        this.address = address;
    }

    public String getBrandname() {
        return brandname;
    }

    public void setBrandname(String brandname) {
        this.brandname = brandname;
    }

    public String getWeburl() {
        return weburl;
    }

    public void setWeburl(String weburl) {
        this.weburl = weburl;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


}

