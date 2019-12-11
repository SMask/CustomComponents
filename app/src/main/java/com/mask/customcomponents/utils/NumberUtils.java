package com.mask.customcomponents.utils;

import android.text.TextUtils;

import java.math.BigDecimal;

/**
 * 数字工具类
 * Created by lishilin on 2019/11/22
 */
public class NumberUtils {

    /**
     * 格式化(默认格式化方法)
     *
     * @param num num
     * @return String
     */
    public static String format(float num) {
        if (Float.isNaN(num)) {
            return String.valueOf(0);
        }
        int intValue = (int) num;
        float decimal = num - intValue;
        if (decimal == 0) {
            return String.valueOf(intValue);
        }
        return String.valueOf(getRoundDecimal(num, 2));
    }

    /**
     * 解析(默认解析方法)
     *
     * @param str str
     * @return float
     */
    public static float parse(CharSequence str) {
        return parseFloat(str);
    }

    /**
     * 解析Int
     *
     * @param str str
     * @return int
     */
    public static int parseInt(CharSequence str) {
        if (TextUtils.isEmpty(str)) {
            return 0;
        }
        try {
            return Integer.parseInt(str.toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 解析Float
     *
     * @param str str
     * @return float
     */
    public static float parseFloat(CharSequence str) {
        if (TextUtils.isEmpty(str)) {
            return 0.0f;
        }
        try {
            return Float.parseFloat(str.toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return 0.0f;
    }

    /**
     * 解析Double
     *
     * @param str str
     * @return double
     */
    public static double parseDouble(CharSequence str) {
        if (TextUtils.isEmpty(str)) {
            return 0.00;
        }
        try {
            return Double.parseDouble(str.toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return 0.00;
    }

    /**
     * 获取小数(四舍五入)
     *
     * @param num   数字
     * @param count 位数
     * @return 小数
     */
    public static float getRoundDecimal(float num, int count) {
        return new BigDecimal(num).setScale(count, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    /**
     * 加
     *
     * @param num1 num1
     * @param num2 num2
     * @return float
     */
    public static float add(float num1, float num2) {
        BigDecimal temp1 = new BigDecimal(String.valueOf(num1));
        BigDecimal temp2 = new BigDecimal(String.valueOf(num2));
        return temp1.add(temp2).floatValue();
    }

    /**
     * 减
     *
     * @param num1 被减数
     * @param num2 减数
     * @return float
     */
    public static float subtract(float num1, float num2) {
        BigDecimal temp1 = new BigDecimal(String.valueOf(num1));
        BigDecimal temp2 = new BigDecimal(String.valueOf(num2));
        return temp1.subtract(temp2).floatValue();
    }

    /**
     * 乘
     *
     * @param num1 num1
     * @param num2 num2
     * @return float
     */
    public static float multiply(float num1, float num2) {
        BigDecimal temp1 = new BigDecimal(String.valueOf(num1));
        BigDecimal temp2 = new BigDecimal(String.valueOf(num2));
        return temp1.multiply(temp2).floatValue();
    }

    /**
     * 除
     *
     * @param num1  被除数
     * @param num2  除数
     * @param count 小数位数
     * @return float
     */
    public static float divide(float num1, float num2, int count) {
        BigDecimal temp1 = new BigDecimal(String.valueOf(num1));
        BigDecimal temp2 = new BigDecimal(String.valueOf(num2));
        return temp1.divide(temp2, count, BigDecimal.ROUND_HALF_UP).floatValue();
    }

}
