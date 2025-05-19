package com.mask.customcomponents.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.mask.customcomponents.R;
import com.mask.customcomponents.view.GradientRotateView;

/**
 * 渐变旋转
 */
public class GradientRotateViewActivity extends AppCompatActivity {

    private GradientRotateView gradient_rotate_view;
    private View btn_add;
    private View btn_subtract;
    private EditText edt_angle;

    private float limit;// 每次增减的进度

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
        btn_add = findViewById(R.id.btn_add);
        btn_subtract = findViewById(R.id.btn_subtract);
        edt_angle = findViewById(R.id.edt_angle);
    }

    private void setListener() {
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gradient_rotate_view.setAngle(gradient_rotate_view.getAngle() + limit);

                refreshView();
            }
        });
        btn_subtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gradient_rotate_view.setAngle(gradient_rotate_view.getAngle() - limit);

                refreshView();
            }
        });
        edt_angle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    gradient_rotate_view.setAngle(Float.parseFloat(s.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initData() {
        limit = 5.0f;

        refreshView();
    }

    /**
     * 刷新 控件
     */
    private void refreshView() {
        edt_angle.setText(String.valueOf(gradient_rotate_view.getAngle()));
    }
}
