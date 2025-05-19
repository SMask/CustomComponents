package com.mask.customcomponents.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.mask.customcomponents.R;
import com.mask.customcomponents.view.RippleView;

/**
 * 水波纹扩散
 */
public class RippleViewActivity extends AppCompatActivity {

    private RippleView layout_ripple;

    /**
     * startActivity
     *
     * @param activity activity
     */
    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, RippleViewActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ripple_view);
        initView();
        setListener();
        initData();
    }

    private void initView() {
        layout_ripple = findViewById(R.id.layout_ripple);
    }

    private void setListener() {

    }

    private void initData() {

    }

    public void onClickStart(View view) {
        layout_ripple.start();
    }

    public void onClickStop(View view) {
        layout_ripple.stop();
    }

}
