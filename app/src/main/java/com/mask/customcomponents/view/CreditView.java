package com.mask.customcomponents.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * 信用分仪表盘View
 * Create By lishilin On 2019/5/24
 */
public class CreditView extends View {

    // 渐变色
    private final int[] colorArr = new int[]{
            Color.parseColor("#FFFE2F03"),// 红
            Color.parseColor("#FFFFD600"),// 黄
            Color.parseColor("#FF8FFE1E")// 绿
    };

    private final int colorProgress = Color.parseColor("#FF9E9E9E");// 颜色(进度条(灰色))
    private final int colorCalibration = Color.parseColor("#FFF2F2F2");// 颜色(刻度线)
    private final int colorCalibrationText = Color.parseColor("#FF666666");// 颜色(刻度线文字)
    private final int colorTime = Color.parseColor("#FF9E9E9E");// 颜色(时间)
    private final int colorScore = Color.parseColor("#FFDBB345");// 颜色(分数)
    private final int colorTips = Color.parseColor("#FF666666");// 颜色(Tips)

    private final int ringWidth = dpToPx(12);// 圆环宽
    private final int calibrationWidth = dpToPx(3);// 刻度线宽

    private final int calibrationTextSize = dpToPx(12); // 文字大小(刻度线)
    private final int timeTextSize = dpToPx(12); // 文字大小(时间)
    private final int scoreTextSize = dpToPx(40); // 文字大小(分数)
    private final int tipsTextSize = dpToPx(14); // 文字大小(Tips)

    private final int calibrationTextOffset = dpToPx(2);// 刻度线文字间距

    private final int angleTotal = 220;// 圆环总角度
    private final int angleStart = 180 - (angleTotal - 180) / 2;// 圆环起始角度

    private final int[] levelArr = {0, 60, 70, 75, 80, 85, 88, 91, 94, 97, 100}; // 圆环的信用等级

    private final int[] levelAngleArr = {6, 3, 2, 2, 2, 1, 1, 1, 1, 1};// 各个信用等级之间的权重
    private final int levelAngle = angleTotal / 20;// 权重为1的角度值

    private PaintFlagsDrawFilter paintFlagsDrawFilter;// 设置画布绘图无锯齿

    private int width;// 宽度
    private int height;// 高度
    private int radius;// 圆环半径

    private Paint ringGradientPaint;// 画笔(圆环渐变)
    private Paint ringProgressPaint;// 画笔(圆环进度)
    private Paint calibrationPaint;// 画笔(刻度线)
    private Paint calibrationTextPaint;// 画笔(刻度线文字)
    private Paint timeTextPaint;// 画笔(时间)
    private Paint scoreTextPaint;// 画笔(分数)
    private Paint tipsTextPaint;// 画笔(Tips)

    private SweepGradient gradientRing;// 圆环渐变色

    private RectF ringArc;// 绘制圆环的矩形

    private String time = "";// 时间
    private int score = 0;// 真实的分数
    private String tips = "";// Tips

    private int scoreDisplay = 0;// 显示的分数(用于动画)

    private boolean isInitData;// 是否已经初始化数据

    public CreditView(Context context) {
        super(context);
        init(context);
    }

    public CreditView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CreditView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public CreditView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // 确定宽高
        width = w;
        height = h;

        // 圆环半径
        radius = width / 2;

        // 圆环
        int offset = calibrationTextSize + calibrationTextOffset + ringWidth / 2;
        ringArc = new RectF(offset, offset, width - offset, width - offset);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.setDrawFilter(paintFlagsDrawFilter);

        drawRing(canvas);
        drawCalibration(canvas);
        drawText(canvas);
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

        paintFlagsDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);

        // 画笔(圆环渐变)
        ringGradientPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        ringGradientPaint.setStrokeCap(Paint.Cap.ROUND);
        ringGradientPaint.setStyle(Paint.Style.STROKE);
        ringGradientPaint.setStrokeWidth(ringWidth);

        // 画笔(圆环进度)
        ringProgressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        ringProgressPaint.setStrokeCap(Paint.Cap.ROUND);
        ringProgressPaint.setColor(colorProgress);
        ringProgressPaint.setStyle(Paint.Style.STROKE);
        ringProgressPaint.setStrokeWidth(ringWidth);

        // 画笔(刻度线)
        calibrationPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        calibrationPaint.setColor(colorCalibration);
        calibrationPaint.setStyle(Paint.Style.STROKE);
        calibrationPaint.setStrokeWidth(calibrationWidth);

        // 画笔(刻度线文字)
        calibrationTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        calibrationTextPaint.setTextSize(calibrationTextSize);
        calibrationTextPaint.setColor(colorCalibrationText);

        // 画笔(时间)
        timeTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        timeTextPaint.setTextAlign(Paint.Align.CENTER);
        timeTextPaint.setStyle(Paint.Style.FILL);
        timeTextPaint.setColor(colorTime);
        timeTextPaint.setTextSize(timeTextSize);

        // 画笔(分数)
        scoreTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        scoreTextPaint.setTextAlign(Paint.Align.CENTER);
        scoreTextPaint.setStyle(Paint.Style.FILL);
        scoreTextPaint.setColor(colorScore);
        scoreTextPaint.setTextSize(scoreTextSize);

        // 画笔(Tips)
        tipsTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        tipsTextPaint.setTextAlign(Paint.Align.CENTER);
        tipsTextPaint.setStyle(Paint.Style.FILL);
        tipsTextPaint.setColor(colorTips);
        tipsTextPaint.setTextSize(tipsTextSize);

        isInitData = true;
    }

    /**
     * 绘制圆环(渐变进度及进度背景)
     *
     * @param canvas canvas
     */
    private void drawRing(Canvas canvas) {
        // 绘制进度背景色
        canvas.drawArc(ringArc, angleStart, angleTotal, false, ringProgressPaint);

        // 计算进度
        int angleProgressTotal = 0;
        // 计算当前等级下标
        int levelIndex = 0;
        for (int i = 0; i < levelArr.length - 1; i++) {
            if (scoreDisplay <= levelArr[i + 1]) {
                levelIndex = i;
                break;
            }
        }
        // 累积当前等级之前的所有等级的角度之和
        for (int i = 0; i < levelIndex; i++) {
            angleProgressTotal += levelAngle * levelAngleArr[i];
        }
        // 累积当前等级的角度
        int levelMin = levelArr[levelIndex];// 当前等级最小分数
        int levelMax = levelArr[levelIndex + 1];// 当前等级最大分数
        int levelAngleTotal = levelAngle * levelAngleArr[levelIndex];// 当前等级角度
        angleProgressTotal += levelAngleTotal * (scoreDisplay - levelMin) / (levelMax - levelMin);

        // 绘制进度渐变色
        if (gradientRing == null) {
            gradientRing = new SweepGradient(ringArc.centerX(), ringArc.centerY(), colorArr, null);
            Matrix matrix = new Matrix();
            matrix.setRotate(90, ringArc.centerX(), ringArc.centerY());
            gradientRing.setLocalMatrix(matrix);
            ringGradientPaint.setShader(gradientRing);
        }
        canvas.drawArc(ringArc, angleStart, angleProgressTotal, false, ringGradientPaint);
    }

    /**
     * 绘制刻度线及文字
     *
     * @param canvas canvas
     */
    private void drawCalibration(Canvas canvas) {
        // 旋转画布进行绘制对应的刻度
        canvas.save();
        canvas.rotate(-(90 + 180 - angleStart), radius, radius);
        // 计算刻度线的起点结束点
        int startY = calibrationTextSize + calibrationTextOffset;
        int endY = (int) (startY + ringGradientPaint.getStrokeWidth());
        int length = levelArr.length;
        for (int i = 0; i < length; i++) {
            // 绘制刻度
            if (i > 0 && i < length - 1) {
                canvas.drawLine(radius, startY, radius, endY, calibrationPaint);
            }

            // 绘制文字
            String level = String.valueOf(levelArr[i]);
            float textWidth = getTextWidth(level, calibrationTextPaint);
            float textHeight = getTextHeight(level, calibrationTextPaint);
            canvas.drawText(level, radius - textWidth / 2, textHeight, calibrationTextPaint);

            // 旋转
            if (i < levelAngleArr.length) {
                canvas.rotate(levelAngle * levelAngleArr[i], radius, radius);
            }
        }

        canvas.restore();
    }

    /**
     * 绘制文字(时间、分数、Tips等)
     *
     * @param canvas canvas
     */
    private void drawText(Canvas canvas) {
        float coordinateY = radius * 0.5f + timeTextSize * 0.5f;
        canvas.drawText(time, radius, coordinateY, timeTextPaint);

        coordinateY = radius;
        canvas.drawText(String.valueOf(scoreDisplay), radius, coordinateY, scoreTextPaint);

        coordinateY += scoreTextSize * 0.5f + tipsTextSize * 0.5f;
        canvas.drawText(tips, radius, coordinateY, tipsTextPaint);
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
     * @param str       str
     * @param textPaint textPaint
     * @return 文字宽
     */
    private int getTextWidth(String str, Paint textPaint) {
        Rect bounds = new Rect();
        textPaint.getTextBounds(str, 0, str.length(), bounds);
        return bounds.right - bounds.left;
    }

    /**
     * 获取文字高
     *
     * @param str       str
     * @param textPaint textPaint
     * @return 文字高
     */
    private int getTextHeight(String str, Paint textPaint) {
        Rect bounds = new Rect();
        textPaint.getTextBounds(str, 0, str.length(), bounds);
        return bounds.bottom - bounds.top;
    }

    /**
     * 开始动画
     */
    private void startAnim() {
        ValueAnimator scoreAnim = ValueAnimator.ofInt(scoreDisplay, score);
        scoreAnim.setDuration(2000);
        scoreAnim.setInterpolator(new LinearInterpolator());
        scoreAnim.addUpdateListener(valueAnimator -> {
            scoreDisplay = (int) valueAnimator.getAnimatedValue();
            postInvalidate();
        });
        scoreAnim.start();
    }

    /**
     * 设置数据
     *
     * @param time  time
     * @param score score
     * @param tips  tips
     */
    public void setData(String time, int score, String tips) {
        if (score < levelArr[0]) {
            score = levelArr[0];
        }
        int lastIndex = levelArr.length - 1;
        if (score > levelArr[lastIndex]) {
            score = levelArr[lastIndex];
        }
        this.time = time;
        this.score = score;
        this.tips = tips;

        startAnim();
    }

}
