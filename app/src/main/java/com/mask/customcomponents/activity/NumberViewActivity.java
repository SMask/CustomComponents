package com.mask.customcomponents.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.mask.customcomponents.R;
import com.mask.customcomponents.view.NumberView;

/**
 * 单个数字边框
 */
public class NumberViewActivity extends AppCompatActivity {

    private NumberView number_view;

    /**
     * startActivity
     *
     * @param activity activity
     */
    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, NumberViewActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_view);
        initView();
        setListener();
        initData();
    }

    private void initView() {
        number_view = findViewById(R.id.number_view);
    }

    private void setListener() {

    }

    private void initData() {
        number_view.postDelayed(new Runnable() {
            @Override
            public void run() {
                number_view.setValue(-87654321);

                number_view.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        number_view.setValue(-12345678);

                        number_view.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                number_view.setValue(-123);

                                number_view.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        number_view.setValue(-123456);
                                    }
                                }, 1500);
                            }
                        }, 1500);
                    }
                }, 1500);
            }
        }, 1500);
    }
}
