package com.mobithink.carbon.station;

import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mobithink.carbon.R;
import com.mobithink.carbon.database.model.EventDTO;
import com.mobithink.carbon.managers.CarbonApplicationManager;
import com.mobithink.carbon.managers.DatabaseManager;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by mplaton on 14/02/2017.
 */

public class StationEventDialogFragment extends DialogFragment {

    private ListView mStationEventListView;

    private IEventSelectedListener mListener;

    EventDTO eventDTO;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.station_event_dialog_fragment, container, false);

        mStationEventListView = (ListView) rootView.findViewById(R.id.station_event_listview);
        final List<String> eventType = stationEventNameList();

        StationEventListViewAdapter adapter = new StationEventListViewAdapter(getActivity(), eventType);
        mStationEventListView.setAdapter(adapter);
        mStationEventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                eventDTO = new EventDTO();
                eventDTO.setEventName(eventType.get(position));
                eventDTO.setStartTime(System.currentTimeMillis());

                eventDTO.setId(DatabaseManager.getInstance().createNewEvent(CarbonApplicationManager.getInstance().getCurrentTripId(), CarbonApplicationManager.getInstance().getCurrentStationDataId(), eventDTO));
                mListener.onEventSelected(eventDTO);
                dismiss();

            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        Window window = getDialog().getWindow();
        Point size = new Point();
        // Store dimensions of the screen in `size`
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);

        super.onResume();
    }

    public List<String> stationEventNameList(){
        List<String> eventType = new ArrayList<String>();
        eventType.add(new String("Aménagement : implantation, accessibilité PMR"));
        eventType.add(new String("Aménagement : position en pied de feu ou intersection"));
        eventType.add(new String("Localisation : optimisation du nombre/regroupements"));
        eventType.add(new String("Attente pour correspondance"));
        eventType.add(new String("Trop de véhicules à l'arrêt"));
        eventType.add(new String("Incident technique bus"));
        return eventType;
    }

    public void setListener(IEventSelectedListener stationActivity) {
        mListener = stationActivity;
    }
}
