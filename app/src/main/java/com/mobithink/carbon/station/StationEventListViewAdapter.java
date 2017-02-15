package com.mobithink.carbon.station;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mobithink.carbon.R;
import com.mobithink.carbon.event.EventActivity;
import com.mobithink.carbon.station.model.EventType;

import java.util.List;

/**
 * Created by mplaton on 14/02/2017.
 */

public class StationEventListViewAdapter extends ArrayAdapter<EventType> {

    public StationEventListViewAdapter(Context context, List<EventType> eventType) {
        super(context, 0, eventType);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.itemview_station_event_listview,parent, false);
        }

        EventViewHolder viewHolder = (EventViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new EventViewHolder();
            viewHolder.stationEventName = (TextView) convertView.findViewById(R.id.station_event_name);

            convertView.setTag(viewHolder);
        }

        EventType eventType = getItem(position);

        viewHolder.stationEventName.setText(eventType.getName());
        viewHolder.stationEventName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent goToStationActivity = new Intent (g,StationActivity.class);
                startActivity(goToStationActivity);*/
            }
        });

        return convertView;
    }

    private class EventViewHolder {
        public TextView stationEventName;

    }
}

