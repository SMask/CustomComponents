package com.mask.customcomponents.view.chart;

import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.mask.customcomponents.utils.NumberUtils;

/**
 * 图表工具类
 * Created by lishilin on 2019/11/06
 */
public class ChartUtils {

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
    public static float dpToPx(float dp) {
        if (dp == 0) {
            return 0;
        }
        return dp * getDensity();
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
     * 获取 value最小/最大值(范围)
     *
     * @param value value
     * @param isMax 是否是最大值
     * @return float
     */
    public static float getLimit(float value, boolean isMax) {
        if (isMax) {
            return getLimitMax(value);
        }
        return -getLimitMax(-value);
    }

    /**
     * 获取 value最大值(范围)
     *
     * @param value value
     * @return float
     */
    private static float getLimitMax(float value) {
        if (value <= 0) {
            return 0;
        }
        if (value < 1) {
            // 如果是小数(先根据小数位数求出10的倍数，然后转为整数计算，最后除以倍数确定最终值)
            final int count = NumberUtils.getDecimalDigits(value);// value小数位数
            final int ratio = (int) Math.pow(10, count);// 倍数
            return NumberUtils.divide(getLimitMax(NumberUtils.multiply(value, ratio)), ratio, count);
        }
        final int count = NumberUtils.getIntegerDigits(value);// value整数位数
        final int temp;// 10的value整数位数减一次方(ex: value为123，则此为100)
        if (count < 3) {
            temp = (int) Math.pow(10, count - 1);
        } else {
            temp = ((int) Math.pow(10, count - 1)) / 2;
        }
        // 如果刚好能整除，则直接返回
        if (value % temp == 0) {
            return (int) value;
        }
        return ((int) (value / temp) + 1) * temp;
    }

}
