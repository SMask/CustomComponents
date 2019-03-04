package com.mask.customcomponents.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.mask.customcomponents.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 折线图
 * Created by lishilin
 */
public class LineChartView extends View {

    private int viewWith;// 控件宽
    private int viewHeight;// 控件高

    private final int textSize = dpToPx(14);// 文字大小

    private int leftOffset;// 左偏移量(预留最大值、最小值显示空间)
    private int rightOffset;// 右偏移量(预留最后一个时间、最后一个ScorePop显示空间)
    private final int topOffset = dpToPx(8);// 上间距
    private final int bottomOffset = topOffset;// 下间距

    private int minScoreYCoordinate;// 最小值Y轴坐标
    private int maxScoreYCoordinate;// 最大值Y轴坐标

    private int timeLineYCoordinate;// X轴上方直线Y坐标

    private final int scoreTimeLineOffset = Math.max(dpToPx(16), textSize / 2 + bottomOffset);// 最小分数与X轴上方直线间距
    private final int defaultOffset = dpToPx(4);// 默认间距(文字的外间距、刻度尺高度、浮窗三角箭头直角边长)
    private final int popOffset = topOffset;// 浮窗与原点间距

    private final int brokenLineWith = dpToPx(1);// 折线宽

    private int selectRingBigColor;// 选中圆环颜色(大圆环)
    private int selectRingSmallColor;// 选中圆环颜色(小圆环)

    private int brokenLineColor;// 折线颜色
    private int straightLineColor;// 最小、最大值虚线及X轴上方直线颜色
    private int textNormalColor;// 默认文字颜色

    private Paint brokenPaint;// 折线、圆点
    private Paint straightPaint;// X轴上方直线
    private Paint dottedPaint;// 虚线
    private Paint textPaint;// 文字
    private Paint gradientPaint;// 渐变

    private Path brokenPath;// 线(折线、选中悬浮窗)

    private final List<Point> scorePoints = new ArrayList<>();// 折线上的点

    private int[] scoreArr = new int[]{80, 61, 115, 141, 110, 120};// 数值数组
    private String[] timeArr = new String[]{"6月", "7月", "8月", "9月", "10月", "11月"};// 时间数组
    private int minScore;// 最小值
    private int maxScore;// 最大值

    private boolean isInterceptTouch = false;// 是否拦截触摸动作

    private boolean isAutoMinMax = true;// 是否自动计算最小、最大值

    private int totalCount;// 原点数量
    private int selectIndex;// 选中的原点下标

    public LineChartView(Context context) {
        super(context);
        init();
    }

