package com.mask.customcomponents.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.mask.customcomponents.utils.SizeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 单个数字边框 View
 * Created by lishilin on 2020/05/09
 */
public class NumberView extends View {

    private float width;// 控件 宽
    private float height;// 控件 高

    private Rect rect;

    private Paint textPaint;// 文本 Paint
    private Paint borderPaint;// 边框 Paint

    private float paddingH;// 横向内间距
    private float paddingV;// 纵向内间距

    private List<RectF> numRectFList;// 数字位置
    private float textFontSize;// 文本字体大小
    private int textColor;// 文本颜色
    private int textAlpha;// 文本透明度
    private float textMarginH;// 文本横向外间距
    private float textMarginV;// 文本纵向外间距
    private float textSpace;// 文本之间间距
    private float borderRadius;// 边框圆角半径
    private float borderWidth;// 边框宽度
    private int borderColor;// 边框颜色

    private float numRectFWidth;// 数字宽(包括边框)
    private float numRectFHeight;// 数字高(包括边框)

    private String numStr;// 数字文本

    private boolean isInitData;// 是否已经初始化数据

    public NumberView(Context context) {
        super(context);
        init(context, null);
    }

    public NumberView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public NumberView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public NumberView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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

        rect = new Rect();

        textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.FILL);

        borderPaint = new TextPaint();
        borderPaint.setAntiAlias(true);
        borderPaint.setStyle(Paint.Style.STROKE);

        paddingH = SizeUtils.dpToPx(8.0f);
        paddingV = SizeUtils.dpToPx(8.0f);

        numRectFList = new ArrayList<>();
        textFontSize = SizeUtils.dpToPx(24.0f);
        textColor = Color.BLACK;
        textAlpha = 0xFF;
        textMarginH = SizeUtils.dpToPx(6.0f);
        textMarginV = SizeUtils.dpToPx(9.0f);
        textSpace = SizeUtils.dpToPx(6.0f);
        borderRadius = SizeUtils.dpToPx(3.0f);
        borderWidth = SizeUtils.dpToPx(1.0f);
        borderColor = Color.BLACK;

        textPaint.setTextSize(textFontSize);
        float textWidth = 0;
        float textHeight = 0;
        String[] tempArr = new String[]{"-", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};
        for (String temp : tempArr) {
            textWidth = Math.max(textWidth, SizeUtils.getTextWidth(temp, textPaint, rect));
            textHeight = Math.max(textHeight, SizeUtils.getTextHeight(temp, textPaint, rect));
        }
        numRectFWidth = textWidth + textMarginH * 2;
        numRectFHeight = textHeight + textMarginV * 2;

        refreshValue(-123456);

        isInitData = true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        refreshDimen();

        float width;
        float height;

        if (!numRectFList.isEmpty()) {
            RectF itemRectF = numRectFList.get(numRectFList.size() - 1);
            width = itemRectF.right + paddingH;
            height = itemRectF.bottom + paddingV;
        } else {
            width = paddingH * 2;
            height = numRectFHeight + paddingV * 2;
        }

        setMeasuredDimension((int) (width + 1), (int) (height + 1));
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

        borderPaint.setStrokeWidth(borderWidth);
        borderPaint.setColor(borderColor);

        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(textFontSize);
        textPaint.setColor(textColor);
        textPaint.setAlpha(textAlpha);

        float textY = (height - SizeUtils.getTextHeight(numStr, textPaint, rect)) / 2 + SizeUtils.getTextBaseline(numStr, textPaint, rect);

        for (int i = 0; i < numStr.length(); i++) {
            RectF itemRectF = numRectFList.get(i);

            // 绘制边框
            canvas.drawRoundRect(itemRectF, borderRadius, borderRadius, borderPaint);

            // 绘制数字
            String text = String.valueOf(numStr.charAt(i));
            float textX = itemRectF.centerX();
            canvas.drawText(text, textX, textY, textPaint);
        }
    }

    /* ******************************************** 内部调用 **********************************************/

    /**
     * 刷新 数据
     */
    private void refreshValue(int value) {
        numStr = String.valueOf(value);
    }

    /**
     * 刷新 尺寸
     */
    private void refreshDimen() {
        while (numRectFList.size() > numStr.length()) {
            numRectFList.remove(numRectFList.size() - 1);
        }
        while (numRectFList.size() < numStr.length()) {
            numRectFList.add(new RectF());
        }

        for (int i = 0; i < numStr.length(); i++) {
            RectF itemRectF = numRectFList.get(i);

            itemRectF.left = paddingH + (numRectFWidth + textSpace) * i;
            itemRectF.right = itemRectF.left + numRectFWidth;
            itemRectF.top = paddingV;
            itemRectF.bottom = itemRectF.top + numRectFHeight;
        }
    }
    /* ******************************************** 内部调用 **********************************************/

    /* ******************************************** 外部调用 **********************************************/

    /**
     * 设置 数据
     *
     * @param value value
     */
    public void setValue(int value) {
        refreshValue(value);

        requestLayout();

        postInvalidateOnAnimation();
    }
    /* ******************************************** 外部调用 **********************************************/

}
