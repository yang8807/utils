package com.example.datautils;

import android.content.Context;

import java.util.Random;

/**
 * 随机创建任务的生日,姓名,nickname
 */
public class RandomUserUtil {
    private static Random random = new Random();
    private static String[] lastNames;
    private static String[] boy_china;
    private static String[] girl_china;
    private static String[] boy_foreign;
    private static String[] girl_foreign;
    private static String[] urls;
    private static String[] addresses;
    private static long DAY = 1000 * 24 * 3600;
    private static long MONTH = DAY * 30;
    private static long YEAR = DAY * 365;
    private static RandomUserUtil randomUserUtil;

    public RandomUserUtil(Context context) {
        lastNames = context.getString(R.string.user_frist_name).split(",");
        boy_china = context.getString(R.string.boy_name).split(",");
        girl_china = context.getString(R.string.girl_name).split(",");
        boy_foreign = context.getString(R.string.boy_engilish_name).split(",");
        girl_foreign = context.getString(R.string.girl_engilish_name).split(",");
        urls = context.getString(R.string.image_url).split(",");
        addresses = context.getString(R.string.road).split(",");
    }

    public static RandomUserUtil init(Context context) {
        if (randomUserUtil == null) randomUserUtil = new RandomUserUtil(context);
        return randomUserUtil;
    }

    public static User createRandomUser() {
        User user = null;
        boolean sex = random.nextInt(1000) % 2 == 0;//随机生成一个性别
        //是不是外国人
        if (random.nextInt(1000) % 2 == 0) {//创建一个外国人
            user = createForeginer(sex);
        } else {//创建一个中国
            user = createChinese(sex, lastNames[random.nextInt(lastNames.length)]);
        }
        return user;
    }

    /**
     * 创建一个中国人
     *
     * @param sex;true为男生,false为女生
     */
    private static User createChinese(boolean sex, String lastName) {
        User user = new User(sex, false);
        user.setImageAvator(urls[random.nextInt(urls.length)]);
        //生成姓氏+生成的男生或者女生的名字
        user.setUserName(lastName + (sex ? boy_china[random.nextInt(boy_china.length)] : girl_china[random.nextInt(boy_foreign.length)]));
        user.setBirthdayTimeStamp(randomTimeStamp());
        user.setAddress(addresses[random.nextInt(addresses.length)] + random.nextInt(200) + "号");
        user.setPhone(createPhoneNumber());
        return user;
    }

    /**
     * 随机生成一个中国人
     */
    public User createRandomChinese() {
        return createChinese(random.nextInt(1000) % 2 == 0, lastNames[random.nextInt(lastNames.length)]);
    }

    /**
     * 根据姓氏生成一个人
     */
    public User createRandomChinese(String lastName) {
        return createChinese(random.nextInt(1000) % 2 == 0, lastName);
    }

    /**
     * 随机生成一个外国人
     */
    public User createRandomForeginer() {
        return createForeginer(random.nextInt(1000) % 2 == 0);
    }

    /**
     * 创建一个外国人
     *
     * @param sex;true为男生,false为女生
     */
    private static User createForeginer(boolean sex) {
        User user = new User(sex, true);
        user.setImageAvator(urls[random.nextInt(urls.length)]);
        user.setUserName(sex ? boy_foreign[random.nextInt(boy_foreign.length)] : girl_foreign[random.nextInt(girl_foreign.length)]);
        user.setBirthdayTimeStamp(randomTimeStamp());
        user.setAddress(addresses[random.nextInt(addresses.length)]);
        user.setPhone(createPhoneNumber());
        return user;
    }

    //五十岁的人还活着,最大的人五十岁
    public static long randomTimeStamp() {
        return (random.nextInt(51) * YEAR + random.nextInt(13) * MONTH + random.nextInt(30) * DAY + random.nextInt(24) * 3600 * 1000);
    }

    /***
     * 随机生成11位的手机号码
     */
    public static String createPhoneNumber() {
        /*/^(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$/*/
        String[] phoneStart = new String[]{"13", "15", "17", "18", "14"};
        int type = random.nextInt(5);
        String phone = phoneStart[type];
        switch (type) {
            case 0://13
                phone = phone + getRandomNumber("0123456789", 1) + getRandomNumber("0123456789", 8);
                break;
            case 1://15
                phone = phone + getRandomNumber("012356789", 1) + getRandomNumber("0123456789", 8);
                break;
            case 2://17
                phone = phone + getRandomNumber("678", 1) + getRandomNumber("0123456789", 8);
                break;
            case 3://18
                phone = phone + getRandomNumber("0123456789", 1) + getRandomNumber("0123456789", 8);
                break;
            case 4://14
                phone = phone + getRandomNumber("57", 1) + getRandomNumber("0123456789", 8);
                break;
        }
        return phone;
    }

    /***
     * 根据给定的数字数,随机生成字符串
     */
    private static String getRandomNumber(String digital, int needCount) {
        char[] phone = digital.toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < needCount; i++) {
            stringBuilder.append(phone[random.nextInt(phone.length)]);
        }
        return stringBuilder.toString();
    }

    /***
     * 判断是不是闰年
     */
    private boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    public static String getRandomImage() {
        if (urls == null || urls.length == 0) return "";
        return urls[random.nextInt(urls.length)];
    }


}
