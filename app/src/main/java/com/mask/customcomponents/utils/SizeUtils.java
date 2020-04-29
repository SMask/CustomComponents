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
     * dp转px
     *
     * @param dp dp
     * @return px
     */
    public static float dpToPx(float dp) {
        return dp * getDensity();
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
     * 返回区间内数据
     *
     * @param value value
     * @param min   min
     * @param max   max
     * @return int
     */
    public static int minMax(int value, int min, int max) {
        return Math.min(Math.max(value, min), max);
    }

    /**
     * 获取当前值百分比(根据区间获取当前值的所占百分比)
     *
     * @param valueCurrent 当前值
     * @param valueStart   起始值
     * @param valueEnd     结束值
     * @return 百分比
     */
    public static float getValuePercent(float valueCurrent, float valueStart, float valueEnd) {
        return getValuePercent(valueCurrent, valueStart, valueEnd, true);
    }

    /**
     * 获取当前值百分比(根据区间获取当前值的所占百分比)
     *
     * @param valueCurrent    当前值
     * @param valueStart      起始值
     * @param valueEnd        结束值
     * @param isLimitInterval 是否限制在当前区间之内
     * @return 百分比
     */
    public static float getValuePercent(float valueCurrent, float valueStart, float valueEnd, boolean isLimitInterval) {
        if (valueCurrent == valueStart) {
            return 0;
        }
        if (valueCurrent == valueEnd) {
            return 1;
        }
        float percent = (valueCurrent - valueStart) / (valueEnd - valueStart);
        if (!isLimitInterval) {
            return percent;
        }
        if (percent < 0) {
            percent = 0;
        }
        if (percent > 1) {
            percent = 1;
        }
        return percent;
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
