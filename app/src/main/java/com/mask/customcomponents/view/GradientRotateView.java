package com.mask.customcomponents.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * 渐变旋转 View
 * Created by lishilin on 2020/08/17
 */
public class GradientRotateView extends View {

    private float width;// 控件 宽
    private float height;// 控件 高

    private Path path;

    private RectF rectF;

    private Matrix matrix;

    private Paint shaderPaint;
    private Paint paint;

    private Shader shader;// 渐变

    private float angle;// 旋转角度

    private boolean isInitData;// 是否已经初始化数据

    public GradientRotateView(Context context) {
        super(context);
        init(context, null);
    }

    public GradientRotateView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public GradientRotateView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public GradientRotateView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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

        path = new Path();

        rectF = new RectF();

        matrix = new Matrix();

        shaderPaint = new Paint();
        shaderPaint.setAntiAlias(true);
        shaderPaint.setStyle(Paint.Style.FILL);

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);

        shader = new LinearGradient(0, 0, 1, 0, new int[]{Color.WHITE, Color.BLACK}, new float[]{0.5f, 0.5f}, Shader.TileMode.REPEAT);
//        shader = new LinearGradient(0, 0, 1, 0, new int[]{Color.WHITE, Color.BLACK}, null, Shader.TileMode.CLAMP);

        angle = 0;

        isInitData = true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        width = w;
        height = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        rectF.set(0, 0, width, height);
        float length = getShaderLength(rectF, angle);

        path.reset();
        path.addRect(rectF, Path.Direction.CW);

        matrix.reset();
        matrix.postScale(length, 1);
        matrix.postTranslate((rectF.width() - length) / 2, 0);
        matrix.postRotate(angle, rectF.centerX(), rectF.centerY());
        shader.setLocalMatrix(matrix);
        shaderPaint.setShader(shader);

        canvas.drawPath(path, shaderPaint);

        paint.setColor(Color.BLUE);
        canvas.drawCircle(rectF.centerX(), rectF.centerY(), 5, paint);
    }

    /* ********************************************* 内部方法 **********************************************/

    /**
     * 获取 渐变长度
     *
     * @param rectF rectF
     * @param angle angle
     * @return float
     */
    private float getShaderLength(RectF rectF, float angle) {
        float width = rectF.width();
        float height = rectF.height();

        // 特殊角度
        if (angle == 0 || angle == 180) {
            return width;
        }
        if (angle == 90 || angle == 270) {
            return height;
        }

        // 计算对角线角度范围(经过中心点的直线是切割宽还是高的角度范围)
        final float diagonalAngle = (float) (Math.atan(height / width) * 180 / Math.PI);
        final float startAngle = diagonalAngle;
        final float endAngle = 180.0f - diagonalAngle;

        // 是否是切割宽
        angle %= 180;
        boolean isCutWith = angle >= startAngle && angle <= endAngle;

        float length;
        if (isCutWith) {// 切割宽
            if (angle < 90) {
                angle = 90 - angle;
            } else {
                angle = angle - 90;
            }
            length = (float) (height / Math.cos(angle * Math.PI / 180));
        } else {// 切割高
            if (angle > 90) {
                angle = 180 - angle;
            }
            length = (float) (width / Math.cos(angle * Math.PI / 180));
        }

        return length;
    }

    /**
     * 格式化 角度
     * <p>
     * 小于0或者大于等于360度的角度格式化为0-360
     *
     * @param angle 角度
     * @return 角度
     */
    private float formatAngle(float angle) {
        angle %= 360;

        while (angle < 0) {
            angle += 360;
        }

        return angle;
    }
    /* ********************************************* 内部方法 **********************************************/

    /* ********************************************* 外部调用 **********************************************/

    /**
     * 获取 旋转角度
     *
     * @return float
     */
    public float getAngle() {
        return angle;
    }

    /**
     * 设置 旋转角度
     */
    public void setAngle(float angle) {
        angle = formatAngle(angle);
        if (this.angle != angle) {
            this.angle = angle;

            postInvalidateOnAnimation();
        }
    }
    /* ********************************************* 外部调用 **********************************************/
}
