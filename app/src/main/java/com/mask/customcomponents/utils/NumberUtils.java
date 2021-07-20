package com.mask.customcomponents.utils;

import android.text.TextUtils;

import java.math.BigDecimal;

/**
 * 数字工具类
 * Created by lishilin on 2019/11/22
 */
public class NumberUtils {

    /**
     * 转换String
     *
     * @param value value
     * @return String
     */
    public static String valueOf(int value) {
        return String.valueOf(value);
    }

    /**
     * 转换String
     *
     * @param value value
     * @return String
     */
    public static String valueOf(long value) {
        return String.valueOf(value);
    }

    /**
     * 转换String
     *
     * @param value value
     * @return String
     */
    public static String valueOf(float value) {
        return valueOf(value, true);
    }

    /**
     * 转换String
     *
     * @param value      value
     * @param isKeepZero 是否保存".0"
     * @return String
     */
    public static String valueOf(float value, boolean isKeepZero) {
        return valueOf(parseDouble(String.valueOf(value)), isKeepZero);
    }

    /**
     * 转换String(不会表示为科学计数法，并且小数点末尾多余的0会被清除)
     *
     * @param value value
     * @return String
     */
    public static String valueOf(double value) {
        return valueOf(value, true);
    }

    /**
     * 转换String(不会表示为科学计数法，并且小数点末尾多余的0会被清除)
     * ex:
     * isKeepZero true
     * 0 -> 0.0
     * 0.0 -> 0.0
     * 0.00 -> 0.0
     * 100 -> 100.0
     * 100.0 -> 100.0
     * 100.00 -> 100.0
     * 0.0000003 -> 0.0000003
     * 0.00000030 -> 0.0000003
     * 0.000000300 -> 0.0000003
     * <p>
     * isKeepZero false
     * 0 -> 0
     * 0.0 -> 0
     * 0.00 -> 0
     * 100 -> 100
     * 100.0 -> 100
     * 100.00 -> 100
     * 0.0000003 -> 0.0000003
     * 0.00000030 -> 0.0000003
     * 0.000000300 -> 0.0000003
     *
     * @param value      value
     * @param isKeepZero 是否保存".0"
     * @return String
     */
    public static String valueOf(double value, boolean isKeepZero) {
        BigDecimal bigDecimal = new BigDecimal(String.valueOf(value));
        if (isKeepZero) {
            double decimal = value - bigDecimal.longValue();
            if (decimal == 0) {
                return bigDecimal.toPlainString();
            }
        }
        return bigDecimal.stripTrailingZeros().toPlainString();
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
        } catch (Exception e) {
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
        } catch (Exception e) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.00;
    }

    /**
     * 获取数字整数位数
     *
     * @param value value
     * @return int
     */
    public static int getIntegerDigits(float value) {
        if (value == 0) {
            return 0;
        }
        int count = (int) Math.log10(Math.abs(value));
        if (count < 0) {
            return 0;
        }
        return count + 1;
    }

    /**
     * 获取数字小数位数
     *
     * @param value value
     * @return int
     */
    public static int getDecimalDigits(float value) {
        BigDecimal bigDecimal = new BigDecimal(String.valueOf(value));
        String numStr = bigDecimal.stripTrailingZeros().toPlainString();
        int pointIndex = numStr.lastIndexOf(".");
        if (pointIndex < 0 || pointIndex >= (numStr.length() - 1)) {
            return 0;
        }
        numStr = numStr.substring(pointIndex + 1);
        return numStr.length();
    }

    /**
     * 获取小数(四舍五入)
     *
     * @param num   数字
     * @param count 位数
     * @return 小数
     */
    public static float getRoundDecimal(float num, int count) {
        return new BigDecimal(String.valueOf(num)).setScale(count, BigDecimal.ROUND_HALF_UP).floatValue();
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
     * 乘
     *
     * @param num1  num1
     * @param num2  num2
     * @param count 小数位数
     * @return float
     */
    public static String multiply(String num1, String num2, int count) {
        if (TextUtils.isEmpty(num1) || (num1.length() == 1 && ".".equals(num1))) {
            num1 = "0";
        }
        if (TextUtils.isEmpty(num2) || (num2.length() == 1 && ".".equals(num2))) {
            num2 = "0";
        }
        BigDecimal temp1 = new BigDecimal(num1);
        BigDecimal temp2 = new BigDecimal(num2);
        return temp1.multiply(temp2).setScale(count, BigDecimal.ROUND_HALF_UP).toPlainString();
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
        if (num2 == 0) {
            return 0;
        }
        BigDecimal temp1 = new BigDecimal(String.valueOf(num1));
        BigDecimal temp2 = new BigDecimal(String.valueOf(num2));
        return temp1.divide(temp2, count, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    /**
     * 除
     *
     * @param num1  被除数
     * @param num2  除数
     * @param count 小数位数
     * @return float
     */
    public static String divide(String num1, String num2, int count) {
        if (TextUtils.isEmpty(num1) || (num1.length() == 1 && ".".equals(num1))) {
            num1 = "0";
        }
        if (parseDouble(num2) == 0) {
            return "0";
        }
        BigDecimal temp1 = new BigDecimal(num1);
        BigDecimal temp2 = new BigDecimal(num2);
        return temp1.divide(temp2, count, BigDecimal.ROUND_HALF_UP).toPlainString();
    }

}
