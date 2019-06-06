package com.mask.customcomponents;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.mask.customcomponents.view.CreditView;
import com.mask.customcomponents.view.LineChartView;

import java.util.Random;

/**
 * 信用分仪表盘
 */
public class CreditViewActivity extends AppCompatActivity {

    private CreditView layout_credit;

    private Random random = new Random();

    /**
     * startActivity
     *
     * @param activity activity
     */
    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, CreditViewActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_view);
        initView();
        setListener();
        initData();
    }

    private void initView() {
        layout_credit = findViewById(R.id.layout_credit);
    }

    private void setListener() {

    }

    private void initData() {

    }

    public void onClickStart(View view) {
        int score = random.nextInt(100);
        layout_credit.setData("更新：2018.08.08", score, "信用优秀");
    }
}
