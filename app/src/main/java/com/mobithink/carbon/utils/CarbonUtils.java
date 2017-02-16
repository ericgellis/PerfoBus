package com.mobithink.carbon.utils;

import android.util.DisplayMetrics;
import android.util.TypedValue;

import com.mobithink.carbon.CarbonApplication;

/**
 * Created by jpaput on 09/02/2017.
 */

public class CarbonUtils {

    public static int dpToPx(float valueInDp) {
        DisplayMetrics metrics = CarbonApplication.getInstance().getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
    }

    private int pxToDp(int px){
        return Math.round(px/(CarbonApplication.getInstance().getResources().getDisplayMetrics().xdpi/DisplayMetrics.DENSITY_DEFAULT));
    }
}
