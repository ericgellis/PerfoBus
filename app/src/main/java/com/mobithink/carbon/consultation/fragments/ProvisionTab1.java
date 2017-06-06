package com.mobithink.carbon.consultation.fragments;


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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.mobithink.carbon.R;
import com.mobithink.carbon.database.model.EventDTO;
import com.mobithink.carbon.database.model.RollingPointDTO;
import com.mobithink.carbon.database.model.StationDataDTO;
import com.mobithink.carbon.managers.PreferenceManager;
import com.mobithink.carbon.utils.Mathematics;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class ProvisionTab1 extends GenericTabFragment implements OnMapReadyCallback {

    private MapView mtripMapView;
    private TextView minDistanceBetweenStations;
    private TextView averageDistanceBetweenStationsTextView;
    private TextView maxDistanceBetweenStations;
    private TextView savingInMinutesTextView;
    private TextView savingInEuroTextView;

    private long interStationObjective = 600;
    private long timeInStation;
    private long totalTimeInStation = 0;
    private long averageTimeInStation;

    private long averageDistanceBetweenStations;
    private double tripBetweenStationsDistance = 0;
    private long tripDistance = 0;

    private long timeSavingResult;
    private long timeSavingInMinutes;

    SimpleDateFormat timeFormat = new SimpleDateFormat("mm:ss", Locale.FRANCE);

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

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getTripDTO();
        timeSavingInStation();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        List<LatLng> rollingPointlatLngList = new ArrayList<>();
        List<LatLng> stationlatLngList = new ArrayList<>();
        List<LatLng> eventStationlatLngList = new ArrayList<>();
        List<LatLng> eventRollinglatLngList = new ArrayList<>();
        List<LatLng> eventCrossroadRollinglatLngList = new ArrayList<>();

        if (getTripDTO().getRollingPointDTOList()!= null){
            for (RollingPointDTO rollingPointDTO : getTripDTO().getRollingPointDTOList()) {
                rollingPointlatLngList.add(new LatLng(rollingPointDTO.getGpsLat(), rollingPointDTO.getGpsLong()));
            }
        }

//        rollingPointlatLngList.add(new LatLng(45.195701543, 0.782943293));
//        rollingPointlatLngList.add(new LatLng(45.1963594, 0.7833852));
//        rollingPointlatLngList.add(new LatLng(45.195751717, 0.782835663));
//        rollingPointlatLngList.add(new LatLng(45.1955591, 0.7822232));
//        rollingPointlatLngList.add(new LatLng(45.195746682, 0.782477209));
//        rollingPointlatLngList.add(new LatLng(45.195575454, 0.782040012));
//        rollingPointlatLngList.add(new LatLng(45.195392733, 0.781652409));
//        rollingPointlatLngList.add(new LatLng(45.195287794, 0.781416649));
//        rollingPointlatLngList.add(new LatLng(45.1952357, 0.7813538));
//        rollingPointlatLngList.add(new LatLng(45.195218047, 0.781236282));
//        rollingPointlatLngList.add(new LatLng(45.195128286, 0.781093408));
//        rollingPointlatLngList.add(new LatLng(45.194863961, 0.781108025));
//        rollingPointlatLngList.add(new LatLng(45.194453169, 0.781198874));
//        rollingPointlatLngList.add(new LatLng(45.1946248, 0.7812517));
//        rollingPointlatLngList.add(new LatLng(45.194006988, 0.781285444));
//
//        rollingPointlatLngList.add(new LatLng(45.19353171, 0.781382732));
//        rollingPointlatLngList.add(new LatLng(45.193128734, 0.78146905));
//        rollingPointlatLngList.add(new LatLng(45.192898301, 0.781531688));
//        rollingPointlatLngList.add(new LatLng(45.1948919, 0.7834772));
//        rollingPointlatLngList.add(new LatLng(45.192588889, 0.78160908));
//        rollingPointlatLngList.add(new LatLng(45.192165519, 0.781703175));
//        rollingPointlatLngList.add(new LatLng(45.191737765, 0.781790631));
//        rollingPointlatLngList.add(new LatLng(45.191357149, 0.781883524));
//        rollingPointlatLngList.add(new LatLng(45.190855283, 0.781995812));
//        rollingPointlatLngList.add(new LatLng(45.190280857, 0.782087904));
//        rollingPointlatLngList.add(new LatLng(45.189694298, 0.782089085));
//        rollingPointlatLngList.add(new LatLng(45.189058048, 0.782112188));
//        rollingPointlatLngList.add(new LatLng(45.1759385, 0.7736862));
//        rollingPointlatLngList.add(new LatLng(45.188320617, 0.782130377));
//        rollingPointlatLngList.add(new LatLng(45.187519889, 0.782147456));


        if (getTripDTO().getStationDataDTOList()!= null) {
            for (StationDataDTO stationDTO : getTripDTO().getStationDataDTOList()) {
                stationlatLngList.add(new LatLng(stationDTO.getGpsLat(), stationDTO.getGpsLong()));
            }
        }

        if(getTripDTO().getEventDTOList()!=null) {
            for (EventDTO eventDTO : getTripDTO().getEventDTOList()) {
                if (eventDTO.getStationName() != null && eventDTO.getStationName().length() > 0) {
                    eventStationlatLngList.add(new LatLng(eventDTO.getGpsLat(), eventDTO.getGpsLong()));
                } else if (eventDTO.getStationName() == null && eventDTO.getEventType().equals("Evènement en section courante")) {
                    eventRollinglatLngList.add(new LatLng(eventDTO.getGpsLat(), eventDTO.getGpsLong()));
                } else if (eventDTO.getStationName() == null && eventDTO.getEventType().equals("Evènement en carrefour")){
                    eventCrossroadRollinglatLngList.add(new LatLng(eventDTO.getGpsLat(), eventDTO.getGpsLong()));
                }
            }
        }

        //draw polyline
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.addAll(rollingPointlatLngList);
        googleMap.addPolyline(polylineOptions.color(R.color.mobiThinkBlue).geodesic(true));

        //draw marker and circle and marker
        for (LatLng latLng : stationlatLngList) {
            googleMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_grey_point)).anchor(0.5f, 0.5f));
            googleMap.addCircle(new CircleOptions().center(latLng).radius(PreferenceManager.getInstance().getStationRadius()));
        }

        LatLng firstStation = stationlatLngList.get(0);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstStation, 13));

        //draw event
        for (LatLng latLng : eventStationlatLngList) {
            googleMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_event_station))
                    .anchor(0.5f, 0.5f)
            );
        }

        for (LatLng latLng : eventRollinglatLngList) {
            googleMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_event_driving)).anchor(0.5f, 0.5f));
        }

        for (LatLng latLng : eventCrossroadRollinglatLngList){
            googleMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_event_crossroad)).anchor(0.5f, 0.5f));
        }
    }

    public void timeSavingInStation(){

        ArrayList<Double> distanceTab = new ArrayList<>() ;
        if (getTripDTO().getStationDataDTOList() != null) {
            for (int i = 0; i + 1 < getTripDTO().getStationDataDTOList().size(); i++) {
                timeInStation = getTripDTO().getStationDataDTOList().get(i).getEndTime() - getTripDTO().getStationDataDTOList().get(i).getStartTime();
                totalTimeInStation += timeInStation;
                tripBetweenStationsDistance = Math.round(Mathematics.calculateGPSDistance(getTripDTO().getStationDataDTOList().get(i).getGpsLat(), getTripDTO().getStationDataDTOList().get(i).getGpsLong(), getTripDTO().getStationDataDTOList().get(i + 1).getGpsLat(), getTripDTO().getStationDataDTOList().get(i + 1).getGpsLong()));
                distanceTab.add(tripBetweenStationsDistance);
                tripDistance += tripBetweenStationsDistance;
            }

        }

        double minVal = Collections.min(distanceTab);
        double maxVal = Collections.max(distanceTab);

        minDistanceBetweenStations.setText(String.valueOf(minVal) + " m");
        maxDistanceBetweenStations.setText(String.valueOf(maxVal)+ " m");

        averageDistanceBetweenStations = tripDistance/(getTripDTO().getStationDataDTOList().size()-1);
        averageDistanceBetweenStationsTextView.setText(String.valueOf(averageDistanceBetweenStations) + " m");

        averageTimeInStation = totalTimeInStation/getTripDTO().getStationDataDTOList().size();

        timeSavingResult = totalTimeInStation-(((tripDistance/interStationObjective)+ 1)*averageTimeInStation);

        timeSavingInMinutes = (((timeSavingResult / 1000)/60) % 60);

        savingInMinutesTextView.setText(timeFormat.format(timeSavingResult)+ " min");
        savingInEuroTextView.setText("soit " +  Math.round(timeSavingInMinutes* PreferenceManager.getInstance().getCostOfProductionByMinute())  + " euro");

    }
}
