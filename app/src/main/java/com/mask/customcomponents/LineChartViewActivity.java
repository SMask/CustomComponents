package com.mask.customcomponents;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mask.customcomponents.view.LineChartView;

public class LineChartViewActivity extends AppCompatActivity {

    private LineChartView lcv_trend;

    /**
     * startActivity
     *
     * @param activity activity
     */
    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, LineChartViewActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_chart_view);
        initView();
        setListener();
        initData();
    }

    private void initView() {
        lcv_trend = findViewById(R.id.lcv_trend);
    }

    private void setListener() {

    }

    private void initData() {
        lcv_trend.setInterceptTouch(false);
    }
}
