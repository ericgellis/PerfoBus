package com.mobithink.carbon.driving;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobithink.carbon.R;
import com.mobithink.carbon.database.model.EventDTO;
import com.mobithink.carbon.managers.RetrofitManager;
import com.mobithink.carbon.station.model.EventType;
import com.mobithink.carbon.webservices.EventService;


import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mplaton on 01/02/2017.
 */

public class EventListViewAdapter extends ArrayAdapter<EventType> {


    public EventListViewAdapter(Context context, List<EventType> eventType) {
        super(context, 0, eventType);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.itemview_event_listview,parent, false);
        }

        EventViewHolder viewHolder = (EventViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new EventViewHolder();
            viewHolder.eventTypeName = (TextView) convertView.findViewById(R.id.event_name_textview);
            //viewHolder.eventIcon = (ImageView) convertView.findViewById(R.id.event_icon);
            viewHolder.itemviewStationLinearLayout = (LinearLayout) convertView.findViewById(R.id.itemview_station_linear_layout);
            convertView.setTag(viewHolder);
        }

        EventType eventType = getItem(position);

        viewHolder.eventTypeName.setText(eventType.getName());
        //viewHolder.eventIcon.setImageResource(eventType.getIcon());
        viewHolder.itemviewStationLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                registerEvent();

            };
        });

        return convertView;
    }

    private class EventViewHolder{
        public TextView eventTypeName;
        public ImageView eventIcon;
        public LinearLayout itemviewStationLinearLayout;
    }

    private void registerEvent(){


    }
}



