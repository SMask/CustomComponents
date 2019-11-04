package com.mask.customcomponents;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.mask.customcomponents.view.IndicatorView;

/**
 * 指示器
 */
public class IndicatorViewActivity extends AppCompatActivity {

    private static final int TOTAL_COUNT = 9;

    private IndicatorView layout_indicator;
    private EditText edt_limit;

    private int limit = 1;// 每次移动的个数

    /**
     * startActivity
     *
     * @param activity activity
     */
    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, IndicatorViewActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indicator_view);
        initView();
        setListener();
        initData();
    }

    private void initView() {
        layout_indicator = findViewById(R.id.layout_indicator);
        edt_limit = findViewById(R.id.edt_limit);

        layout_indicator.setCount(TOTAL_COUNT);
        layout_indicator.setSelectedPosition(2, false);

        int[] colorNormalArr = new int[]{Color.parseColor("#FFFFFFFF"), Color.parseColor("#FFFFFFFF")};
        int[] colorSelectedArr = new int[]{Color.parseColor("#FF28ACFF"), Color.parseColor("#FFE851FF")};
        layout_indicator.setColorShader(colorNormalArr, colorSelectedArr);
    }

    private void setListener() {
        edt_limit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    limit = 0;
                } else {
                    limit = Integer.parseInt(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initData() {

    }

    public void onClickPrevious(View view) {
        int position = layout_indicator.getSelectedPosition();
        position = position - limit;
        while (position < 0) {
            position = position + TOTAL_COUNT;
        }
        layout_indicator.setSelectedPosition(position, true);
    }

    public void onClickNext(View view) {
        int position = layout_indicator.getSelectedPosition();
        position = (position + limit) % TOTAL_COUNT;
        layout_indicator.setSelectedPosition(position, true);
    }

}
