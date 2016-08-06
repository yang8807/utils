package utils;

import android.graphics.Paint;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextViewUtil {

    /**
     *  Set part size for TextView
     */
    public static void setPartialSize(TextView tv, int start, int end, int textSize) {
        String s = tv.getText().toString();
        Spannable spannable = new SpannableString(s);
        spannable.setSpan(new AbsoluteSizeSpan(textSize), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(spannable);
    }

    /**
     *  Set the color to the TextView
     */
    public static void setPartialColor(TextView tv, int start, int end, int textColor) {
        String s = tv.getText().toString();
        Spannable spannable = new SpannableString(s);
        spannable.setSpan(new ForegroundColorSpan(textColor), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(spannable);
    }

    public static synchronized SpannableStringBuilder GetBuilder(String alltext, String containtext, int speicalcolor) {
        SpannableStringBuilder builder = new SpannableStringBuilder(alltext);
        synchronized (TextViewUtil.class) {
            int startposition = alltext.indexOf(containtext);
            int endposition = startposition + containtext.length();
            if (startposition >= 0) {
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(speicalcolor);
                builder.setSpan(colorSpan, startposition, endposition, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

//		tv.setText(builder);
        return builder;
    }

    /**
     * To set the underscore for TextView
     */
    public static void setUnderLine(TextView tv) {
        if (tv.getText() != null) {
            String udata = tv.getText().toString();
            SpannableString content = new SpannableString(udata);
            content.setSpan(new UnderlineSpan(), 0, udata.length(), 0);
            tv.setText(content);
            content.setSpan(new UnderlineSpan(), 0, udata.length(), 0);
        } else {
            tv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        }
    }

    /**
     * To cancel the TextView's settings.
     */
    public static void clearUnderLine(TextView tv) {
        tv.getPaint().setFlags(0);
    }

    /**
     * @return For full width conversion
     */
    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    /**
     * @return Removing special characters or replacing all Chinese label with an English label
     */
    public static String replaceCharacter(String str) {
        str = str.replaceAll("【", "[").replaceAll("】", "]")
                .replaceAll("！", "!").replaceAll("：", ":").replaceAll("（", "(").replaceAll("（", ")");
        String regEx = "[『』]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

}