package com.mobithink.carbon.consultation.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mobithink.carbon.R;
import com.mobithink.carbon.database.model.EventDTO;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by mplaton on 05/04/2017.
 */

public class EventMainListViewAdapter extends ArrayAdapter<EventDTO>{

    public EventMainListViewAdapter(Context context, List<EventDTO> eventDTO) {
        super(context, 0, eventDTO);
    }

    private class EventViewHolder{
        public TextView eventName;
        public TextView eventStationName;
        public TextView eventTime;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.itemview_event_main_listview, parent, false);
        }

        EventViewHolder viewHolder = (EventViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new EventViewHolder();
            viewHolder.eventName = (TextView) convertView.findViewById(R.id.eventName);
            viewHolder.eventStationName = (TextView) convertView.findViewById(R.id.eventStationName);
            viewHolder.eventTime = (TextView) convertView.findViewById(R.id.eventTime);
            convertView.setTag(viewHolder);
        }

        EventDTO eventDTO = getItem(position);

        viewHolder.eventName.setText(eventDTO.getEventName());

        if(eventDTO.getStationName() != null){
            viewHolder.eventStationName.setText(eventDTO.getStationName());
         } else {
            viewHolder.eventStationName.setText("En roulage");
        }

        SimpleDateFormat timeFormat = new SimpleDateFormat("mm:ss", Locale.FRANCE);
        long eventDuration = eventDTO.getEndTime()- eventDTO.getStartTime();
        String timeString = timeFormat.format(eventDuration);
        viewHolder.eventTime.setText(timeString);


        return convertView;
    }


}
