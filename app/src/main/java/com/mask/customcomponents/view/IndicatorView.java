package com.mask.customcomponents.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.AttrRes;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;

import com.mask.customcomponents.R;
import com.mask.customcomponents.utils.SizeUtils;

/**
 * 指示器 View
 * Create By lishilin On 2019/3/28
 */
public class IndicatorView extends View implements ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener {

    private static final int STYLE_CIRCLE = 0;// 圆形
    private static final int STYLE_RECT = 1;// 矩形

    private int count = 4;// 总数
    private int selectedPosition = 0;// 选中的位置

    private int margin = SizeUtils.dpToPx(6);// 点与点间距
    private int colorNormal = getResources().getColor(R.color.white);// 默认颜色
    private int colorSelected = getResources().getColor(R.color.main);// 选中颜色
    private LinearGradient colorShaderNormal;// 默认颜色(用于渐变，优先级最高)
    private LinearGradient colorShaderSelected;// 选中颜色(用于渐变，优先级最高)
    private Matrix matrixShader;// 渐变矩阵(用于计算绘制渐变偏移量)
    private int style = STYLE_CIRCLE;// 样式

    private int radiusNormal = SizeUtils.dpToPx(3);// 默认半径(STYLE_CIRCLE 有效)
    private int radiusSelected = SizeUtils.dpToPx(3);// 选中半径(STYLE_CIRCLE 有效)

    private int widthNormal = SizeUtils.dpToPx(10);// 默认宽度(STYLE_RECT 有效)
    private int widthSelected = SizeUtils.dpToPx(20);// 选中宽度(STYLE_RECT 有效)
    private int heightNormal = SizeUtils.dpToPx(6);// 默认高度(STYLE_RECT 有效)
    private int heightSelected = SizeUtils.dpToPx(6);// 选中高度(STYLE_RECT 有效)
    private int corners = SizeUtils.dpToPx(6);// 圆角半径(STYLE_RECT 有效)

    private ValueAnimator animatorLeft;// 动画 左
    private ValueAnimator animatorRight;// 动画 右
    private DecelerateInterpolator decelerateInterpolator;// 动画插值器 先快再减速
    private AccelerateInterpolator accelerateInterpolator;// 动画插值器 先慢再加速

    private int selectLeftValue;// 选中的数值 左
    private int selectRightValue;// 选中的数值 右

    private boolean isAnimRunning;// 是否动画正在执行
    private boolean isAnimRunningHalf;// 是否动画正在执行(前半段)
    private boolean isAnimCancel;// 是否动画取消执行

    private boolean isAnimEnable = true;// 是否允许动画执行
    private boolean isAnimToRight;// 是否动画向右执行

    private LinearGradient colorShaderAnim;// 动画颜色
    private Matrix matrixAnim;// 动画渐变矩阵(用于计算绘制渐变偏移量等)

    private int selectedPositionPrevious = selectedPosition;// 上一个选中的位置(用于防止动画中未选中错位)

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

    public IndicatorView(Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup(context, attrs);
    }

    public IndicatorView(Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
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

        animatorLeft = new ValueAnimator();
        animatorLeft.setDuration(250);
        animatorLeft.addUpdateListener(this);
        animatorLeft.addListener(this);

        animatorRight = new ValueAnimator();
        animatorRight.setDuration(250);
        animatorRight.addUpdateListener(this);
        animatorRight.addListener(this);

        decelerateInterpolator = new DecelerateInterpolator();
        accelerateInterpolator = new AccelerateInterpolator();

        paint = new Paint();
        paint.setAntiAlias(true);

        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.IndicatorView);

            count = typedArray.getInt(R.styleable.IndicatorView_indicator_count, count);
            selectedPositionPrevious = selectedPosition = typedArray.getInt(R.styleable.IndicatorView_indicator_selectedPosition, selectedPosition);

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
                height = paddingTop + paddingBottom + Math.max(radiusNormal, radiusSelected) * 2;
                break;
            case STYLE_RECT:
                width = paddingStart + paddingEnd + widthNormal * (count - 1) + widthSelected + margin * (count - 1);
                height = paddingTop + paddingBottom + Math.max(heightNormal, heightSelected);
                break;
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int height = getHeight();

        int selectedPosition = isAnimRunningHalf ? this.selectedPositionPrevious : this.selectedPosition;

