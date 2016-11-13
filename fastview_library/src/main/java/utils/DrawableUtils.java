package utils;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.NonNull;

import com.yan.fastview_library.static_userful.ShapeType;

public class DrawableUtils {

    public static StateListDrawable getStateCorRectDrawable(int color, int radius) {
        return addToStateDrawable(getCorRectDrawable(color, radius), getCorRectDrawable(getPressColor(color), radius));
    }

    public static StateListDrawable getStateCorRectDrawable(int color, int pressColor, int radius) {
        return addToStateDrawable(getCorRectDrawable(color, radius), getCorRectDrawable(getPressColor(pressColor), radius));
    }

    public static StateListDrawable getStateOvalDrawable(int color) {
        return addToStateDrawable(getOvalDrawable(color), getOvalDrawable(getPressColor(color)));
    }

    public static StateListDrawable getStateOvalDrawable(int color, int pressColor) {
        return addToStateDrawable(getOvalDrawable(color), getOvalDrawable(getPressColor(pressColor)));
    }

    @NonNull
    public static StateListDrawable getStateRectDrawable(int color, int pressColor) {
        return addToStateDrawable(getRectDrawable(color), getRectDrawable(pressColor));
    }

    @NonNull
    public static StateListDrawable getStateRectDrawable(int color) {
        return addToStateDrawable(getRectDrawable(color), getRectDrawable(getPressColor(color)));
    }

    @NonNull
    private static StateListDrawable addToStateDrawable(GradientDrawable normalDrawable, GradientDrawable pressDrawable) {
        StateListDrawable sd = new StateListDrawable();
        sd.addState(new int[]{android.R.attr.state_enabled, android.R.attr.state_focused}, pressDrawable);
        sd.addState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled}, pressDrawable);
        sd.addState(new int[]{android.R.attr.state_focused}, pressDrawable);
        sd.addState(new int[]{android.R.attr.state_pressed}, pressDrawable);
        sd.addState(new int[]{android.R.attr.state_enabled}, normalDrawable);
        sd.addState(new int[]{}, normalDrawable);
        return sd;
    }


    public static int getPressColor(int color) {
        int alpha = (int) (Color.alpha(color) * 0.8);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        if (red>200&&green>200&&blue>200){
            red= (int) (red*0.9);
            green= (int) (green*0.9);
            blue= (int) (blue*0.9);
        }
        color = Color.argb(alpha, red, green, blue);
        return color;
    }


    public static GradientDrawable getRectDrawable(int color) {
        return getGradientDrawable(ShapeType.RECT, color, 0);
    }


    public static GradientDrawable getOvalDrawable(int color) {
        return getGradientDrawable(ShapeType.OVAL, color, 0);
    }


    public static GradientDrawable getCorRectDrawable(int color, int radius) {
        return getGradientDrawable(ShapeType.CORNERRECT, color, radius);
    }


    @NonNull
    public static GradientDrawable getGradientDrawable(int shapeType, int color, int radius) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        if (shapeType == ShapeType.RECT) {
            gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        } else if (shapeType == ShapeType.OVAL) {
            gradientDrawable.setShape(GradientDrawable.OVAL);
        } else if (shapeType == ShapeType.CORNERRECT) {
            gradientDrawable.setShape(GradientDrawable.RECTANGLE);
            gradientDrawable.setCornerRadii(new float[]{radius, radius, radius, radius});
        }
        gradientDrawable.setDither(true);
        gradientDrawable.setColor(color);
        gradientDrawable.setCornerRadius(radius);
        return gradientDrawable;
    }
}
