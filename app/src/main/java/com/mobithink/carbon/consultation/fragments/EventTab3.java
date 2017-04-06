package com.mobithink.carbon.consultation.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.content.pm.ActivityInfoCompat;
import android.util.Log;
import android.view.Display;
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
import com.mobithink.carbon.consultation.adapter.EventMainListViewAdapter;
import com.mobithink.carbon.consultation.adapter.EventStationExpandableListViewAdapter;
import com.mobithink.carbon.consultation.adapter.ExpandableEventStationListHelper;
import com.mobithink.carbon.database.model.EventDTO;
import com.mobithink.carbon.utils.PerformanceExplanations;
import com.squareup.picasso.Picasso;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class EventTab3 extends GenericTabFragment {

    RelativeLayout mainRelativeLayout;
    RelativeLayout detailedRelativeLayout;

    ListView eventMainListView;
    ArrayList<EventDTO> eventNameMainList;
    EventMainListViewAdapter adapter;

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
    ImageView eventImageView;

    private String selectedEventName;
    public static final int MY_REQUEST_CODE = 0;

    public EventTab3() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView");

        View rootView = inflater.inflate(R.layout.fragment_event_tab3, container, false);

        if (ActivityCompat.checkSelfPermission(getContext(),Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_REQUEST_CODE);
        }

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
        eventImageView = (ImageView) rootView.findViewById(R.id.eventImageView);

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
        adapter = new EventMainListViewAdapter(getContext(), eventNameMainList);
        eventMainListView.setAdapter(adapter);
        eventMainListView.setSelector(R.color.lightBlue);
        eventMainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showDetailedInformations(position);
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
        SimpleDateFormat timeFormat = new SimpleDateFormat("mm:ss", Locale.FRANCE);

        for(EventDTO eventDTO : getTripDTO().getEventDTOList()){
                eventNameMainList.add(eventDTO);
        }

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

        adapter.notifyDataSetChanged();
        eventStationExpandableListViewAdapter.notifyDataSetChanged();
    }
    
    public void showGeneralInformations(){
        detailedRelativeLayout.setVisibility(View.GONE);
        mainRelativeLayout.setVisibility(View.VISIBLE);
        totalTrip.setBackgroundResource(R.color.lightBlue);
        totalTrip.setTextColor(getResources().getColor(R.color.white));

    }

    public void showDetailedInformations(int position){
        mainRelativeLayout.setVisibility(View.GONE);
        totalTrip.setBackgroundResource(R.color.white);
        totalTrip.setTextColor(getResources().getColor(R.color.black));
        detailedRelativeLayout.setVisibility(View.VISIBLE);

        if (eventNameMainList.get(position).getStationName() != null){
            eventName.setText(eventNameMainList.get(position).getEventName()+ " - " + eventNameMainList.get(position).getStationName());
        } else {
            eventName.setText(eventNameMainList.get(position).getEventName()+ " - En roulage");
        }
        eventTimeSaving.setText("Gain de temps : " + eventNameMainList.get(position).getTimeSaving());
        PerformanceExplanations performanceExplanations = new PerformanceExplanations();
        eventExplanations.setText(performanceExplanations.performanceExplanations(eventNameMainList.get(position)));

//                if (eventNameMainList.get(position).getPicture() != null){
//                    File root = Environment.getExternalStorageDirectory();
//                    Bitmap bMap = BitmapFactory.decodeFile(root+"/mobithinkPhoto/"+eventNameMainList.get(position).getPicture()+".jpg");
//                    eventImageView.setImageBitmap(bMap);
//                }
        final String picturePath = eventNameMainList.get(position).getPicture() + ".jpg" ;
        File imgFile = new  File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath()+"/mobithinkPhoto/" +picturePath);

        if(imgFile.exists()){

            //Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            //eventImageView.setImageBitmap(myBitmap);
            //eventImageView.setImageURI(Uri.fromFile(imgFile));
            eventImageView.setImageURI(Uri.parse((Environment.getExternalStorageDirectory().getAbsolutePath()+"/mobithinkPhoto/" +picturePath).toString()));

        }


        //Picasso.with(getContext()).load(new File (android.os.Environment.getExternalStorageDirectory().getAbsolutePath()+"/mobithinkPhoto/" +picturePath)).into(eventImageView);

        final String audioPath = eventNameMainList.get(position).getVoiceMemo()+".3gp";

        if (eventNameMainList.get(position).getVoiceMemo() != null){

            eventAudioView.setVisibility(View.VISIBLE);
            eventAudioView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MediaPlayer mediaPlayer = new MediaPlayer();
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    try {
                        mediaPlayer.setDataSource(android.os.Environment.getExternalStorageDirectory().getAbsolutePath()+"/mobithinkAudio/"+audioPath);
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                    } catch (IOException ioe){
                        ioe.printStackTrace();
                    }
                }
            });


        }
    }


}
