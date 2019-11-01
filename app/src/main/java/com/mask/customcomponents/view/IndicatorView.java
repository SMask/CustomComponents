package com.mask.customcomponents.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.mask.customcomponents.R;
import com.mask.customcomponents.utils.SizeUtils;

/**
 * 指示器 View
 * Create By lishilin On 2019/3/28
 */
public class IndicatorView extends View {

    private static final int STYLE_CIRCLE = 0;// 圆形
    private static final int STYLE_RECT = 1;// 矩形

    private int count = 4;// 总数
    private int selectedPosition = 0;// 选中的位置

    private int margin = SizeUtils.dpToPx(6);// 点与点间距
    private int colorNormal = getResources().getColor(R.color.white);// 默认颜色
    private int colorSelected = getResources().getColor(R.color.main);// 选中颜色
    private int style = STYLE_CIRCLE;// 样式

    private int radiusNormal = SizeUtils.dpToPx(3);// 默认半径(STYLE_CIRCLE 有效)
    private int radiusSelected = SizeUtils.dpToPx(3);// 选中半径(STYLE_CIRCLE 有效)

    private int widthNormal = SizeUtils.dpToPx(10);// 默认宽度(STYLE_RECT 有效)
    private int widthSelected = SizeUtils.dpToPx(20);// 选中宽度(STYLE_RECT 有效)
    private int heightNormal = SizeUtils.dpToPx(6);// 默认高度(STYLE_RECT 有效)
    private int heightSelected = SizeUtils.dpToPx(6);// 选中高度(STYLE_RECT 有效)
    private int corners = SizeUtils.dpToPx(6);// 圆角半径(STYLE_RECT 有效)

    private Paint paint;

    private boolean isInitData;// 是否已经初始化数据

    public IndicatorView(Context context) {
        super(context);
        setup(context, null);
    }

    public IndicatorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setup(context, attrs);
    }

    public IndicatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup(context, attrs);
    }

    public IndicatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setup(context, attrs);
    }

    /**
     * 设置
     *
     * @param context context
     * @param attrs   attrs
     */
    private void setup(Context context, AttributeSet attrs) {
        if (isInitData) {
            return;
        }

        paint = new Paint();
        paint.setAntiAlias(true);

        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.IndicatorView);

            count = typedArray.getInt(R.styleable.IndicatorView_indicator_count, count);
            selectedPosition = typedArray.getInt(R.styleable.IndicatorView_indicator_selectedPosition, selectedPosition);

            margin = typedArray.getDimensionPixelOffset(R.styleable.IndicatorView_indicator_margin, margin);
            colorNormal = typedArray.getColor(R.styleable.IndicatorView_indicator_colorNormal, colorNormal);
            colorSelected = typedArray.getColor(R.styleable.IndicatorView_indicator_colorSelected, colorSelected);
            style = typedArray.getInt(R.styleable.IndicatorView_indicator_style, style);

            radiusNormal = typedArray.getDimensionPixelOffset(R.styleable.IndicatorView_indicator_radiusNormal, radiusNormal);
            radiusSelected = typedArray.getDimensionPixelOffset(R.styleable.IndicatorView_indicator_radiusSelected, radiusSelected);

            widthNormal = typedArray.getDimensionPixelOffset(R.styleable.IndicatorView_indicator_widthNormal, widthNormal);
            widthSelected = typedArray.getDimensionPixelOffset(R.styleable.IndicatorView_indicator_widthSelected, widthSelected);
            heightNormal = typedArray.getDimensionPixelOffset(R.styleable.IndicatorView_indicator_heightNormal, heightNormal);
            heightSelected = typedArray.getDimensionPixelOffset(R.styleable.IndicatorView_indicator_heightSelected, heightSelected);
            corners = typedArray.getDimensionPixelOffset(R.styleable.IndicatorView_indicator_corners, corners);

            typedArray.recycle();
        }

        isInitData = true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int paddingStart = getPaddingStart();
        int paddingTop = getPaddingTop();
        int paddingEnd = getPaddingEnd();
        int paddingBottom = getPaddingBottom();

        int width;
        int height;
        switch (style) {
            default:
            case STYLE_CIRCLE:
                width = paddingStart + paddingEnd + radiusNormal * 2 * (count - 1) + radiusSelected * 2 + margin * (count - 1);
                height = paddingTop + paddingBottom + (radiusNormal < radiusSelected ? radiusSelected : radiusNormal) * 2;
                break;
            case STYLE_RECT:
                width = paddingStart + paddingEnd + widthNormal * (count - 1) + widthSelected + margin * (count - 1);
                height = paddingTop + paddingBottom + (heightNormal >= heightSelected ? heightNormal : heightSelected);
                break;
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int paddingStart = getPaddingStart();
        int paddingTop = getPaddingTop();
        int height = getHeight();

        for (int i = 0; i < count; i++) {
            boolean isSelected = i == selectedPosition;// 是否是当前选中的
            boolean isBehindSelected = i > selectedPosition;// 是否在选中的后面

            paint.setColor(isSelected ? colorSelected : colorNormal);

            switch (style) {
                default:
                case STYLE_CIRCLE:
                    int left;
                    if (i == 0) {
                        left = paddingStart;
                    } else {
                        left = paddingStart + radiusNormal * 2 * (i - 1) + (isBehindSelected ? radiusSelected : radiusNormal) * 2 + margin * i;
                    }

                    int radius = isSelected ? radiusSelected : radiusNormal;
                    canvas.drawCircle(left + radius, (float) (height / 2), radius, paint);
                    break;
                case STYLE_RECT:
                    if (i == 0) {
                        left = paddingStart;
                    } else {
                        left = paddingStart + widthNormal * (i - 1) + (isBehindSelected ? widthSelected : widthNormal) + margin * i;
                    }
                    int top;
                    if (heightSelected > heightNormal) {
                        top = paddingTop + (isSelected ? 0 : (heightSelected - heightNormal) / 2);
                    } else {
                        top = paddingTop + (isSelected ? (heightNormal - heightSelected) / 2 : 0);
                    }
                    int right = left + (isSelected ? widthSelected : widthNormal);
                    int bottom = top + (isSelected ? heightSelected : heightNormal);

                    canvas.drawRoundRect(left, top, right, bottom, corners, corners, paint);
                    break;
            }
        }
    }

    /**
     * 设置 总数
     *
     * @param count count
     */
    public void setCount(int count) {
        if (this.count == count) {
            return;
        }
        this.count = count;
        requestLayout();
    }

    /**
     * 获取 选中的位置
     *
     * @return selectedPosition
     */
    public int getSelectedPosition() {
        return selectedPosition;
    }

    /**
     * 设置 选中的位置
     *
     * @param selectedPosition selectedPosition
     */
    public void setSelectedPosition(int selectedPosition) {
        if (this.selectedPosition == selectedPosition) {
            return;
        }
        this.selectedPosition = selectedPosition;
        invalidate();
    }
}
