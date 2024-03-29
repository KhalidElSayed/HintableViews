package com.alorma.hintableviews;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

/**
 * Created by Bernat on 25/03/2014.
 */
public class HintTitleEditText extends HintTitleLinearLayout implements TextWatcher, View.OnFocusChangeListener {

    private EditText editText;
    private TextWatcher textWatcher;

    public HintTitleEditText(Context context) {
        super(context);
    }
    public HintTitleEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HintTitleEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public EditText createHintedView(int textViewId, float density) {
        editText = new EditText(getContext());
        editText.setVisibility(View.VISIBLE);
        editText.addTextChangedListener(this);
        editText.setOnFocusChangeListener(this);
        return editText;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus)  {
            focusAnimation();
        } else {
            unfocusAnimation();
        }
    }

    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public View getHintableView() {
        return editText;
    }

    @Override
    protected Object saveState() {
        return editText.getText().toString();
    }

    @Override
    protected void restoreState(Object data) {
        String text = String.valueOf(data);
        editText.setText(text);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if (this.textWatcher != null) {
            this.textWatcher.beforeTextChanged(s, start, count, after);
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (this.textWatcher != null) {
            this.textWatcher.onTextChanged(s, start, before, count);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() > 0) {
            showTitle();
        } else {
            hideTitle();
        }
        if (this.textWatcher != null) {
            this.textWatcher.afterTextChanged(s);
        }
    }

    public EditText getEditText() {
        return editText;
    }

    @Override
    public void setHint(int hint) {
        super.setHint(hint);
        editText.setHint(hint);
    }

    @Override
    public void setHint(String hint) {
        super.setHint(hint);
        editText.setHint(hint);
    }

    private Editable getText() {
        return editText.getText();
    }

    public void addTextChangedListener(TextWatcher textWatcher) {
        this.textWatcher = textWatcher;
    }
}