        for (int i = 0; i < count; i++) {
            boolean isSelected = i == selectedPosition;// 是否是当前选中的

            switch (style) {
                default:
                case STYLE_CIRCLE:
                    int left = getLeftValue(i, selectedPosition);

                    setColor(isSelected, left);

                    int radius = isSelected ? radiusSelected : radiusNormal;
                    canvas.drawCircle(left + radius, (float) (height / 2), radius, paint);
                    break;
                case STYLE_RECT:
                    left = getLeftValue(i, selectedPosition);
                    int top = getTopValue(i, selectedPosition);
                    int right = getRightValue(i, selectedPosition);
                    int bottom = getBottomValue(i, selectedPosition);

                    setColor(isSelected, left);

                    canvas.drawRoundRect(left, top, right, bottom, corners, corners, paint);
                    break;
            }
        }

        if (!isAnimRunning) {
            selectLeftValue = getLeftValue(selectedPosition, selectedPosition);
            selectRightValue = getRightValue(selectedPosition, selectedPosition);
        }

        if (isAnimRunning) {
            if (colorShaderAnim == null) {
                // 绘制纯色
                paint.setColor(colorSelected);
            } else {
                // 绘制渐变
                float ratio = (selectRightValue - selectLeftValue) * 1.0f / widthSelected;
                matrixAnim.setScale(ratio, 1);
                matrixAnim.postTranslate(selectLeftValue, 0);
                colorShaderAnim.setLocalMatrix(matrixAnim);
                paint.setShader(colorShaderAnim);
            }

            int radius;
            switch (style) {
                default:
                case STYLE_CIRCLE:
                    radius = radiusSelected;
                    break;
                case STYLE_RECT:
                    radius = corners;
                    break;
            }
            canvas.drawRoundRect(selectLeftValue, getTopValue(selectedPosition, selectedPosition), selectRightValue, getBottomValue(selectedPosition, selectedPosition), radius, radius, paint);
        }
    }

    /**
     * 获取 Left
     *
     * @param position         position
     * @param selectedPosition selectedPosition
     * @return int
     */
    private int getLeftValue(int position, int selectedPosition) {
        int paddingStart = getPaddingStart();

        if (position == 0) {
            return paddingStart;
        }

        boolean isBehindSelected = position > selectedPosition;// 是否在选中的后面

        switch (style) {
            default:
            case STYLE_CIRCLE:
                return paddingStart + radiusNormal * 2 * (position - 1) + (isBehindSelected ? radiusSelected : radiusNormal) * 2 + margin * position;
            case STYLE_RECT:
                return paddingStart + widthNormal * (position - 1) + (isBehindSelected ? widthSelected : widthNormal) + margin * position;
        }
    }

    /**
     * 获取 Top
     *
     * @param position         position
     * @param selectedPosition selectedPosition
     * @return int
     */
    private int getTopValue(int position, int selectedPosition) {
        int paddingTop = getPaddingTop();

        boolean isSelected = position == selectedPosition;// 是否是当前选中的

        int top;
        switch (style) {
            default:
            case STYLE_CIRCLE:
                if (radiusSelected > radiusNormal) {
                    top = paddingTop + (isSelected ? 0 : (radiusSelected - radiusNormal));
                } else {
                    top = paddingTop + (isSelected ? (radiusNormal - radiusSelected) : 0);
                }
                break;
            case STYLE_RECT:
                if (heightSelected > heightNormal) {
                    top = paddingTop + (isSelected ? 0 : (heightSelected - heightNormal) / 2);
                } else {
                    top = paddingTop + (isSelected ? (heightNormal - heightSelected) / 2 : 0);
                }
                break;
        }
        return top;
    }

    /**
     * 获取 Right
     *
     * @param position         position
     * @param selectedPosition selectedPosition
     * @return int
     */
    private int getRightValue(int position, int selectedPosition) {
        boolean isSelected = position == selectedPosition;// 是否是当前选中的

        switch (style) {
            default:
            case STYLE_CIRCLE:
                int radius = isSelected ? radiusSelected : radiusNormal;
                return getLeftValue(position, selectedPosition) + radius * 2;
            case STYLE_RECT:
                return getLeftValue(position, selectedPosition) + (isSelected ? widthSelected : widthNormal);
        }
    }

    /**
     * 获取 Bottom
     *
     * @param position         position
     * @param selectedPosition selectedPosition
     * @return int
     */
    private int getBottomValue(int position, int selectedPosition) {
        boolean isSelected = position == selectedPosition;// 是否是当前选中的

        switch (style) {
            default:
            case STYLE_CIRCLE:
                int radius = isSelected ? radiusSelected : radiusNormal;
                return getTopValue(position, selectedPosition) + radius * 2;
            case STYLE_RECT:
                return getTopValue(position, selectedPosition) + (isSelected ? heightSelected : heightNormal);
        }
    }

    /**
     * 设置颜色
     *
     * @param isSelected isSelected
     * @param left       left
     */
    private void setColor(boolean isSelected, int left) {
        // 绘制纯色
        if (colorShaderNormal == null || colorShaderSelected == null) {
            paint.setColor(isSelected ? colorSelected : colorNormal);
            return;
        }

        // 绘制渐变
        matrixShader.setTranslate(left, 0);
        if (isSelected) {
            colorShaderSelected.setLocalMatrix(matrixShader);
        } else {
            colorShaderNormal.setLocalMatrix(matrixShader);
        }
        paint.setShader(isSelected ? colorShaderSelected : colorShaderNormal);
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        if (animation == animatorLeft) {
            selectLeftValue = (int) animation.getAnimatedValue();
        }
        if (animation == animatorRight) {
            selectRightValue = (int) animation.getAnimatedValue();
        }

        invalidate();
    }

    @Override
    public void onAnimationStart(Animator animation) {
        isAnimRunning = true;

        if ((animation == animatorRight && isAnimToRight) || (animation == animatorLeft && !isAnimToRight)) {
            isAnimRunningHalf = true;
        }

        isAnimCancel = false;
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        if (isAnimCancel) {
            return;
        }

        if ((animation == animatorRight && isAnimToRight) || (animation == animatorLeft && !isAnimToRight)) {
            isAnimRunningHalf = false;

            this.selectedPositionPrevious = this.selectedPosition;
        }

        if (!isAnimRunningHalf) {
            if (isAnimToRight) {
                startAnimLeft();
            } else {
                startAnimRight();
            }
        }

        if ((animation == animatorLeft && isAnimToRight) || (animation == animatorRight && !isAnimToRight)) {
            isAnimRunning = false;
        }
    }

    @Override
    public void onAnimationCancel(Animator animation) {
        isAnimCancel = true;
    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

    /**
     * 开始动画
     */
    private void startAnim() {
        animatorLeft.cancel();
        animatorRight.cancel();

        if (isAnimToRight) {
            startAnimRight();
        } else {
            startAnimLeft();
        }
    }

    /**
     * 开始动画 右
     */
    private void startAnimRight() {
        int selectedPositionRight = getRightValue(selectedPosition, selectedPosition);

        if (isAnimToRight) {
            animatorRight.setInterpolator(decelerateInterpolator);
        } else {
            animatorRight.setInterpolator(accelerateInterpolator);
        }

        animatorRight.setIntValues(selectRightValue, selectedPositionRight);

        animatorRight.cancel();

        animatorRight.start();
    }

    /**
     * 开始动画 左
     */
    private void startAnimLeft() {
        int selectedPositionLeft = getLeftValue(selectedPosition, selectedPosition);

        if (isAnimToRight) {
            animatorLeft.setInterpolator(accelerateInterpolator);
        } else {
            animatorLeft.setInterpolator(decelerateInterpolator);
        }

        animatorLeft.setIntValues(selectLeftValue, selectedPositionLeft);

        animatorLeft.cancel();

        animatorLeft.start();
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
        setSelectedPosition(selectedPosition, this.isAnimEnable);
    }

    /**
     * 设置 选中的位置
     *
     * @param selectedPosition selectedPosition
     * @param isAnimEnable     isAnimEnable
     */
    public void setSelectedPosition(int selectedPosition, boolean isAnimEnable) {
        this.isAnimEnable = isAnimEnable;
        if (this.selectedPosition == selectedPosition) {
            return;
        }
        this.selectedPosition = selectedPosition;

        isAnimToRight = this.selectedPosition > this.selectedPositionPrevious;

        if (isAnimEnable) {
            startAnim();
        } else {
            this.selectedPositionPrevious = this.selectedPosition;

            invalidate();
        }
    }

    /**
     * 设置颜色 用于渐变
     *
     * @param colorNormalArr   默认颜色
     * @param colorSelectedArr 选中颜色
     */
    public void setColorShader(int[] colorNormalArr, int[] colorSelectedArr) {
        this.colorShaderNormal = new LinearGradient(0, 0, widthNormal, 0, colorNormalArr, null, LinearGradient.TileMode.CLAMP);
        this.colorShaderSelected = new LinearGradient(0, 0, widthSelected, 0, colorSelectedArr, null, LinearGradient.TileMode.CLAMP);
        this.colorShaderAnim = new LinearGradient(0, 0, widthSelected, 0, colorSelectedArr, null, LinearGradient.TileMode.CLAMP);
        if (matrixShader == null) {
            matrixShader = new Matrix();
        }
        if (matrixAnim == null) {
            matrixAnim = new Matrix();
        }
        invalidate();
    }

}
