package com.mask.customcomponents.view.chart;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.PathInterpolator;

import androidx.annotation.AttrRes;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;

import com.mask.customcomponents.view.bezier.BezierUtils;
import com.mask.customcomponents.view.bezier.CtrlPoint;

import java.util.ArrayList;
import java.util.List;

/**
 * 曲线图
 * Created by lishilin on 2022/03/30
 */
public class CurveChartView extends View {

    private Context context;

    // 控件内间距
    private float paddingLeft;
    private float paddingTop;
    private float paddingRight;
    private float paddingBottom;

    private final RectF viewRectF = new RectF();// 控件位置
    private final RectF graphRectF = new RectF();// 绘图区位置(减去内间距之后的位置)


    public PathInterpolator interpolatorEnter;// 自定义动画插值器 三阶贝塞尔曲线 入场
    public PathInterpolator interpolatorExit;// 自定义动画插值器 三阶贝塞尔曲线 退场

    private ValueAnimator animEnter;// 动画 入场
    private ValueAnimator animExit;// 动画 退场

    private boolean isAnimEnterRunning;// 动画是否运行中 入场
    private boolean isAnimExitRunning;// 动画是否运行中 退场

    private float progressStart = 0;// 动画起始进度
    private float progressEnd = 1200;// 动画结束进度
    private float progress = progressStart;// 当前动画进度


    private float markMin;// 最小刻度
    private final PointF markMinPoint = new PointF();// 最小刻度 坐标
    private float markMax;// 最大刻度
    private final PointF markMaxPoint = new PointF();// 最大刻度 坐标
    private final int markNum = 6;// 刻度线的数量(水平线的数量)
    private final List<PointF> markPointList = new ArrayList<>();// 刻度的坐标集合


    private final List<PointF> pointList = new ArrayList<>();// 数据点坐标集合
    private final List<CtrlPoint> ctrlPointList = new ArrayList<>();// 控制点集合


    private final List<ChartData> dataList = new ArrayList<>();// 数据集合


    private Paint paintMark;// Paint 刻度
    private Paint paintGraph;// Paint 图形

    private final Path path = new Path();// 路径
    private final Path pathGraph = new Path();// 图形路径

    private final PathMeasure animPathMeasure = new PathMeasure();// 绘制动画截取Path用


    private boolean isInitData;// 是否已经初始化数据

    public CurveChartView(Context context) {
        super(context);
        init(context, null);
    }

