package com.mask.customcomponents.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.mask.customcomponents.R;
import com.mask.customcomponents.utils.NumberUtils;
import com.mask.customcomponents.view.RoundBevelRectView;

/**
 * 圆角/平角矩形(四个角单独半径)
 * Created by lishilin on 2020/09/18
 */
public class RoundBevelRectActivity extends AppCompatActivity {

    private RoundBevelRectView round_bevel_rect_view;
    private EditText edt_width;
    private EditText edt_height;
    private EditText edt_left_top;
    private EditText edt_right_top;
    private EditText edt_right_bottom;
    private EditText edt_left_bottom;

    /**
     * startActivity
     *
     * @param activity activity
     */
    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, RoundBevelRectActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round_bevel_rect);
        initView();
        setListener();
        initData();
    }

    private void initView() {
        round_bevel_rect_view = findViewById(R.id.round_bevel_rect_view);

        edt_width = findViewById(R.id.edt_width);
        edt_height = findViewById(R.id.edt_height);
        edt_left_top = findViewById(R.id.edt_left_top);
        edt_right_top = findViewById(R.id.edt_right_top);
        edt_right_bottom = findViewById(R.id.edt_right_bottom);
        edt_left_bottom = findViewById(R.id.edt_left_bottom);
    }

    private void setListener() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                refreshRatio();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        edt_width.addTextChangedListener(textWatcher);
        edt_height.addTextChangedListener(textWatcher);
        edt_left_top.addTextChangedListener(textWatcher);
        edt_right_top.addTextChangedListener(textWatcher);
        edt_right_bottom.addTextChangedListener(textWatcher);
        edt_left_bottom.addTextChangedListener(textWatcher);
    }

    private void initData() {
        refreshRatio();
    }

    private void refreshRatio() {
        float widthRatio = NumberUtils.parseFloat(edt_width.getText());
        float heightRatio = NumberUtils.parseFloat(edt_height.getText());
        int leftTopRatio = NumberUtils.parseInt(edt_left_top.getText());
        int rightTopRatio = NumberUtils.parseInt(edt_right_top.getText());
        int rightBottomRatio = NumberUtils.parseInt(edt_right_bottom.getText());
        int leftBottomRatio = NumberUtils.parseInt(edt_left_bottom.getText());

        widthRatio = Math.max(widthRatio, 0);
        heightRatio = Math.max(heightRatio, 0);
        leftTopRatio = Math.min(Math.max(leftTopRatio, 0), 100);
        rightTopRatio = Math.min(Math.max(rightTopRatio, 0), 100);
        rightBottomRatio = Math.min(Math.max(rightBottomRatio, 0), 100);
        leftBottomRatio = Math.min(Math.max(leftBottomRatio, 0), 100);

        round_bevel_rect_view.refreshRatio(widthRatio, heightRatio, leftTopRatio, rightTopRatio, rightBottomRatio, leftBottomRatio);
    }
}
