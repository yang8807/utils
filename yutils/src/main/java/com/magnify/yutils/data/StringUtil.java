package com.magnify.yutils.data;

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
 * 字符串操作工具包
 *
 * @author xubing
 */
public class StringUtil {

    public static final String RAMDOM_BASE = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /**
     * md5编码
     */
    public static final String ALGORITHM_MD5 = "MD5";

    /**
     * sha1编码
     */
    public static final String ALGORITHM_SHA1 = "SHA1";

    /**
     * 字符串是否为空(空字符串或null)
     */
    public static boolean isEmpty(String s) {
        return s == null || s.length() == 0;
    }

    /***
     * 将字符串进行Base64编码。编码结果超过76字符会插入换行符，末尾有换行符
     */
    public static String base64Encode(String str) {
        return Base64.encodeToString(str.getBytes(), Base64.DEFAULT);
    }

    /***
     * 将字符串进行Base64编码，去掉其中所有的换行符
     *
     * @param str
     */
    public static String base64EncodeNoCR(String str) {
        return base64Encode(str).replaceAll("\\n", "");
    }

    /***
     * 将字符串进行Base64解码
     *
     * @param str
     */
    public static String base64Decode(String str) {
        return new String(Base64.decode(str.getBytes(), Base64.DEFAULT));
    }

