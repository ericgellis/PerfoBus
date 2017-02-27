package com.mobithink.carbon.driving;

import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Point;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
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
import com.mobithink.carbon.database.model.EventDTO;
import com.mobithink.carbon.event.EventActivity;
import com.mobithink.carbon.managers.CarbonApplicationManager;
import com.mobithink.carbon.managers.DatabaseManager;
import com.mobithink.carbon.services.LocationService;
import com.mobithink.carbon.services.weatherdata.Location;
import com.mobithink.carbon.station.StationActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by mplaton on 09/02/2017.
 */

public class EventDialogFragment extends DialogFragment {

    ExpandableListView expandableListView;
    EventExpendableListViewAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;

    EventDTO eventDTO;
    LocationService locationService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.event_dialog_box, container, false);

        locationService = new LocationService();

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
                Toast.makeText(getActivity(),expandableListTitle.get(groupPosition)+ " -> "
                                + expandableListDetail.get(expandableListTitle.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT
                ).show();

                String eventName = (String) expandableListAdapter.getChild(groupPosition, childPosition);

                EventDTO eventDTO = new EventDTO();
                eventDTO.setEventName(eventName);
                eventDTO.setStartTime(System.currentTimeMillis());
                long eventId = DatabaseManager.getInstance().createNewEvent(CarbonApplicationManager.getInstance().getCurrentTripId(), CarbonApplicationManager.getInstance().getCurrentStationDataId(), eventDTO);

                Bundle bundle = new Bundle();
                bundle.putSerializable("eventId", eventId);
                Intent goToEventActivity = new Intent (getActivity(), EventActivity.class);
                goToEventActivity.putExtras(bundle);
                startActivity(goToEventActivity);
                dismiss();
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

