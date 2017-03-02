package com.mobithink.carbon.consultation.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobithink.carbon.R;


public class EventTab3 extends GenericTabFragment {


    public EventTab3() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_event_tab3, container, false);



        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getTripDTO();
    }




}
