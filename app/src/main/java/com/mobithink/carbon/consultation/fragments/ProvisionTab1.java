package com.mobithink.carbon.consultation.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.MapView;
import com.mobithink.carbon.R;


public class ProvisionTab1 extends GenericTabFragment {

    MapView mtripMapView;
    TextView minDistanceBetweenStations;
    TextView averageDistanceBetweenStations;
    TextView maxDistanceBetweenStations;

    public ProvisionTab1() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_provision_tab1, container, false);

        mtripMapView = (MapView) rootView.findViewById(R.id.tripMapView);

        minDistanceBetweenStations = (TextView) rootView.findViewById(R.id.minDistance);
        averageDistanceBetweenStations = (TextView) rootView.findViewById(R.id.averageDistance);
        maxDistanceBetweenStations = (TextView) rootView.findViewById(R.id.maxDistance);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getTripDTO();
    }
}
