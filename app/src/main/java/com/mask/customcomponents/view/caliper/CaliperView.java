package com.mask.customcomponents.view.caliper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.mask.customcomponents.utils.SizeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 游标卡尺 View
 * Created by lishilin on 2020/04/28
 */
public class CaliperView extends View {

    /**
     * 选择监听
     */
    public static abstract class OnSelectListener {

        /**
         * 选中
         *
         * @param startIndex startIndex
         * @param endIndex   endIndex
         */
        public void onSelected(int startIndex, int endIndex) {

        }

    }

    private Context context;

    private float width;// 控件 宽
    private float height;// 控件 高

    private Path path;// 路径
    private Path pathClip;// 裁切路径

    private RectF rectF;
    private Rect rect;

    private Paint textPaint;// 文本 Paint
    private Paint valuePaint;// 内容 Paint
    private Paint caliperPaint;// 卡尺 Paint
    private Paint markPaint;// 刻度 Paint

    // 内容容器
    private RectF valueRootRectF;// 内容容器位置
    private float valueItemWidthMax;// 内容item显示最大宽
    private float valueItemWidth;// 内容item显示宽
    private float valueRootHeight;// 内容容器高
    private float valueRootRadius;// 内容容器圆角半径
    private int valueRootColorNormal;// 内容容器颜色
    private int valueRootAlphaNormal;// 内容容器透明度
    private int valueRootColorSelected;// 内容容器颜色
    private int valueRootAlphaSelected;// 内容容器透明度

    // 内容
    private List<RectF> valueRectFList;// 内容位置
    private float valueFontSize;// 内容字体大小
    private int valueColorNormal;// 内容颜色
    private int valueAlphaNormal;// 内容透明度
    private int valueColorSelected;// 内容颜色
    private int valueAlphaSelected;// 内容透明度

    // 卡尺
    private RectF caliperRectF;// 卡尺位置(完整区域)
    private RectF caliperLeftRectF;// 卡尺左位置(左触摸区域)
    private RectF caliperRightRectF;// 卡尺右位置(右触摸区域)
    private float caliperWidth;// 卡尺宽(主要是两边的触摸区域)
    private float caliperHeight;// 卡尺高
    private float caliperRadius;// 卡尺圆角半径
    private int caliperColor;// 卡尺颜色
    private int caliperAlpha;// 卡尺透明度
    private float caliperPointWidth;// 卡尺触摸点宽(主要是两边触摸区域中间的竖线)
    private float caliperPointHeight;// 卡尺触摸点高
    private float caliperPointRadius;// 卡尺触摸点圆角半径
    private int caliperPointColor;// 卡尺触摸点颜色
    private int caliperPointAlpha;// 卡尺触摸点透明度

    // 刻度线
    private float markWidth;// 刻度宽
    private float markHeight;// 刻度高
    private float markHeightText;// 刻度高(带文字的)
    private int markColor;// 刻度颜色
    private int markAlpha;// 刻度透明度

    // 刻度值
    private int markValue;// 刻度值(一刻度多少秒)
    private int markTextSpace;// 刻度值间距(每间隔多少显示一次)
    private float markTextMarginTop;// 刻度值上间距
    private float markTextFontSize;// 刻度值字体大小
    private int markTextColor;// 刻度值颜色
    private int markTextAlpha;// 刻度值透明度

    // 手势
    private boolean isHandled = false;
    private boolean isHandledCaliperLeft = false;
    private boolean isHandledCaliperRight = false;
    private boolean isHandledValue = false;
    private boolean isHandledChange = false;
    private float touchMoveStartX;// 开始滑动的X
    private float touchMoveOffsetX;// X轴滑动偏移量

