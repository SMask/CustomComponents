package com.mask.customcomponents.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.mask.customcomponents.R;
import com.mask.customcomponents.view.chart.ChartData;
import com.mask.customcomponents.view.chart.CurveChartView;

import java.util.ArrayList;
import java.util.List;

/**
 * 曲线图
 * Created by lishilin on 2022/03/30
 */
public class CurveChartViewActivity extends AppCompatActivity {

    private CurveChartView chart_curve;

    /**
     * startActivity
     *
     * @param activity activity
     */
    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, CurveChartViewActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curve_chart_view);
        initView();
        setListener();
        initData();
    }

    private void initView() {
        chart_curve = findViewById(R.id.chart_curve);
    }

    private void setListener() {

    }

    private void initData() {
        final List<ChartData> dataList = new ArrayList<>();
        dataList.add(new ChartData("A", 20));
        dataList.add(new ChartData("B", 45));
        dataList.add(new ChartData("C", 59));
        dataList.add(new ChartData("D", 20));
        dataList.add(new ChartData("E", 71));
        chart_curve.setData(dataList);
    }

    public void onAnimClick(View view) {
        final int viewId = view.getId();
        if (viewId == R.id.btn_anim_enter) {
            chart_curve.startAnimEnter();
        } else if (viewId == R.id.btn_anim_exit) {
            chart_curve.startAnimExit();
        }
    }
}