    public CurveChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CurveChartView(Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public CurveChartView(Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    /**
     * 初始化
     *
     * @param context context
     * @param attrs   attrs
     */
    private void init(Context context, final AttributeSet attrs) {
        if (isInitData) {
            return;
        }

        this.context = context;

        initAnim();

        paintMark = new Paint();
        paintMark.setAntiAlias(true);// 设置抗锯齿
        paintMark.setStyle(Paint.Style.STROKE);// 设置线模式
        paintMark.setStrokeWidth(ChartUtils.dpToPx(1));// 设置线宽
        paintMark.setColor(0x80FFFFFF);// 设置画笔颜色，也就是绘制线的颜色
        paintMark.setPathEffect(new DashPathEffect(new float[]{ChartUtils.dpToPx(3), ChartUtils.dpToPx(3)}, 0));// 设置虚线样式

        paintGraph = new Paint();
        paintGraph.setAntiAlias(true);
        paintGraph.setStyle(Paint.Style.STROKE);
        paintGraph.setStrokeWidth(ChartUtils.dpToPx(2));
        paintGraph.setColor(0xFFFFFFFF);

        isInitData = true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        paddingLeft = paddingRight = w * 0.04f;
        paddingTop = paddingBottom = h * 0.04f;

        viewRectF.set(0, 0, w, h);

        graphRectF.set(viewRectF);
        graphRectF.left += paddingLeft;
        graphRectF.top += paddingTop;
        graphRectF.right -= paddingRight;
        graphRectF.bottom -= paddingBottom;

        refreshDimen();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 设置背景色，方便查看
        canvas.drawColor(0xFF212833);

        drawMark(canvas);
        drawGraph(canvas);
    }

    /* ********************************************* 绘制相关 **********************************************/

    /**
     * 绘制 刻度
     *
     * @param canvas canvas
     */
    private void drawMark(Canvas canvas) {
        final float percent = getAnimPercent(0, 400);
        if (percent <= 0) {
            return;
        }

        for (int i = 0; i < markPointList.size(); i++) {
            final PointF point = markPointList.get(i);

            final float startX = graphRectF.left;
            final float startY = point.y;
            final float endX = graphRectF.right;
            final float endY = point.y;

            final float percentX = ChartUtils.getPercentValue(percent, startX, endX);
            final float percentY = ChartUtils.getPercentValue(percent, startY, endY);

            path.reset();
            path.moveTo(startX, startY);
            path.lineTo(percentX, percentY);

            canvas.drawPath(path, paintMark);
        }
    }

    /**
     * 绘制 图形
     *
     * @param canvas canvas
     */
    private void drawGraph(Canvas canvas) {
        final float percent = getAnimPercent(0, 1200);
        if (percent <= 0) {
            return;
        }

        path.reset();
        if (percent < 1) {
            animPathMeasure.setPath(pathGraph, false);
            animPathMeasure.getSegment(0, animPathMeasure.getLength() * percent, this.path, true);
        } else {
            path.set(pathGraph);
        }

        canvas.drawPath(path, paintGraph);
    }
    /* ********************************************* 绘制相关 **********************************************/

    /* ********************************************* 数据、尺寸相关 **********************************************/

    /**
     * 刷新 数据
     */
    private void refreshData() {
        // 计算刻度
        float valueMin = 0;
        float valueMax = 1;
        for (int i = 0; i < dataList.size(); i++) {
            final float value = dataList.get(i).getValue();
            if (i == 0) {
                valueMin = valueMax = value;
            } else {
                valueMin = Math.min(valueMin, value);
                valueMax = Math.max(valueMax, value);
            }
        }
        this.markMin = ChartUtils.getLimit(valueMin, false);
        this.markMax = ChartUtils.getLimit(valueMax, true);
    }

    /**
     * 刷新 尺寸
     */
    private void refreshDimen() {
        // 计算刻度 坐标
        markMinPoint.set(graphRectF.left, graphRectF.bottom);
        markMaxPoint.set(graphRectF.left, graphRectF.top);

        while (markPointList.size() < markNum) {
            markPointList.add(new PointF());
        }
        while (markPointList.size() > markNum) {
            markPointList.remove(markPointList.size() - 1);
        }
        for (int i = 0; i < markPointList.size(); i++) {
            final PointF point = markPointList.get(i);

            point.x = graphRectF.left;
            point.y = ChartUtils.getPercentValue(i * 1.0f / (markNum - 1), markMinPoint.y, markMaxPoint.y);
        }

        // 计算数据点 坐标
        final int dataNum = dataList.size();
        while (pointList.size() < dataNum) {
            pointList.add(new PointF());
        }
        while (pointList.size() > dataNum) {
            pointList.remove(pointList.size() - 1);
        }
        final float itemSize = graphRectF.width() / dataNum;
        for (int i = 0; i < pointList.size(); i++) {
            final float value = dataList.get(i).getValue();
            final PointF point = pointList.get(i);

            point.x = graphRectF.left + itemSize * i + itemSize / 2;
            point.y = ChartUtils.getPercentValue(ChartUtils.getValuePercent(value, markMin, markMax), markMinPoint.y, markMaxPoint.y);
        }

        // 计算控制点 坐标
        ctrlPointList.clear();
        ctrlPointList.addAll(BezierUtils.getControlPointList(pointList));

        // 计算路径
        pathGraph.reset();
        for (int i = 0; i < pointList.size(); i++) {
            final PointF point = pointList.get(i);

            if (i == 0) {// 移动到起始点
                pathGraph.moveTo(point.x, point.y);
            } else {
                if (!ctrlPointList.isEmpty()) {
                    // 三阶贝塞尔曲线
                    final CtrlPoint ctrlPoint = ctrlPointList.get(i - 1);
                    ctrlPoint.cubicTo(pathGraph, point);
                } else {
                    pathGraph.lineTo(point.x, point.y);
                }
            }
        }
    }
    /* ********************************************* 数据、尺寸相关 **********************************************/

    /* ********************************************* 动画相关 **********************************************/

    /**
     * 初始化 动画
     */
    private void initAnim() {
        interpolatorEnter = new PathInterpolator(0.35f, 0.0f, 0.35f, 1.0f);
        interpolatorExit = new PathInterpolator(0.7f, 0.03f, 0.3f, 1.0f);

        final Interpolator interpolator = new LinearInterpolator();
        final ValueAnimator.AnimatorUpdateListener animatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                progress = (float) animation.getAnimatedValue();

                invalidate();
            }
        };
        final AnimatorListenerAdapter animatorListenerAdapter = new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                boolean isEnter = animation.equals(animEnter);
                boolean isExit = animation.equals(animExit);
                if (isEnter) {
                    isAnimEnterRunning = true;
                }
                if (isExit) {
                    isAnimExitRunning = true;
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                boolean isEnter = animation.equals(animEnter);
                boolean isExit = animation.equals(animExit);
                if (isEnter) {
                    isAnimEnterRunning = false;
                }
                if (isExit) {
                    isAnimExitRunning = false;
                }
            }
        };

