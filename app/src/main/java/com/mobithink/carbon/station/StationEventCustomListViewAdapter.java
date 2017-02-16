package com.mobithink.carbon.station;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import com.mobithink.carbon.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mplaton on 15/02/2017.
 */

public class StationEventCustomListViewAdapter extends BaseAdapter {

    private final LayoutInflater mInflater;
    List<String> eventTypeList = new ArrayList<>();

    public StationEventCustomListViewAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if(eventTypeList == null){
            return 0;
        }else{
            return eventTypeList.size();
        }

    }

    @Override
    public String getItem(int position) {
        return eventTypeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return  position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = mInflater.inflate(R.layout.station_event_layout,parent, false);
        }

       EventViewHolder viewHolder = (EventViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new StationEventCustomListViewAdapter.EventViewHolder();
            viewHolder.stationEventName = (TextView) convertView.findViewById(R.id.station_event_name);
            viewHolder.stationEventChronometer = (Chronometer) convertView.findViewById(R.id.station_event_chronometer);
            viewHolder.microButton = (Button) convertView.findViewById(R.id.micro_button);
            viewHolder.photoButton = (Button) convertView.findViewById(R.id.photo_button);
            viewHolder.stopButton = (Button) convertView.findViewById(R.id.station_event_stop_button);

            convertView.setTag(viewHolder);
        }

        String eventType = getItem(position);
        viewHolder.stationEventName.setText(eventType);

        return convertView;
    }

    public void addData(String eventType) {
        eventTypeList.add(eventType);
    }

    private class EventViewHolder {
        TextView stationEventName;
        Chronometer stationEventChronometer;
        Button microButton;
        Button photoButton;
        Button stopButton;

    }
}
