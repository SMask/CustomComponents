package com.mask.customcomponents;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.mask.customcomponents.view.GradientRotateView;

/**
 * 渐变旋转
 */
public class GradientRotateViewActivity extends AppCompatActivity {

    private GradientRotateView gradient_rotate_view;

    /**
     * startActivity
     *
     * @param activity activity
     */
    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, GradientRotateViewActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gradient_rotate);
        initView();
        setListener();
        initData();
    }

    private void initView() {
        gradient_rotate_view = findViewById(R.id.gradient_rotate_view);
    }

    private void setListener() {

    }

    private void initData() {

    }
}
