package com.mask.customcomponents.utils;

import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.DisplayMetrics;

public class SizeUtils {

    /**
     * 获取屏幕信息
     *
     * @return DisplayMetrics
     */
    public static DisplayMetrics getScreenDisplayMetrics() {
        return Resources.getSystem().getDisplayMetrics();
    }

    /**
     * 获取屏幕密度
     *
     * @return 屏幕密度
     */
    public static float getDensity() {
        DisplayMetrics display = getScreenDisplayMetrics();
        return display.density;
    }

    /**
     * dp转px
     *
     * @param dp dp
     * @return px
     */
    public static int dpToPx(int dp) {
        return (int) (dp * getDensity() + 0.5f);
    }

    /**
     * px转dp
     *
     * @param px px
     * @return dp
     */
    public static int pxToDp(int px) {
        return (int) (px / getDensity() + 0.5f);
    }

    /**
     * 获取当前百分比值(根据区间获取当前所占百分比的值)
     *
     * @param percent    当前百分比
     * @param valueStart 起始值
     * @param valueEnd   结束值
     * @return float
     */
    public static float getPercentValue(float percent, float valueStart, float valueEnd) {
        if (percent == 0) {
            return valueStart;
        }
        if (percent == 1) {
            return valueEnd;
        }
        return valueStart + (valueEnd - valueStart) * percent;
    }

    /**
     * 获取文字宽(此为贴边宽度，不需要贴边使用{@link Paint#measureText(String)} ()}来获取)
     *
     * @param str       str
     * @param textPaint textPaint
     * @param rect      rect
     * @return 文字宽
     */
    public static float getTextWidth(String str, Paint textPaint, Rect rect) {
        textPaint.getTextBounds(str, 0, str.length(), rect);
        return rect.right - rect.left;
    }

    /**
     * 获取文字高(此为贴边高度，不需要贴边使用{@link Paint#getFontMetrics()}来获取)
     *
     * @param str       str
     * @param textPaint textPaint
     * @param rect      rect
     * @return 文字高
     */
    public static float getTextHeight(String str, Paint textPaint, Rect rect) {
        textPaint.getTextBounds(str, 0, str.length(), rect);
        return rect.bottom - rect.top;
    }

    /**
     * 获取文字基线位置(Top是相对于Baseline的值，如果不需要贴边使用{@link Paint#getFontMetrics()}来获取)
     *
     * @param str       str
     * @param textPaint textPaint
     * @param rect      rect
     * @return 文字高
     */
    public static float getTextBaseline(String str, Paint textPaint, Rect rect) {
        textPaint.getTextBounds(str, 0, str.length(), rect);
        return -rect.top;
    }

}
