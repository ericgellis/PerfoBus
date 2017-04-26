package com.mobithink.carbon.driving;

import android.Manifest;
import android.app.DialogFragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

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

public class DrivingEventDialogFragment extends DialogFragment implements OnMapReadyCallback {
    EventDTO eventDTO;
    private ListView mStationEventListView;
    private IEventSelectedListener mListener;

    Location location = null;
    double longitude;
    double latitude;

    Context mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.station_event_dialog_fragment, container, false);

        mContext = getContext();

        LocationManager lm = (LocationManager)  mContext.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission( mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission( mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        }
        final android.location.LocationListener locationListener = new android.location.LocationListener() {
            public void onLocationChanged(Location location) {
                longitude = location.getLongitude();
                latitude = location.getLatitude();
            }

            public void onStatusChanged(String s, int i, Bundle bundle) {
            }

            public void onProviderEnabled(String s) {
            }

            public void onProviderDisabled(String s) {
            }
        };

        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, (long) 2000, (float) 10, locationListener);

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
                eventDTO.setEventType("Evènement en section courante");
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
        List<String> eventType = new ArrayList<>();
        eventType.add("Giration difficile");
        eventType.add("Voie étroite");
        eventType.add("Chicane, écluse");
        eventType.add("Dos d'âne, trapézoïdal");
        eventType.add("Pavé trop rugueux");
        eventType.add("Stationnement latéral");
        eventType.add("Stationnement illicite");
        eventType.add("Stationnement alterné (effet chicane)");
        eventType.add("Passage à niveau");
        eventType.add("Itinéraire en tiroir ou boucle");
        eventType.add("Itinéraire sinueux");
        eventType.add("Trafic");
        eventType.add("Panne");

        return eventType;
    }

    public void setListener(IEventSelectedListener drivingActivity) {
        mListener = drivingActivity;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
    }

}
