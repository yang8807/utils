package com.magnify.yutils;

import android.content.Context;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * �ַ����������߰�
 *
 * @author xubing
 */
public class StringUtil {

    public static final String RAMDOM_BASE = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /**
     * md5����
     */
    public static final String ALGORITHM_MD5 = "MD5";

    /**
     * sha1����
     */
    public static final String ALGORITHM_SHA1 = "SHA1";

    /**
     * �ַ����Ƿ�Ϊ��(���ַ�����null)
     */
    public static boolean isEmpty(String s) {
        return s == null || s.length() == 0;
    }

    /***
     * ���ַ�������Base64���롣����������76�ַ�����뻻�з���ĩβ�л��з�
     */
    public static String base64Encode(String str) {
        return Base64.encodeToString(str.getBytes(), Base64.DEFAULT);
    }

    /***
     * ���ַ�������Base64���룬ȥ���������еĻ��з�
     *
     * @param str
     */
    public static String base64EncodeNoCR(String str) {
        return base64Encode(str).replaceAll("\\n", "");
    }

    /***
     * ���ַ�������Base64����
     *
     * @param str
     */
    public static String base64Decode(String str) {
        return new String(Base64.decode(str.getBytes(), Base64.DEFAULT));
    }

    /**
     * ʮ����תʮ�����ƶ�Ӧ��
     */
    private static final char HEX_DIGITS[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    /**
     * �ֽ�תʮ������
     */
    public static String toHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            sb.append(HEX_DIGITS[(bytes[i] & 0xf0) >>> 4]);
            sb.append(HEX_DIGITS[bytes[i] & 0x0f]);
        }
        return sb.toString();
    }

    /**
     * MD5���룬��д��ʹ��Ĭ��charset(UTF-8)
     */
    public static String md5(String str) {
        return encode(str.getBytes(), ALGORITHM_MD5);
    }

    /**
     * MD5���룬Сд��ʹ��Ĭ��charset(UTF-8)
     */
    public static String md5Lcase(String str) {
        String output = encode(str.getBytes(), ALGORITHM_MD5);
        return output == null ? null : output.toLowerCase(Locale.getDefault());
    }

    /**
     * MD5���룬��д��ʹ���ض�charset
     */
    public static String md5(String str, String charsetName) {
        try {
            return encode(str.getBytes(charsetName), ALGORITHM_MD5);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * MD5���룬Сд��ʹ���ض�charset
     */
    public static String md5Lcase(String str, String charsetName) {
        String output = md5(str, charsetName);
        return output == null ? null : output.toLowerCase(Locale.getDefault());
    }

    /**
     * MD5���룬Сд
     */
    public static String md5Lcase(byte[] bytes) {
        String output = encode(bytes, ALGORITHM_MD5);
        return output == null ? null : output.toLowerCase(Locale.getDefault());
    }

    /**
     * SHA1���룬��д��ʹ��Ĭ��charset(UTF-8)
     */
    public static String sha1(String str) {
        return encode(str.getBytes(), ALGORITHM_SHA1);
    }

    /**
     * SHA1���룬Сд��ʹ��Ĭ��charset(UTF-8)
     */
    public static String sha1Lcase(String str) {
        String output = encode(str.getBytes(), ALGORITHM_SHA1);
        return output == null ? null : output.toLowerCase(Locale.getDefault());
    }

    /**
     * SHA1���룬��д��ʹ���ض�charset
     */
    public static String sha1(String str, String charsetName) {
        try {
            return encode(str.getBytes(charsetName), ALGORITHM_SHA1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * SHA1���룬Сд��ʹ���ض�charset
     */
    public static String sha1Lcase(String str, String charsetName) {
        String output = sha1(str, charsetName);
        return output == null ? null : output.toLowerCase(Locale.getDefault());
    }

    /**
     * SHA1���룬Сд
     */
    public static String sha1Lcase(byte[] bytes) {
        String output = encode(bytes, ALGORITHM_SHA1);
        return output == null ? null : output.toLowerCase(Locale.getDefault());
    }

    /**
     * SHA1��MD5���룬��д
     *
     * @param bytes     ����
     * @param algorithm �����㷨 {@link#ALGORITHM_MD5}, {@link#ALGORITHM_SHA1}
     *                  ���
     */
    public static String encode(byte[] bytes, String algorithm) {
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            digest.update(bytes);
            return toHexString(digest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * ����ɫ��ֵתΪ#RRGGBB��ʽ
     */
    public static String getRGBColor(int color) {
        return "#" + Integer.toHexString(color | 0xff000000).substring(2);
    }

    /**
     * ����ɫ��ֵתΪ#AARRGGBB��ʽ
     */
    public static String getARGBColor(int color) {
        return "#" + Long.toHexString(color | 0xffffffff00000000L).substring(8);
    }

    /**
     * ����ͬһ��textview������ֵĲ�ͬ��ɫ
     *
     * @param format �������ű���Ҫ��ɫ�����֣����� "[12��]����[8��]ӡ��"
     * @param colors ���� "#dd465e"�������������format���������������Ļ�ȡ���һ��
     *               ���ص�Spanned����ֱ������TextView.setText()
     */
    public static Spanned formatColor(String format, String... colors) {
        if (colors == null || colors.length == 0)
            return Html.fromHtml(format);
        Pattern pattern = Pattern.compile("(\\[[^\\[\\]]*\\])");
        StringBuffer buffer = new StringBuffer(format.length());
        Matcher matcher = pattern.matcher(format);
        int i = 0;
        while (matcher.find()) {
            int c = i < colors.length ? i : colors.length - 1;
            String group = matcher.group();
            group = "<font color=\"" + colors[c] + "\">" + group.substring(1, group.length() - 1) + "</font>";
            matcher.appendReplacement(buffer, group);
            i++;
        }
        matcher.appendTail(buffer);
        return Html.fromHtml(buffer.toString());
    }

    /**
     * ����ͬһ��textview������ֵĲ�ͬ��ɫ
     *
     * @param format �������ű���Ҫ��ɫ�����֣����� "[12��]����[8��]ӡ��"
     * @param colors ��ɫֵ������ Color.RED, 0xffdd465e������͸���ȡ������������format���������������Ļ�ȡ���һ��
     *               ���ص�Spanned����ֱ������TextView.setText()
     */
    public static Spanned formatColor(String format, int... colors) {
        if (colors == null || colors.length == 0)
            return Html.fromHtml(format);
        String[] colors2 = new String[colors.length];
        for (int i = 0; i < colors.length; i++) {
            colors2[i] = getRGBColor(colors[i]);
        }
        return formatColor(format, colors2);
    }

    /**
     * ����ͬһ��textview������ֵĲ�ͬ��ɫ
     *
     * @param format   �������ű���Ҫ��ɫ�����֣����� "[12��]����[8��]ӡ��"
     * @param context  ������
     * @param colorIds ��ɫ��Դid������ R.color.eelly_red ������͸���ȡ������������format���������������Ļ�ȡ���һ��
     *                 ���ص�Spanned����ֱ������TextView.setText()
     */
    public static Spanned formatColorResource(String format, Context context, int... colorIds) {
        if (colorIds == null || colorIds.length == 0)
            return Html.fromHtml(format);
        String[] colors2 = new String[colorIds.length];
        for (int i = 0; i < colorIds.length; i++) {
            colors2[i] = getRGBColor(context.getResources().getColor(colorIds[i]));
        }
        return formatColor(format, colors2);
    }

    /**
     * ��ô���ɫ������
     *
     * @param text
     * @param color
     */
    public static SpannableStringBuilder getColoredText(String text, int color) {
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        builder.setSpan(new ForegroundColorSpan(color), 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return builder;
    }

    /**
     * @see #getColoredText(String, int)
     */
    public static SpannableStringBuilder getColoredText(Context context, String text, int colorId) {
        return getColoredText(text, context.getResources().getColor(colorId));
    }

    /**
     * @see #getColoredText(String, int)
     */
    public static SpannableStringBuilder getColoredText(Context context, int textId, int colorId) {
        return getColoredText(context.getString(textId), context.getResources().getColor(colorId));
    }

    /**
     * ���ַ����в��ҵ�һ��ƥ����ַ�
     *
     * @param source     Դ�ַ���
     * @param target     Ҫ���ҵ�Ŀ���ַ���
     * @param ignoreCase �Ƿ���Դ�Сд
     *                   �ַ������ҽ��,û�ҵ�ʱ����null
     */
    public static StringFind find(String source, String target, boolean ignoreCase) {
        Pattern pattern = ignoreCase ? Pattern.compile(target, Pattern.CASE_INSENSITIVE | Pattern.LITERAL) : Pattern.compile(target, Pattern.LITERAL);
        Matcher matcher = pattern.matcher(source);
        if (matcher.find()) {
            return new StringFind(matcher.start(), matcher.end(), matcher.group());
        }
        return null;
    }

    /**
     * �ַ������ҽ������
     *
     * @author ����
     */
    public static class StringFind {

        /**
         * �����ַ����Ŀ�ʼλ��
         */
        public int start;

        /**
         * �����ַ����Ľ���λ��+1
         */
        public int end;

        /**
         * �����ַ�����Դ�ַ����е�ԭʼֵ(��Ҫ������ĸ��Сд�������ȡԭʼֵ)
         */
        public String original;

        StringFind(int start, int end, String original) {
            this.start = start;
            this.end = end;
            this.original = original;
        }
    }

    /**
     * �����ַ������ֽڳ���(��Ƿ��ż�1��ȫ�Ƿ��ż�2)
     */
    public static int getByteLength(String string) {
        int count = 0;
        for (int i = 0; i < string.length(); i++) {
            count += Integer.toHexString(string.charAt(i)).length() == 4 ? 2 : 1;
        }
        return count;
    }

    /**
     * ��ָ�����ȣ��ض��ַ��������������ָ����׺<br>
     * ��Ƿ��ų���Ϊ1��ȫ�Ƿ��ų���Ϊ2
     *
     * @param string �ַ���
     * @param length �����ַ�������
     * @param suffix ����ʱ��ӵĺ�׺
     *               �ضϺ���ַ���
     */
    public static String trimString(String string, int length, String suffix) {
        if (getByteLength(string) <= length)
            return string;
        StringBuffer sb = new StringBuffer();
        int count = 0;
        if (suffix == null)
            suffix = "";
        int slength = getByteLength(suffix);
        for (int i = 0; i < string.length(); i++) {
            char temp = string.charAt(i);
            count += Integer.toHexString(temp).length() == 4 ? 2 : 1;
            if (count + slength <= length) {
                sb.append(temp);
            }
            if (count + slength >= length) {
                break;
            }
        }
        sb.append(suffix);
        return sb.toString();
    }

    /**
     * ��ָ�����ȣ��ض��ַ�������������ӡ�<br>
     * ��Ƿ��ų���Ϊ1��ȫ�Ƿ��ų���Ϊ2
     *
     * @param string �ַ���
     * @param length �����ַ�������
     *               �ضϺ���ַ���
     */
    public static String trimString(String string, int length) {
        return trimString(string, length, "��");
    }

    /**
     * �������ؿ�Ƚ�ȡ�ַ���<br>
     * �����Ϊ��ĳЩ�ط�����Ҫʡ�ԺŽ�β����TextView�Ŀ�Ȳ��������������ֻ�
     */
    public static String trimPixelLength(String str, int pixel, float textSize) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        float width = getTextWidth(str, textSize);
        if (Math.ceil(width) <= pixel) {
            return str.toString();
        } else {
            for (int i = str.length() - 1; i > 0; i--) {
                String temp = str.substring(0, i);
                float w = getTextWidth(temp, textSize);
                if (Math.ceil(w) <= pixel) {
                    return temp;
                }
            }
        }
        return "";
    }

    private static float getTextWidth(String text, float textSize) {
        TextPaint paint = new TextPaint();
        paint.setTextSize(textSize);
        return paint.measureText(text);
    }

    /**
     * ����Html����,��Html��ʽ������תΪ��ͨ�ı�
     *
     * @param source
     */
    public static String htmlToString(String source) {
        return Html.fromHtml(source).toString();
    }

    /**
     * ��Ǯ��λ��������
     *
     * @param money ��Ǯ
     *              ��������֮��Ľ�Ǯ
     */
    public static String moneyRound(String money) {
        if (money.length() == 0) {
            money = "0";
        }
        BigDecimal decimal = new BigDecimal(Double.valueOf(money));
        return decimal.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
    }

    /**
     * javascript escape
     *
     * @param s
     */
    public static String escape(String s) {
        StringBuilder sb = new StringBuilder();
        int len = s.length();
        for (int i = 0; i < len; i++) {
            int ch = s.charAt(i);
            if (isEscapePersistDigit(ch)) {
                sb.append((char) ch);
            } else if (ch <= 0x007F) {
                sb.append('%');
                sb.append(HEX_DIGITS[(ch & 0xf0) >>> 4]);
                sb.append(HEX_DIGITS[ch & 0x0f]);
            } else {
                sb.append('%');
                sb.append('u');
                sb.append(HEX_DIGITS[(ch & 0xf000) >>> 12]);
                sb.append(HEX_DIGITS[(ch & 0x0f00) >>> 8]);
                sb.append(HEX_DIGITS[(ch & 0x00f0) >>> 4]);
                sb.append(HEX_DIGITS[ch & 0x000f]);
            }
        }
        return sb.toString();
    }

    /**
     * javascript unescape
     */
    public static String unescape(String s) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        int len = s.length();
        while (i < len) {
            int ch = s.charAt(i);
            if (isEscapePersistDigit(ch)) {
                sb.append((char) ch);
            } else if (ch == '%') {
                boolean flag = false;
                int cint = 0;
                if ('u' == s.charAt(i + 1) || 'U' == s.charAt(i + 1)) {
                    if (i + 5 < len) {
                        char[] chs = new char[4];
                        s.getChars(i + 2, i + 6, chs, 0);
                        if (isHexDigits(chs)) {
                            cint = Integer.parseInt(new String(chs), 16);
                            i += 5;
                            flag = true;
                        }
                    }
                } else {
                    if (i + 2 < len) {
                        char[] chs = new char[2];
                        s.getChars(i + 1, i + 3, chs, 0);
                        if (isHexDigits(chs)) {
                            cint = Integer.parseInt(new String(chs), 16);
                            i += 2;
                            flag = true;
                        }
                    }
                }
                if (flag)
                    sb.append((char) cint);
                else
                    sb.append((char) ch);
            } else {
                sb.append((char) ch);
            }
            i++;
        }
        return sb.toString();
    }

    private static boolean isHexDigit(int ch) {
        return ('A' <= ch && ch <= 'Z') || ('a' <= ch && ch <= 'z') || ('0' <= ch && ch <= '9');
    }

    private static boolean isHexDigits(char[] chs) {
        for (int i = 0; i < chs.length; i++) {
            if (!isHexDigit(chs[i]))
                return false;
        }
        return true;
    }

    private static boolean isEscapePersistDigit(int ch) {
        return isHexDigit(ch) || ch == '*' || ch == '@' || ch == '-' || ch == '_' || ch == '+' || ch == '.' || ch == '/';
    }

    /**
     * �ж��ַ��Ƿ�Ϊ���֣�����С��
     */
    public static boolean isNumeric(String input) {
        Pattern pattern = Pattern.compile("-?[0-9]*.?[0-9]*");
        Matcher isNum = pattern.matcher(input);
        return !TextUtils.isEmpty(input) && isNum.matches();
    }

    /**
     * ��ȡ����ַ������ַ������ֺʹ�Сд�ַ����
     *
     * @param length ��ȡ���ַ�������
     *               ��Ӧ���ȵ�����ַ���
     */
    public static String getRandomString(int length) {
        return getRandomString(length, null);
    }

    /**
     * ��ȡ����ַ������ַ������ֺʹ�Сд�ַ����
     *
     * @param length ��ȡ���ַ�������
     * @param base   �������ַ������ַ�����nullʱ��Ĭ��ʹ��{@value #RAMDOM_BASE}
     *               ��Ӧ���ȵ�����ַ���
     */
    public static String getRandomString(int length, String base) {
        if (TextUtils.isEmpty(base)) {
            base = RAMDOM_BASE;
        }
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * �ַ�����ת
     *
     * @param str Ҫ��ת���ַ���
     */
    public static String reverseString(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        StringBuffer sb = new StringBuffer(str);
        return sb.reverse().toString();
    }

}
