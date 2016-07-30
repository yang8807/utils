package com.magnify.yutils;

import java.io.UnsupportedEncodingException;
import java.util.Random;

public class RandomCharUtils {
    /**
     *Generate random Chinese characters
     * @param encode GBK or utf-8
     *
     * @return
     */
    public static final String GBK="GBK";
    public static final String UTF_8="UTF-8";
    public static char getRandomChar(String encode) {
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
            str = new String(b, encode);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str.charAt(0);
    }
    public static char getRandomChar(){
        return getRandomChar(GBK);
    }
}
