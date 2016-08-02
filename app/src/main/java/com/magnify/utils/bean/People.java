package com.magnify.utils.bean;

/**
 * Created by heinigger on 16/7/30.
 */
public class People {
    private String userName;
    private int age;
    private String nickNamee;
    private String sex;

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

    @Override
    public String toString() {
        return "People{" +
                "userName='" + userName + '\'' +
                ", age=" + age +
                ", nickNamee='" + nickNamee + '\'' +
                ", sex='" + sex + '\'' +
                '}';
    }
}
