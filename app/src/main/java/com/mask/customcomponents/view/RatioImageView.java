package com.mask.customcomponents.view;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.appcompat.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.mask.customcomponents.R;

/**
 * 等比例长宽的ImageView
 * Created by lishilin on 2016/8/4.
 */
public class RatioImageView extends AppCompatImageView {

    private int aspectX;// X轴比例
    private int aspectY;// Y轴比例
    private int aspectBase;// 比例参照(以X或Y为比例参照；X为1，Y为2，默认X)

    public RatioImageView(Context context) {
        super(context);
    }

    public RatioImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup(attrs);
    }

    public RatioImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setup(attrs);
    }

    private void setup(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.RatioView);
        aspectX = typedArray.getInt(R.styleable.RatioView_aspectX, 1);
        aspectY = typedArray.getInt(R.styleable.RatioView_aspectY, 1);
        aspectBase = typedArray.getInt(R.styleable.RatioView_aspectBase, 1);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (width <= 0) {
            width = getMeasuredWidth();
        }
        if (height <= 0) {
            height = getMeasuredHeight();
        }
        if (aspectBase == 1) {
            height = width * aspectY / aspectX;
        } else if (aspectBase == 2) {
            width = height * aspectX / aspectY;
        }
        setMeasuredDimension(width, height);
    }

    public int getAspectX() {
        return aspectX;
    }

    public int getAspectY() {
        return aspectY;
    }

    public void setAspectXY(int aspectX, int aspectY) {
        if (aspectX * this.aspectY == aspectY * this.aspectX) {
            return;
        }
        this.aspectX = aspectX;
        this.aspectY = aspectY;
        requestLayout();
    }

}
