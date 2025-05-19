package com.mask.customcomponents.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.mask.customcomponents.R;
import com.mask.customcomponents.view.chart.LineChartView;

/**
 * 折线图
 */
public class LineChartViewActivity extends AppCompatActivity {

    private LineChartView chart_line;

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
        chart_line = findViewById(R.id.chart_line);
    }

    private void setListener() {

    }

    private void initData() {
        int[] scoreArr = new int[]{80, 61, 115, 141, 110, 120};// 数值数组
        String[] timeArr = new String[]{"6月", "7月", "8月", "9月", "10月", "11月"};// 时间数组

        chart_line.setInterceptTouch(false);
        chart_line.setData(scoreArr, timeArr);
    }
}
