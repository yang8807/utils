package com.magnify.utils.bean;

import com.example.datautils.User;

import java.io.Serializable;
import java.util.Random;

/**
 * Created by heinigger on 16/7/30.
 */
public class People implements Serializable{
    /*姓名*/
    private String userName;
    /*年龄*/
    private int age;
    /*昵称*/
    private String nickNamee;
    /*性别*/
    private String sex;
    /*用户头像*/
    private String avators;
    private static long YEAR = 3600 * 24 * 365 * 1000;
    private String phone;

    public People(User randomUser) {
        this.userName = randomUser.getUserName();
        this.age = randAge((int) (randomUser.getBirthdayTimeStamp() / YEAR));
        this.sex = randomUser.getSex();
        this.avators = randomUser.getImageAvator();
        this.phone = randomUser.getPhone();
    }

    private int randAge(int i) {
        if (i > 100) return randAge(new Random().nextInt(i));
        return i;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getNickNamee() {
        return nickNamee;
    }

    public void setNickNamee(String nickNamee) {
        this.nickNamee = nickNamee;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public People() {
    }

    public People(String userName, int age, String nickNamee, String sex) {
        this.userName = userName;
        this.age = age;
        this.nickNamee = nickNamee;
        this.sex = sex;
    }

    public String getAvators() {
        return avators;
    }

    public void setAvators(String avators) {
        this.avators = avators;
    }

}
