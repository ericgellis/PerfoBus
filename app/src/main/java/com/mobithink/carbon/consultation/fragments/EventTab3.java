package com.mobithink.carbon.consultation.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobithink.carbon.R;
import com.mobithink.carbon.consultation.adapter.EventStationExpandableListViewAdapter;
import com.mobithink.carbon.consultation.adapter.ExpandableEventStationListHelper;
import com.mobithink.carbon.database.model.EventDTO;
import com.mobithink.carbon.database.model.StationDataDTO;
import com.mobithink.carbon.utils.Mathematics;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class EventTab3 extends GenericTabFragment {

    RelativeLayout mainRelativeLayout;
    RelativeLayout detailedRelativeLayout;

    ListView stationListView;
    ArrayList<String> stationNameList;

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

    TextView stationName;
    ListView eventListView;
    ArrayList<String> eventList;
    private String selectedStationName;

    long interStationObjective = 600;
    long timeInStation;
    long totalTimeInStation = 0;
    long averageTimeInStation;

    long tripBetweenStationsDistance = 0;
    long tripDistance = 0;

    long timeSavingResult;
    long timeSavingResultPourcent;

    public EventTab3() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_event_tab3, container, false);

        mainRelativeLayout = (RelativeLayout) rootView.findViewById(R.id.mainRelativeLayout);
        detailedRelativeLayout = (RelativeLayout) rootView.findViewById(R.id.detailedRelativeLayout);

        eventTotalDurationTextView = (TextView) rootView.findViewById(R.id.eventTotalDuration);
        lossTimePourcentTextView  = (TextView) rootView.findViewById(R.id.lossTimePourcent);
        eventInStationTotalDurationTextView  = (TextView) rootView.findViewById(R.id.eventInStationTotalDuration);
        stationLossTimePourcenttextView  = (TextView) rootView.findViewById(R.id.stationLossTimePourcent);

        stationName = (TextView) rootView.findViewById(R.id.stationName);
        eventListView = (ListView) rootView.findViewById(R.id.eventList);

        totalTrip = (TextView) rootView.findViewById(R.id.totalTrip);
        totalTrip.setBackgroundResource(R.color.lightBlue);
        totalTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGeneralInformations();
            }
        });

        stationListView = (ListView) rootView.findViewById(R.id.station_list_view);
        stationNameList = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.itemview_station_listview, R.id.stationNameTextView, stationNameList);
        stationListView.setAdapter(adapter);
        stationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedStationName = stationNameList.get(position);
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

        timeSavingInStation();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getTripDTO();
        for(StationDataDTO stationDataDTO : getTripDTO().getStationDataDTOList()){
            stationNameList.add(stationDataDTO.getStationName());
        }

        String totalTimeString;
        SimpleDateFormat timeFormat = new SimpleDateFormat("mm:ss", Locale.FRANCE);

        long eventTotalDuration = 0;
        for(EventDTO eventDTO : getTripDTO().getEventDTOList()){

            if (eventDTO != null && eventDTO.getStationName() == null) {
                long eventDuration = eventDTO.getEndTime()- eventDTO.getStartTime();
                String timeString = timeFormat.format(eventDuration);
                eventInDrivingList.add(eventDTO.getEventName() + " - " + timeString) ;
                eventTotalDuration+=eventDuration;

            }
            totalTimeString = timeFormat.format(eventTotalDuration);
            eventTotalDurationTextView.setText(" - " +totalTimeString + " - ");

        }

        long eventInStationTotalDuration = 0;
        for(EventDTO eventDTO : getTripDTO().getEventDTOList()){
            if (eventDTO != null && eventDTO.getStationName() != null) {
                long eventDuration = eventDTO.getEndTime()- eventDTO.getStartTime();
                eventInStationTotalDuration+=eventDuration;
            }
            totalTimeString = timeFormat.format(eventInStationTotalDuration);
            eventInStationTotalDurationTextView.setText(" - " +totalTimeString + " - ");
        }
    }
    
    public void showGeneralInformations(){
        detailedRelativeLayout.setVisibility(View.GONE);
        mainRelativeLayout.setVisibility(View.VISIBLE);
        totalTrip.setBackgroundResource(R.color.lightBlue);
        totalTrip.setTextColor(getResources().getColor(R.color.white));
        stationListView.setBackgroundResource(R.color.white);
    }

    public void showDetailedInformations(){
        mainRelativeLayout.setVisibility(View.GONE);
        totalTrip.setBackgroundResource(R.color.white);
        totalTrip.setTextColor(getResources().getColor(R.color.black));
        detailedRelativeLayout.setVisibility(View.VISIBLE);

        eventList = new ArrayList<>();
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(getContext(), R.layout.itemview_event_driving_listview, R.id.eventName, eventList);
        eventListView.setAdapter(adapter3);
        SimpleDateFormat timeFormat = new SimpleDateFormat("mm:ss", Locale.FRANCE);
        long eventStationTotalDuration = 0;

        for(EventDTO eventDTO : getTripDTO().getEventDTOList()){
            if (eventDTO.getStationName() != null && eventDTO.getStationName().equals(selectedStationName) ) {
                long eventDuration = eventDTO.getEndTime()- eventDTO.getStartTime();
                String timeString = timeFormat.format(eventDuration);
                eventList.add(eventDTO.getEventName() + " - " + timeString) ;
                eventStationTotalDuration +=eventDuration;
            }
        }

        stationName.setText(selectedStationName +" - " +timeFormat.format(eventStationTotalDuration));
    }

    public void timeSavingInStation(){

        for(StationDataDTO stationDataDTO : getTripDTO().getStationDataDTOList()){
            for(int i=0; i+1< getTripDTO().getStationDataDTOList().size(); i++) {
                timeInStation = stationDataDTO.getEndTime() - stationDataDTO.getStartTime();
                tripBetweenStationsDistance = Math.round(Mathematics.calculateGPSDistance(getTripDTO().getStationDataDTOList().get(i).getGpsLat(), getTripDTO().getStationDataDTOList().get(i).getGpsLong(), getTripDTO().getStationDataDTOList().get(i+1).getGpsLat(), getTripDTO().getStationDataDTOList().get(i+1).getGpsLong()));
            }
            totalTimeInStation += timeInStation;
            tripDistance+=tripBetweenStationsDistance;
        }

        averageTimeInStation = totalTimeInStation/getTripDTO().getStationDataDTOList().size();

        timeSavingResult = totalTimeInStation-(((tripDistance/interStationObjective)+ 1)*averageTimeInStation);

        timeSavingResultPourcent = (timeSavingResult/totalTimeInStation)*100;

        stationLossTimePourcenttextView.setText(+timeSavingResultPourcent + "% - (gain potentiel "+ timeSavingResult/60000+" min)");

    }

}
