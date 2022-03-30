package com.mask.customcomponents.view.bezier;

import android.graphics.Path;
import android.graphics.PointF;

/**
 * 控制点
 * Created by lishilin on 2020/12/2
 */
public class CtrlPoint {

    private final PointF ctrlPoint1 = new PointF();// 控制点1
    private final PointF ctrlPoint2 = new PointF();// 控制点2

    public void setCtrlPoint1(PointF ctrlPoint1) {
        this.ctrlPoint1.set(ctrlPoint1);
    }

    public void setCtrlPoint2(PointF ctrlPoint2) {
        this.ctrlPoint2.set(ctrlPoint2);
    }

    /**
     * 三阶贝塞尔曲线
     *
     * @param path  path
     * @param point point
     */
    public void cubicTo(Path path, PointF point) {
        path.cubicTo(ctrlPoint1.x, ctrlPoint1.y, ctrlPoint2.x, ctrlPoint2.y, point.x, point.y);
    }

}
