package com.mobithink.carbon.consultation.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mobithink.carbon.R;
import com.mobithink.carbon.database.model.StationDataDTO;

import java.util.ArrayList;


public class EventTab3 extends GenericTabFragment {

    ListView stationListView;
    ArrayList<String> stationNameList;

    public EventTab3() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_event_tab3, container, false);

        stationListView = (ListView) rootView.findViewById(R.id.station_list_view);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.itemview_station_listview, R.id.stationNameTextView, stationNameList);
        stationListView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getTripDTO();
        for(StationDataDTO stationDataDTO : getTripDTO().getStationDataDTOList()){
            stationNameList.add(stationDataDTO.getStationName());
        }
    }




}
