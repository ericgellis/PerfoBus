package com.mobithink.carbon.driving;

import android.app.DialogFragment;
import android.graphics.Point;
import android.location.Location;
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

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.mobithink.carbon.R;
import com.mobithink.carbon.database.model.EventDTO;
import com.mobithink.carbon.managers.CarbonApplicationManager;
import com.mobithink.carbon.managers.DatabaseManager;
import com.mobithink.carbon.station.EventListViewAdapter;
import com.mobithink.carbon.station.IEventSelectedListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mplaton on 15/03/2017.
 */

public class CrossRoadEventDialogFragment extends DialogFragment implements LocationListener, OnMapReadyCallback {

    EventDTO eventDTO;
    private ListView mStationEventListView;
    private IEventSelectedListener mListener;

    double longitude;
    double latitude;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.station_event_dialog_fragment, container, false);

        mStationEventListView = (ListView) rootView.findViewById(R.id.station_event_listview);
        final List<String> eventType = stationEventNameList();

        EventListViewAdapter adapter = new EventListViewAdapter(getActivity(), eventType);
        mStationEventListView.setAdapter(adapter);
        mStationEventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                eventDTO = new EventDTO();
                eventDTO.setEventName(eventType.get(position));
                eventDTO.setStartTime(System.currentTimeMillis());
                eventDTO.setGpsLat(latitude);
                eventDTO.setGpsLong(longitude);
                eventDTO.setId(DatabaseManager.getInstance().createNewEvent(CarbonApplicationManager.getInstance().getCurrentTripId(), null, eventDTO));
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
        eventType.add(new String("Giratoire : remontée des files d'attente"));
        eventType.add(new String("Giratoire : attente"));
        eventType.add(new String("Giratoire : giration trop importante"));
        eventType.add(new String("Carrefour à feux : remontée des files d'attente"));
        eventType.add(new String("Carrefour à feux : attente"));
        eventType.add(new String("Carrefour à feux : étroit car îlot refuge"));
        eventType.add(new String("Carrefour à feux : ligne de feu trop avancée"));

        return eventType;
    }

    public void setListener(IEventSelectedListener drivingActivity) {
        mListener = drivingActivity;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
    }
    @Override
    public void onLocationChanged(Location location) {
        longitude = location.getLongitude();
        latitude = location.getLatitude();

    }
}
