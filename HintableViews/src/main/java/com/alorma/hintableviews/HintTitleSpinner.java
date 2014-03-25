package com.alorma.hintableviews;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bernat on 25/03/2014.
 */
public class HintTitleSpinner extends HintTitleRelativeLayout implements AdapterView.OnItemSelectedListener {

    private Spinner spinner;
    private CustomSpinnerAdapter adapter;
    private AdapterView.OnItemSelectedListener itemSelectedListener;

    public HintTitleSpinner(Context context) {
        super(context);
    }

    public HintTitleSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HintTitleSpinner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    public void createHintedView(int topId, float density) {
        spinner = new HintableSpinner(getContext());
        spinner.setVisibility(View.VISIBLE);
        spinner.setOnItemSelectedListener(this);

        setItems();

        LayoutParams paramsSpinner = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        paramsSpinner.addRule(RelativeLayout.ABOVE, topId);
        paramsSpinner.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        paramsSpinner.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            paramsSpinner.addRule(RelativeLayout.ALIGN_PARENT_START);
            paramsSpinner.addRule(RelativeLayout.ALIGN_PARENT_END);
        }

        paramsSpinner.setMargins(0, (int) (16 * density), 0, 0);

        addView(spinner, paramsSpinner);
    }

    @Override
    public boolean isValid() {
        return adapter.isSelected();
    }

    public Spinner getSpinner() {
        return spinner;
    }

    private void setItems() {
        setItems(new ArrayList<String>());
    }

    public void setItems(List<String> items) {
        adapter = new CustomSpinnerAdapter(getContext(), this.hint);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        for (String item : items) {
            adapter.add(item);
        }

        spinner.setAdapter(adapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (adapter.isSelected()) {
            showTitle();
        }
        spinner.invalidate();
        adapter.notifyDataSetChanged();

        if (itemSelectedListener != null) {
            itemSelectedListener.onItemSelected(parent, view, position, id);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        if (itemSelectedListener != null) {
            itemSelectedListener.onNothingSelected(parent);
        }
    }

    public void setItemSelectedListener(AdapterView.OnItemSelectedListener itemSelectedListener) {
        this.itemSelectedListener = itemSelectedListener;
    }

    private class CustomSpinnerAdapter extends ArrayAdapter<String> {
        private String hint;
        private boolean selected = false;

        private TextView firstTextView;

        public CustomSpinnerAdapter(Context context, String hint) {
            super(context, android.R.layout.simple_spinner_dropdown_item);
            this.hint = hint;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = super.getView(position, convertView, parent);

            if (v != null) {
                if (position == 0) {
                    firstTextView = (TextView) v.findViewById(android.R.id.text1);
                    if (spinner.getSelectedItemPosition() == 0 && !selected) {
                        firstTextView.setText("");
                        firstTextView.setHint(hint);
                    } else {
                        firstTextView.setText(getItem(0));
                    }
                }
            }

            return v;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            selected = true;
            return super.getView(position, convertView, parent);
        }

        public boolean isSelected() {
            return this.selected;
        }
    }

    private class HintableSpinner extends Spinner {

        private OnItemSelectedListener itemSelectedListener;

        public HintableSpinner(Context context) {
            super(context);
            isInEditMode();
        }

        @Override
        public void setSelection(int position) {
            super.setSelection(position);
            if (itemSelectedListener != null) {
                itemSelectedListener.onItemSelected(this, null, position, getItemIdAtPosition(position));
            }
        }

        @Override
        public void setOnItemSelectedListener(OnItemSelectedListener listener) {
            this.itemSelectedListener = listener;
        }
    }
}
