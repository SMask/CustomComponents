package com.mask.customcomponents.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.annotation.Size;

import com.mask.customcomponents.utils.SizeUtils;

/**
 * 颜色拾取器
 * 参考资料：https://zh.wikipedia.org/wiki/HSL%E5%92%8CHSV%E8%89%B2%E5%BD%A9%E7%A9%BA%E9%97%B4
 * 正方形、圆形相互映射：http://squircular.blogspot.com/2015/09/mapping-circle-to-square.html
 * Created by lishilin on 2020/06/15
 */
public class ColorPickerView extends View {

    // 基础数据(用于计算等比例后的真实数据)
    private static final float BASE_SIDE = 1000;

    private float width;
    private float height;
    private float side;// 正方形边长

    private Paint ringPaint;// Paint 圆环
    private Paint circlePaint;// Paint 中心圆
    private Paint selectorPaint;// Paint 选择器

    private PointF pointF;

    private RectF rectF;

    private Matrix matrix;

    // 内容位置
    private RectF contentRectF;
    private float centerX;
    private float centerY;

    // 圆环
    private Shader ringShader;
    private float ringWidthRatioValue;// 圆环线宽的比例值(相对内容宽)
    private float ringWidth;// 圆环线宽
    private float ringSpaceRatioValue;// 圆环间距的比例值(相对内容宽)
    private float ringSpace;// 圆环间距
    private float ringRadius;// 圆环半径(圆环线中点半径)
    private Path ringPath;// 圆环路径
    private Region ringRegion;// 圆环区域
    private boolean isHandleRing;// 是否处理圆环
    private boolean isChangeRing;// 是否圆环改变
    private PointF ringPointF;

    // 中心圆
    private Thread circleThread;// 主要是创建Bitmap耗时
    private int circleRadius;// 中心圆半径
    private Shader satShader;// 饱和度渐变
    private Shader valShader;// 明度渐变
    private Bitmap satValBitmap;// 饱和度、明度渐变组合Bitmap
    private Shader satValShader;// 饱和度、明度渐变组合BitmapShader
    private final SparseArray<Shader> circleShaderMap = new SparseArray<>();// 中心圆颜色
    private Path circlePath;// 中心圆路径
    private Region circleRegion;// 中心圆区域
    private boolean isHandleCircle;// 是否处理中心圆
    private boolean isChangeCircle;// 是否圆环改变
    private PointF circlePointF;

    // 选择器
    private float selectorStrokeWidthRatioValue;// 选择器线宽的比例值(相对内容宽)
    private float selectorStrokeWidth;// 选择器线宽
    private float selectorRadiusRatioValue;// 选择器半径的比例值(相对内容宽)
    private float selectorRadius;// 选择器半径(主要是中心圆上的选择器半径)
    private int selectorColor;// 选择器颜色

    // 颜色
    private final float[] hueArr = new float[]{0, 1, 1};// 色相颜色数组(圆环相关)
    private final float[] hsvArr = new float[]{0, 0, 0};// 实际颜色数组(圆环、中心圆相关)

    private boolean isInitData;// 是否已经初始化数据

    public ColorPickerView(Context context) {
        super(context);
        init(context, null);
    }

