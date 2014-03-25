package com.alorma.hintableviews;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

/**
 * Created by Bernat on 25/03/2014.
 */
public class HintableButton extends HintTitleRelativeLayout implements View.OnClickListener {
    private Button button;
    private int count = 0;

    public HintableButton(Context context) {
        super(context);
    }

    public HintableButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HintableButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void createHintedView(int textViewId, float density) {
        button = new Button(getContext());
        button.setVisibility(View.VISIBLE);
        button.setOnClickListener(this);

        LayoutParams paramsEditText = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        paramsEditText.addRule(RelativeLayout.ABOVE, textViewId);
        paramsEditText.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            paramsEditText.addRule(RelativeLayout.ALIGN_PARENT_START);
            paramsEditText.addRule(RelativeLayout.ALIGN_PARENT_END);
        }

        paramsEditText.setMargins(0, (int) (16 * density), 0, 0);

        addView(button, paramsEditText);
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public void setHint(String hint) {
        super.setHint(hint);
        button.setHint(hint);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == button.getId()) {
            showTitle();
            button.setText("Count: " + (count++));
        }
    }
}