    // 数据
    private int caliperLeftOffset;// 卡尺左边空余item数量
    private int caliperRightOffset;// 卡尺右边空余item数量
    private int valueItemNumDisplayMax;// 内容item显示最大数量(屏幕宽显示多少个)
    private int valueItemNumDisplay;// 内容item显示数量(屏幕宽显示多少个)
    private int caliperIndexMin;// 卡尺最小下标
    private int caliperIndexMax;// 卡尺最大下标
    private int caliperIndexStart;// 卡尺起始下标
    private int caliperIndexEnd;// 卡尺结束下标
    private int valueStartIndexMin;// 当前内容最小起始下标
    private int valueStartIndexMax;// 当前内容最大起始下标
    private int valueStartIndex;// 当前内容起始下标

    private List<String> valueList;// 内容

    private OnSelectListener onSelectListener;// 监听

    private boolean isInitData;// 是否已经初始化数据

    public CaliperView(Context context) {
        super(context);
        init(context, null);
    }

    public CaliperView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CaliperView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public CaliperView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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

        this.context = context;

        path = new Path();
        pathClip = new Path();

        rectF = new RectF();
        rect = new Rect();

        textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.FILL);

        valuePaint = new Paint();
        valuePaint.setAntiAlias(true);
        valuePaint.setStyle(Paint.Style.FILL);

        caliperPaint = new Paint();
        caliperPaint.setAntiAlias(true);
        caliperPaint.setStyle(Paint.Style.FILL);

        markPaint = new Paint();
        markPaint.setAntiAlias(true);
        markPaint.setStyle(Paint.Style.STROKE);

        // 内容容器
        valueRootRectF = new RectF();
        valueItemWidthMax = SizeUtils.dpToPx(32.0f);
        valueRootHeight = SizeUtils.dpToPx(24.0f);
        valueRootRadius = SizeUtils.dpToPx(2.0f);
        valueRootColorNormal = 0xFF212635;
        valueRootAlphaNormal = 0x80;
        valueRootColorSelected = 0xFF212635;
        valueRootAlphaSelected = 0xFF;

        // 内容
        valueRectFList = new ArrayList<>();
        valueFontSize = SizeUtils.dpToPx(9.0f);
        valueColorNormal = 0xFFE8E8E9;
        valueAlphaNormal = 0x33;
        valueColorSelected = 0xFFE8E8E9;
        valueAlphaSelected = 0xFF;

        // 卡尺
        caliperRectF = new RectF();
        caliperLeftRectF = new RectF();
        caliperRightRectF = new RectF();
        caliperWidth = SizeUtils.dpToPx(12.0f);
        caliperHeight = SizeUtils.dpToPx(28.0f);
        caliperRadius = SizeUtils.dpToPx(4.0f);
        caliperColor = 0xFF5475FF;
        caliperAlpha = 0xFF;
        caliperPointWidth = SizeUtils.dpToPx(2.0f);
        caliperPointHeight = SizeUtils.dpToPx(8.0f);
        caliperPointRadius = SizeUtils.dpToPx(1.0f);
        caliperPointColor = 0xFF212635;
        caliperPointAlpha = 0xFF;

        // 刻度线
        markWidth = SizeUtils.dpToPx(1.0f);
        markHeight = SizeUtils.dpToPx(2.0f);
        markHeightText = SizeUtils.dpToPx(4.0f);
        markColor = Color.WHITE;
        markAlpha = 0x4D;

        // 刻度值
        markValue = 5;
        markTextSpace = 6;
        markTextMarginTop = SizeUtils.dpToPx(10.0f);
        markTextFontSize = SizeUtils.dpToPx(8.0f);
        markTextColor = 0xFFE8E8E9;
        markTextAlpha = 0xFF;

        // 数据
        caliperLeftOffset = 2;
        caliperRightOffset = 2;
        valueItemNumDisplayMax = 28;
        valueItemNumDisplay = valueItemNumDisplayMax;
        caliperIndexMin = caliperLeftOffset;
        valueStartIndexMin = 0;

        valueList = new ArrayList<>();
        resetValue();

        isInitData = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final float x = event.getX();
        final float y = event.getY();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                isHandledCaliperLeft = false;
                isHandledCaliperRight = false;
                isHandledValue = false;

                isHandledCaliperLeft = isHandledCaliperLeft(x, y);
                if (!isHandledCaliperLeft) {
                    isHandledCaliperRight = isHandledCaliperRight(x, y);
                    if (!isHandledCaliperRight) {
                        isHandledValue = isHandledValue(x, y);
                    }
                }
                if (isHandledCaliperLeft || isHandledCaliperRight || isHandledValue) {
                    isHandled = true;
                    touchMoveStartX = x;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (isHandled) {
                    touchMoveOffsetX = x - touchMoveStartX;
                    if (isHandledCaliperLeft) {
                        isHandledChange = handleCaliperLeft();
                    }
                    if (isHandledCaliperRight) {
                        isHandledChange = handleCaliperRight();
                    }
                    if (isHandledValue) {
                        isHandledChange = handleValue();
                    }
                }
                break;
        }

        if (isHandledChange) {
            refreshDimen();

            postInvalidateOnAnimation();

            notifyOnSelected();
        }

        return isHandled || super.onTouchEvent(event);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        width = w;
        height = h;

        refreshDimen();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawValue(canvas);
        drawCaliper(canvas);
        drawMark(canvas);
    }

    /**
     * 绘制 内容
     *
     * @param canvas canvas
     */
    private void drawValue(Canvas canvas) {
        // 内容容器(卡尺内)
        canvas.save();
        rectF.left = caliperLeftRectF.right;
        rectF.right = caliperRightRectF.left;
        rectF.top = 0;
        rectF.bottom = height;
        canvas.clipRect(rectF);

        valuePaint.setColor(valueRootColorSelected);
        valuePaint.setAlpha(valueRootAlphaSelected);
        canvas.drawRoundRect(valueRootRectF, valueRootRadius, valueRootRadius, valuePaint);

        canvas.restore();

        // 内容容器(卡尺外)
        path.reset();
        path.addRoundRect(valueRootRectF, valueRootRadius, valueRootRadius, Path.Direction.CW);

        pathClip.reset();
        pathClip.addRect(rectF, Path.Direction.CW);
        path.op(pathClip, Path.Op.DIFFERENCE);

        valuePaint.setColor(valueRootColorNormal);
        valuePaint.setAlpha(valueRootAlphaNormal);
        canvas.drawPath(path, valuePaint);

        // 内容
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(valueFontSize);

        for (int i = 0; i < valueList.size(); i++) {
            RectF itemRectF = valueRectFList.get(i);

            String text = valueList.get(i);
            if (TextUtils.isEmpty(text)) {
                continue;
            }

            int index = i - valueStartIndex;
            if (index >= caliperIndexStart && index <= caliperIndexEnd) {
                textPaint.setColor(valueColorSelected);
                textPaint.setAlpha(valueAlphaSelected);
            } else {
                textPaint.setColor(valueColorNormal);
                textPaint.setAlpha(valueAlphaNormal);
            }

            // 裁切防止超出范围
            canvas.save();
            canvas.clipRect(itemRectF);

            float textX = itemRectF.centerX();
            float textY = itemRectF.centerY() - SizeUtils.getTextHeight(text, textPaint, rect) / 2 + SizeUtils.getTextBaseline(text, textPaint, rect);
            canvas.drawText(text, textX, textY, textPaint);

            canvas.restore();
        }
    }

    /**
     * 绘制 卡尺
     *
     * @param canvas canvas
     */
    private void drawCaliper(Canvas canvas) {
        // 卡尺
        path.reset();
        path.addRoundRect(caliperRectF, caliperRadius, caliperRadius, Path.Direction.CW);

        pathClip.reset();
        rectF.set(valueRootRectF);
        rectF.left = caliperLeftRectF.right;
        rectF.right = caliperRightRectF.left;
        pathClip.addRoundRect(rectF, valueRootRadius, valueRootRadius, Path.Direction.CW);
        path.op(pathClip, Path.Op.DIFFERENCE);

        caliperPaint.setColor(caliperColor);
        caliperPaint.setAlpha(caliperAlpha);
        canvas.drawPath(path, caliperPaint);

        // 卡尺触摸点
        caliperPaint.setColor(caliperPointColor);
        caliperPaint.setAlpha(caliperPointAlpha);

        // 卡尺触摸点左
        rectF.set(caliperLeftRectF);
        rectF.inset((caliperLeftRectF.width() - caliperPointWidth) / 2, (caliperLeftRectF.height() - caliperPointHeight) / 2);
        canvas.drawRoundRect(rectF, caliperPointRadius, caliperPointRadius, caliperPaint);

        // 卡尺触摸点右
        rectF.set(caliperRightRectF);
        rectF.inset((caliperRightRectF.width() - caliperPointWidth) / 2, (caliperRightRectF.height() - caliperPointHeight) / 2);
        canvas.drawRoundRect(rectF, caliperPointRadius, caliperPointRadius, caliperPaint);
    }

    /**
     * 绘制 刻度
     *
     * @param canvas canvas
     */
    private void drawMark(Canvas canvas) {
        float startY = Math.max(caliperRectF.bottom, valueRootRectF.bottom);

        markPaint.setStrokeWidth(markWidth);
        markPaint.setColor(markColor);
        markPaint.setAlpha(markAlpha);

        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setTextSize(markTextFontSize);
        textPaint.setColor(markTextColor);
        textPaint.setAlpha(markTextAlpha);

        for (int i = caliperIndexStart; i <= caliperIndexEnd + 1; i++) {
            int index = i - caliperIndexStart;
            boolean isLast = i == caliperIndexEnd + 1;// 是否是最后一个
            boolean isShowMarkText = (isLast || index % markTextSpace == 0);// 是否显示刻度值

            RectF itemRectF = valueRectFList.get(i + valueStartIndex - (isLast ? 1 : 0));

            // 刻度
            float lineX = isLast ? itemRectF.right : itemRectF.left;
            float stopY = startY + (isShowMarkText ? markHeightText : markHeight);
            canvas.drawLine(lineX, startY, lineX, stopY, markPaint);

            if (isShowMarkText) {
                // 刻度值
                String text = getMarkText(index);

                float textX = isLast ? itemRectF.right : itemRectF.left;
                float textY = startY + markTextMarginTop + SizeUtils.getTextBaseline(text, textPaint, rect);
                canvas.drawText(text, textX, textY, textPaint);
            }
        }
    }

    /* ******************************************** 内部调用 **********************************************/

    /**
     * 重置 内容
     */
    private void resetValue() {
        valueList.clear();
        for (int i = 0; i < caliperLeftOffset + caliperRightOffset; i++) {
            valueList.add("");
        }
    }

    /**
     * 通知 选中
     */
    private void notifyOnSelected() {
        if (onSelectListener != null) {
            if (valueList.isEmpty() || valueList.size() <= (caliperLeftOffset + caliperRightOffset)) {
                return;
            }

            int startIndex = caliperIndexStart + valueStartIndex - caliperLeftOffset;
            int endIndex = caliperIndexEnd + valueStartIndex - caliperLeftOffset;
            onSelectListener.onSelected(startIndex, endIndex);
        }
    }

    /**
     * 是否处理(内容)
     *
     * @param x x
     * @param y y
     * @return boolean
     */
    private boolean isHandledValue(float x, float y) {
        return valueRootRectF.contains(x, y);
    }

    /**
     * 处理 内容
     *
     * @return boolean
     */
    private boolean handleValue() {
        // 计算滑动的偏移item数量
        int offsetItem = (int) (Math.abs(touchMoveOffsetX) / valueItemWidth);
        if (offsetItem <= 0) {
            return false;
        }

        // 重新计算起始下标
        int tempIndex = valueStartIndex;
        if (touchMoveOffsetX <= 0) {// 向左滑
            tempIndex += offsetItem;
            touchMoveStartX -= valueItemWidth * offsetItem;
        } else {// 向右滑
            tempIndex -= offsetItem;
            touchMoveStartX += valueItemWidth * offsetItem;
        }

        // 防止超出显示范围
        tempIndex = SizeUtils.minMax(tempIndex, valueStartIndexMin, valueStartIndexMax);

        if (tempIndex == valueStartIndex) {
            return false;
        }

        valueStartIndex = tempIndex;

        return true;
    }

    /**
     * 是否处理(卡尺左触摸区域)
     *
     * @param x x
     * @param y y
     * @return boolean
     */
    private boolean isHandledCaliperLeft(float x, float y) {
        rectF.set(caliperLeftRectF);
        rectF.inset(-(valueItemWidth - rectF.width()) / 2, 0);
        return rectF.contains(x, y);
    }

    /**
     * 处理 卡尺左触摸区域
     *
     * @return boolean
     */
    private boolean handleCaliperLeft() {
        // 计算滑动的偏移item数量
        int offsetItem = (int) (Math.abs(touchMoveOffsetX) / valueItemWidth);
        if (offsetItem <= 0) {
            return false;
        }

        // 重新计算起始下标
        int tempIndex = caliperIndexStart;
        if (touchMoveOffsetX <= 0) {// 向左滑
            tempIndex -= offsetItem;
            touchMoveStartX -= valueItemWidth * offsetItem;
        } else {// 向右滑
            tempIndex += offsetItem;
            touchMoveStartX += valueItemWidth * offsetItem;
        }

        // 防止超出显示范围
        tempIndex = SizeUtils.minMax(tempIndex, caliperIndexMin, caliperIndexEnd);

        if (tempIndex == caliperIndexStart) {
            return false;
        }

        caliperIndexStart = tempIndex;

        return true;
    }

    /**
     * 是否处理(卡尺右触摸区域)
     *
     * @param x x
     * @param y y
     * @return boolean
     */
    private boolean isHandledCaliperRight(float x, float y) {
        rectF.set(caliperRightRectF);
        rectF.inset(-(valueItemWidth - rectF.width()) / 2, 0);
        return rectF.contains(x, y);
    }

    /**
     * 处理 卡尺右触摸区域
     *
     * @return boolean
     */
    private boolean handleCaliperRight() {
        // 计算滑动的偏移item数量
        int offsetItem = (int) (Math.abs(touchMoveOffsetX) / valueItemWidth);
        if (offsetItem <= 0) {
            return false;
        }

        // 重新计算起始下标
        int tempIndex = caliperIndexEnd;
        if (touchMoveOffsetX <= 0) {// 向左滑
            tempIndex -= offsetItem;
            touchMoveStartX -= valueItemWidth * offsetItem;
        } else {// 向右滑
            tempIndex += offsetItem;
            touchMoveStartX += valueItemWidth * offsetItem;
        }

        // 防止超出显示范围
        tempIndex = SizeUtils.minMax(tempIndex, caliperIndexStart, caliperIndexMax);

        if (tempIndex == caliperIndexEnd) {
            return false;
        }

        caliperIndexEnd = tempIndex;

        return true;
    }

    /**
     * 刷新 属性
     */
    private void refreshAttr() {
        // 数据相关
        int valueSize = valueList.size();
        valueItemNumDisplay = Math.min(valueItemNumDisplayMax, valueSize);
        caliperIndexMax = valueItemNumDisplay - caliperRightOffset - 1;
        caliperIndexStart = caliperIndexMin;
        caliperIndexEnd = caliperIndexMax;
        valueStartIndexMax = valueSize - valueItemNumDisplay;
        valueStartIndex = valueStartIndexMin;
    }

    /**
     * 刷新 尺寸
     */
    private void refreshDimen() {
        if (width <= 0 || height <= 0) {
            return;
        }

        // 内容容器
        valueItemWidth = Math.min(valueItemWidthMax, width / valueItemNumDisplay);
        float valueRootWidth = valueItemWidth * valueItemNumDisplay;
        valueRootRectF.left = (width - valueRootWidth) / 2;
        valueRootRectF.right = valueRootRectF.left + valueRootWidth;
        valueRootRectF.top = Math.max(valueRootHeight, caliperHeight) / 2 - valueRootHeight / 2;
        valueRootRectF.bottom = valueRootRectF.top + valueRootHeight;

        // 内容
        while (valueRectFList.size() > valueList.size()) {
            valueRectFList.remove(valueRectFList.size() - 1);
        }
        while (valueRectFList.size() < valueList.size()) {
            valueRectFList.add(new RectF());
        }
        float offsetX = valueItemWidth * valueStartIndex;// X轴偏移量
        for (int i = 0; i < valueList.size(); i++) {
            RectF itemRectF = valueRectFList.get(i);

            itemRectF.left = valueRootRectF.left + valueItemWidth * i;
            itemRectF.right = itemRectF.left + valueItemWidth;
            itemRectF.top = valueRootRectF.top;
            itemRectF.bottom = valueRootRectF.bottom;

            itemRectF.offset(-offsetX, 0);
        }

        // 卡尺
        RectF itemRectFStart = valueRectFList.get(caliperIndexStart + valueStartIndex);
        RectF itemRectFEnd = valueRectFList.get(caliperIndexEnd + valueStartIndex);
        caliperRectF.left = itemRectFStart.left - caliperWidth;
        caliperRectF.right = itemRectFEnd.right + caliperWidth;
        caliperRectF.top = Math.max(caliperHeight, valueRootHeight) / 2 - caliperHeight / 2;
        caliperRectF.bottom = caliperRectF.top + caliperHeight;

        caliperLeftRectF.left = caliperRectF.left;
        caliperLeftRectF.right = itemRectFStart.left;
        caliperLeftRectF.top = caliperRectF.top;
        caliperLeftRectF.bottom = caliperRectF.bottom;

        caliperRightRectF.left = itemRectFEnd.right;
        caliperRightRectF.right = caliperRectF.right;
        caliperRightRectF.top = caliperRectF.top;
        caliperRightRectF.bottom = caliperRectF.bottom;
    }
    /* ******************************************** 内部调用 **********************************************/

    /* ******************************************** 获取数据 **********************************************/

    /**
     * 获取 刻度值
     *
     * @param index index
     * @return String
     */
    private String getMarkText(int index) {
        int valueTotal = markValue * index;
        int minute = valueTotal / 60;
        int second = valueTotal % 60;
        if (minute == 0 && second == 0) {
            return "0";
        }
        String text = "";
        if (minute != 0) {
            text += minute + "m";
        }
        if (minute != 0 && second != 0) {
            text += " ";
        }
        if (second != 0) {
            text += second + "s";
        }
        return text;
    }
    /* ******************************************** 获取数据 **********************************************/

    /* ******************************************** 外部调用 **********************************************/

    /**
     * 设置 内容
     *
     * @param valueList valueList
     */
    public void setValue(List<String> valueList) {
        resetValue();
        if (valueList != null && !valueList.isEmpty()) {
            this.valueList.addAll(caliperLeftOffset, valueList);
        }

        refreshAttr();

        refreshDimen();

        postInvalidateOnAnimation();

        notifyOnSelected();
    }

    /**
     * 设置 监听
     *
     * @param onSelectListener onSelectListener
     */
    public void setOnSelectListener(OnSelectListener onSelectListener) {
        this.onSelectListener = onSelectListener;
    }
    /* ******************************************** 外部调用 **********************************************/
}
