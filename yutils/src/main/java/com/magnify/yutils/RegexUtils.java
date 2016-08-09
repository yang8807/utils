package com.magnify.yutils;

import java.util.regex.Pattern;

/**
 * Created by  on 2016/8/2.
 */
public class RegexUtils {
    //手机号码
    public final static String REGEX_PHONE_NUMBER = "^(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$";
    //电子邮箱
    public final static String REGEX_EMAIL = "[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?";
    //网址
    public final static String REGEX_URL = "[a-zA-z]+://[^\\s]*";
    //身份证号码
    public final static String REGEX_USER_ID_NUMBER = "^(\\d{6})(\\d{4})(\\d{2})(\\d{2})(\\d{3})([0-9]|X)$";
    //中文
    public final static String REGEX_IS_CHINESE = "[\\u4e00-\\u9fa5]";
    //双字符,包括中文
    public final static String REGEX_DBCS = "[^\\x00-\\xff]";

    public static boolean isMobileNumber(String mobiles) {
        return isMatch(REGEX_PHONE_NUMBER, mobiles);
    }

    public static boolean isEmail(String emial) {
        return isMatch(REGEX_EMAIL, emial);
    }

    public static boolean isURL(String url) {
        return isMatch(REGEX_URL, url);
    }

    public static boolean isIDNumber(String id) {
        return isMatch(REGEX_USER_ID_NUMBER, id);
    }

    public static boolean isChinese(String chinese) {
        return isMatch(REGEX_DBCS, chinese);
    }

    private static boolean isMatch(String regex, String text) {
        return Pattern.compile(regex).matcher(text).matches();
    }

}
