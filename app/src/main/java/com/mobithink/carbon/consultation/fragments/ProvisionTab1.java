package com.mobithink.carbon.consultation.fragments;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mobithink.carbon.R;
import com.mobithink.carbon.database.model.RollingPointDTO;
import com.mobithink.carbon.database.model.StationDataDTO;
import com.mobithink.carbon.utils.DrawBusTrip;


public class ProvisionTab1 extends GenericTabFragment implements OnMapReadyCallback, DrawBusTrip.onDrawRoute {

    MapView mtripMapView;
    TextView minDistanceBetweenStations;
    TextView averageDistanceBetweenStations;
    TextView maxDistanceBetweenStations;
    double latitude;
    double longitude;

    private GoogleMap mGoogleMap;


    public ProvisionTab1() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_provision_tab1, container, false);

        mtripMapView = (MapView) rootView.findViewById(R.id.tripMapView);
        mtripMapView.onCreate(savedInstanceState);
        mtripMapView.onResume();
        mtripMapView.getMapAsync(this);

        minDistanceBetweenStations = (TextView) rootView.findViewById(R.id.minDistance);
        averageDistanceBetweenStations = (TextView) rootView.findViewById(R.id.averageDistance);
        maxDistanceBetweenStations = (TextView) rootView.findViewById(R.id.maxDistance);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getTripDTO();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        DrawBusTrip.getInstance(this, getActivity()).setFromLatLong(43.600000, 1.433333)
                .setToLatLong(43.6667, 1.4833).setGmapAndKey("AIzaSyDNRm3UOtZ9_o-Y2Tpoq5w2S8aj3P2K7eo", mGoogleMap)
                .run();

        MarkerOptions markers = new MarkerOptions();
        markers.position(new LatLng(43.600000, 1.433333));
        mGoogleMap.addMarker(markers);
        markers.position(new LatLng(43.6667, 1.4833));
        mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(43.6667, 1.4833)).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_grey_point)));

        /*for(RollingPointDTO rollingPointDTO : getTripDTO().getRollingPointDTOList()) {
            for(int i = 0; i<= getTripDTO().getRollingPointDTOList().size(); i++) {
                DrawBusTrip.getInstance(this, getActivity()).setFromLatLong(rollingPointDTO.getGpsLat(), rollingPointDTO.getGpsLong())
                        .setToLatLong(rollingPointDTO.getGpsLat(), rollingPointDTO.getGpsLong()).setGmapAndKey("AIzaSyDNRm3UOtZ9_o-Y2Tpoq5w2S8aj3P2K7eo", mGoogleMap)
                        .run();
            }
        }

        for(StationDataDTO stationDataDTO : getTripDTO().getStationDataDTOList()){
            MarkerOptions markers = new MarkerOptions();
            markers.position(new LatLng(stationDataDTO.getGpsLat(), stationDataDTO.getGpsLong()));
            mGoogleMap.addMarker(markers);
        }*/
    }

    @Override
    public void afterDraw(String result) {
    }
}
