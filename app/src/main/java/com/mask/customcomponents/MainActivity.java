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

    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.btn_line_chart_view:
                LineChartViewActivity.startActivity(this);
                break;
            case R.id.btn_password_input_view:
                PasswordInputViewActivity.startActivity(this);
                break;
            case R.id.btn_clean_edit_text:
                CleanEditTextActivity.startActivity(this);
                break;
            case R.id.btn_credit_view:
                CreditViewActivity.startActivity(this);
                break;
            case R.id.btn_ripple_view:
                RippleViewActivity.startActivity(this);
                break;
            case R.id.btn_indicator_view:
                IndicatorViewActivity.startActivity(this);
                break;
            case R.id.btn_raykite_progress_bar:
                RaykiteProgressBarActivity.startActivity(this);
                break;
        }
    }

}
