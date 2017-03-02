package com.mobithink.carbon.consultation.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobithink.carbon.R;


public class SpeedTab2 extends GenericTabFragment {

    public SpeedTab2() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View rootView = inflater.inflate(R.layout.fragment_speed_tab2, container, false);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getTripDTO();
    }

}
