package com.mask.customcomponents.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.FloatRange;
import androidx.annotation.Nullable;

import com.mask.customcomponents.utils.NumberUtils;
import com.mask.customcomponents.utils.SizeUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Raykite进度条
 * Created by lishilin on 2019/12/11
 */
public class RaykiteProgressBar extends View {

    /**
     * 粒子实体类
     */
    public static class Particle {

        private long createTimeX;// X轴动画创建时间
        private long createTimeY;// Y轴动画创建时间
        private long durationX;// X轴动画时长
        private long durationY;// Y轴动画时长

        public float width;// 宽

        public Particle(long durationX, long durationY, float width) {
            this.createTimeX = System.currentTimeMillis();
            this.createTimeY = System.currentTimeMillis();
            this.durationX = durationX;
            this.durationY = durationY;
            this.width = width;
        }

        /**
         * 重置
         *
         * @param durationY durationY
         * @param width     width
         */
        public void reset(long durationY, float width) {
            this.createTimeX = System.currentTimeMillis();
            this.durationY = durationY;
            this.width = width;
        }

        /**
         * 是否可以运行
         *
         * @return boolean
         */
        public boolean isEnable() {
            return System.currentTimeMillis() - createTimeX <= durationX;
        }

        /**
         * 获取 当前Alpha
         *
         * @param alphaStart alphaStart
         * @param alphaEnd   alphaEnd
         * @return int
         */
        public int getCurrentAlpha(int alphaStart, int alphaEnd) {
            return (int) (SizeUtils.getPercentValue(getCurrentPercentX(), alphaStart, alphaEnd));
        }

        /**
         * 获取 当前PointX
         *
         * @param pointXStart pointXStart
         * @param pointXEnd   pointXEnd
         * @return float
         */
        public float getCurrentPointX(float pointXStart, float pointXEnd) {
            return SizeUtils.getPercentValue(getCurrentPercentX(), pointXStart, pointXEnd);
        }

        /**
         * 获取 当前PointY
         *
         * @param pointYStart pointYStart
         * @param pointYEnd   pointYEnd
         * @return float
         */
        public float getCurrentPointY(float pointYStart, float pointYEnd) {
            return SizeUtils.getPercentValue(getCurrentPercentY(), pointYStart, pointYEnd);
        }

        /**
         * 获取 X轴当前百分比
         *
         * @return float
         */
        private float getCurrentPercentX() {
            return ((System.currentTimeMillis() - createTimeX) % durationX) * 1.0f / durationX;
        }

        /**
         * 获取 Y轴当前百分比
         *
         * @return float
         */
        private float getCurrentPercentY() {
            long time = (System.currentTimeMillis() - createTimeY + durationY / 2) % (durationY * 2);
            if (time <= durationY) {
                return SizeUtils.getValuePercent(time, 0, durationY);
            } else {
                return SizeUtils.getValuePercent(time - durationY, durationY, 0);
            }
        }

    }

    private final int backgroundColor = 0xFF21232C;// 背景颜色
    private final Shader barShader = new LinearGradient(0, 0, 1, 0, new int[]{0xFF3B5BDC, 0xFF6F72ED}, null, Shader.TileMode.CLAMP);// 进度条颜色
    private final int particleColor = 0xFF6F72ED;// 粒子颜色
    private final int textColor = Color.WHITE;// 文本颜色

    private final int radius = SizeUtils.dpToPx(2);// 圆角半径
    private final int[] particleWidthArr = new int[]{8, 5, 5, 6, 9, 6, 4, 3};// 粒子宽度集合
    private final int[] particleDurationYArr = new int[]{800, 500, 500, 600, 900, 600, 400, 300};// 粒子Y轴动画周期集合
    private final int fontText = SizeUtils.dpToPx(13);// 文本文字大小

    private final long particleDurationX = 2000;// 粒子X轴动画周期
    private final long particleSpeed = 150;// 粒子的创建速度
    private final List<Particle> particleList = new ArrayList<>();// 粒子集合
    private final List<Particle> particlePool = new ArrayList<>();// 粒子缓存池
    private int particleNextCreateIndex;// 下一次创建粒子的下标
    private long particleLastCreateTime;// 最后一次创建粒子的时间

    private int width;// 宽
    private int height;// 高

    private float particleContentWidth;// 粒子整体宽

    private Paint backgroundPaint;// Paint 背景
    private Paint particlePaint;// Paint 粒子
    private Paint barPaint;// Paint 进度条
    private Paint textPaint;// Paint 文本

    private Rect rect;

    private Matrix matrix;

