package com.mobithink.carbon.driving.adapters;

import android.util.Log;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

/**
 * Created by mplaton on 20/03/2017.
 */

public class MyXAxisValueFormatter implements IAxisValueFormatter ,IValueFormatter {
    private static final String TAG = MyXAxisValueFormatter.class.getName();

    private String[] mValues;

    public MyXAxisValueFormatter(String[] values) {
        this.mValues = values;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        // "value" represents the position of the label on the axis (x or y)
        int intValue = Math.round(value);
        if (intValue < 0) {
            return mValues[0];
        } else if (intValue > (mValues.length -1)){
            return mValues[mValues.length - 1];
        } else {
            return mValues[intValue];
        }
    }

    /** this is only needed if numbers are returned, else return 0 */

    public int getDecimalDigits() { return 0; }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        // "value" represents the position of the label on the axis (x or y)
        //Log.d(TAG, "getFormattedValue: " + value + " and entry : " + entry.toString() + "and data index : " + dataSetIndex);
        int intXValue = Math.round(entry.getX());
        if (intXValue < 0) {
            Log.d(TAG, mValues[0]);
            return mValues[0];
        } else if (intXValue > (mValues.length -1)){
            Log.d(TAG, mValues[mValues.length - 1]);
            return mValues[mValues.length - 1];
        } else {
            Log.d(TAG, mValues[intXValue]);
            return mValues[intXValue];
        }
    }
}
