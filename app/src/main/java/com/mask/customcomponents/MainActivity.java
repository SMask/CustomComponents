package com.mask.customcomponents;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickLineChartView(View view) {
        LineChartViewActivity.startActivity(this);
    }

    public void onClickPasswordInputView(View view) {
        PasswordInputViewActivity.startActivity(this);
    }

    public void onClickCleanEditText(View view) {
        CleanEditTextActivity.startActivity(this);
    }

    public void onClickCreditView(View view) {
        CreditViewActivity.startActivity(this);
    }

    public void onClickRippleView(View view) {
        RippleViewActivity.startActivity(this);
    }

}
