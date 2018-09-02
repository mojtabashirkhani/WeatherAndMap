package com.example.slimshady.googlemap.Chart;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

/**
 * Created by slim shady on 05/31/2018.
 */

public class MyXAxisValueFormatter implements IAxisValueFormatter {



    private String[] mValues;
    private String values;
    public MyXAxisValueFormatter(String[] values) {
        this.mValues = values;
    }

    public MyXAxisValueFormatter(String s) {
        this.values = s;

    }


    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return mValues[(int) value];
    }
}