    public ColorPickerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ColorPickerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public ColorPickerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    /**
     * 初始化
     *
     * @param context context
     * @param attrs   attrs
     */
    private void init(Context context, AttributeSet attrs) {
        if (isInitData) {
            return;
        }

        ringPaint = new Paint();
        ringPaint.setAntiAlias(true);
        ringPaint.setStyle(Paint.Style.FILL);

        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setStyle(Paint.Style.FILL);

        selectorPaint = new Paint();
        selectorPaint.setAntiAlias(true);
        selectorPaint.setStyle(Paint.Style.STROKE);

        pointF = new PointF();

        rectF = new RectF();

        matrix = new Matrix();

        contentRectF = new RectF();

        // 饱和度、明度为1，色相渐变色数组
        int[] hueColorArr = new int[]{0xFFFF0000, 0xFFFF00FF, 0xFF0000FF, 0xFF00FFFF, 0xFF00FF00, 0xFFFFFF00, 0xFFFF0000};
        ringShader = new SweepGradient(0, 0, hueColorArr, null);
        ringWidthRatioValue = 120.0f;
        ringSpaceRatioValue = 25.0f;
        ringPath = new Path();
        ringRegion = new Region();
        ringPointF = new PointF();

        satShader = new LinearGradient(0, 0, 1, 0, new int[]{0xFFFFFFFF, 0x00FFFFFF}, null, Shader.TileMode.CLAMP);
        valShader = new LinearGradient(0, 0, 0, 1, new int[]{0x00000000, 0xFF000000}, null, Shader.TileMode.CLAMP);
        circlePath = new Path();
        circleRegion = new Region();
        circlePointF = new PointF();

        selectorStrokeWidthRatioValue = 5.0f;
        selectorRadiusRatioValue = 25.0f;
        selectorColor = Color.WHITE;

        isInitData = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                isHandleRing = isHandleRing(event);
                isHandleCircle = isHandleCircle(event);
                if (isHandleRing || isHandleCircle) {
                    ViewParent parent = getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        if (isHandleRing) {
            handleRing(event);
        }
        if (isHandleCircle) {
            handleCircle(event);
        }
        if (isHandleRing || isHandleCircle) {
            postInvalidateOnAnimation();
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        side = Math.min(width, height);

        refreshDimen();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawRing(canvas);
        drawCircle(canvas);
        drawRingSelector(canvas);
        drawCircleSelector(canvas);
    }

    /**
     * 绘制 圆环
     *
     * @param canvas canvas
     */
    private void drawRing(Canvas canvas) {
        ringPaint.setStrokeWidth(ringWidth);

        if (ringShader != null) {
            matrix.reset();
            matrix.setTranslate(centerX, centerY);
            ringShader.setLocalMatrix(matrix);
        }
        ringPaint.setShader(ringShader);

        canvas.drawPath(ringPath, ringPaint);
    }

    /**
     * 绘制 中心圆
     *
     * @param canvas canvas
     */
    private void drawCircle(Canvas canvas) {
        // 1. canvas.clipPath 有锯齿，无毛边
        // 2. canvas.drawPath + BitmapShader 无锯齿，有毛边
        // 3. paint.setXfermode + BitmapShader 无锯齿，有毛边
        // 4. ComposeShader + BitmapShader 无锯齿，无毛边

        int hueColor = Color.HSVToColor(hueArr);
        Shader shader = getCircleShader(hueColor);
        if (shader != null) {
            matrix.reset();
            matrix.setTranslate(centerX - circleRadius, centerY - circleRadius);
            shader.setLocalMatrix(matrix);
            circlePaint.setShader(shader);
            canvas.drawPath(circlePath, circlePaint);
        }
    }

    /**
     * 绘制 圆环选择器
     *
     * @param canvas canvas
     */
    private void drawRingSelector(Canvas canvas) {
        selectorPaint.setStrokeWidth(selectorStrokeWidth);
        selectorPaint.setColor(selectorColor);

        float radius = (ringWidth - selectorStrokeWidth) / 2;

        canvas.drawCircle(ringPointF.x, ringPointF.y, radius, selectorPaint);
    }

    /**
     * 绘制 中心圆选择器
     *
     * @param canvas canvas
     */
    private void drawCircleSelector(Canvas canvas) {
        selectorPaint.setStrokeWidth(selectorStrokeWidth);
        selectorPaint.setColor(selectorColor);

        float radius = selectorRadius - selectorStrokeWidth / 2;

        canvas.drawCircle(circlePointF.x, circlePointF.y, radius, selectorPaint);
    }

    /* ******************************************** 内部调用 **********************************************/

    /**
     * 是否处理 圆环
     *
     * @param event event
     * @return boolean
     */
    private boolean isHandleRing(MotionEvent event) {
        final float x = event.getX();
        final float y = event.getY();
        return ringRegion.contains((int) x, (int) y);
    }

    /**
     * 处理 圆环
     *
     * @param event event
     */
    private void handleRing(MotionEvent event) {
        isChangeRing = true;

        final float x = event.getX();
        final float y = event.getY();

        // 当前坐标的角度
        float angle = SizeUtils.formatAngle(SizeUtils.getAngle(x, y, centerX, centerY));
        ringPointF.set(centerX, centerY);
        SizeUtils.getCirclePoint(ringPointF, ringRadius, angle);

        pointToColor();
    }

    /**
     * 是否处理 中心圆
     *
     * @param event event
     * @return boolean
     */
    private boolean isHandleCircle(MotionEvent event) {
        final float x = event.getX();
        final float y = event.getY();
        return circleRegion.contains((int) x, (int) y);
    }

    /**
     * 处理 中心圆
     *
     * @param event event
     */
    private void handleCircle(MotionEvent event) {
        isChangeCircle = true;

        final float x = event.getX();
        final float y = event.getY();

        // 如果是中心圆范围内
        if (circleRegion.contains((int) x, (int) y)) {
            circlePointF.set(x, y);
        } else {
            // 当前坐标的角度
            float angle = SizeUtils.formatAngle(SizeUtils.getAngle(x, y, centerX, centerY));
            circlePointF.set(centerX, centerY);
            SizeUtils.getCirclePoint(circlePointF, circleRadius, angle);
        }

        pointToColor();
    }

    /**
     * 刷新 尺寸
     */
    private void refreshDimen() {
        contentRectF.set(0, 0, width, height);
        contentRectF.inset((width - side) / 2, (height - side) / 2);
        centerX = contentRectF.centerX();
        centerY = contentRectF.centerY();

        ringWidth = getValueRatio(ringWidthRatioValue);
        ringSpace = getValueRatio(ringSpaceRatioValue);
        ringRadius = (side - ringWidth) / 2;
        isChangeRing = true;

        circleRadius = (int) (side / 2 - ringWidth - ringSpace);// 强转为int，主要是为了下面生成渐变Bitmap用
        isChangeCircle = true;

        selectorStrokeWidth = getValueRatio(selectorStrokeWidthRatioValue);
        selectorRadius = getValueRatio(selectorRadiusRatioValue);

        refreshPath();
        if (circleThread == null) {
            circleThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    refreshSatValShader();
                }
            });
        }
        circleThread.start();

