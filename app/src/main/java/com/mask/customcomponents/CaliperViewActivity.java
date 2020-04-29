package com.mask.customcomponents;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mask.customcomponents.view.caliper.CaliperAdapter;
import com.mask.customcomponents.view.caliper.CaliperView;

import java.util.ArrayList;
import java.util.List;

/**
 * 游标卡尺
 */
public class CaliperViewActivity extends AppCompatActivity {

    private TextView tv_info;
    private RecyclerView rv_caliper;
    private CaliperView caliper_view;

    private List<String> valueList;
    private CaliperAdapter caliperAdapter;

    /**
     * startActivity
     *
     * @param activity activity
     */
    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, CaliperViewActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caliper_view);
        initView();
        setListener();
        initData();
    }

    private void initView() {
        tv_info = findViewById(R.id.tv_info);
        rv_caliper = findViewById(R.id.rv_caliper);
        caliper_view = findViewById(R.id.caliper_view);

        // 使RecyclerView保持固定的大小,这样会提高RecyclerView的性能
        rv_caliper.setHasFixedSize(true);

        // 布局
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_caliper.setLayoutManager(layoutManager);

//        SnapHelper snapHelper = new LinearSnapHelper();
//        snapHelper.attachToRecyclerView(rv_caliper);
    }

    private void setListener() {
        caliper_view.setOnSelectListener(new CaliperView.OnSelectListener() {
            @Override
            public void onSelected(int startIndex, int endIndex) {
                super.onSelected(startIndex, endIndex);

                tv_info.setText(startIndex + " " + endIndex);
                tv_info.append("\n");
                tv_info.append(valueList.get(startIndex) + " " + valueList.get(endIndex));
            }
        });
    }

    private void initData() {
        valueList = new ArrayList<>();
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                valueList.add(i + "/" + j);
            }
        }
        caliperAdapter = new CaliperAdapter(this, valueList);
        rv_caliper.setAdapter(caliperAdapter);

//        valueList.clear();
//        valueList.add("1/1");
//        valueList.add("1/2");
        caliper_view.setValue(valueList);
    }
}