    public LineChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LineChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化画笔等
     */
    private void init() {
        Context context = getContext();

        selectRingBigColor = ContextCompat.getColor(context, R.color.main_tran_10);
        selectRingSmallColor = ContextCompat.getColor(context, R.color.main_tran_38);

        brokenLineColor = ContextCompat.getColor(context, R.color.main);
        straightLineColor = ContextCompat.getColor(context, R.color.gray);
        textNormalColor = ContextCompat.getColor(context, R.color.main_font_lv_1);

        brokenPath = new Path();

        brokenPaint = new Paint();
        brokenPaint.setAntiAlias(true);
        brokenPaint.setStyle(Paint.Style.STROKE);
        brokenPaint.setStrokeWidth(brokenLineWith);
        brokenPaint.setStrokeCap(Paint.Cap.ROUND);

        straightPaint = new Paint();
        straightPaint.setAntiAlias(true);
        straightPaint.setStyle(Paint.Style.STROKE);
        straightPaint.setStrokeWidth(brokenLineWith);
        straightPaint.setColor((straightLineColor));
        straightPaint.setStrokeCap(Paint.Cap.ROUND);

        dottedPaint = new Paint();
        dottedPaint.setAntiAlias(true);
        dottedPaint.setStyle(Paint.Style.STROKE);
        dottedPaint.setStrokeWidth(brokenLineWith);
        dottedPaint.setColor((straightLineColor));
        dottedPaint.setStrokeCap(Paint.Cap.ROUND);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor((textNormalColor));
        textPaint.setTextSize(textSize);

        gradientPaint = new Paint();
        gradientPaint.setAntiAlias(true);
        gradientPaint.setStyle(Paint.Style.FILL);
        gradientPaint.setStrokeWidth(brokenLineWith);
        gradientPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        int[][] resultArr = getMinMax(scoreArr);

        // 计算原点数量
        totalCount = Math.min(scoreArr.length, timeArr.length);

        // 默认选中最大值
        selectIndex = isInterceptTouch ? -1 : resultArr[1][1];

        // 自动计算最小、最大值
        if (isAutoMinMax) {
            int min = resultArr[0][0];
            int max = resultArr[0][1];
            minScore = floorInt(min - (max - min) / 10);
            maxScore = ceilInt(max);
            if (maxScore == minScore) {
                maxScore += 10;
            }
        }

        // 数据初始化
        final int firstScorePopWidth = getTextWidth(String.valueOf(scoreArr[0])) + defaultOffset * 2;
        final int firstTimeWidth = getTextWidth(timeArr[0]);
        leftOffset = Math.max(firstScorePopWidth, firstTimeWidth) / 2 + dpToPx(8);

        final int lastScorePopWidth = getTextWidth(String.valueOf(scoreArr[totalCount - 1])) + defaultOffset * 2;
        final int lastTimeWidth = getTextWidth(timeArr[totalCount - 1]);
        rightOffset = Math.max(lastScorePopWidth, lastTimeWidth) / 2 + dpToPx(8);

        timeLineYCoordinate = viewHeight - bottomOffset - textSize - defaultOffset * 2;// 文字下间距、文字高度、文字上间距、刻度尺高度

        minScoreYCoordinate = timeLineYCoordinate - scoreTimeLineOffset;
        if (isInterceptTouch) {
            maxScoreYCoordinate = topOffset;
        } else {
            maxScoreYCoordinate = topOffset + defaultOffset * 2 + textSize + popOffset;// 预留浮窗显示空间
        }

        // 折线原点初始化
        scorePoints.clear();
        float newWith = viewWith - leftOffset - rightOffset;// 折线的总宽度(第一个点 到 最后一个点)
        for (int i = 0; i < totalCount; i++) {
            Point point = new Point();
            point.x = (int) (newWith * ((float) (i) / (totalCount - 1)) + leftOffset);
            if (scoreArr[i] > maxScore) {
                scoreArr[i] = maxScore;
            } else if (scoreArr[i] < minScore) {
                scoreArr[i] = minScore;
            }
            point.y = (int) (((float) (maxScore - scoreArr[i]) / (maxScore - minScore)) * (minScoreYCoordinate - maxScoreYCoordinate) + maxScoreYCoordinate);
            scorePoints.add(point);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isInterceptTouch) {
            return super.onTouchEvent(event);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                onActionUpEvent(event);
                break;
        }
        return true;
    }

    /**
     * 抬起手指
     *
     * @param event event
     */
    private void onActionUpEvent(MotionEvent event) {
        boolean isValidTouch = validateTouch(event.getX(), event.getY());

        if (isValidTouch) {
            invalidate();
        }
    }

