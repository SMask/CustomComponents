package com.mask.customcomponents.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputType;
import android.util.AttributeSet;

/**
 * 不可见密码输入框
 * Created by lishilin
 */
public class PasswordInputView extends AppCompatEditText {

    private int textLength = 0;// 当前内容长度

    private int borderColor = Color.parseColor("#808080");// 外边框颜色
    private final int separateColor = Color.parseColor("#CCCCCC");// 分割线颜色
    private float borderWidth = 0.1f;// 边框宽
    private float borderRadius = 5;// 边框圆角

    private int passwordLength = 6;// 密码长度
    private int passwordColor = Color.parseColor("#1A1A1A");// 密码颜色
    private float passwordWidth = 24;// 密码宽度

    private final Paint passwordPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private final int defaultContMargin = 1;// 默认内容间距
    private final int defaultSplitLineWidth = 1;// 默认分隔线宽

    public PasswordInputView(Context context, AttributeSet attrs) {
        super(context, attrs);

        super.setInputType(InputType.TYPE_CLASS_NUMBER);
        super.setCursorVisible(false);

        passwordPaint.setStrokeWidth(passwordWidth);
        passwordPaint.setStyle(Paint.Style.FILL);
        passwordPaint.setColor(passwordColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();

        // 绘制外边框
        borderPaint.setColor(borderColor);
        borderPaint.setStrokeWidth(borderWidth);
        canvas.drawRoundRect(0, 0, width, height, borderRadius, borderRadius, borderPaint);

        // 绘制内容区
        borderPaint.setColor(Color.WHITE);
        canvas.drawRoundRect(defaultContMargin, defaultContMargin, width - defaultContMargin, height - defaultContMargin, borderRadius, borderRadius, borderPaint);

        // 绘制分割线
        borderPaint.setColor(separateColor);
        borderPaint.setStrokeWidth(defaultSplitLineWidth);
        for (int i = 1; i < passwordLength; i++) {
            int x = width * i / passwordLength;
            canvas.drawLine(x, 0, x, height, borderPaint);
        }

        // 绘制密码
        int cx, cy = height / 2;
        int half = width / passwordLength / 2;
        for (int i = 0; i < textLength; i++) {
            cx = width * i / passwordLength + half;
            canvas.drawCircle(cx, cy, passwordWidth, passwordPaint);
        }
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        textLength = text.length();
        if (textLength > passwordLength) {
            super.setText(text.subSequence(0, passwordLength));
            super.requestFocus();
            super.setSelection(length());
        } else {
            invalidate();
        }
    }

    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
        borderPaint.setColor(borderColor);
        invalidate();
    }

    public void setBorderWidth(float borderWidth) {
        this.borderWidth = borderWidth;
        borderPaint.setStrokeWidth(borderWidth);
        invalidate();
    }

    public void setBorderRadius(float borderRadius) {
        this.borderRadius = borderRadius;
        invalidate();
    }

    public void setPasswordLength(int passwordLength) {
        this.passwordLength = passwordLength;
        invalidate();
    }

    public void setPasswordColor(int passwordColor) {
        this.passwordColor = passwordColor;
        passwordPaint.setColor(passwordColor);
        invalidate();
    }

    public void setPasswordWidth(float passwordWidth) {
        this.passwordWidth = passwordWidth;
        passwordPaint.setStrokeWidth(passwordWidth);
        invalidate();
    }

}
