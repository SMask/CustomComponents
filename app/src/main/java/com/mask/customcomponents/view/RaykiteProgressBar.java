package com.mask.customcomponents.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.support.annotation.FloatRange;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.mask.customcomponents.utils.NumberUtils;
import com.mask.customcomponents.utils.SizeUtils;

/**
 * Raykite进度条
 * Created by lishilin on 2019/12/11
 */
public class RaykiteProgressBar extends View {

    private final int backgroundColor = 0xFF21232C;// 背景颜色
    private final Shader barShader = new LinearGradient(0, 0, 1, 0, new int[]{0xFF3B5BDC, 0xFF6F72ED}, null, Shader.TileMode.CLAMP);// 进度条颜色
    private final int particleColor = 0xFF6F72ED;// 粒子颜色
    private final int textColor = Color.WHITE;// 文本颜色

    private final int radius = SizeUtils.dpToPx(2);// 圆角半径
    private final int[] particleWidthArr = new int[]{SizeUtils.dpToPx(6), SizeUtils.dpToPx(3), SizeUtils.dpToPx(4), SizeUtils.dpToPx(10)};// 粒子宽度集合
    private final int fontText = SizeUtils.dpToPx(13);// 文本文字大小

    private int width;// 宽
    private int height;// 高

    private Paint backgroundPaint;// Paint 背景
    private Paint particlePaint;// Paint 粒子
    private Paint barPaint;// Paint 进度条
    private Paint textPaint;// Paint 文本

    private Rect rect;

    private Matrix matrix;

    private float percent;// 当前进度百分比
    private String percentStr;// 当前进度百分比字符串

    private boolean isInitData;// 是否已经初始化数据

    public RaykiteProgressBar(Context context) {
        super(context);
        initData(null);
    }

    public RaykiteProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initData(attrs);
    }

    public RaykiteProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData(attrs);
    }

    public RaykiteProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initData(attrs);
    }

    /**
     * 初始化 数据
     *
     * @param attrs attrs
     */
    protected void initData(AttributeSet attrs) {
        if (isInitData) {
            return;
        }

        backgroundPaint = new Paint();
        backgroundPaint.setAntiAlias(true);
        backgroundPaint.setStyle(Paint.Style.FILL);
        backgroundPaint.setColor(backgroundColor);

        particlePaint = new Paint();
        particlePaint.setAntiAlias(true);
        particlePaint.setStyle(Paint.Style.FILL);
        particlePaint.setColor(particleColor);

        barPaint = new Paint();
        barPaint.setAntiAlias(true);
        barPaint.setStyle(Paint.Style.FILL);

        textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(fontText);
        textPaint.setColor(textColor);

        rect = new Rect();

        matrix = new Matrix();

        percent = 0;
        percentStr = getValuePercentStr(percent);

        isInitData = true;
    }

    /**
     * 刷新 数据
     */
    protected void refreshData() {

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        width = w;
        height = h;

        refreshData();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBackground(canvas);
        drawBar(canvas);
        drawText(canvas);
    }

    /**
     * 绘制 背景
     *
     * @param canvas canvas
     */
    private void drawBackground(Canvas canvas) {
        canvas.drawRoundRect(0, 0, width, height, radius, radius, backgroundPaint);
    }

    /**
     * 绘制 进度条
     *
     * @param canvas canvas
     */
    private void drawBar(Canvas canvas) {
        float right = SizeUtils.getPercentValue(percent, 0, width);

        matrix.reset();
        matrix.setScale(right, 1);
        barShader.setLocalMatrix(matrix);
        barPaint.setShader(barShader);

        canvas.drawRoundRect(0, 0, right, height, radius, radius, barPaint);
    }

    /**
     * 绘制 文本
     *
     * @param canvas canvas
     */
    private void drawText(Canvas canvas) {
        float textX = width * 1.0f / 2;
        float textYOffset = height * 1.0f / 2 - SizeUtils.getTextHeight(percentStr, textPaint, rect) / 2;
        float textY = SizeUtils.getTextBaseline(percentStr, textPaint, rect) + textYOffset;
        canvas.drawText(percentStr, textX, textY, textPaint);
    }

    /* ********************************************* 内部方法 **********************************************/

    /**
     * 获取 百分比字符串
     *
     * @param percent percent
     * @return String
     */
    public String getValuePercentStr(float percent) {
        return NumberUtils.multiply(NumberUtils.getRoundDecimal(percent, 3), 100) + "%";
    }

    /* ********************************************* 内部方法 **********************************************/

    /* ********************************************* 外部调用 **********************************************/

    /**
     * 获取 当前进度百分比
     *
     * @return float
     */
    public float getPercent() {
        return percent;
    }

    /**
     * 设置 当前进度百分比
     *
     * @param percent percent
     */
    public void setPercent(@FloatRange(from = 0.0, to = 1.0) float percent) {
        if (percent < 0) {
            percent = 0;
        }
        if (percent > 1) {
            percent = 1;
        }
        this.percent = percent;
        this.percentStr = getValuePercentStr(percent);
        postInvalidateOnAnimation();
    }
    /* ********************************************* 外部调用 **********************************************/
}
