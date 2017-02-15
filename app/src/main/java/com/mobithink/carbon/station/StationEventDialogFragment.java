package com.mobithink.carbon.station;

import android.app.DialogFragment;
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
import android.widget.ListView;

import com.mobithink.carbon.R;
import com.mobithink.carbon.station.model.EventType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mplaton on 14/02/2017.
 */

public class StationEventDialogFragment extends DialogFragment {

    private ListView mStationEventListView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.station_event_dialog_fragment, container, false);

        mStationEventListView = (ListView) rootView.findViewById(R.id.station_event_listview);
        List<EventType> eventType = stationEventNameList();

        StationEventListViewAdapter adapter = new StationEventListViewAdapter(getActivity(), eventType);
        mStationEventListView.setAdapter(adapter);

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

    public List<EventType> stationEventNameList(){
        List<EventType> eventType = new ArrayList<EventType>();
        eventType.add(new EventType("Aménagement : implantation, accessibilité PMR"));
        eventType.add(new EventType("Aménagement : position en pied de feu ou intersection"));
        eventType.add(new EventType("Localisation : optimisation du nombre/regroupements"));
        eventType.add(new EventType("Attente pour correspondance"));
        eventType.add(new EventType("Trop de véhicules à l'arrêt"));
        eventType.add(new EventType("Incident technique bus"));
        return eventType;
    }
}
