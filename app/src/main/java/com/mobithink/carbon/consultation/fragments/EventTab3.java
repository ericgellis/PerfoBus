package com.mobithink.carbon.consultation.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobithink.carbon.R;
import com.mobithink.carbon.consultation.adapter.EventRecyclerViewAdapter;
import com.mobithink.carbon.consultation.adapter.EventStationExpandableListViewAdapter;
import com.mobithink.carbon.consultation.adapter.ExpandableEventStationListHelper;
import com.mobithink.carbon.database.model.EventDTO;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class EventTab3 extends GenericTabFragment {

    RelativeLayout mainRelativeLayout;
    RelativeLayout detailedRelativeLayout;

    ListView eventMainListView;
    ArrayList<String> eventNameMainList;

    ListView eventInDrivingListView;
    ArrayList<String> eventInDrivingList;

    ExpandableListView eventStationListView;
    EventStationExpandableListViewAdapter eventStationExpandableListViewAdapter;
    List<String> expandableListTitle;

    HashMap<String, List<String>> expandableListDetailData;

    TextView totalTrip;
    TextView eventTotalDurationTextView;
    TextView lossTimePourcentTextView;
    TextView eventInStationTotalDurationTextView;
    TextView stationLossTimePourcenttextView;

    TextView eventName;
    TextView eventTimeSaving;
    TextView eventExplanations;
    ImageView eventAudioView;
    RecyclerView eventRecyclerView;
    EventRecyclerViewAdapter eventRecyclerViewAdapter;


    private String selectedEventName;


    public EventTab3() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView");

        View rootView = inflater.inflate(R.layout.fragment_event_tab3, container, false);

        mainRelativeLayout = (RelativeLayout) rootView.findViewById(R.id.mainRelativeLayout);
        detailedRelativeLayout = (RelativeLayout) rootView.findViewById(R.id.detailedRelativeLayout);

        eventTotalDurationTextView = (TextView) rootView.findViewById(R.id.eventTotalDuration);
        lossTimePourcentTextView  = (TextView) rootView.findViewById(R.id.lossTimePourcent);
        eventInStationTotalDurationTextView  = (TextView) rootView.findViewById(R.id.eventInStationTotalDuration);
        stationLossTimePourcenttextView  = (TextView) rootView.findViewById(R.id.stationLossTimePourcent);

        eventName = (TextView) rootView.findViewById(R.id.eventName);
        eventTimeSaving = (TextView) rootView.findViewById(R.id.eventTimeSaving);
        eventExplanations = (TextView) rootView.findViewById(R.id.eventExplanations);
        eventAudioView = (ImageView) rootView.findViewById(R.id.eventAudioView);
        eventRecyclerView = (RecyclerView) rootView.findViewById(R.id.eventRecyclerView);

        totalTrip = (TextView) rootView.findViewById(R.id.totalTrip);
        totalTrip.setBackgroundResource(R.color.lightBlue);
        totalTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGeneralInformations();
            }
        });

        eventMainListView = (ListView) rootView.findViewById(R.id.event_list_view);
        eventNameMainList = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.itemview_station_listview, R.id.stationNameTextView, eventNameMainList);
        eventMainListView.setAdapter(adapter);
        eventMainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedEventName = eventNameMainList.get(position);
                showDetailedInformations();
            }
        });

        eventInDrivingListView = (ListView) rootView.findViewById(R.id.eventInDrivingListView);
        eventInDrivingList = new ArrayList<>();
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getContext(), R.layout.itemview_event_driving_listview, R.id.eventName, eventInDrivingList);
        eventInDrivingListView.setAdapter(adapter2);

        eventStationListView = (ExpandableListView) rootView.findViewById(R.id.eventStationListView);
        expandableListDetailData = ExpandableEventStationListHelper.getData(getTripDTO());
        expandableListTitle = new ArrayList<String>(expandableListDetailData.keySet());
        eventStationExpandableListViewAdapter = new EventStationExpandableListViewAdapter(getActivity(), expandableListTitle, expandableListDetailData);
        eventStationListView.setAdapter(eventStationExpandableListViewAdapter);
        eventStationListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
            }
        });
        eventStationListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getTripDTO();
        String totalTimeString;
        long eventTotalDuration = 0;
        long eventInStationTotalDuration = 0;
        SimpleDateFormat timeFormat = new SimpleDateFormat("mm:ss", Locale.FRANCE);

        if (getTripDTO().getEventDTOList() != null) {
            for(EventDTO eventDTO : getTripDTO().getEventDTOList()){
                if (eventDTO.getStationName() != null){
                    long eventDuration = eventDTO.getEndTime()- eventDTO.getStartTime();
                    String timeString = timeFormat.format(eventDuration);
                    eventNameMainList.add(eventDTO.getEventName() + " - " + eventDTO.getStationName() + " - " + timeString);
                    //String timeSavingString = timeFormat.format(eventDTO.getTimeSaving());
                    //eventTimeSaving.setText(timeSavingString);
                } else {
                    long eventDuration = eventDTO.getEndTime()- eventDTO.getStartTime();
                    String timeString = timeFormat.format(eventDuration);
                    eventNameMainList.add(eventDTO.getEventName() + " - " + timeString);
                    //String timeSavingString = timeFormat.format(eventDTO.getTimeSaving());
                    //eventTimeSaving.setText(timeSavingString);
                }

                if (eventDTO.getStationName() == null) {
                    long eventDuration = eventDTO.getEndTime()- eventDTO.getStartTime();
                    String timeString = timeFormat.format(eventDuration);
                    eventInDrivingList.add(eventDTO.getEventName() + " - " + timeString) ;
                    eventTotalDuration+=eventDuration;

                }
                totalTimeString = timeFormat.format(eventTotalDuration);
                eventTotalDurationTextView.setText(" - " +totalTimeString + " - ");

                if (eventDTO.getStationName() != null) {
                    long eventDuration = eventDTO.getEndTime()- eventDTO.getStartTime();
                    eventInStationTotalDuration+=eventDuration;
                }
                totalTimeString = timeFormat.format(eventInStationTotalDuration);
                eventInStationTotalDurationTextView.setText(" - " +totalTimeString + " - ");

            }
        }
    }
    
    public void showGeneralInformations(){
        detailedRelativeLayout.setVisibility(View.GONE);
        mainRelativeLayout.setVisibility(View.VISIBLE);
        totalTrip.setBackgroundResource(R.color.lightBlue);
        totalTrip.setTextColor(getResources().getColor(R.color.white));
        eventMainListView.setBackgroundResource(R.color.white);
    }

    public void showDetailedInformations(){
        mainRelativeLayout.setVisibility(View.GONE);
        totalTrip.setBackgroundResource(R.color.white);
        totalTrip.setTextColor(getResources().getColor(R.color.black));
        detailedRelativeLayout.setVisibility(View.VISIBLE);

        eventName.setText(selectedEventName);

        /*eventRecyclerViewAdapter = new EventRecyclerViewAdapter(getContext(),);
        eventRecyclerView.setAdapter(eventRecyclerViewAdapter);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        eventRecyclerView.setLayoutManager(horizontalLayoutManager);*/

    }


}