    /**
     * 十进制转十六进制对应表
     */
    private static final char HEX_DIGITS[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    /**
     * 字节转十六进制
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
     * MD5编码，大写，使用默认charset(UTF-8)
     */
    public static String md5(String str) {
        return encode(str.getBytes(), ALGORITHM_MD5);
    }

    /**
     * MD5编码，小写，使用默认charset(UTF-8)
     */
    public static String md5Lcase(String str) {
        String output = encode(str.getBytes(), ALGORITHM_MD5);
        return output == null ? null : output.toLowerCase(Locale.getDefault());
    }

    /**
     * MD5编码，大写，使用特定charset
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
     * MD5编码，小写，使用特定charset
     */
    public static String md5Lcase(String str, String charsetName) {
        String output = md5(str, charsetName);
        return output == null ? null : output.toLowerCase(Locale.getDefault());
    }

    /**
     * MD5编码，小写
     */
    public static String md5Lcase(byte[] bytes) {
        String output = encode(bytes, ALGORITHM_MD5);
        return output == null ? null : output.toLowerCase(Locale.getDefault());
    }

    /**
     * SHA1编码，大写，使用默认charset(UTF-8)
     */
    public static String sha1(String str) {
        return encode(str.getBytes(), ALGORITHM_SHA1);
    }

    /**
     * SHA1编码，小写，使用默认charset(UTF-8)
     */
    public static String sha1Lcase(String str) {
        String output = encode(str.getBytes(), ALGORITHM_SHA1);
        return output == null ? null : output.toLowerCase(Locale.getDefault());
    }

    /**
     * SHA1编码，大写，使用特定charset
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
     * SHA1编码，小写，使用特定charset
     */
    public static String sha1Lcase(String str, String charsetName) {
        String output = sha1(str, charsetName);
        return output == null ? null : output.toLowerCase(Locale.getDefault());
    }

    /**
     * SHA1编码，小写
     */
    public static String sha1Lcase(byte[] bytes) {
        String output = encode(bytes, ALGORITHM_SHA1);
        return output == null ? null : output.toLowerCase(Locale.getDefault());
    }

    /**
     * SHA1和MD5编码，大写
     *
     * @param bytes     输入
     * @param algorithm 编码算法 {@link#ALGORITHM_MD5}, {@link#ALGORITHM_SHA1}
     *                  输出
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
     * 把颜色数值转为#RRGGBB形式
     */
    public static String getRGBColor(int color) {
        return "#" + Integer.toHexString(color | 0xff000000).substring(2);
    }

    /**
     * 把颜色数值转为#AARRGGBB形式
     */
    public static String getARGBColor(int color) {
        return "#" + Long.toHexString(color | 0xffffffff00000000L).substring(8);
    }

    /**
     * 设置同一个textview里边文字的不同颜色
     *
     * @param format 以中括号标明要上色的文字，例如 "[12人]给了[8种]印象"
     * @param colors 例如 "#dd465e"。如果数量少于format里的括号数，不足的会取最后一个
     *               返回的Spanned可以直接用于TextView.setText()
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
     * 设置同一个textview里边文字的不同颜色
     *
     * @param format 以中括号标明要上色的文字，例如 "[12人]给了[8种]印象"
     * @param colors 颜色值，例如 Color.RED, 0xffdd465e，忽略透明度。如果数量少于format里的括号数，不足的会取最后一个
     *               返回的Spanned可以直接用于TextView.setText()
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
     * 设置同一个textview里边文字的不同颜色
     *
     * @param format   以中括号标明要上色的文字，例如 "[12人]给了[8种]印象"
     * @param context  上下文
     * @param colorIds 颜色资源id，例如 R.color.eelly_red ，忽略透明度。如果数量少于format里的括号数，不足的会取最后一个
     *                 返回的Spanned可以直接用于TextView.setText()
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
     * 获得带颜色的文字
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
     * 在字符串中查找第一个匹配的字符
     *
     * @param source     源字符串
     * @param target     要查找的目标字符串
     * @param ignoreCase 是否忽略大小写
     *                   字符串查找结果,没找到时返回null
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
     * 字符串查找结果对象
     *
     * @author 李欣
     */
    public static class StringFind {

        /**
         * 查找字符串的开始位置
         */
        public int start;

        /**
         * 查找字符串的结束位置+1
         */
        public int end;

        /**
         * 查找字符串在源字符串中的原始值(主要用于字母大小写的情况获取原始值)
         */
        public String original;

        StringFind(int start, int end, String original) {
            this.start = start;
            this.end = end;
            this.original = original;
        }
    }

    /**
     * 计算字符串的字节长度(半角符号计1，全角符号计2)
     */
    public static int getByteLength(String string) {
        int count = 0;
        for (int i = 0; i < string.length(); i++) {
            count += Integer.toHexString(string.charAt(i)).length() == 4 ? 2 : 1;
        }
        return count;
    }

    /**
     * 按指定长度，截断字符串，超长会添加指定后缀<br>
     * 半角符号长度为1，全角符号长度为2
     *
     * @param string 字符串
     * @param length 保留字符串长度
     * @param suffix 超长时添加的后缀
     *               截断后的字符串
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
     * 按指定长度，截断字符串，超长会添加…<br>
     * 半角符号长度为1，全角符号长度为2
     *
     * @param string 字符串
     * @param length 保留字符串长度
     *               截断后的字符串
     */
    public static String trimString(String string, int length) {
        return trimString(string, length, "…");
    }

    /**
     * 根据像素宽度截取字符串<br>
     * 这个是为了某些地方不需要省略号结尾，而TextView的宽度不够，这样最后的字会
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
     * 过滤Html内容,将Html格式的内容转为普通文本
     *
     * @param source
     */
    public static String htmlToString(String source) {
        return Html.fromHtml(source).toString();
    }

    /**
     * 金钱单位四舍五入
     *
     * @param money 金钱
     *              四舍五入之后的金钱
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
     * 判断字符是否为数字，包括小数
     */
    public static boolean isNumeric(String input) {
        Pattern pattern = Pattern.compile("-?[0-9]*.?[0-9]*");
        Matcher isNum = pattern.matcher(input);
        return !TextUtils.isEmpty(input) && isNum.matches();
    }

    /**
     * 获取随机字符串，字符由数字和大小写字符组成
     *
     * @param length 获取的字符串长度
     *               相应长度的随机字符串
     */
    public static String getRandomString(int length) {
        return getRandomString(length, null);
    }

    /**
     * 获取随机字符串，字符由数字和大小写字符组成
     *
     * @param length 获取的字符串长度
     * @param base   组成随机字符串的字符，传null时，默认使用{@value #RAMDOM_BASE}
     *               相应长度的随机字符串
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
     * 字符串翻转
     *
     * @param str 要翻转的字符串
     */
    public static String reverseString(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        StringBuffer sb = new StringBuffer(str);
        return sb.reverse().toString();
    }

    /*
    **
     * 判断字符串是否为null或长度为0
    *
     * @param s 待校验字符串
    * @return {@code true}: 空<br> {@code false}: 不为空
    */
    public static boolean isEmpty(CharSequence s) {
        return s == null || s.length() == 0;
    }

    /**
     * 判断字符串是否为null或全为空格
     *
     * @param s 待校验字符串
     * @return {@code true}: null或全空格<br> {@code false}: 不为null且不全空格
     */
    public static boolean isSpace(String s) {
        return (s == null || s.trim().length() == 0);
    }

    /**
     * null转为长度为0的字符串
     *
     * @param s 待转字符串
     * @return s为null转为长度为0字符串，否则不改变
     */
    public static String null2Length0(String s) {
        return s == null ? "" : s;
    }

    /**
     * 返回字符串长度
     *
     * @param s 字符串
     * @return null返回0，其他返回自身长度
     */
    public static int length(CharSequence s) {
        return s == null ? 0 : s.length();
    }

    /**
     * 首字母大写
     *
     * @param s 待转字符串
     * @return 首字母大写字符串
     */
    public static String upperFirstLetter(String s) {
        if (isEmpty(s) || !Character.isLowerCase(s.charAt(0))) {
            return s;
        }
        return String.valueOf((char) (s.charAt(0) - 32)) + s.substring(1);
    }

    /**
     * 首字母小写
     *
     * @param s 待转字符串
     * @return 首字母小写字符串
     */
    public static String lowerFirstLetter(String s) {
        if (isEmpty(s) || !Character.isUpperCase(s.charAt(0))) {
            return s;
        }
        return String.valueOf((char) (s.charAt(0) + 32)) + s.substring(1);
    }

    /**
     * 反转字符串
     *
     * @param s 待反转字符串
     * @return 反转字符串
     */
    public static String reverse(String s) {
        int len = length(s);
        if (len <= 1) return s;
        int mid = len >> 1;
        char[] chars = s.toCharArray();
        char c;
        for (int i = 0; i < mid; ++i) {
            c = chars[i];
            chars[i] = chars[len - i - 1];
            chars[len - i - 1] = c;
        }
        return new String(chars);
    }

    /**
     * 转化为半角字符
     *
     * @param s 待转字符串
     * @return 半角字符串
     */
    public static String toDBC(String s) {
        if (isEmpty(s)) {
            return s;
        }
        char[] chars = s.toCharArray();
        for (int i = 0, len = chars.length; i < len; i++) {
            if (chars[i] == 12288) {
                chars[i] = ' ';
            } else if (65281 <= chars[i] && chars[i] <= 65374) {
                chars[i] = (char) (chars[i] - 65248);
            } else {
                chars[i] = chars[i];
            }
        }
        return new String(chars);
    }

    /**
     * 转化为全角字符
     *
     * @param s 待转字符串
     * @return 全角字符串
     */
    public static String toSBC(String s) {
        if (isEmpty(s)) {
            return s;
        }
        char[] chars = s.toCharArray();
        for (int i = 0, len = chars.length; i < len; i++) {
            if (chars[i] == ' ') {
                chars[i] = (char) 12288;
            } else if (33 <= chars[i] && chars[i] <= 126) {
                chars[i] = (char) (chars[i] + 65248);
            } else {
                chars[i] = chars[i];
            }
        }
        return new String(chars);
    }

    private static int[] pyValue = new int[]{-20319, -20317, -20304, -20295, -20292, -20283, -20265, -20257, -20242,
            -20230, -20051, -20036, -20032,
            -20026, -20002, -19990, -19986, -19982, -19976, -19805, -19784, -19775, -19774, -19763, -19756, -19751,
            -19746, -19741, -19739, -19728,
            -19725, -19715, -19540, -19531, -19525, -19515, -19500, -19484, -19479, -19467, -19289, -19288, -19281,
            -19275, -19270, -19263, -19261,
            -19249, -19243, -19242, -19238, -19235, -19227, -19224, -19218, -19212, -19038, -19023, -19018, -19006,
            -19003, -18996, -18977, -18961,
            -18952, -18783, -18774, -18773, -18763, -18756, -18741, -18735, -18731, -18722, -18710, -18697, -18696,
            -18526, -18518, -18501, -18490,
            -18478, -18463, -18448, -18447, -18446, -18239, -18237, -18231, -18220, -18211, -18201, -18184, -18183,
            -18181, -18012, -17997, -17988,
            -17970, -17964, -17961, -17950, -17947, -17931, -17928, -17922, -17759, -17752, -17733, -17730, -17721,
            -17703, -17701, -17697, -17692,
            -17683, -17676, -17496, -17487, -17482, -17468, -17454, -17433, -17427, -17417, -17202, -17185, -16983,
            -16970, -16942, -16915, -16733,
            -16708, -16706, -16689, -16664, -16657, -16647, -16474, -16470, -16465, -16459, -16452, -16448, -16433,
            -16429, -16427, -16423, -16419,
            -16412, -16407, -16403, -16401, -16393, -16220, -16216, -16212, -16205, -16202, -16187, -16180, -16171,
            -16169, -16158, -16155, -15959,
            -15958, -15944, -15933, -15920, -15915, -15903, -15889, -15878, -15707, -15701, -15681, -15667, -15661,
            -15659, -15652, -15640, -15631,
            -15625, -15454, -15448, -15436, -15435, -15419, -15416, -15408, -15394, -15385, -15377, -15375, -15369,
            -15363, -15362, -15183, -15180,
            -15165, -15158, -15153, -15150, -15149, -15144, -15143, -15141, -15140, -15139, -15128, -15121, -15119,
            -15117, -15110, -15109, -14941,
            -14937, -14933, -14930, -14929, -14928, -14926, -14922, -14921, -14914, -14908, -14902, -14894, -14889,
            -14882, -14873, -14871, -14857,
            -14678, -14674, -14670, -14668, -14663, -14654, -14645, -14630, -14594, -14429, -14407, -14399, -14384,
            -14379, -14368, -14355, -14353,
            -14345, -14170, -14159, -14151, -14149, -14145, -14140, -14137, -14135, -14125, -14123, -14122, -14112,
            -14109, -14099, -14097, -14094,
            -14092, -14090, -14087, -14083, -13917, -13914, -13910, -13907, -13906, -13905, -13896, -13894, -13878,
            -13870, -13859, -13847, -13831,
            -13658, -13611, -13601, -13406, -13404, -13400, -13398, -13395, -13391, -13387, -13383, -13367, -13359,
            -13356, -13343, -13340, -13329,
            -13326, -13318, -13147, -13138, -13120, -13107, -13096, -13095, -13091, -13076, -13068, -13063, -13060,
            -12888, -12875, -12871, -12860,
            -12858, -12852, -12849, -12838, -12831, -12829, -12812, -12802, -12607, -12597, -12594, -12585, -12556,
            -12359, -12346, -12320, -12300,
            -12120, -12099, -12089, -12074, -12067, -12058, -12039, -11867, -11861, -11847, -11831, -11798, -11781,
            -11604, -11589, -11536, -11358,
            -11340, -11339, -11324, -11303, -11097, -11077, -11067, -11055, -11052, -11045, -11041, -11038, -11024,
            -11020, -11019, -11018, -11014,
            -10838, -10832, -10815, -10800, -10790, -10780, -10764, -10587, -10544, -10533, -10519, -10331, -10329,
            -10328, -10322, -10315, -10309,
            -10307, -10296, -10281, -10274, -10270, -10262, -10260, -10256, -10254};

    private static String[] pyStr = new String[]{"a", "ai", "an", "ang", "ao", "ba", "bai", "ban", "bang", "bao",
            "bei", "ben", "beng", "bi", "bian",
            "biao", "bie", "bin", "bing", "bo", "bu", "ca", "cai", "can", "cang", "cao", "ce", "ceng", "cha", "chai",
            "chan", "chang", "chao", "che",
            "chen", "cheng", "chi", "chong", "chou", "chu", "chuai", "chuan", "chuang", "chui", "chun", "chuo", "ci",
            "cong", "cou", "cu", "cuan",
            "cui", "cun", "cuo", "da", "dai", "dan", "dang", "dao", "de", "deng", "di", "dian", "diao", "die",
            "ding", "diu", "dong", "dou", "du",
            "duan", "dui", "dun", "duo", "e", "en", "er", "fa", "fan", "fang", "fei", "fen", "feng", "fo", "fou",
            "fu", "ga", "gai", "gan", "gang",
            "gao", "ge", "gei", "gen", "geng", "gong", "gou", "gu", "gua", "guai", "guan", "guang", "gui", "gun",
            "guo", "ha", "hai", "han", "hang",
            "hao", "he", "hei", "hen", "heng", "hong", "hou", "hu", "hua", "huai", "huan", "huang", "hui", "hun",
            "huo", "ji", "jia", "jian",
            "jiang", "jiao", "jie", "jin", "jing", "jiong", "jiu", "ju", "juan", "jue", "jun", "ka", "kai", "kan",
            "kang", "kao", "ke", "ken",
            "keng", "kong", "kou", "ku", "kua", "kuai", "kuan", "kuang", "kui", "kun", "kuo", "la", "lai", "lan",
            "lang", "lao", "le", "lei", "leng",
            "li", "lia", "lian", "liang", "liao", "lie", "lin", "ling", "liu", "long", "lou", "lu", "lv", "luan",
            "lue", "lun", "luo", "ma", "mai",
            "man", "mang", "mao", "me", "mei", "men", "meng", "mi", "mian", "miao", "mie", "min", "ming", "miu",
            "mo", "mou", "mu", "na", "nai",
            "nan", "nang", "nao", "ne", "nei", "nen", "neng", "ni", "nian", "niang", "niao", "nie", "nin", "ning",
            "niu", "nong", "nu", "nv", "nuan",
            "nue", "nuo", "o", "ou", "pa", "pai", "pan", "pang", "pao", "pei", "pen", "peng", "pi", "pian", "piao",
            "pie", "pin", "ping", "po", "pu",
            "qi", "qia", "qian", "qiang", "qiao", "qie", "qin", "qing", "qiong", "qiu", "qu", "quan", "que", "qun",
            "ran", "rang", "rao", "re",
            "ren", "reng", "ri", "rong", "rou", "ru", "ruan", "rui", "run", "ruo", "sa", "sai", "san", "sang", "sao",
            "se", "sen", "seng", "sha",
            "shai", "shan", "shang", "shao", "she", "shen", "sheng", "shi", "shou", "shu", "shua", "shuai", "shuan",
            "shuang", "shui", "shun",
            "shuo", "si", "song", "sou", "su", "suan", "sui", "sun", "suo", "ta", "tai", "tan", "tang", "tao", "te",
            "teng", "ti", "tian", "tiao",
            "tie", "ting", "tong", "tou", "tu", "tuan", "tui", "tun", "tuo", "wa", "wai", "wan", "wang", "wei",
            "wen", "weng", "wo", "wu", "xi",
            "xia", "xian", "xiang", "xiao", "xie", "xin", "xing", "xiong", "xiu", "xu", "xuan", "xue", "xun", "ya",
            "yan", "yang", "yao", "ye", "yi",
            "yin", "ying", "yo", "yong", "you", "yu", "yuan", "yue", "yun", "za", "zai", "zan", "zang", "zao", "ze",
            "zei", "zen", "zeng", "zha",
            "zhai", "zhan", "zhang", "zhao", "zhe", "zhen", "zheng", "zhi", "zhong", "zhou", "zhu", "zhua", "zhuai",
            "zhuan", "zhuang", "zhui",
            "zhun", "zhuo", "zi", "zong", "zou", "zu", "zuan", "zui", "zun", "zuo"};

    /**
     * 单个汉字转成ASCII码
     *
     * @param s 单个汉字字符串
     * @return 如果字符串长度是1返回的是对应的ascii码，否则返回-1
     */
    private static int oneCn2ASCII(String s) {
        if (s.length() != 1) return -1;
        int ascii = 0;
        try {
            byte[] bytes = s.getBytes("GB2312");
            if (bytes.length == 1) {
                ascii = bytes[0];
            } else if (bytes.length == 2) {
                int highByte = 256 + bytes[0];
                int lowByte = 256 + bytes[1];
                ascii = (256 * highByte + lowByte) - 256 * 256;
            } else {
                throw new IllegalArgumentException("Illegal resource string");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return ascii;
    }

    /**
     * 单个汉字转成拼音
     *
     * @param s 单个汉字字符串
     * @return 如果字符串长度是1返回的是对应的拼音，否则返回{@code null}
     */
    private static String oneCn2PY(String s) {
        int ascii = oneCn2ASCII(s);
        if (ascii == -1) return null;
        String ret = null;
        if (0 <= ascii && ascii <= 127) {
            ret = String.valueOf((char) ascii);
        } else {
            for (int i = pyValue.length - 1; i >= 0; i--) {
                if (pyValue[i] <= ascii) {
                    ret = pyStr[i];
                    break;
                }
            }
        }
        return ret;
    }

    /**
     * 获得第一个汉字首字母
     *
     * @param s 单个汉字字符串
     * @return 拼音
     */
    public static String getPYFirstLetter(String s) {
        if (isSpace(s)) return "";
        String first, py;
        first = s.substring(0, 1);
        py = oneCn2PY(first);
        if (py == null) return null;
        return py.substring(0, 1);
    }

    /**
     * 中文转拼音
     *
     * @param s 汉字字符串
     * @return 拼音
     */
    public static String cn2PY(String s) {
        String hz, py;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            hz = s.substring(i, i + 1);
            py = oneCn2PY(hz);
            if (py == null) {
                py = "?";
            }
            sb.append(py);
        }
        return sb.toString();
    }


}
