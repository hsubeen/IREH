package com.example.ghd_t.myapplication;


/**
 * Created by ghd-t on 2018-04-28.
 */

public class WriteClassData {
    String title;
    String contents;
    String person;
    String money_min;
    String money_max;
    String img1;
    String img2;
    String img3;
    String img4;

    public WriteClassData(String title, String contents, String person, String money_min, String money_max, String img1, String img2, String img3, String img4) {
        this.title = title;
        this.contents = contents;
        this.person = person;
        this.money_min = money_min;
        this.money_max = money_max;
        this.img1 = img1;
        this.img2 = img2;
        this.img3 = img3;
        this.img4 = img4;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getMoney_min() {
        return money_min;
    }

    public void setMoney_min(String money_min) {
        this.money_min = money_min;
    }

    public String getMoney_max() {
        return money_max;
    }

    public void setMoney_max(String money_max) {
        this.money_max = money_max;
    }

    public String getImg1() {
        return img1;
    }

    public void setImg1(String img1) {
        this.img1 = img1;
    }

    public String getImg2() {
        return img2;
    }

    public void setImg2(String img2) {
        this.img2 = img2;
    }

    public String getImg3() {
        return img3;
    }

    public void setImg3(String img3) {
        this.img3 = img3;
    }

    public String getImg4() {
        return img4;
    }

    public void setImg4(String img4) {
        this.img4 = img4;
    }
}
