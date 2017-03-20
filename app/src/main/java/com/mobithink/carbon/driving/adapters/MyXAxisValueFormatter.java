package com.mobithink.carbon.driving.adapters;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

/**
 * Created by mplaton on 20/03/2017.
 */

public class MyXAxisValueFormatter implements IAxisValueFormatter {

    private String[] mValues;

    public MyXAxisValueFormatter(String[] values) {
        this.mValues = values;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        // "value" represents the position of the label on the axis (x or y)
        return mValues[(int) value];
    }

    /** this is only needed if numbers are returned, else return 0 */

    public int getDecimalDigits() { return 0; }
}
