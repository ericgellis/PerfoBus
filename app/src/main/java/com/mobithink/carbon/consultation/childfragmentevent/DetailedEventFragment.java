package com.mobithink.carbon.consultation.childfragmentevent;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mobithink.carbon.R;
import com.mobithink.carbon.consultation.fragments.GenericTabFragment;
import com.mobithink.carbon.database.model.EventDTO;
import com.mobithink.carbon.utils.PerformanceExplanations;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by mplaton on 26/04/2017.
 */

public class DetailedEventFragment extends GenericTabFragment {

    public static final int MY_REQUEST_CODE = 0;

    EventDTO mEventDTO;

    private TextView mEventNameTextView;
    private TextView mEventTypeTextView;
    private TextView mTimeTextView;
    private TextView mAudioRetranscriptionTextView;
    private TextView mEventExplanations;

    private ImageView mEventPhoto;
    private MapView mEventMap;
    private GoogleMap mEventGoogleMap;

    SimpleDateFormat timeFormat = new SimpleDateFormat("mm:ss", Locale.FRANCE);

    private String STATIC_MAP_API_ENDPOINT = "http://maps.googleapis.com/maps/api/staticmap?size=230x200&path=";

    public DetailedEventFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.child_fragment_detailed_event, container, false);

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_REQUEST_CODE);
        }

        mEventNameTextView = (TextView) rootView.findViewById(R.id.eventNameTextView);
        mEventTypeTextView = (TextView) rootView.findViewById(R.id.eventTypeTextView);
        mTimeTextView = (TextView) rootView.findViewById(R.id.timeTextView);
        mAudioRetranscriptionTextView = (TextView) rootView.findViewById(R.id.audioRetranscriptionTextView);
        mEventExplanations = (TextView) rootView.findViewById(R.id.eventExplanations);

        mEventPhoto = (ImageView) rootView.findViewById(R.id.eventPhoto);
        mEventMap = (MapView) rootView.findViewById(R.id.eventMap);

//        mEventMap.getMapAsync(new OnMapReadyCallback() {
//            @Override
//            public void onMapReady(GoogleMap mMap) {
//
//                mEventGoogleMap = mMap;
//                mEventGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
//                mEventGoogleMap.setBuildingsEnabled(false);
//
//            }
//        });


        Bundle extras = getArguments();
        mEventDTO = (EventDTO) extras.getSerializable("eventDTO");

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getTripDTO();

        mEventNameTextView.setText(mEventDTO.getEventName());
        mEventTypeTextView.setText(mEventDTO.getEventType());
        double eventTime = mEventDTO.getEndTime()-mEventDTO.getStartTime();
        mTimeTextView.setText(timeFormat.format(eventTime));
        PerformanceExplanations performanceExplanations = new PerformanceExplanations();
        mEventExplanations.setText(performanceExplanations.performanceExplanations(mEventDTO));

        final String picturePath = mEventDTO.getPicture() + ".jpg" ;
        boolean existsImage = (new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath()+ "/mobithinkPhoto/"+picturePath)).exists();
        if(mEventDTO.getPicture() != null && existsImage){
            Bitmap bitmap = BitmapFactory.decodeFile(android.os.Environment.getExternalStorageDirectory().getAbsolutePath()+"/mobithinkPhoto/" +picturePath);
            mEventPhoto.setImageBitmap(bitmap);

        }

//        if (mEventDTO.getGpsLat() != null || mEventDTO.getGpsLong() != null) {
//            mEventGoogleMap.addMarker(new MarkerOptions().position(
//                    new LatLng(mEventDTO.getGpsLat(), mEventDTO.getGpsLong())).icon(
//                    BitmapDescriptorFactory.defaultMarker()));
//        }
//        try {
//            String marker_dest = "color:orange|label:1|San Francisco,USA";
//            marker_dest = URLEncoder.encode(marker_dest, "UTF-8");
//
//            STATIC_MAP_API_ENDPOINT = STATIC_MAP_API_ENDPOINT + "&markers=" + marker_dest;
//
//            Log.d("STATICMAPS", STATIC_MAP_API_ENDPOINT);
//            Picasso.with(getContext()).load(STATIC_MAP_API_ENDPOINT).resize(200, 200).centerCrop().into(mEventMap);
//
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }

    }


}
