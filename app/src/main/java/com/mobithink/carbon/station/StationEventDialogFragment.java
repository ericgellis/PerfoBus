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

    EventDTO eventDTO;
    private ListView mStationEventListView;
    private IEventSelectedListener mListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.station_event_dialog_fragment, container, false);

        Bundle bundle = getArguments();
        final double stationLongitude = (double) bundle.getSerializable("stationLongitude");
        final double stationLatitude = (double) bundle.getSerializable("stationLatitude");
        final String stationName = (String) bundle.getSerializable("stationName");

        mStationEventListView = (ListView) rootView.findViewById(R.id.station_event_listview);
        final List<String> eventType = stationEventNameList();

        EventListViewAdapter adapter = new EventListViewAdapter(getActivity(), eventType);
        mStationEventListView.setAdapter(adapter);
        mStationEventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                eventDTO = new EventDTO();
                eventDTO.setEventName(eventType.get(position));
                eventDTO.setStationName(stationName);
                eventDTO.setStartTime(System.currentTimeMillis());
                eventDTO.setGpsLat((long) stationLatitude);
                eventDTO.setGpsLong((long) stationLongitude);
                eventDTO.setId(DatabaseManager.getInstance().createNewEvent(CarbonApplicationManager.getInstance().getCurrentTripId(), CarbonApplicationManager.getInstance().getCurrentStationDataName(), eventDTO));
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
        eventType.add(new String("Trop d'arrêts"));
        eventType.add(new String("Stationnement illicite"));
        eventType.add(new String("Attente pour correspondance"));
        eventType.add(new String("Capacité station"));
        eventType.add(new String("Foule"));
        eventType.add(new String("Incivilité"));
        eventType.add(new String("Demande d'informations à bord"));
        eventType.add(new String("Vente à bord et échange de monnaie"));
        eventType.add(new String("Réinsertion dans la circulation"));
        eventType.add(new String("PMR"));
        eventType.add(new String("Incident technique"));
        return eventType;
    }

    public void setListener(IEventSelectedListener stationActivity) {
        mListener = stationActivity;
    }
}
