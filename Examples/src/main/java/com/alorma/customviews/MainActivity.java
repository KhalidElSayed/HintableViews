package com.alorma.customviews;

import android.app.Activity;
import android.os.Bundle;

import com.alorma.hintableviews.HintTitleSpinner;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HintTitleSpinner titleSpinner = (HintTitleSpinner) findViewById(R.id.hintTitleSpinner);

        List<String> items = new ArrayList<String>();
        for (int i = 0; i < 10; i++) {
            items.add("Item: " + i);
        }

        titleSpinner.setItems(items);
    }
}
