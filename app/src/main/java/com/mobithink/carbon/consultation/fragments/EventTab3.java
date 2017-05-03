package com.mobithink.carbon.consultation.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mobithink.carbon.R;
import com.mobithink.carbon.consultation.childfragmentevent.GeneralEventFragment;
import com.mobithink.carbon.consultation.childfragmentevent.GeneralTripFragment;
import com.mobithink.carbon.database.model.EventDTO;

import java.util.ArrayList;


public class EventTab3 extends GenericTabFragment {


    private TextView mTotalTrip;
    private ListView mGeneralListView;
    String[] generalities = new String[]{"Evènement section courante", "Evènement carrefours", "Evènement stations" };

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

        mTotalTrip = (TextView) rootView.findViewById(R.id.totalTrip);
        mTotalTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTotalTripInformations();
            }
        });

        mGeneralListView = (ListView) rootView.findViewById(R.id.generalListView);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, generalities);
        mGeneralListView.setAdapter(adapter);
        mGeneralListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                eventGenerality(position);
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getTripDTO();

        showTotalTripInformations();

////        if (getTripDTO().getEventDTOList() != null) {
////            long eventTotalDuration = 0;
////            long eventInStationTotalDuration = 0;
////
////            for(EventDTO eventDTO : getTripDTO().getEventDTOList()){
////                eventNameMainList.add(eventDTO);
////
////                if (eventDTO.getStationName() == null) {
////                    long eventDuration = eventDTO.getEndTime()- eventDTO.getStartTime();
////                    String timeString = timeFormat.format(eventDuration);
////                    eventInDrivingList.add(eventDTO.getEventName() + " - " + timeString) ;
////                    eventTotalDuration+=eventDuration;
////
////                } else {
////                    long eventDuration = eventDTO.getEndTime()- eventDTO.getStartTime();
////                    eventInStationTotalDuration+=eventDuration;
////                }
////                eventTotalDurationTextView.setText(" - " +timeFormat.format(eventTotalDuration) + " - ");
////                eventInStationTotalDurationTextView.setText(" - " +timeFormat.format(eventInStationTotalDuration) + " - ");
////            }
////        }
//
//        eventMainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                showDetailedInformations(position);
//            }
//        });
//
//        adapter.notifyDataSetChanged();

    }

    public void showTotalTripInformations(){

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        GeneralTripFragment generalTripFragment = new GeneralTripFragment();
        fragmentTransaction.replace(R.id.your_placeholder, generalTripFragment);
        fragmentTransaction.commit();

    }

    public void eventGenerality(int position){

        ArrayList<EventDTO> eventInDrivingTab = new ArrayList<>();
        ArrayList<EventDTO> eventInCrossroadTab = new ArrayList<>();
        ArrayList<EventDTO> eventInStationTab = new ArrayList<>();

        Bundle bundle = new Bundle();

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        GeneralEventFragment generalEventFragment = new GeneralEventFragment();

       if (position ==0 ){
           if(getTripDTO().getEventDTOList() != null){
               for(EventDTO eventDTO : getTripDTO().getEventDTOList()){
                   if (eventDTO.getEventType().equals("Evènement en section courante")){

                       eventInDrivingTab.add(eventDTO);
                   }
               }
           }
           bundle.putSerializable("eventInDrivingList", eventInDrivingTab );
       }else if (position == 1){
           if(getTripDTO().getEventDTOList() != null){
               for(EventDTO eventDTO : getTripDTO().getEventDTOList()){
                   if (eventDTO.getEventType().equals("Evènement en carrefour")){

                       eventInCrossroadTab.add(eventDTO);
                   }
               }
           }
           bundle.putSerializable("eventInCrossroadList", eventInCrossroadTab);

       }else if (position == 2){

           if(getTripDTO().getEventDTOList() != null){
               for(EventDTO eventDTO : getTripDTO().getEventDTOList()){
                   if (eventDTO.getEventType().equals("Evènement en station")){

                       eventInStationTab.add(eventDTO);
                   }
               }
           }
           bundle.putSerializable("eventInStationList", eventInStationTab);
       }

        generalEventFragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.your_placeholder, generalEventFragment).addToBackStack(null).commit();
    }

    public void showDetailedInformations(int position){

//        if (eventNameMainList.get(position).getStationName() != null){
//            eventName.setText(eventNameMainList.get(position).getEventName()+ " - " + eventNameMainList.get(position).getStationName());
//        } else {
//            eventName.setText(eventNameMainList.get(position).getEventName()+ " - En roulage");
//        }
//        eventTimeSaving.setText("Gain de temps : " + eventNameMainList.get(position).getTimeSaving()+ " min");
//        PerformanceExplanations performanceExplanations = new PerformanceExplanations();
//        eventExplanations.setText(performanceExplanations.performanceExplanations(eventNameMainList.get(position)));
//
//
//        final String picturePath = eventNameMainList.get(position).getPicture() + ".jpg" ;
//        boolean existsImage = (new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath()+ "/mobithinkPhoto/"+picturePath)).exists();
//        if(eventNameMainList.get(position).getPicture() != null && existsImage){
//            Bitmap bitmap = BitmapFactory.decodeFile(android.os.Environment.getExternalStorageDirectory().getAbsolutePath()+"/mobithinkPhoto/" +picturePath);
//            eventImageView.setImageBitmap(bitmap);
//
//        }
//
//
//        final String audioPath = eventNameMainList.get(position).getVoiceMemo()+".3gp";
//        boolean existsAudio = (new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath()+"/mobithinkAudio/"+audioPath)).exists();
//        if (eventNameMainList.get(position).getVoiceMemo() != null && existsAudio){
//            eventAudioView.setVisibility(View.VISIBLE);
//            eventAudioView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    MediaPlayer mediaPlayer = new MediaPlayer();
//                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//                    try {
//                        mediaPlayer.setDataSource(android.os.Environment.getExternalStorageDirectory().getAbsolutePath()+"/mobithinkAudio/"+audioPath);
//                        mediaPlayer.prepare();
//                        mediaPlayer.start();
//                    } catch (IOException ioe){
//                        ioe.printStackTrace();
//                    }
//                }
//            });
//
//        }
    }

}
