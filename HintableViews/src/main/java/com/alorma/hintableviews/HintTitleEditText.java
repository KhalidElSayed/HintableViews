package com.alorma.hintableviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Bernat on 25/03/2014.
 */
public class HintTitleEditText extends HintTitleRelativeLayout implements TextWatcher {

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
    public void createHintedView(int textViewId, float density) {
        editText = new EditText(getContext());
        editText.setVisibility(View.VISIBLE);
        editText.addTextChangedListener(this);

        LayoutParams paramsEditText = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        paramsEditText.addRule(RelativeLayout.ABOVE, textViewId);
        paramsEditText.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        paramsEditText.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            paramsEditText.addRule(RelativeLayout.ALIGN_PARENT_START);
            paramsEditText.addRule(RelativeLayout.ALIGN_PARENT_END);
        }

        paramsEditText.setMargins(0, (int) (16 * density), 0, 0);

        addView(editText, paramsEditText);
    }

    @Override
    public boolean isValid() {
        return false;
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
