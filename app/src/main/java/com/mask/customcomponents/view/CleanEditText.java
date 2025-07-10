package com.mask.customcomponents.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.AttrRes;

import com.mask.customcomponents.R;


/**
 * 带删除输入框
 * Created by lishilin on 2016/9/13.
 */
public class CleanEditText extends LinearLayout {

    private View layout_clean;
    private ImageView img_icon, img_clean;
    private EditText edt_content;

    private boolean edtHasFocus = false;// 输入框是否拥有焦点

    private OnFocusChangeListener onFocusChangeListener;// 焦点改变监听

    public CleanEditText(Context context) {
        super(context);
        setup(context, null);
    }

    public CleanEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup(context, attrs);
    }

    public CleanEditText(Context context, AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup(context, attrs);
    }

    /**
     * 设置
     *
     * @param context context
     * @param attrs   attrs
     */
    private void setup(Context context, AttributeSet attrs) {
        View view = View.inflate(context, R.layout.layout_clean_edit_text, this);
        img_icon = view.findViewById(R.id.img_icon);
        img_clean = view.findViewById(R.id.img_clean);
        edt_content = view.findViewById(R.id.edt_content);
        layout_clean = view.findViewById(R.id.layout_clean);

        img_clean.setVisibility(GONE);

        setListener();

        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CleanEditText);

            // img_icon
            int iconRes = typedArray.getResourceId(R.styleable.CleanEditText_icon_res, -1);
            if (iconRes == -1) {
                setIconVisibility(GONE);
            } else {
                setIconRes(iconRes);
            }
            int iconSize = typedArray.getDimensionPixelOffset(R.styleable.CleanEditText_iconSize, 48);
            if (iconSize >= 0) {
                ViewGroup.LayoutParams params = img_icon.getLayoutParams();
                params.width = iconSize;
                img_icon.setLayoutParams(params);
            }

            // img_clean
            int cleanRes = typedArray.getResourceId(R.styleable.CleanEditText_clean_res, -1);
            if (cleanRes == -1) {
                setCleanVisibility(GONE);
            } else {
                setCleanRes(cleanRes);
            }
            int cleanSize = typedArray.getDimensionPixelOffset(R.styleable.CleanEditText_cleanSize, 48);
            if (cleanSize >= 0) {
                ViewGroup.LayoutParams params = img_clean.getLayoutParams();
                params.width = cleanSize;
                img_clean.setLayoutParams(params);
            }

            // hint
            CharSequence hint = typedArray.getText(R.styleable.CleanEditText_hint);
            edt_content.setHint(hint);

            // text
            CharSequence text = typedArray.getText(R.styleable.CleanEditText_text);
            edt_content.setText(text);

            // maxLength
            int maxLength = typedArray.getInt(R.styleable.CleanEditText_maxLength, -1);
            if (maxLength >= 0) {
                edt_content.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
            } else {
                edt_content.setFilters(new InputFilter[0]);
            }

            // textSize
            int textSize = typedArray.getDimensionPixelOffset(R.styleable.CleanEditText_textSize, -1);
            if (textSize >= 0) {
                edt_content.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            }

            // textColor
            ColorStateList textColor = typedArray.getColorStateList(R.styleable.CleanEditText_textColor);
            if (textColor != null) {
                edt_content.setTextColor(textColor);
            }

            // textColorHint
            ColorStateList textColorHint = typedArray.getColorStateList(R.styleable.CleanEditText_textColorHint);
            if (textColorHint != null) {
                edt_content.setHintTextColor(textColorHint);
            }

            // inputType
            int inputTypeValue = typedArray.getInt(R.styleable.CleanEditText_inputType, -1);
            int inputType = -1;
            switch (inputTypeValue) {
                case 1:// number
                    inputType = InputType.TYPE_CLASS_NUMBER;
                    break;
                case 2:// phone
                    inputType = InputType.TYPE_CLASS_PHONE;
                    break;
                case 3:// textEmailAddress
                    inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS;
                    break;
                case 4:// textPassword
                    inputType = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;
                    break;
                case 5:// numberPassword
                    inputType = InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD;
                    break;
            }
            if (inputType != -1) {
                edt_content.setInputType(inputType);
            }

            // imeOptions
            int imeOptionsValue = typedArray.getInt(R.styleable.CleanEditText_imeOptions, -1);
            int imeOptions = -1;
            switch (imeOptionsValue) {
                case 1:// actionSearch
                    imeOptions = EditorInfo.IME_ACTION_SEARCH;
                    break;
                case 2:// actionDone
                    imeOptions = EditorInfo.IME_ACTION_DONE;
                    break;
            }
            if (imeOptions != -1) {
                edt_content.setImeOptions(imeOptions);
            }

            // digits
            CharSequence digits = typedArray.getText(R.styleable.CleanEditText_digits);
            if (!TextUtils.isEmpty(digits)) {
                edt_content.setKeyListener(DigitsKeyListener.getInstance(digits.toString()));
            }

            typedArray.recycle();
        }
    }

    /**
     * 设置监听
     */
    private void setListener() {
        edt_content.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                edtHasFocus = hasFocus;
                resetCleanView();
                if (onFocusChangeListener != null) {
                    onFocusChangeListener.onFocusChange(v, hasFocus);
                }
            }
        });
        edt_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                resetCleanView();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        img_clean.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_content.setText(null);
            }
        });
    }

    /**
     * 刷新删除View
     */
    private void resetCleanView() {
        // 在输入框没有输入时不显示清除图标，有输入后显示
        if (edtHasFocus && !TextUtils.isEmpty(edt_content.getText())) {
            img_clean.setVisibility(View.VISIBLE);
        } else {
            img_clean.setVisibility(View.GONE);
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        img_icon.setEnabled(enabled);
        edt_content.setEnabled(enabled);
        img_clean.setEnabled(enabled);
        layout_clean.setEnabled(enabled);
    }

    public void setCleanRes(int cleanRes) {
        img_clean.setImageResource(cleanRes);
    }

    public void setCleanVisibility(int visibility) {
        img_clean.setVisibility(visibility);
    }

    public void setIconRes(int iconRes) {
        img_icon.setImageResource(iconRes);
    }

    public void setIconVisibility(int visibility) {
        img_icon.setVisibility(visibility);
    }

    public EditText getEditText() {
        return edt_content;
    }

    public Editable getText() {
        return edt_content.getText();
    }

    public void setText(CharSequence text) {
        edt_content.setText(text);
    }

    public void setText(int resId) {
        edt_content.setText(resId);
    }

    public void setHint(CharSequence hint) {
        edt_content.setHint(hint);
    }

    public void setHint(int resId) {
        edt_content.setHint(resId);
    }

    public void append(CharSequence text) {
        edt_content.append(text);
    }

    public void setSelection(int index) {
        edt_content.setSelection(index);
    }

    public void setFilters(InputFilter[] filters) {
        edt_content.setFilters(filters);
    }

    public void setInputType(int inputType) {
        edt_content.setInputType(inputType);
    }

    public int length() {
        return edt_content.length();
    }

    public void addTextChangedListener(TextWatcher textWatcher) {
        edt_content.addTextChangedListener(textWatcher);
    }

    public void setOnEditorActionListener(TextView.OnEditorActionListener l) {
        edt_content.setOnEditorActionListener(l);
    }

    @Override
    public void setOnFocusChangeListener(OnFocusChangeListener onFocusChangeListener) {
        super.setOnFocusChangeListener(onFocusChangeListener);
        this.onFocusChangeListener = onFocusChangeListener;
    }

}
