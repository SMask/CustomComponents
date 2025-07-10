package com.mask.customcomponents.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.AttrRes;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;

/**
 * 圆角/平角矩形(四个角单独半径) View
 * Created by lishilin on 2020/09/18
 */
public class RoundBevelRectView extends View {

    private float width;// 控件 宽
    private float height;// 控件 高

    private Path path;

    private RectF rectF;

    private Paint paint;

    private float widthRatio;
    private float heightRatio;
    private float leftTopRatio;
    private float rightTopRatio;
    private float rightBottomRatio;
    private float leftBottomRatio;

    private float[] radiusArr = new float[8];

    private boolean isInitData;// 是否已经初始化数据

    public RoundBevelRectView(Context context) {
        super(context);
        init(context, null);
    }

    public RoundBevelRectView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RoundBevelRectView(Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public RoundBevelRectView(Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
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

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);

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

        float rectWidth = width / 2;
        float rectHeight = rectWidth * heightRatio / widthRatio;
        float rectLength = Math.min(rectWidth, rectHeight);

        float leftTopRadius = rectLength * leftTopRatio * 1.0f / 100;
        float rightTopRadius = rectLength * rightTopRatio * 1.0f / 100;
        float rightBottomRadius = rectLength * rightBottomRatio * 1.0f / 100;
        float leftBottomRadius = rectLength * leftBottomRatio * 1.0f / 100;

        rectF.set(0, 0, rectWidth, rectHeight);
        rectF.offset((width - rectWidth) / 2, (height - rectHeight) / 2);

        // 圆角矩形
        radiusArr[0] = radiusArr[1] = leftTopRadius;
        radiusArr[2] = radiusArr[3] = rightTopRadius;
        radiusArr[4] = radiusArr[5] = rightBottomRadius;
        radiusArr[6] = radiusArr[7] = leftBottomRadius;

        path.reset();
        path.addRoundRect(rectF, radiusArr, Path.Direction.CW);

        paint.setColor(Color.GREEN);
        canvas.drawPath(path, paint);

        // 平角矩形
        float leftRatio = (leftTopRadius + leftBottomRadius) / rectHeight;
        float topRatio = (leftTopRadius + rightTopRadius) / rectWidth;
        float rightRatio = (rightTopRadius + rightBottomRadius) / rectHeight;
        float bottomRatio = (leftBottomRadius + rightBottomRadius) / rectWidth;
        float ratio = Math.max(leftRatio, Math.max(topRatio, Math.max(rightRatio, bottomRatio)));
        if (ratio > 1) {
            leftTopRadius /= ratio;
            rightTopRadius /= ratio;
            rightBottomRadius /= ratio;
            leftBottomRadius /= ratio;
        }

        path.reset();
        path.moveTo(rectF.left + leftTopRadius, rectF.top);
        path.lineTo(rectF.right - rightTopRadius, rectF.top);
        path.lineTo(rectF.right, rectF.top + rightTopRadius);
        path.lineTo(rectF.right, rectF.bottom - rightBottomRadius);
        path.lineTo(rectF.right - rightBottomRadius, rectF.bottom);
        path.lineTo(rectF.left + leftBottomRadius, rectF.bottom);
        path.lineTo(rectF.left, rectF.bottom - leftBottomRadius);
        path.lineTo(rectF.left, rectF.top + leftTopRadius);
        path.close();

        paint.setColor(Color.RED);
        paint.setAlpha(0x80);
        canvas.drawPath(path, paint);
    }

    /* ********************************************* 外部调用 **********************************************/

    /**
     * 刷新比例值
     *
     * @param widthRatio       widthRatio
     * @param heightRatio      heightRatio
     * @param leftTopRatio     leftTopRatio
     * @param rightTopRatio    rightTopRatio
     * @param rightBottomRatio rightBottomRatio
     * @param leftBottomRatio  leftBottomRatio
     */
    public void refreshRatio(float widthRatio, float heightRatio, float leftTopRatio, float rightTopRatio, float rightBottomRatio, float leftBottomRatio) {
        this.widthRatio = widthRatio;
        this.heightRatio = heightRatio;
        this.leftTopRatio = leftTopRatio;
        this.rightTopRatio = rightTopRatio;
        this.rightBottomRatio = rightBottomRatio;
        this.leftBottomRatio = leftBottomRatio;

        postInvalidateOnAnimation();
    }
    /* ********************************************* 外部调用 **********************************************/
}
