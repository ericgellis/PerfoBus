package com.mobithink.carbon.station;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.mobithink.carbon.R;
import com.mobithink.carbon.station.model.EventType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mplaton on 09/02/2017.
 */

public class EventDialogFragment extends DialogFragment {

    ListView eventListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //super.onCreate(savedInstanceState);
        //setContentView(R.layout.event_dialog_fragment);

        View rootView = inflater.inflate(R.layout.event_dialog_box, container, false);
        getDialog().setTitle("Choisir un évènement");

        eventListView = (ListView) rootView.findViewById(R.id.event_list_view);

        List<EventType> tweets = showEvent();

        EventListViewAdapter adapter = new EventListViewAdapter(getActivity(), tweets);
        eventListView.setAdapter(adapter);

        return rootView;
    }

    public List <EventType> showEvent(){
        List<EventType> eventType = new ArrayList<EventType>();
        eventType.add(new EventType("Stationnement gênant", R.mipmap.ic_grey_parking_obstruction));
        eventType.add(new EventType("Altération de la voirie", R.mipmap.ic_grey_alteration_road));
        eventType.add(new EventType("Carrefour à feux", R.mipmap.ic_grey_crossroad_with_traffic_lights));
        eventType.add(new EventType("Carrefour", R.mipmap.ic_grey_crossroad));
        eventType.add(new EventType("Giratoire", R.mipmap.ic_grey_roundabout));
        eventType.add(new EventType("Trafic", R.mipmap.ic_grey_traffic));
        eventType.add(new EventType("Autre", R.mipmap.ic_grey_other));

        return eventType;

    }
}
