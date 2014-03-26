package com.alorma.hintableviews;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

/**
 * Created by Bernat on 25/03/2014.
 */
public class HintableButton extends HintTitleLinearLayout implements View.OnClickListener {
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
    public Button createHintedView(int textViewId, float density) {
        button = new Button(getContext());
        button.setVisibility(View.VISIBLE);
        button.setOnClickListener(this);

        LayoutParams paramsButtom = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        button.setLayoutParams(paramsButtom);

        return button;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public View getHintableView() {
        return button;
    }

    @Override
    protected Object saveState() {
        return button.getText();
    }

    @Override
    protected void restoreState(Object data) {
        String text = String.valueOf(data);
        button.setText(text);
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
