package com.example.datautils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 用于制造数据的类
 */
public class User {
    //用户名称
    private String userName;
    //性别
    private String sex;
    //是外国人吗
    private boolean isForegein;
    //用户的头像
    private String imageAvator;
    //用户的生日的时间戳
    private long birthdayTimeStamp;
    private String phone;
    private String address;
    private String sortKey;

    public String getSortKey() {
        this.sortKey = HanziToPinyin.getSortKey(userName).toUpperCase();
        return sortKey;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setBirthdayTimeStamp(long birthdayTimeStamp) {
        this.birthdayTimeStamp = birthdayTimeStamp;
    }

    public boolean isForegein() {
        return isForegein;
    }

    public long getBirthdayTimeStamp() {
        return birthdayTimeStamp;
    }

    public void setForegein(boolean foregein) {
        isForegein = foregein;
    }

    public User(boolean sex, boolean isForegein) {
        this.sex = sex ? "男" : "女";
        this.isForegein = isForegein;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public boolean getIsForegein() {
        return isForegein;
    }

    public void setIsForegein(boolean isForegein) {
        this.isForegein = isForegein;
    }

    public String getImageAvator() {
        return imageAvator;
    }

    public void setImageAvator(String imageAvator) {
        this.imageAvator = imageAvator;
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", sex='" + sex + '\'' +
                ", isForegein=" + isForegein +
                ", imageAvator='" + imageAvator + '\'' +
                ", birthdayTimeStamp=" + time2Date(birthdayTimeStamp) +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

    public String time2Date(long birthdayTimeStamp) {
        Date date = new Date(birthdayTimeStamp);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }
}
