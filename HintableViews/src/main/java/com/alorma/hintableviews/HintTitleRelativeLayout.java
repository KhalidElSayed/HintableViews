package com.alorma.hintableviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Bernat on 25/03/2014.
 */
public abstract class HintTitleRelativeLayout extends RelativeLayout {

    private float density;
    protected String hint;
    private TextView textView;

    public HintTitleRelativeLayout(Context context) {
        super(context);
        init(context, null);
    }

    public HintTitleRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public HintTitleRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        this.hint = getHint(attrs);

        density = context.getResources().getDisplayMetrics().density;

        textView = new TextView(context);
        if (isInEditMode()) {
            textView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.GONE);
        }
        addTextView(textView);

        createHintedView(textView.getId(), density);

        setHint(hint);
    }

    public void setHint(int hint) {
        textView.setText(hint);
    }

    public void setHint(String hint) {
        textView.setText(hint);
    }

    public abstract void createHintedView(int textViewId, float density);

    public abstract boolean isValid();

    private String getHint(AttributeSet attrs) {
        if (getContext() != null && attrs != null) {
            int[] attributes = new int[]{android.R.attr.hint};
            TypedArray a = getContext().obtainStyledAttributes(attrs, attributes);
            if (a != null) {
                return a.getString(0);
            }
        }
        return "";
    }

    private void addTextView(TextView textView) {
        LayoutParams paramsTextView = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        paramsTextView.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        paramsTextView.addRule(RelativeLayout.ALIGN_PARENT_TOP);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            paramsTextView.addRule(RelativeLayout.ALIGN_PARENT_START);
        }

        paramsTextView.setMargins((int) (16 * density), 0, 0, 0);
        addView(textView, paramsTextView);
    }

    protected void showTitle() {
        textView.setVisibility(VISIBLE);
    }

    protected void hideTitle() {
        textView.setVisibility(GONE);
    }
}
