package com.mask.customcomponents;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.mask.customcomponents.utils.NumberUtils;
import com.mask.customcomponents.view.RaykiteProgressBar;

/**
 * Raykite进度条
 */
public class RaykiteProgressBarActivity extends AppCompatActivity {

    private RaykiteProgressBar layout_bar;
    private EditText edt_limit;

    private float limit;// 每次增减的进度

    /**
     * startActivity
     *
     * @param activity activity
     */
    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, RaykiteProgressBarActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raykite_progress_bar);
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

    }

    public void onClickAdd(View view) {

    }

}
