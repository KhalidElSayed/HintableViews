package com.alorma.hintableviews;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.Serializable;

/**
 * Created by Bernat on 25/03/2014.
 */
public abstract class HintTitleLinearLayout extends LinearLayout {

    private static final String TITLE_SHOWING = "TITLE_SHOWING";
    private static final String TITLE_FOCUSED = "TITLE_FOCUSED";
    private float density;
    protected String hint;
    private TextView textView;

    private int focusedColor = Color.parseColor("#27ae60");
    private int unFocusedColor = Color.parseColor("#7f8c8d");
    private Boolean focused = null;

    public HintTitleLinearLayout(Context context) {
        super(context);
        init(context, null, 0);
    }

    public HintTitleLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public HintTitleLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int style) {

        setOrientation(VERTICAL);

        this.hint = getHint(attrs);

        density = context.getResources().getDisplayMetrics().density;

        textView = new TextView(context, attrs, style);
        textView.setId(hashCode());
        textView.setVisibility(View.INVISIBLE);
        textView.setTextColor(unFocusedColor);
        textView.setTypeface(textView.getTypeface(), Typeface.BOLD);

        addTextView(textView);

        View view = createHintedView(textView.getId(), density);
        view.setId(hashCode());
        addView(view);

        setHint(hint);
    }

    public void setHint(int hint) {
        textView.setText(hint);
    }

    public void setHint(String hint) {
        textView.setText(hint);
    }

    public abstract View createHintedView(int textViewId, float density);

    public abstract boolean isValid();

    private String getHint(AttributeSet attrs) {
        String hint = "";
        if (getContext() != null && attrs != null) {
            int[] attributes = new int[]{android.R.attr.hint};
            TypedArray a = getContext().obtainStyledAttributes(attrs, attributes);
            if (a != null) {

                hint = a.getString(0);
                a.recycle();
            }
        }
        return hint;
    }

    private void addTextView(TextView textView) {
        LayoutParams paramsTextView = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        paramsTextView.setMargins((int) (16 * density), 0, 0, 0);
        addView(textView, paramsTextView);
    }

    protected void showTitle() {
        if (textView.getVisibility() == View.INVISIBLE) {
            textView.setVisibility(View.VISIBLE);
            showAnimation();
        }
    }

    protected void hideTitle() {
        if (textView.getVisibility() == View.VISIBLE) {
            hideAnimation();
        }
    }

    private void showAnimation() {
        ValueAnimator fadeAnim = ObjectAnimator.ofFloat(textView, "alpha", 0f, 1f);
        fadeAnim.setDuration(650);
        fadeAnim.setInterpolator(new AccelerateInterpolator());

        float viewCenterY = textView.getY() + 25f;

        if (getHintableView() != null) {
            viewCenterY = getHintableView().getY() + getHintableView().getMeasuredHeight() / 2;
        }

        ValueAnimator stretchAnim = ObjectAnimator.ofFloat(textView, "y", viewCenterY, textView.getY());
        stretchAnim.setDuration(500);
        stretchAnim.setInterpolator(new DecelerateInterpolator());

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(fadeAnim).with(stretchAnim);

        animatorSet.start();

        focusAnimation();
    }

    private void hideAnimation() {
        ValueAnimator fadeAnim = ObjectAnimator.ofFloat(textView, "alpha", 1f, 0f);
        fadeAnim.setDuration(300);
        fadeAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                textView.setVisibility(View.INVISIBLE);
            }
        });
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(fadeAnim);
        animatorSet.start();
    }

    public abstract View getHintableView();

    public void focusAnimation() {
        focused = Boolean.TRUE;
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), textView.getCurrentTextColor(), focusedColor);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                textView.setTextColor((Integer) animator.getAnimatedValue());
            }
        });
        colorAnimation.setDuration(650);
        colorAnimation.start();
    }

    public void unfocusAnimation() {
        focused = Boolean.FALSE;
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), textView.getCurrentTextColor(), unFocusedColor);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                textView.setTextColor((Integer) animator.getAnimatedValue());
            }
        });
        colorAnimation.setDuration(650);
        colorAnimation.start();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable state = super.onSaveInstanceState();

        Object data;
        if (textView.getVisibility() == View.VISIBLE) {
            data = saveState();
        } else {
            data = null;
        }
        return new SavedState(state, textView.getVisibility(), focused, data);
    }

    @Override
    protected void onRestoreInstanceState(final Parcelable state) {
        if (state instanceof SavedState) {
            final SavedState savedState = (SavedState) state;
            int visibility = savedState.getVisibility();
            textView.setVisibility(visibility);

            if (savedState.isFocused() == null || savedState.isFocused()) {
                focusAnimation();
            } else {
                unfocusAnimation();
            }

            if (visibility == View.VISIBLE && savedState.getData() != null) {
                restoreState(savedState.getData());
            }

            super.onRestoreInstanceState(savedState.getSuperState());
        }
    }

    protected abstract Object saveState();

    protected abstract void restoreState(Object data);

    /**
     * Convenience class to save / restore the lock combination picker state. Looks clumsy
     * but once created is easy to maintain and use.
     */
    protected static class SavedState extends BaseSavedState {

        private int visibility;

        private Boolean focused = null;
        private Object data;

        private SavedState(Parcelable superState, int visibility, Boolean focused, Object data) {
            super(superState);
            this.visibility = visibility;
            this.focused = focused;
            this.data = data;
        }

        private SavedState(Parcel in) {
            super(in);
            visibility = in.readInt();
            int focusedInt = in.readInt();

            if (focusedInt == 0) {
                focused = true;
            } else if (focusedInt == 1) {
                focused = false;
            } else {
                focused = null;
            }

            CustomSerializable customSerializable = (CustomSerializable) in.readSerializable();
            if (data != null) {
                data = customSerializable.getData();
            }
        }

        public int getVisibility() {
            return visibility;
        }

        public Boolean isFocused() {
            return focused;
        }

        public Object getData() {
            return data;
        }

        @Override
        public void writeToParcel(Parcel destination, int flags) {
            super.writeToParcel(destination, flags);
            destination.writeInt(visibility);
            int focusedInt;
            if (focused != null) {
                if (focused) {
                    focusedInt = 1;
                } else {
                    focusedInt = 0;
                }
            } else {
                focusedInt = -1;
            }
            destination.writeInt(focusedInt);

            if (data != null) {
                try {
                    destination.writeSerializable(new CustomSerializable(data));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        public static final Parcelable.Creator<SavedState> CREATOR = new Creator<SavedState>() {

            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };

        public class CustomSerializable implements Serializable {
            private Object data;

            public CustomSerializable(Object data) {
                this.data = data;
            }

            public Object getData() {
                return data;
            }

            public void setData(Object data) {
                this.data = data;
            }
        }
    }
}