    private float percent;// 当前进度百分比
    private float percentStart;// 实际进度百分比(动画用)
    private float percentEnd;// 实际进度百分比(动画用)

    private long percentStartTime;// 进度百分比动画开始时间
    private long percentDuration = 500;// 进度百分比动画时长

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
        percentStart = 0;
        percentEnd = 0;
        percentStartTime = System.currentTimeMillis();

        isInitData = true;
    }

    /**
     * 刷新 数据
     */
    protected void refreshData() {
        particleContentWidth = width / 7.0f;
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

        if (percent != percentEnd) {
            float animPercent = SizeUtils.getValuePercent(System.currentTimeMillis() - percentStartTime, 0, percentDuration);
            percent = SizeUtils.getPercentValue(animPercent, percentStart, percentEnd);
        }

        drawBackground(canvas);
        drawParticle(canvas);
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
        String percentStr = getValuePercentStr(percent);

        float textX = width * 1.0f / 2;
        float textYOffset = height * 1.0f / 2 - SizeUtils.getTextHeight(percentStr, textPaint, rect) / 2;
        float textY = SizeUtils.getTextBaseline(percentStr, textPaint, rect) + textYOffset;
        canvas.drawText(percentStr, textX, textY, textPaint);
    }

    /* ********************************************* 粒子相关 **********************************************/

    /**
     * 绘制 粒子
     *
     * @param canvas canvas
     */
    private void drawParticle(Canvas canvas) {
        float left = SizeUtils.getPercentValue(percent, 0, width);
        float right = left + particleContentWidth;
        float top = 0;
        float bottom = top + height;
        if (left >= width) {
            return;
        }

        createParticle();

        // 裁切
        canvas.save();
        canvas.clipRect(0, top, right, bottom);

        Iterator<Particle> iterator = particleList.iterator();
        while (iterator.hasNext()) {
            Particle particle = iterator.next();
            if (particle.isEnable()) {
                particlePaint.setAlpha(particle.getCurrentAlpha(0xFF, 0x00));

                float pointX = particle.getCurrentPointX(left, right);
                float pointY = particle.getCurrentPointY(top, bottom);
                float widthHalf = particle.width / 2;
                canvas.drawRoundRect(pointX - widthHalf, pointY - widthHalf, pointX + widthHalf, pointY + widthHalf, radius, radius, particlePaint);
            } else {
                iterator.remove();
                particlePool.add(particle);
            }
        }

        canvas.restore();

        postInvalidateOnAnimation();
    }

    /**
     * 创建 粒子
     */
    private void createParticle() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - particleLastCreateTime < particleSpeed) {
            return;
        }

        if (particleNextCreateIndex >= particleWidthArr.length) {
            particleNextCreateIndex = 0;
        }
        int particleWidth = SizeUtils.dpToPx(particleWidthArr[particleNextCreateIndex]);
        int particleDurationY = particleDurationYArr[particleNextCreateIndex];

        Particle particle;
        if (particlePool.size() > 0) {
            particle = particlePool.get(0);
            particlePool.remove(0);
            particle.reset(particleDurationY, particleWidth);
        } else {
            particle = new Particle(particleDurationX, particleDurationY, particleWidth);
        }
        particleList.add(particle);

        particleNextCreateIndex++;
        particleLastCreateTime = currentTime;
    }
    /* ********************************************* 粒子相关 **********************************************/

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
     * 设置 进度百分比动画时长
     *
     * @param duration duration
     */
    public void setPercentDuration(long duration) {
        if (duration < 0) {
            duration = 0;
        }
        this.percentDuration = duration;
    }

    /**
     * 获取 当前进度百分比
     *
     * @return float
     */
    public float getPercent() {
        return percentEnd;
    }

    /**
     * 设置 当前进度百分比
     *
     * @param percent percent
     */
    public void setPercent(@FloatRange(from = 0.0, to = 1.0) float percent) {
        setPercent(percent, true);
    }

    /**
     * 设置 当前进度百分比
     *
     * @param percent percent
     * @param isAnim  是否需要动画
     */
    public void setPercent(@FloatRange(from = 0.0, to = 1.0) float percent, boolean isAnim) {
        if (percent < 0) {
            percent = 0;
        }
        if (percent > 1) {
            percent = 1;
        }
        if (isAnim) {
            this.percentStart = this.percent;
            this.percentEnd = percent;
        } else {
            this.percent = percent;
            this.percentStart = this.percent;
            this.percentEnd = percent;
        }
        this.percentStartTime = System.currentTimeMillis();
        postInvalidateOnAnimation();
    }
    /* ********************************************* 外部调用 **********************************************/
}
