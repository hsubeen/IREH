package com.example.ghd_t.myapplication;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

/**
 * Created by ghd-t on 2018-02-24.
 */

public class BrandListItemData {

    private Bitmap icon;
    private String brand_title;
    private String brand_areaname;
    private String brand_info;
    private String brand_priceinfo_min;
    private String brand_priceinfo_max;
    private String index;



    public BrandListItemData(Bitmap icon, String brand_title, String brand_areaname, String brand_info, String brand_priceinfo_min, String brand_priceinfo_max,String index) {
        this.icon = icon;
        this.brand_title = brand_title;
        this.brand_areaname = brand_areaname;
        this.brand_info = brand_info;
        this.brand_priceinfo_min = brand_priceinfo_min;
        this.brand_priceinfo_max = brand_priceinfo_max;
        this.index = index;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public void setIcon(Bitmap icon) {
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

    public String getBrand_priceinfo_min() {
        return brand_priceinfo_min;
    }

    public void setBrand_priceinfo_min(String brand_priceinfo_min) {
        this.brand_priceinfo_min = brand_priceinfo_min;
    }

    public String getBrand_priceinfo_max() {
        return brand_priceinfo_max;
    }

    public void setBrand_priceinfo_max(String brand_priceinfo_max) {
        this.brand_priceinfo_max = brand_priceinfo_max;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }
}