        animEnter = new ValueAnimator();
        animEnter.setInterpolator(interpolator);
        animEnter.addUpdateListener(animatorUpdateListener);
        animEnter.addListener(animatorListenerAdapter);

        animExit = new ValueAnimator();
        animExit.setInterpolator(interpolator);
        animExit.addUpdateListener(animatorUpdateListener);
        animExit.addListener(animatorListenerAdapter);
    }

    /**
     * 获取 动画时长
     *
     * @param isAnimEnter 是否是入场动画
     * @return long
     */
    private long getAnimDuration(boolean isAnimEnter) {
        long duration;

        if (isAnimEnter) {
            duration = (long) (progressEnd - progress);
        } else {
            // 退场动画时长减半
            duration = (long) ((progress - progressStart) / 2);
        }

        if (duration < 0) {
            duration = 0;
        }

        return duration;
    }

    /**
     * 获取 动画进度
     *
     * @param progressStart 起始值
     * @param progressEnd   结束值
     * @return percent
     */
    public float getAnimPercent(float progressStart, float progressEnd) {
        final float percent = ChartUtils.getValuePercent(progress, progressStart, progressEnd);
        if (percent <= 0) {
            return 0;
        }
        if (isAnimEnterRunning) {
            return interpolatorEnter.getInterpolation(percent);
        }
        if (isAnimExitRunning) {
            return interpolatorExit.getInterpolation(percent);
        }
        return percent;
    }
    /* ********************************************* 动画相关 **********************************************/

    /* ********************************************* 外部调用 **********************************************/

    /**
     * 设置 数据
     *
     * @param dataList dataList
     */
    public void setData(List<ChartData> dataList) {
        this.dataList.clear();
        if (dataList != null) {
            this.dataList.addAll(dataList);
        }
        refreshData();
        refreshDimen();
    }

    /**
     * 开始 入场动画
     */
    public void startAnimEnter() {
        if (animEnter.isRunning()) {
            return;
        }
        if (animExit.isRunning()) {
            animExit.cancel();
        }
        if (progress >= progressEnd) {
            return;
        }
        if (progress < progressStart) {
            progress = progressStart;
        }
        animEnter.setFloatValues(progress, progressEnd);
        animEnter.setDuration(getAnimDuration(true));
        animEnter.start();
    }

    /**
     * 开始 退场动画
     */
    public void startAnimExit() {
        if (animExit.isRunning()) {
            return;
        }
        if (animEnter.isRunning()) {
            animEnter.cancel();
        }
        if (progress <= progressStart) {
            return;
        }
        if (progress > progressEnd) {
            progress = progressEnd;
        }
        animExit.setFloatValues(progress, progressStart);
        animExit.setDuration(getAnimDuration(false));
        animExit.start();
    }
    /* ********************************************* 外部调用 **********************************************/

}
