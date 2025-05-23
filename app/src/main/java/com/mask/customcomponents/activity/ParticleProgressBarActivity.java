package com.mask.customcomponents.activity;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.mask.customcomponents.R;
import com.mask.customcomponents.utils.NumberUtils;
import com.mask.customcomponents.utils.SizeUtils;
import com.mask.customcomponents.view.ParticleProgressBar;

/**
 * 粒子动画进度条
 */
public class ParticleProgressBarActivity extends AppCompatActivity {

    private ParticleProgressBar layout_bar;
    private EditText edt_limit;

    private float limit;// 每次增减的进度

    private ValueAnimator animator;// 动画

    /**
     * startActivity
     *
     * @param activity activity
     */
    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, ParticleProgressBarActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_particle_progress_bar);
        initView();
        setListener();
        initData();
    }

    private void initView() {
        layout_bar = findViewById(R.id.layout_bar);
        edt_limit = findViewById(R.id.edt_limit);
    }

    private void setListener() {
        edt_limit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                limit = NumberUtils.parseFloat(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initData() {
        limit = NumberUtils.parseFloat(edt_limit.getText());
    }

    public void onClickSubtract(View view) {
        layout_bar.setPercent(layout_bar.getPercent() - limit);
    }

    public void onClickAdd(View view) {
        layout_bar.setPercent(layout_bar.getPercent() + limit);
    }

    public void onClickStart(View view) {
        if (animator == null) {
            animator = new ValueAnimator();
            animator.setInterpolator(new AccelerateDecelerateInterpolator());

            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    layout_bar.setPercent((float) animation.getAnimatedValue(), false);
                }
            });
        }
        if (animator.isRunning()) {
            return;
        }
        float percent = layout_bar.getPercent();
        animator.setFloatValues(percent, 1.0f);
        animator.setDuration((long) SizeUtils.getPercentValue(percent, 10000, 0));
        animator.start();
    }

}
