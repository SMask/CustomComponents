package com.mask.customcomponents.view.chart;

/**
 * 图表数据
 * Created by lishilin on 2022/03/30
 */
public class ChartData {

    private String key;// key
    private float value;// value

    public ChartData(String key, float value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
