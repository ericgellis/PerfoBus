package com.mobithink.carbon.consultation.fragments;

import android.content.pm.PackageManager;
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
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.mobithink.carbon.R;
import com.mobithink.carbon.event.BFConstant;

import static com.mobithink.carbon.event.EventActivity.UPDATE_LOCATION_INTERVAL;


public class ProvisionTab1 extends GenericTabFragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    MapView mtripMapView;
    TextView minDistanceBetweenStations;
    TextView averageDistanceBetweenStations;
    TextView maxDistanceBetweenStations;
    double longitude;
    double latitude;
    private GoogleMap mGoogleMap;
    private GoogleApiClient mGoogleApiClient;

    public ProvisionTab1() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_provision_tab1, container, false);

        /*mGoogleApiClient = new GoogleApiClient.Builder(getContext()).addApi(LocationServices.API).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();*/

        mtripMapView = (MapView) rootView.findViewById(R.id.tripMapView);
        mtripMapView.onCreate(savedInstanceState);
        mtripMapView.onResume();

        /*try {
            MapsInitializer.initialize(this.getContext());
        } catch (Exception e) {
            e.printStackTrace();

        }*/

        mtripMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {

                mGoogleMap = mMap;
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
                mGoogleMap.setBuildingsEnabled(true);
                //enableLocationForGoogleMap();

            }
        });

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

    public void onLocationAuthorizationGranted() {

        //enableLocationForGoogleMap();
        //requestLocationUpdate();
    }

    private void enableLocationForGoogleMap() {
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mGoogleMap.setMyLocationEnabled(true);
        } else {

            if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mGoogleMap.setMyLocationEnabled(true);
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        BFConstant.PERMISSIONS_REQUEST_FINE_LOCATION);
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        requestLocationUpdate();
    }

    private void requestLocationUpdate() {

        if (ContextCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            LocationRequest mLocationRequest = LocationRequest.create();
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            mLocationRequest.setInterval(UPDATE_LOCATION_INTERVAL);

            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("BFMapFragment", "GoogleApiClient connection has been suspended");

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        longitude = location.getLongitude();
        latitude = location.getLatitude();

    }
}