    /**
     * 是否是有效的触摸范围
     *
     * @param x x
     * @param y y
     * @return 是否是有效的触摸范围
     */
    private boolean validateTouch(float x, float y) {
        // 折线触摸区域
        for (int i = 0; i < scorePoints.size(); i++) {
            // dpToPx(8)乘以2为了适当增大触摸面积
            Point point = scorePoints.get(i);
            if (x > (point.x - dpToPx(8) * 2) && x < (point.x + dpToPx(8) * 2)) {
                if (y > (point.y - dpToPx(8) * 2) && y < (point.y + dpToPx(8) * 2)) {
                    selectIndex = i;
                    return true;
                }
            }
        }

        // 时间触摸区域
        // 计算每个时间X坐标的中心点
        float timeTouchY = timeLineYCoordinate - dpToPx(4);// 减去dipToPx(4)增大触摸面积

        float newWith = viewWith - leftOffset - rightOffset;// 折线的总宽度
        float validTouchX[] = new float[totalCount];
        for (int i = 0; i < totalCount; i++) {
            validTouchX[i] = newWith * ((float) (i) / (totalCount - 1)) + leftOffset;
        }

        if (y > timeTouchY) {
            for (int i = 0; i < validTouchX.length; i++) {
                if (x < validTouchX[i] + dpToPx(8) && x > validTouchX[i] - dpToPx(8)) {
                    selectIndex = i;
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWith = w;
        viewHeight = h;
        initData();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawGradient(canvas);
        drawText(canvas);
        drawTimeLine(canvas);
        drawBrokenLine(canvas);
        drawPoint(canvas);
    }

    /**
     * 绘制渐变
     *
     * @param canvas canvas
     */
    private void drawGradient(Canvas canvas) {
        int size = scorePoints.size();
        if (size < 2) {
            return;
        }
        LinearGradient linearGradient = new LinearGradient(0, maxScoreYCoordinate, 0, minScoreYCoordinate, new int[]{0x1A04AA04, Color.TRANSPARENT}, null, LinearGradient.TileMode.CLAMP);
        gradientPaint.setShader(linearGradient);

        brokenPath.reset();

        Point pointFirst = scorePoints.get(0);
        brokenPath.moveTo(pointFirst.x, pointFirst.y);

        for (int i = 1; i < size; i++) {
            Point point = scorePoints.get(i);
            brokenPath.lineTo(point.x, point.y);
        }

        Point pointLast = scorePoints.get(size - 1);
        brokenPath.lineTo(pointLast.x, minScoreYCoordinate);
        brokenPath.lineTo(pointFirst.x, minScoreYCoordinate);
        brokenPath.moveTo(pointFirst.x, pointFirst.y);

        canvas.drawPath(brokenPath, gradientPaint);
    }

    /**
     * 绘制文本
     *
     * @param canvas canvas
     */
    private void drawText(Canvas canvas) {
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(textNormalColor);

        float newWith = viewWith - leftOffset - rightOffset;// 绘制时间的总宽度(和折线的总宽度一致)
        for (int i = 0; i < totalCount; i++) {
            float coordinateX = newWith * ((float) (i) / (totalCount - 1)) + leftOffset;// 分隔线X坐标

            // 更改选中的颜色
            textPaint.setColor(i == selectIndex ? brokenLineColor : textNormalColor);

            // 绘制时间
            canvas.drawText(timeArr[i], coordinateX, timeLineYCoordinate + defaultOffset * 2 + textSize, textPaint);// 刻度尺高度、文字上间距、文字高度
        }
    }

    /**
     * 绘制X轴的直线(包括刻度)
     *
     * @param canvas canvas
     */
    private void drawTimeLine(Canvas canvas) {
        // 绘制虚线
//        canvas.drawLine(0, maxScoreYCoordinate, viewWith, maxScoreYCoordinate, dottedPaint);
//        canvas.drawLine(0, minScoreYCoordinate, viewWith, minScoreYCoordinate, dottedPaint);

        // 绘制X轴的直线
        canvas.drawLine(0, timeLineYCoordinate, viewWith, timeLineYCoordinate, straightPaint);

        float newWith = viewWith - leftOffset - rightOffset;// 绘制X轴刻度的总宽度(和折线的总宽度一致)
        for (int i = 0; i < totalCount; i++) {
            float coordinateX = newWith * ((float) (i) / (totalCount - 1)) + leftOffset;
            canvas.drawLine(coordinateX, timeLineYCoordinate, coordinateX, timeLineYCoordinate + defaultOffset, straightPaint);
        }
    }

    /**
     * 绘制折线
     *
     * @param canvas canvas
     */
    private void drawBrokenLine(Canvas canvas) {
        brokenPath.reset();
        brokenPaint.setColor(brokenLineColor);
        brokenPaint.setStyle(Paint.Style.STROKE);
        if (totalCount == 0) {
            return;
        }
        Point point = scorePoints.get(0);
        brokenPath.moveTo(point.x, point.y);
        for (int i = 0; i < scorePoints.size(); i++) {
            point = scorePoints.get(i);
            brokenPath.lineTo(point.x, point.y);
        }
        canvas.drawPath(brokenPath, brokenPaint);
    }

    /**
     * 绘制折线穿过的点
     *
     * @param canvas canvas
     */
    protected void drawPoint(Canvas canvas) {
        for (int i = 0; i < scorePoints.size(); i++) {
            Point point = scorePoints.get(i);
            brokenPaint.setStyle(Paint.Style.FILL);
            // 选中状态
            if (i == selectIndex) {
                // 选中的圆环
                brokenPaint.setColor(selectRingBigColor);
                canvas.drawCircle(point.x, point.y, dpToPx(8f), brokenPaint);
                brokenPaint.setColor(selectRingSmallColor);
                canvas.drawCircle(point.x, point.y, dpToPx(5f), brokenPaint);

                // 绘制浮动文本背景框
                String scoreContent = String.valueOf(scoreArr[i]);
                int textWidth = getTextWidth(scoreContent);
                int textHeight = getTextHeight(scoreContent);
                drawFloatTextBackground(canvas, point.x, point.y - popOffset, textWidth, textHeight);// 浮窗距圆点上间距

                // 绘制浮动文字
                textPaint.setColor(Color.WHITE);
                canvas.drawText(scoreContent, point.x, point.y - popOffset - defaultOffset * 2, textPaint);// 距圆点上间距(8+4+4)
            }
            // 默认状态
            brokenPaint.setColor(brokenLineColor);
            canvas.drawCircle(point.x, point.y, dpToPx(2.5f), brokenPaint);
            brokenPaint.setColor(Color.WHITE);
            canvas.drawCircle(point.x, point.y, dpToPx(1.5f), brokenPaint);
        }
    }

    /**
     * 绘制显示浮动文字的背景
     *
     * @param canvas     canvas
     * @param x          x
     * @param y          y
     * @param textWidth  文字宽
     * @param textHeight 文字高
     */
    private void drawFloatTextBackground(Canvas canvas, int x, int y, int textWidth, int textHeight) {
        brokenPath.reset();
        brokenPaint.setColor(brokenLineColor);
        brokenPaint.setStyle(Paint.Style.FILL);

        // P1
        Point point = new Point(x, y);
        brokenPath.moveTo(point.x, point.y);

        // P2
        point.x = point.x + defaultOffset;
        point.y = point.y - defaultOffset;
        brokenPath.lineTo(point.x, point.y);

        // P3
        int offsetX = textWidth / 2;
        point.x = point.x + offsetX;
        brokenPath.lineTo(point.x, point.y);

        // P4
        int offsetY = textHeight + defaultOffset * 2;
        point.y = point.y - offsetY;
        brokenPath.lineTo(point.x, point.y);

        // P5
        offsetX = textWidth + defaultOffset * 2;
        point.x = point.x - offsetX;
        brokenPath.lineTo(point.x, point.y);

        // P6
        offsetY = textHeight + defaultOffset * 2;
        point.y = point.y + offsetY;
        brokenPath.lineTo(point.x, point.y);

        // P7
        offsetX = textWidth / 2;
        point.x = point.x + offsetX;
        brokenPath.lineTo(point.x, point.y);

        // 最后一个点连接到第一个点
        brokenPath.lineTo(x, y);

        canvas.drawPath(brokenPath, brokenPaint);
    }

    /**
     * dp 转换成px
     *
     * @param dp dp
     * @return px
     */
    private int dpToPx(float dp) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f * (dp >= 0 ? 1 : -1));
    }

    /**
     * 获取文字宽
     *
     * @param str str
     * @return 文字宽
     */
    private int getTextWidth(String str) {
        Rect bounds = new Rect();
        textPaint.getTextBounds(str, 0, str.length(), bounds);
        return bounds.right - bounds.left;
    }

    /**
     * 获取文字高
     *
     * @param str str
     * @return 文字高
     */
    private int getTextHeight(String str) {
        Rect bounds = new Rect();
        textPaint.getTextBounds(str, 0, str.length(), bounds);
        return bounds.bottom - bounds.top;
    }

    /**
     * 向上取整
     * A. 1-10 return 10
     * B. 11-20 return 20
     * C. 41-50 return 50
     * D. 91-100 return 100
     * E. 101-110 return 110
     *
     * @param num num
     * @return num
     */
    private int ceilInt(int num) {
        return (int) (Math.ceil(num / 10f) * 10);
    }

    /**
     * 向下取整
     * A. 0-9 return 0
     * B. 10-19 return 10
     * C. 40-49 return 40
     * D. 90-99 return 90
     * E. 100-109 return 100
     *
     * @param num num
     * @return num
     */
    private int floorInt(int num) {
        if (num < 0) {
            return 0;
        }
        return (int) Math.max(0, Math.floor(num / 10f) * 10);
    }

    /**
     * 获取数组最大最小值及下标
     *
     * @param arr arr
     * @return new int[]{{min, max}, {minIndex, maxIndex}}
     */
    private int[][] getMinMax(int[] arr) {
        int[][] result = new int[][]{{0, 0}, {0, 0}};
        if (arr == null || arr.length <= 0) {
            return result;
        }
        result[0][0] = result[0][1] = arr[0];
        for (int i = 0; i < arr.length; i++) {
            int num = arr[i];
            result[0][0] = Math.min(result[0][0], num);
            if (result[0][0] == num) {
                result[1][0] = i;
            }

            result[0][1] = Math.max(result[0][1], num);
            if (result[0][1] == num) {
                result[1][1] = i;
            }
        }
        return result;
    }

    /**
     * 设置数据
     *
     * @param scoreArr 数值数组
     * @param timeArr  时间数组
     * @param minScore 最小值
     * @param maxScore 最大值
     */
    public void setData(int[] scoreArr, String[] timeArr, int minScore, int maxScore) {
        this.scoreArr = scoreArr;
        this.timeArr = timeArr;
        this.minScore = minScore;
        this.maxScore = maxScore;
        isAutoMinMax = false;
        initData();
        invalidate();
    }

    /**
     * 设置数据
     *
     * @param scoreArr 数值数组
     * @param timeArr  时间数组
     */
    public void setData(int[] scoreArr, String[] timeArr) {
        this.scoreArr = scoreArr;
        this.timeArr = timeArr;
        isAutoMinMax = true;
        initData();
        invalidate();
    }

    /**
     * 设置是否拦截触摸操作
     *
     * @param interceptTouch 是否拦截触摸动作
     */
    public void setInterceptTouch(boolean interceptTouch) {
        isInterceptTouch = interceptTouch;
    }
}
