package com.mask.customcomponents;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.mask.customcomponents.view.CleanEditText;

/**
 * 带清空按钮的输入框
 */
public class CleanEditTextActivity extends AppCompatActivity {

    private CleanEditText edt_search;

    /**
     * startActivity
     *
     * @param activity activity
     */
    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, CleanEditTextActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clean_edit_text);
        initView();
        setListener();
        initData();
    }

    private void initView() {
        edt_search = findViewById(R.id.edt_search);
    }

    private void setListener() {
        edt_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH && edt_search.length() > 0) {
                    Toast.makeText(getApplicationContext(), edt_search.getText(), Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
    }

    private void initData() {

    }
}
