package com.mask.customcomponents.view.bezier;

import android.graphics.PointF;

import java.util.ArrayList;
import java.util.List;

/**
 * 贝塞尔 工具类
 * Created by lishilin on 2019/11/18
 */
public class BezierUtils {

    private static final float MULTIPLE = 0.33f;// 倍数

    /**
     * 获取 当前点的前后控制点
     *
     * @param point     point
     * @param pointPrev pointPrev
     * @param pointNext pointNext
     * @return PointF[]
     */
    private static PointF[] getControlPoint(PointF point, PointF pointPrev, PointF pointNext) {
        final PointF[] pointArr = new PointF[2];

        final PointF v0 = VectorUtils.multiply(VectorUtils.subtract(pointNext, pointPrev), MULTIPLE);
        float distance0 = VectorUtils.distance(point, pointPrev);
        float distance1 = VectorUtils.distance(point, pointNext);
        final float distanceSum = distance0 + distance1;
        if (distanceSum != 0) {
            distance0 = distance0 / distanceSum;
            distance1 = distance1 / distanceSum;
        }
        final PointF v1 = VectorUtils.multiply(v0, -distance0);
        final PointF v2 = VectorUtils.multiply(v0, distance1);
        final PointF point1 = VectorUtils.add(point, v1);
        final PointF point2 = VectorUtils.add(point, v2);
        pointArr[0] = point1;
        pointArr[1] = point2;

        return pointArr;
    }

    /**
     * 获取 控制点集合
     *
     * @param pointList pointList
     * @return List<CtrlPoint>
     */
    public static List<CtrlPoint> getControlPointList(List<? extends PointF> pointList) {
        final List<CtrlPoint> ctrlPointList = new ArrayList<>();

        final int size = pointList.size();

        // 小于3个点则没有控制点
        if (size < 3) {
            return ctrlPointList;
        }

        do {
            ctrlPointList.add(new CtrlPoint());
        } while (ctrlPointList.size() < size - 1);

        for (int i = 0; i < size; i++) {
            final PointF point = pointList.get(i);

            if (i == 0) {
                final CtrlPoint ctrlPoint = ctrlPointList.get(i);
                ctrlPoint.setCtrlPoint1(point);

            } else if (i == size - 1) {
                final CtrlPoint ctrlPointPrev = ctrlPointList.get(i - 1);
                ctrlPointPrev.setCtrlPoint2(point);

            } else {
                final PointF pointPrev = pointList.get(i - 1);
                final PointF pointNext = pointList.get(i + 1);

                final PointF[] pointArr = getControlPoint(point, pointPrev, pointNext);

                final CtrlPoint ctrlPointPrev = ctrlPointList.get(i - 1);
                final CtrlPoint ctrlPoint = ctrlPointList.get(i);

                ctrlPointPrev.setCtrlPoint2(pointArr[0]);
                ctrlPoint.setCtrlPoint1(pointArr[1]);
            }
        }

        return ctrlPointList;
    }


}
