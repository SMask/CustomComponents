package com.mask.customcomponents.utils;

import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.PointF;
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
     * 返回区间内数据
     *
     * @param value value
     * @param min   min
     * @param max   max
     * @return int
     */
    public static float minMax(float value, float min, float max) {
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
        return textPaint.measureText(str);
//        textPaint.getTextBounds(str, 0, str.length(), rect);
//        return rect.right - rect.left;
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

    /**
     * sin
     *
     * @param angle 角度
     * @return 对边/斜边
     */
    public static float sin(float angle) {
        return (float) Math.sin(angle * Math.PI / 180);
    }

    /**
     * cos
     *
     * @param angle 角度
     * @return 邻边/斜边
     */
    public static float cos(float angle) {
        return (float) Math.cos(angle * Math.PI / 180);
    }

    /**
     * asin
     *
     * @param value 对边/斜边
     * @return 角度
     */
    public static float asin(float value) {
        return (float) (Math.asin(value) * 180 / Math.PI);
    }

    /**
     * acos
     *
     * @param value 邻边/斜边
     * @return 角度
     */
    public static float acos(float value) {
        return (float) (Math.acos(value) * 180 / Math.PI);
    }

    /**
     * 格式化 角度
     * <p>
     * 小于0或者大于等于360度的角度格式化为0-360
     *
     * @param angle 角度
     * @return 角度
     */
    public static float formatAngle(float angle) {
        if (angle >= 360) {
            return angle % 360;
        }
        while (angle < 0) {
            angle += 360;
        }
        return angle;
    }

    /**
     * 获取 角度
     * <p>
     * 默认圆心坐标(0, 0)
     *
     * @param x x
     * @param y y
     * @return 角度
     */
    public static float getAngle(float x, float y) {
        return (float) (Math.atan2(y, x) * 180 / Math.PI);
    }

    /**
     * 获取 角度
     * <p>
     * 指定圆心坐标
     *
     * @param x       x
     * @param y       y
     * @param centerX 圆心坐标
     * @param centerY 圆心坐标
     * @return 角度
     */
    public static float getAngle(float x, float y, float centerX, float centerY) {
        return getAngle(x - centerX, y - centerY);
    }

    /**
     * 获取 圆上某个角度的坐标
     *
     * @param pointF 圆心的坐标
     * @param radius 半径
     * @param angle  角度
     * @return PointF
     */
    public static PointF getCirclePoint(PointF pointF, float radius, float angle) {
        pointF.offset(radius * cos(angle), radius * sin(angle));
        return pointF;
    }

    /**
     * 获取 正方形映射为圆形的坐标
     *
     * @param pointF 圆心的坐标
     * @param radius 半径
     * @param x      待映射坐标
     * @param y      待映射坐标
     * @return PointF
     */
    public static PointF getPointSquareToCircle(final PointF pointF, final float radius, final float x, float y) {
        final float centerX = pointF.x;
        final float centerY = pointF.y;
        final float xScale = (x - centerX) / radius;
        final float yScale = (y - centerY) / radius;

        // 具体映射逻辑
        final float xResult = (float) (xScale * Math.sqrt(1.0 - yScale * yScale / 2.0));
        final float yResult = (float) (yScale * Math.sqrt(1.0 - xScale * xScale / 2.0));

        pointF.set(xResult * radius + centerX, yResult * radius + centerY);
        return pointF;
    }

    /**
     * 获取 圆形映射为正方形的坐标
     *
     * @param pointF 圆心的坐标
     * @param radius 半径
     * @param x      待映射坐标
     * @param y      待映射坐标
     * @return PointF
     */
    public static PointF getPointCircleToSquare(final PointF pointF, final float radius, final float x, final float y) {
        final float centerX = pointF.x;
        final float centerY = pointF.y;
        final float xScale = (x - centerX) / radius;
        final float yScale = (y - centerY) / radius;

        // 具体映射逻辑
        final double xScale2 = xScale * xScale;
        final double yScale2 = yScale * yScale;
        final double twoSqrt2 = 2.0 * Math.sqrt(2.0);
        final double subTermX = 2.0 + xScale2 - yScale2;
        final double subTermY = 2.0 - xScale2 + yScale2;
        final double termX1 = subTermX + xScale * twoSqrt2;
        final double termX2 = subTermX - xScale * twoSqrt2;
        final double termY1 = subTermY + yScale * twoSqrt2;
        final double termY2 = subTermY - yScale * twoSqrt2;
        final float xResult = (float) (0.5 * Math.sqrt(termX1) - 0.5 * Math.sqrt(termX2));
        final float yResult = (float) (0.5 * Math.sqrt(termY1) - 0.5 * Math.sqrt(termY2));

        pointF.set(xResult * radius + centerX, yResult * radius + centerY);
        return pointF;
    }

}
