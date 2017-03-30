package com.mobithink.carbon.station;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import com.mobithink.carbon.R;
import com.mobithink.carbon.database.model.EventDTO;
import com.mobithink.carbon.managers.CarbonApplicationManager;
import com.mobithink.carbon.managers.DatabaseManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.R.attr.bitmap;
import static android.content.ContentValues.TAG;

/**
 * Created by mplaton on 15/02/2017.
 */

public class EventCustomListViewAdapter extends BaseAdapter implements LocationListener, OnMapReadyCallback {

    private static MediaRecorder mediaRecorder;
    boolean isRecording = true;

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private final LayoutInflater mInflater;
    List<EventDTO> eventDTOList = new ArrayList<>();

    double longitude;
    double latitude;

    Uri imageUri;
    Context mContext;

    String mCurrentPhotoPath;
    static final int REQUEST_TAKE_PHOTO = 1;

    private Uri mImageUri;
    File photo;
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
        viewHolder.microButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerVoice();
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

        /*boolean exists = (new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath()+ "/fname_")).exists();
        if (!exists) {
            new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath()+"/fname_").mkdirs();
        }*/

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ((Activity) mContext).startActivityForResult(intent,REQUEST_IMAGE_CAPTURE);
        /*imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/fname_/" +
                String.valueOf(System.currentTimeMillis()) + ".jpg"));
        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageUri);*/

        //Bitmap bitmap = (Bitmap)mContext.getExtras().get("data");
        /*try {
            FileOutputStream outStream = null;
            File sdCard = Environment.getExternalStorageDirectory();
            File dir = new File(sdCard.getAbsolutePath() + "/camtest");
            dir.mkdirs();
            String fileName = String.format("%d.jpg", System.currentTimeMillis());
            File outFile = new File(dir, fileName);
            outStream = new FileOutputStream(outFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            outStream.flush();
            outStream.close();}
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }*/

        /*File outputFile = new File(Environment.getExternalStorageDirectory(), "photo_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }





    public void registerVoice(){

        boolean exists = (new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath()+"/myaudio.3gp")).exists();
        if (!exists) {
            new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath()+"/myaudio.3gp").mkdirs();
        }

        if (hasMicrophone()){
            if(isRecording){
                try {
                    mediaRecorder = new MediaRecorder();
                    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                    mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                    mediaRecorder.setOutputFile(android.os.Environment.getExternalStorageDirectory().getAbsolutePath()+"/myaudio.3gp");
                    mediaRecorder.prepare();
                } catch (IllegalStateException e) {
                    Log.e("RECORDING :: ",e.getMessage());
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.e("RECORDING :: ",e.getMessage());
                    e.printStackTrace();
                }
                Toast.makeText(mContext, "Micro ouvert", Toast.LENGTH_SHORT).show();
                mediaRecorder.start();
                isRecording = false;
            } else {
                if (mediaRecorder != null){
                    mediaRecorder.stop();
                    Toast.makeText(mContext, "Micro fermé", Toast.LENGTH_SHORT).show();
                    mediaRecorder.release();
                    mediaRecorder = null;
                    isRecording = true;
                }
            }
        }
    }

    protected boolean hasMicrophone() {
        PackageManager pmanager = mContext.getPackageManager();
        return pmanager.hasSystemFeature(
                PackageManager.FEATURE_MICROPHONE);
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
        LocationManager lm = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        }
    }

}
