package com.mask.customcomponents.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mask.customcomponents.R;
import com.mask.customcomponents.view.ColorPickerView;

/**
 * 颜色拾取器
 */
public class ColorPickerViewActivity extends AppCompatActivity {

    private TextView tv_info;
    private EditText edt_color;
    private View btn_change;
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
        tv_info = findViewById(R.id.tv_info);
        edt_color = findViewById(R.id.edt_color);
        btn_change = findViewById(R.id.btn_change);
        color_picker = findViewById(R.id.color_picker);
    }

    private void setListener() {
        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String colorStr = ("#" + edt_color.getText()).toUpperCase();
                    int color = Color.parseColor(colorStr);
                    float[] hsvArr = new float[3];
                    Color.colorToHSV(color, hsvArr);

                    color_picker.setColor(colorStr);

                    refreshInfo(hsvArr, color, colorStr);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        color_picker.setOnColorChangeListener(new ColorPickerView.OnColorChangeListener() {
            @Override
            public void onColorChange(float[] hsvArr, int color, String colorStr) {
                super.onColorChange(hsvArr, color, colorStr);
                refreshInfo(hsvArr, color, colorStr);
            }
        });
    }

    private void initData() {
        color_picker.setOnlyUpCallback(false);
    }

    private void refreshInfo(float[] hsvArr, int color, String colorStr) {
        tv_info.setText(null);
        tv_info.append("HSV Hue: " + hsvArr[0]);
        tv_info.append("\n");
        tv_info.append("HSV Sat: " + hsvArr[1]);
        tv_info.append("\n");
        tv_info.append("HSV Val: " + hsvArr[2]);
        tv_info.append("\n");
        tv_info.append("ColorInt: " + color);
        tv_info.append("\n");
        tv_info.append("ColorStr: " + colorStr);
    }
}