        colorToPoint();
    }

    /**
     * 刷新 路径
     */
    private void refreshPath() {
        Region clipRegion = new Region(0, 0, (int) width, (int) height);

        // 圆环
        ringPath.reset();
        ringPath.addCircle(centerX, centerY, side / 2, Path.Direction.CW);
        ringPath.addCircle(centerX, centerY, side / 2 - ringWidth, Path.Direction.CCW);

        ringRegion.setPath(ringPath, clipRegion);

        // 中心圆
        circlePath.reset();
        circlePath.addCircle(centerX, centerY, circleRadius, Path.Direction.CW);

        circleRegion.setPath(circlePath, clipRegion);
    }

    /**
     * 刷新 饱和度、明度Shader
     * 主要用于遮罩
     */
    private void refreshSatValShader() {
        int bitmapWidth = circleRadius * 2;
        int bitmapHeight = circleRadius * 2;

        if (satValBitmap != null) {
            if (satValBitmap.getWidth() == bitmapWidth && satValBitmap.getHeight() == bitmapHeight) {
                return;
            }
            satValBitmap.recycle();
            satValBitmap = null;

            satValShader = null;

            circleShaderMap.clear();
        }

        // 正方形
        Bitmap satValBitmapSquare = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(satValBitmapSquare);

        rectF.set(0, 0, bitmapWidth, bitmapHeight);

        // 饱和度
        if (satShader != null) {
            matrix.reset();
            matrix.setScale(bitmapWidth, bitmapHeight);
            satShader.setLocalMatrix(matrix);
        }
        circlePaint.setShader(satShader);
        canvas.drawRect(rectF, circlePaint);

        // 明度
        if (valShader != null) {
            matrix.reset();
            matrix.setScale(bitmapWidth, bitmapHeight);
            valShader.setLocalMatrix(matrix);
        }
        circlePaint.setShader(valShader);
        canvas.drawRect(rectF, circlePaint);

        // 正方形映射为圆形
        satValBitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
        float centerX = bitmapWidth / 2.0f;
        float centerY = bitmapHeight / 2.0f;
        for (int x = 0; x < bitmapWidth; x++) {
            for (int y = 0; y < bitmapHeight; y++) {
                pointF.set(centerX, centerY);
                SizeUtils.getPointSquareToCircle(pointF, circleRadius, x, y);
                satValBitmap.setPixel((int) pointF.x, (int) pointF.y, satValBitmapSquare.getPixel(x, y));
            }
        }

        satValBitmapSquare.recycle();
        satValShader = new BitmapShader(satValBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        postInvalidateOnAnimation();
    }

    /**
     * 获取 中心圆颜色
     *
     * @param color color
     * @return Shader
     */
    private Shader getCircleShader(int color) {
        if (satValShader == null) {
            return null;
        }
        Shader shader = circleShaderMap.get(color);
        if (shader == null) {
            Shader bgShader = new LinearGradient(0, 0, 0, 1, color, color, Shader.TileMode.CLAMP);
            shader = new ComposeShader(bgShader, satValShader, PorterDuff.Mode.SRC_OVER);
            circleShaderMap.put(color, shader);
        }
        return shader;
    }

    /**
     * 获取 成比例的数据(相对内容宽)
     *
     * @param value 相对基础数据比例的数据
     * @return 计算后的真实数据
     */
    private float getValueRatio(float value) {
        if (value == 0) {
            return 0;
        }
        return side * value / BASE_SIDE;
    }

    /**
     * 颜色转换为点
     */
    private void colorToPoint() {
        if (isChangeRing) {
            isChangeRing = false;
            ringPointF.set(centerX, centerY);
            SizeUtils.getCirclePoint(ringPointF, ringRadius, 360 - hueArr[0]);
        }
        if (isChangeCircle) {
            isChangeCircle = false;
        }
    }

    /**
     * 点转换为颜色
     */
    private void pointToColor() {
        if (isChangeRing) {
            isChangeRing = false;
            // 当前坐标的角度
            float angle = SizeUtils.formatAngle(SizeUtils.getAngle(ringPointF.x, ringPointF.y, centerX, centerY));
            hsvArr[0] = hueArr[0] = 360 - angle;
        }
        if (isChangeCircle) {
            isChangeCircle = false;
        }
    }
    /* ******************************************** 内部调用 **********************************************/

    /* ******************************************** 外部调用 **********************************************/

    /**
     * 设置 颜色
     *
     * @param colorString colorString
     */
    public void setColor(@Size(min = 1) String colorString) {
        setColor(Color.parseColor(colorString));
    }

    /**
     * 设置 颜色
     *
     * @param color color
     */
    public void setColor(@ColorInt int color) {
        float[] hsvArr = new float[3];
        Color.colorToHSV(color, hsvArr);
        setColor(hsvArr);
    }

    /**
     * 设置 颜色
     *
     * @param hsvArr HSV颜色数组
     */
    public void setColor(@Size(3) float[] hsvArr) {
        if (this.hsvArr[0] != hsvArr[0]) {
            isChangeRing = true;
            this.hsvArr[0] = hueArr[0] = hsvArr[0];
        }
        if (this.hsvArr[1] != hsvArr[1] || this.hsvArr[2] != hsvArr[2]) {
            isChangeCircle = true;
            this.hsvArr[1] = hsvArr[1];
            this.hsvArr[2] = hsvArr[2];
        }

        if (isChangeRing || isChangeCircle) {
            colorToPoint();

            postInvalidateOnAnimation();
        }
    }
    /* ******************************************** 外部调用 **********************************************/
}
