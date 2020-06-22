package com.mask.customcomponents;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.mask.customcomponents.view.ColorPickerView;

/**
 * 颜色拾取器
 */
public class ColorPickerViewActivity extends AppCompatActivity {

    private ColorPickerView color_picker;

    /**
     * startActivity
     *
     * @param activity activity
     */
    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, ColorPickerViewActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_picker_view);
        initView();
        setListener();
        initData();
    }

    private void initView() {
        color_picker = findViewById(R.id.color_picker);
    }

    private void setListener() {

    }

    private void initData() {

    }
}
