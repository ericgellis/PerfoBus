package com.mobithink.carbon.station;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.mobithink.carbon.R;
import com.mobithink.carbon.database.model.EventDTO;
import com.mobithink.carbon.managers.CarbonApplicationManager;
import com.mobithink.carbon.managers.DatabaseManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by mplaton on 15/02/2017.
 */

public class EventCustomListViewAdapter extends BaseAdapter implements LocationListener, OnMapReadyCallback {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private final LayoutInflater mInflater;
    List<EventDTO> eventDTOList = new ArrayList<>();
    EventDTO eventDTO;

    double longitude;
    double latitude;

    Uri imageUri;
    Context mContext;


    public EventCustomListViewAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (eventDTOList == null) {
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

        mContext = parent.getContext();

        if(convertView == null){
            convertView = mInflater.inflate(R.layout.station_event_layout,parent, false);
        }

        EventViewHolder viewHolder = (EventViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new EventCustomListViewAdapter.EventViewHolder();
            viewHolder.stationEventRelativeLayout = (RelativeLayout) convertView.findViewById(R.id.station_event_layout);
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

        final RelativeLayout layoutCopi = viewHolder.stationEventRelativeLayout;

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
                layoutCopi.setVisibility(View.GONE);

            }
        });

        return convertView;
    }


    public void addData(EventDTO event) {
        eventDTOList.add(event);
    }

    public void takePhoto() {
        /*Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }*/

        /*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"fname_" +
                String.valueOf(System.currentTimeMillis()) + ".jpg"));
        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageUri);
        ((Activity) mContext).startActivityForResult(intent,REQUEST_IMAGE_CAPTURE);*/
    }

    public void stopAndRegisterEvent(EventDTO event) {

        event.setGpsEndLat((long) latitude);
        event.setGpsEndLong((long) longitude);
        event.setEndTime(System.currentTimeMillis());
        DatabaseManager.getInstance().updateEvent(CarbonApplicationManager.getInstance().getCurrentTripId(), CarbonApplicationManager.getInstance().getCurrentStationDataName(), event);

        Log.i(TAG, "stopAndRegisterEvent: Event has been registered");

    }

    private class EventViewHolder {
        public RelativeLayout stationEventRelativeLayout;
        public TextView stationEventName;
        public Chronometer stationEventChronometer;
        public Button microButton;
        public Button photoButton;
        public Button stopButton;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
    }
    @Override
    public void onLocationChanged(Location location) {
        longitude = location.getLongitude();
        latitude = location.getLatitude();

    }
}
