package com.magnify.util;

import java.io.UnsupportedEncodingException;
import java.util.Random;

/**
 * Created by 黄艳武 on 2016/5/17.
 */
public class RandomCharUtils {
    /**
     * 生成随机汉字
     *
     * @return
     */
    public static char getRandomChar() {
        String str = "";
        int hightPos;
        int lowPos;
        Random random = new Random();

        hightPos = (176 + Math.abs(random.nextInt(39)));
        lowPos = (161 + Math.abs(random.nextInt(93)));

        byte[] b = new byte[2];
        b[0] = (Integer.valueOf(hightPos)).byteValue();
        b[1] = (Integer.valueOf(lowPos)).byteValue();

        try {
            str = new String(b, "GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str.charAt(0);
    }
}
