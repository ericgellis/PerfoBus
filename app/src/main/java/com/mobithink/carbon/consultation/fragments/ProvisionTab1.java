package com.mobithink.carbon.consultation.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.mobithink.carbon.R;
import com.mobithink.carbon.database.model.EventDTO;
import com.mobithink.carbon.database.model.RollingPointDTO;
import com.mobithink.carbon.database.model.StationDataDTO;
import com.mobithink.carbon.utils.Mathematics;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProvisionTab1 extends GenericTabFragment implements OnMapReadyCallback {

    MapView mtripMapView;
    TextView minDistanceBetweenStations;
    TextView averageDistanceBetweenStationsTextView;
    TextView maxDistanceBetweenStations;
    TextView savingInMinutesTextView;
    TextView savingInEuroTextView;

    long interStationObjective = 600;
    long timeInStation;
    long totalTimeInStation = 0;
    long averageTimeInStation;
    long tripTotalTime;

    long tripBetweenStationsDistance = 0;
    long tripDistance = 0;

    long timeSavingResult;
    long timeSavingResultPourcent;

    public ProvisionTab1() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView");

        View rootView = inflater.inflate(R.layout.fragment_provision_tab1, container, false);

        mtripMapView = (MapView) rootView.findViewById(R.id.tripMapView);
        mtripMapView.onCreate(savedInstanceState);
        mtripMapView.onResume();
        mtripMapView.getMapAsync(this);

        minDistanceBetweenStations = (TextView) rootView.findViewById(R.id.minDistance);
        averageDistanceBetweenStationsTextView = (TextView) rootView.findViewById(R.id.averageDistance);
        maxDistanceBetweenStations = (TextView) rootView.findViewById(R.id.maxDistance);
        savingInMinutesTextView = (TextView) rootView.findViewById(R.id.savingInMinutesTextView);
        savingInEuroTextView = (TextView) rootView.findViewById(R.id.savingInEuroTextView);

        timeSavingInStation();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getTripDTO();
        SimpleDateFormat timeFormat = new SimpleDateFormat("mm:ss", Locale.FRANCE);
        savingInMinutesTextView.setText(timeFormat.format(timeSavingResult)+ "min");
        savingInEuroTextView.setText("soit euro");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        List<LatLng> rollingPointlatLngList = new ArrayList<>();
        List<LatLng> stationlatLngList = new ArrayList<>();
        List<LatLng> eventStationlatLngList = new ArrayList<>();
        List<LatLng> eventRollinglatLngList = new ArrayList<>();

        //true code
        for(RollingPointDTO rollingPointDTO : getTripDTO().getRollingPointDTOList()){
            rollingPointlatLngList.add(new LatLng(rollingPointDTO.getGpsLat(),rollingPointDTO.getGpsLong()));
        }

        for(StationDataDTO stationDTO : getTripDTO().getStationDataDTOList()){
            stationlatLngList.add(new LatLng(stationDTO.getGpsLat(),stationDTO.getGpsLong()));
        }

        for(EventDTO eventDTO : getTripDTO().getEventDTOList()){
            if (eventDTO.getStationName() != null && eventDTO.getStationName().length() > 0) {
                eventStationlatLngList.add(new LatLng(eventDTO.getGpsLat(),eventDTO.getGpsLong()));
            } else {
                eventRollinglatLngList.add(new LatLng(eventDTO.getGpsLat(),eventDTO.getGpsLong()));
            }
        }

        //fake code
//        rollingPointlatLngList.add(new LatLng(43.600000, 1.433333));
//        rollingPointlatLngList.add(new LatLng(43.605000, 1.443333));
//        rollingPointlatLngList.add(new LatLng(43.607232, 1.451205));
//        rollingPointlatLngList.add(new LatLng(43.609942, 1.455105));
//        rollingPointlatLngList.add(new LatLng(43.614354, 1.462143));
//
//        rollingPointlatLngList.add(new LatLng(43.616467, 1.465061));
//        rollingPointlatLngList.add(new LatLng(43.619139, 1.468494));
//        rollingPointlatLngList.add(new LatLng(43.620139, 1.469994));
//        rollingPointlatLngList.add(new LatLng(43.625414, 1.475361));
//        rollingPointlatLngList.add(new LatLng(43.631192, 1.478279));
//
//        rollingPointlatLngList.add(new LatLng(43.640572, 1.475275));
//        rollingPointlatLngList.add(new LatLng(43.646162, 1.470211));
//        rollingPointlatLngList.add(new LatLng(43.654422, 1.475961));
//        rollingPointlatLngList.add(new LatLng(43.6667, 1.4833));
//
//        stationlatLngList.add(new LatLng(43.600000, 1.433333));
//        stationlatLngList.add(new LatLng(43.607232, 1.451205));
//        stationlatLngList.add(new LatLng(43.609942, 1.455105));
//        stationlatLngList.add(new LatLng(43.614354, 1.462143));
//
//        stationlatLngList.add(new LatLng(43.616467, 1.465061));
//        stationlatLngList.add(new LatLng(43.619139, 1.468494));
//        stationlatLngList.add(new LatLng(43.625414, 1.475361));
//        stationlatLngList.add(new LatLng(43.631192, 1.478279));
//
//        stationlatLngList.add(new LatLng(43.640572, 1.475275));
//        stationlatLngList.add(new LatLng(43.646162, 1.470211));
//        stationlatLngList.add(new LatLng(43.654422, 1.475961));
//        stationlatLngList.add(new LatLng(43.6667, 1.4833));
//
//
//        eventStationlatLngList.add(new LatLng(43.607232, 1.451205));
//        eventStationlatLngList.add(new LatLng(43.614354, 1.462143));
//        eventStationlatLngList.add(new LatLng(43.646162, 1.470211));
//
//        eventRollinglatLngList.add(new LatLng(43.605000, 1.443333));
//        eventRollinglatLngList.add(new LatLng(43.620139, 1.469994));
        

        //draw polyline
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.addAll(rollingPointlatLngList);
        googleMap.addPolyline(polylineOptions.color(R.color.mobiThinkBlue).geodesic(true));

        //draw marker and circle and marker
        for (LatLng latLng : stationlatLngList) {
            googleMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_grey_point)).anchor(0.5f,0.5f));
            googleMap.addCircle(new CircleOptions().center(latLng).radius(150d));
        }

        LatLng firstStation = stationlatLngList.get(0);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstStation,13));

        //draw event
        for (LatLng latLng : eventStationlatLngList) {
            googleMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_blue_red_danger))
                    .anchor(0.5f,0.5f)
            );
        }

        for (LatLng latLng : eventRollinglatLngList) {
            googleMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_yellow_red_danger)).anchor(0.5f,0.5f));
        }



        /*ArrayList<Long> distanceTab = new ArrayList<>() ;
            for(int i=0; i+1< getTripDTO().getStationDataDTOList().size(); i++) {
                tripBetweenStationsDistance = Math.round(Mathematics.calculateGPSDistance(getTripDTO().getStationDataDTOList().get(i).getGpsLat(), getTripDTO().getStationDataDTOList().get(i).getGpsLong(), getTripDTO().getStationDataDTOList().get(i + 1).getGpsLat(), getTripDTO().getStationDataDTOList().get(i + 1).getGpsLong()));
                distanceTab.add(tripBetweenStationsDistance);
            }

        long minVal = distanceTab.indexOf(Collections.min(distanceTab));
        long maxVal = distanceTab.indexOf(Collections.max(distanceTab));

        minDistanceBetweenStations.setText(String.valueOf(minVal) + " m");
        maxDistanceBetweenStations.setText(String.valueOf(maxVal)+ " m");*/
    }

    public void timeSavingInStation(){

        for(StationDataDTO stationDataDTO : getTripDTO().getStationDataDTOList()){
            for(int i=0; i+1< getTripDTO().getStationDataDTOList().size(); i++) {
                timeInStation = stationDataDTO.getEndTime() - stationDataDTO.getStartTime();
                tripBetweenStationsDistance = Math.round(Mathematics.calculateGPSDistance(getTripDTO().getStationDataDTOList().get(i).getGpsLat(), getTripDTO().getStationDataDTOList().get(i).getGpsLong(), getTripDTO().getStationDataDTOList().get(i+1).getGpsLat(), getTripDTO().getStationDataDTOList().get(i+1).getGpsLong()));
            }
            totalTimeInStation += timeInStation;
            tripDistance+=tripBetweenStationsDistance;
        }

        /*averageDistanceBetweenStations = tripDistance/(getTripDTO().getStationDataDTOList().size()-1);
        averageDistanceBetweenStationsTextView.setText(String.valueOf(averageDistanceBetweenStations) + " m");*/

        averageTimeInStation = totalTimeInStation/getTripDTO().getStationDataDTOList().size();

        timeSavingResult = totalTimeInStation-(((tripDistance/interStationObjective)+ 1)*averageTimeInStation);

        tripTotalTime = getTripDTO().getEndTime()-getTripDTO().getStartTime();

        timeSavingResultPourcent = (timeSavingResult/tripTotalTime)*100;

    }
}
