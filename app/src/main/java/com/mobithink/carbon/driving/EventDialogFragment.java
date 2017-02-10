package com.mobithink.carbon.driving;

import android.app.DialogFragment;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.mobithink.carbon.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by mplaton on 09/02/2017.
 */

public class EventDialogFragment extends DialogFragment {

    /*ListView eventListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

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

    @Override
    public void onResume() {

        Window window = getDialog().getWindow();
        Point size = new Point();
        // Store dimensions of the screen in `size`
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);*/


        /*// Get existing layout params for the window
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        // Assign window properties to fill the parent
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

        super.onResume();
    }*/

    ExpandableListView expandableListView;
    EventExpendableListViewAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.event_dialog_box, container, false);

        expandableListView = (ExpandableListView) rootView.findViewById(R.id.event_expendable_list_view);
        expandableListDetail = ExpendableEventListData.getData();
        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());

        expandableListAdapter = new EventExpendableListViewAdapter(getActivity(), expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {

            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Toast.makeText(
                        getActivity(),
                        expandableListTitle.get(groupPosition)
                                + " -> "
                                + expandableListDetail.get(
                                expandableListTitle.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT
                ).show();
                return false;
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
}

