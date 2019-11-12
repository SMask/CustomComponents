package com.mask.customcomponents.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 水波纹扩散View
 * Create By lishilin On 2019/6/6
 */
public class RippleView extends View {

    private int color = Color.parseColor("#FF67C6E5");// 颜色

    private float radiusInit;// 初始化半径
    private float radiusMax;// 最大半径
    private float radiusMaxRatio = 1f;// 最大半径屏幕比例

    private long duration = 5000;// 一个波纹从创建到消失的持续时间
    private long speed = 500;// 波纹的创建速度，每xx毫秒创建一个

    private boolean isRunning;// 是否正在运行
    private long lastCreateTime;// 最后一次创建波纹的时间

    private Paint paint;// 画笔

    private Interpolator interpolator = new LinearOutSlowInInterpolator();// 动画插值器

    private List<Circle> circleList = new ArrayList<>();// 圆集合

    private boolean isInitData;// 是否已经初始化数据

    private Runnable createCircleRunnable = new Runnable() {
        @Override
        public void run() {
            if (isRunning) {
                createCircle();
                postDelayed(createCircleRunnable, speed);
            }
        }
    };

    /**
     * 创建圆
     */
    private void createCircle() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastCreateTime < speed) {
            return;
        }
        Circle circle = new Circle();
        circleList.add(circle);
        invalidate();
        lastCreateTime = currentTime;
    }

    /**
     * 圆 实体类
     */
    private class Circle {

        private long createTime;

        Circle() {
            createTime = System.currentTimeMillis();
        }

        int getAlpha() {
            float percent = (getCurrentRadius() - radiusInit) / (radiusMax - radiusInit);
            return (int) (255 - interpolator.getInterpolation(percent) * 255);
        }

        float getCurrentRadius() {
            float percent = (System.currentTimeMillis() - createTime) * 1.0f / duration;
            return radiusInit + interpolator.getInterpolation(percent) * (radiusMax - radiusInit);
        }
    }

    public RippleView(Context context) {
        super(context);
        init(context);
    }

    public RippleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RippleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public RippleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    /**
     * 初始化
     *
     * @param context context
     */
    private void init(Context context) {
        if (isInitData) {
            return;
        }

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color);

        isInitData = true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        radiusMax = Math.min(w, h) * radiusMaxRatio / 2.0f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawCircle(canvas);
    }

    /**
     * 画圆
     *
     * @param canvas canvas
     */
    private void drawCircle(Canvas canvas) {
        Iterator<Circle> iterator = circleList.iterator();
        while (iterator.hasNext()) {
            Circle circle = iterator.next();
            float radius = circle.getCurrentRadius();
            if (System.currentTimeMillis() - circle.createTime < duration) {
                paint.setAlpha(circle.getAlpha());
                canvas.drawCircle(getWidth() * 0.5f, getHeight() * 0.5f, radius, paint);
            } else {
                iterator.remove();
            }
        }
        if (circleList.size() > 0) {
            postInvalidateDelayed(10);
        }
    }

    /**
     * 开始
     */
    public void start() {
        if (!isRunning) {
            isRunning = true;
            createCircleRunnable.run();
        }
    }

    /**
     * 缓慢停止
     */
    public void stop() {
        isRunning = false;
    }

    /**
     * 立即停止
     */
    public void stopImmediately() {
        isRunning = false;
        circleList.clear();
        invalidate();
    }

}
