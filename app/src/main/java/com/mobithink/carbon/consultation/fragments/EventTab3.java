package com.mobithink.carbon.consultation.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
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
        mTotalTrip.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.lightBlue));
        mTotalTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTotalTrip.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.lightBlue));
                mGeneralListView.setSelector(R.color.white);
                showTotalTripInformations();
            }
        });

        mGeneralListView = (ListView) rootView.findViewById(R.id.generalListView);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, generalities);
        mGeneralListView.setAdapter(adapter);
        mGeneralListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mTotalTrip.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
                mGeneralListView.setSelector(R.color.lightBlue);
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

    }

    public void showTotalTripInformations(){

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        GeneralTripFragment generalTripFragment = new GeneralTripFragment();
        fragmentTransaction.replace(R.id.your_placeholder, generalTripFragment);
        fragmentTransaction.addToBackStack(null).commit();

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

}
