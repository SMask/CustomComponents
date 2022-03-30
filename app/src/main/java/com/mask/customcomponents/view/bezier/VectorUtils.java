package com.mask.customcomponents.view.bezier;

import android.graphics.PointF;

/**
 * 贝塞尔 向量 工具类
 * Created by lishilin on 2019/11/18
 */
public class VectorUtils {

    private static final PointF ZERO = new PointF(0, 0);

    /**
     * 求向量长度
     *
     * @param point point
     * @return float
     */
    public static float length(PointF point) {
        return (float) Math.sqrt(point.x * point.x + point.y * point.y);
    }

    /**
     * 转单位向量
     *
     * @param point point
     * @return PointF
     */
    public static PointF normalize(PointF point) {
        if (point.equals(ZERO)) {
            return ZERO;
        }
        final float length = length(point);
        if (length < 0.000000001) {
            return ZERO;
        }
        float inv = 1.0f / length;
        return new PointF(point.x * inv, point.y * inv);
    }

    /**
     * 向量距离
     *
     * @param point1 point1
     * @param point2 point2
     * @return PointF
     */
    public static float distance(PointF point1, PointF point2) {
        return (float) Math.sqrt((point1.x - point2.x) * (point1.x - point2.x) + (point1.y - point2.y) * (point1.y - point2.y));
    }

    /**
     * 向量相加
     *
     * @param point1 point1
     * @param point2 point2
     * @return PointF
     */
    public static PointF add(PointF point1, PointF point2) {
        return new PointF(point1.x + point2.x, point1.y + point2.y);
    }

    /**
     * 向量相减
     *
     * @param point1 point1
     * @param point2 point2
     * @return PointF
     */
    public static PointF subtract(PointF point1, PointF point2) {
        return new PointF(point1.x - point2.x, point1.y - point2.y);
    }

    /**
     * 向量缩放
     *
     * @param point    point
     * @param multiple 倍数
     * @return PointF
     */
    public static PointF multiply(PointF point, float multiple) {
        return new PointF(point.x * multiple, point.y * multiple);
    }

    /**
     * 点积、数量积
     *
     * @param point1 point1
     * @param point2 point2
     * @return PointF
     */
    public static float dot(PointF point1, PointF point2) {
        return point1.x * point2.x + point1.y * point2.y;
    }

    /**
     * 两个向量的夹角
     *
     * @param point1 point1
     * @param point2 point2
     * @return PointF
     */
    public static float angle(PointF point1, PointF point2) {
        if (point1.equals(ZERO) || point2.equals(ZERO)) {
            return 0;
        }
        final float value = dot(point1, point2) / (length(point1) * length(point2));
        return (float) (Math.acos(value) * 180.0f / Math.PI);
    }
}
