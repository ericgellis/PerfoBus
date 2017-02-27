package com.mobithink.carbon.station;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import com.mobithink.carbon.R;
import com.mobithink.carbon.database.model.EventDTO;
import com.mobithink.carbon.managers.CarbonApplicationManager;
import com.mobithink.carbon.managers.DatabaseManager;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by mplaton on 15/02/2017.
 */

public class StationEventCustomListViewAdapter extends BaseAdapter {

    private final LayoutInflater mInflater;
    List<EventDTO> eventDTOList = new ArrayList<>();

    static final int REQUEST_IMAGE_CAPTURE = 1;

    EventDTO eventDTO;


    public StationEventCustomListViewAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if(eventDTOList == null){
            return 0;
        }else{
            return eventDTOList.size();
        }

    }

    @Override
    public String getItem(int position) {
        return eventDTOList.get(position).getEventName();
    }

    @Override
    public long getItemId(int position) {
        return  position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = mInflater.inflate(R.layout.station_event_layout,parent, false);
        }

        EventViewHolder viewHolder = (EventViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new StationEventCustomListViewAdapter.EventViewHolder();
            viewHolder.stationEventName = (TextView) convertView.findViewById(R.id.station_event_name);
            viewHolder.stationEventChronometer = (Chronometer) convertView.findViewById(R.id.station_event_chronometer);
            viewHolder.stationEventChronometer.setBase(SystemClock.elapsedRealtime());
            viewHolder.microButton = (Button) convertView.findViewById(R.id.micro_button);
            viewHolder.photoButton = (Button) convertView.findViewById(R.id.photo_button);
            viewHolder.stopButton = (Button) convertView.findViewById(R.id.station_event_stop_button);

            convertView.setTag(viewHolder);
        }

        final EventDTO event = eventDTOList.get(position);

        viewHolder.stationEventName.setText(getItem(position));

        viewHolder.stationEventChronometer.start();
        final Chronometer copi = viewHolder.stationEventChronometer;

        viewHolder.photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });

        viewHolder.stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopAndRegisterEvent(event);
                copi.stop();
            }
        });

        return convertView;
    }


    public void addData(EventDTO event) {
        eventDTOList.add(event);
    }

    private class EventViewHolder {
        public TextView stationEventName;
        public Chronometer stationEventChronometer;
        public Button microButton;
        public Button photoButton;
        public Button stopButton;
    }

    public void takePhoto(){
        /*Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }*/
    }


    public void stopAndRegisterEvent(EventDTO event){

        event.setEndTime(System.currentTimeMillis());
        DatabaseManager.getInstance().updateEvent(CarbonApplicationManager.getInstance().getCurrentTripId(), CarbonApplicationManager.getInstance().getCurrentStationDataId(), event);

        Log.i(TAG, "stopAndRegisterEvent: Event has been registered");

    }
}
