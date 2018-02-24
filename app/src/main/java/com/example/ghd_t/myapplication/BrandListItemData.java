package com.example.ghd_t.myapplication;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/**
 * Created by ghd-t on 2018-02-24.
 */

public class BrandListItemData {

    private Drawable icon;
    private String brand_title;
    private String brand_areaname;
    private String brand_info;
    private String brand_priceinfo;

    public BrandListItemData(Drawable icon, String brand_title, String brand_areaname, String brand_info, String brand_priceinfo) {
        this.icon = icon;
        this.brand_title = brand_title;
        this.brand_areaname = brand_areaname;
        this.brand_info = brand_info;
        this.brand_priceinfo = brand_priceinfo;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getBrand_title() {
        return brand_title;
    }

    public void setBrand_title(String brand_title) {
        this.brand_title = brand_title;
    }

    public String getBrand_areaname() {
        return brand_areaname;
    }

    public void setBrand_areaname(String brand_areaname) {
        this.brand_areaname = brand_areaname;
    }

    public String getBrand_fieldname() {
        return brand_info;
    }

    public void setBrand_fieldname(String brand_fieldname) {
        this.brand_info = brand_fieldname;
    }

    public String getBrand_priceinfo() {
        return brand_priceinfo;
    }

    public void setBrand_priceinfo(String brand_priceinfo) {
        this.brand_priceinfo = brand_priceinfo;
    }
}
