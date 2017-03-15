package com.mobithink.carbon.consultation.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.PolylineOptions;
import com.mobithink.carbon.R;
import com.mobithink.carbon.database.model.StationDataDTO;
import com.mobithink.carbon.utils.DrawBusTrip;

import java.util.ArrayList;

public class ProvisionTab1 extends GenericTabFragment implements OnMapReadyCallback, DrawBusTrip.onDrawRoute {

    MapView mtripMapView;
    TextView minDistanceBetweenStations;
    TextView averageDistanceBetweenStations;
    TextView maxDistanceBetweenStations;

    private GoogleMap mGoogleMap;
    PolylineOptions stationLatLng;

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
        PolylineOptions polylineOptions = new PolylineOptions();

        /*polylineOptions.add(new LatLng(43.600000, 1.433333));
        polylineOptions.add(new LatLng(43.607232, 1.451205));
        polylineOptions.add(new LatLng(43.609942, 1.455105));
        polylineOptions.add(new LatLng(43.614354, 1.462143));

        polylineOptions.add(new LatLng(43.616467, 1.465061));
        polylineOptions.add(new LatLng(43.619139, 1.468494));
        polylineOptions.add(new LatLng(43.625414, 1.475361));
        polylineOptions.add(new LatLng(43.631192, 1.478279));

        polylineOptions.add(new LatLng(43.640572, 1.475275));
        polylineOptions.add(new LatLng(43.646162, 1.470211));
        polylineOptions.add(new LatLng(43.654422, 1.475961));
        polylineOptions.add(new LatLng(43.6667, 1.4833));*/

        for(StationDataDTO stationDataDTO : getTripDTO().getStationDataDTOList()){
            for (int i = 0; i<= getTripDTO().getStationDataDTOList().size(); i++ ){
                polylineOptions.add(new LatLng(stationDataDTO.getGpsLat(),stationDataDTO.getGpsLong()));

                ArrayList<LatLng> latLngList = new ArrayList();
                LatLng latLng = new LatLng(stationDataDTO.getGpsLat(), stationDataDTO.getGpsLong());
                latLngList.add(latLng);
                for(int j = 0; j<latLngList.size(); j++){
                    polylineOptions.add(latLngList.get(j));
                    mGoogleMap.addPolyline(polylineOptions.add(latLngList.get(j)).geodesic(true).color(R.color.mobiThinkBlue));
                }

            }

        }


        //LatLngBounds.Builder builder = new LatLngBounds.Builder();
        //mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 15));
        //prepareBuilder(latLngList);
        //googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 17));

        /*mGoogleMap.addPolyline(new PolylineOptions().add(stationLatLng).color(Color.BLUE)
                .geodesic(true));*/

        /*DrawBusTrip.getInstance(this, getActivity()).setFromLatLong(43.600000, 1.433333)
                .setToLatLong(43.6667, 1.4833).setGmapAndKey("AIzaSyDNRm3UOtZ9_o-Y2Tpoq5w2S8aj3P2K7eo", mGoogleMap)
                .run();

        MarkerOptions markers = new MarkerOptions();
        markers.position(new LatLng(43.600000, 1.433333));
        mGoogleMap.addMarker(markers);
        markers.position(new LatLng(43.6667, 1.4833));
        mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(43.6667, 1.4833)).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_grey_point)));*/

        /*for(StationDataDTO stationDataDTO : getTripDTO().getStationDataDTOList()) {
            for(int i = 0; i<= getTripDTO().getStationDataDTOList().size(); i++) {
                DrawBusTrip.getInstance(this, getActivity()).setFromLatLong(stationDataDTO.getGpsLat(), stationDataDTO.getGpsLong())
                        .setToLatLong(stationDataDTO.getGpsLat(), stationDataDTO.getGpsLong()).setGmapAndKey("AIzaSyDNRm3UOtZ9_o-Y2Tpoq5w2S8aj3P2K7eo", mGoogleMap)
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
