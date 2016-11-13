package com.magnify.yutils.data;

import java.security.MessageDigest;

/**
 * ****************************************************************<br>
 * 文件名称 : MD5Util.java<br>
 * 作 者 :   裴小勇<br>
 * 创建时间 : 2016/7/21 11:53<br>
 * 文件描述 : MD5加密工具类，放在公共目录下面<br>
 * 版权声明 : 广州市衣联网络有限公司 版权所有<br>
 * 修改历史 : 初始版本<br>
 * ****************************************************************
 */
public class MD5Util {

    private MD5Util() {
    }

    public final static String getMessageDigest(byte[] buffer) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(buffer);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }
}